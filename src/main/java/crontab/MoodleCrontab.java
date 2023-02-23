package crontab;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.MoodleModel;
import utils.FileUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author haotn
 * @version 1.0
 * @date 15/02/2023 23:21
 */
public class MoodleCrontab {
	private static final int INTERVAL = 5000;
	private static final int POOL_THREAD = 5;

	private static List<String> assignmentIds;

	private static String getFileFromSubmission(JsonObject submission){
		JsonArray plugins = submission.get("plugins").getAsJsonArray();
		JsonArray fileareas = plugins.get(0).getAsJsonObject().get("fileareas").getAsJsonArray();
		JsonArray files = fileareas.get(0).getAsJsonObject().get("files").getAsJsonArray();

		return files.get(files.size() - 1).getAsJsonObject().get("fileurl").getAsString();
	}

	private static void _getSubmission(String assignmentId) {
		List<JsonObject> submissions = MoodleModel.INSTANCE.getSubmissions(assignmentId);
		if (submissions != null) {
			for (JsonObject submission : submissions) {
				String fileurl = getFileFromSubmission(submission);
				FileUtils.downloadFileFromUrl(fileurl, "/home/lap15454/Desktop/file.zip");
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
