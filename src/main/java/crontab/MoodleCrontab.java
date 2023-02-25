package crontab;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.MoodleModel;
import scan.SonarScanner;
import utils.FileUtils;
import utils.HttpUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author haotn
 * @version 1.0
 * @date 15/02/2023 23:21
 */
public class MoodleCrontab {
	private static final int INTERVAL = 5000*30;
	private static final int POOL_THREAD = 5;

	private static List<String> assignmentIds;

	private static ArrayList<JsonObject> getFilesFromSubmission(JsonObject submission){
		ArrayList<JsonObject> res=new ArrayList<>();
		JsonArray plugins = submission.get("plugins").getAsJsonArray();
		JsonArray fileareas = plugins.get(0).getAsJsonObject().get("fileareas").getAsJsonArray();
		JsonArray files = fileareas.get(0).getAsJsonObject().get("files").getAsJsonArray();

		for(int i=0;i<files.size();i++)
		{
			JsonObject file=files.get(i).getAsJsonObject();
			res.add(file);
		}
		return res;
	}

	private static void _getSubmission(String assignmentId) {
		List<JsonObject> submissions = MoodleModel.INSTANCE.getSubmissions(assignmentId);
		if (submissions != null) {
			for (JsonObject submission : submissions) {

				ArrayList<JsonObject> files = getFilesFromSubmission(submission);
				for(int i=0;i<files.size();i++)
				{
					JsonObject fileJson=files.get(i);
					String fileurl=fileJson.get("fileurl").getAsString();
					String filename=fileJson.get("filename").getAsString();
					FileUtils.downloadFileFromUrl(fileurl, HttpUtils.getToken(), "C:/Users/Admins/Downloads/Compressed/"+filename);
					String filePath=FileUtils.extractArchiveFile("C:/Users/Admins/Downloads/Compressed/"+filename,"E:\\Bai Lam\\Nam 4\\DATN\\Extract");
					if(!filePath.isEmpty())

					{
						if(filePath.contains(".jar"))
						{
							filePath= FileUtils.extractArchiveFile(filePath,"E:\\Bai Lam\\Nam 4\\DATN\\Extract");
							System.out.println(filePath);
						}
						//passing token late, current using default
						SonarScanner.scanProject(filePath,"","");
					}
				}


			}
		}
	}

	private static void executor() {
		ExecutorService executor = Executors.newFixedThreadPool(POOL_THREAD);

		for (String id : assignmentIds) {
			executor.execute(() -> {
				_getSubmission(id);
			});
		}

		executor.shutdown();

		// Wait until all threads are finish
		while (!executor.isTerminated()) {}
	}

	public static void start() {
		assignmentIds = new ArrayList<>(Arrays.asList("1"));

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				executor();
			}
		}, 0, INTERVAL);
	}
}
