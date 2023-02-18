package crontab;

import com.google.gson.JsonObject;
import model.MoodleModel;

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

	private static void _getSubmission(String assignmentId) {
		List<JsonObject> submissions = MoodleModel.INSTANCE.getSubmissions(assignmentId);
		System.out.println(submissions);
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

//		try {
//			Thread.sleep(7000);
//			assignmentIds.remove(0);
//		} catch (InterruptedException e) {
//			throw new RuntimeException(e);
//		}
	}
}
