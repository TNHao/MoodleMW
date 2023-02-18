package app;

import crontab.MoodleCrontab;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.HttpUtils;

/**
 * @author haotn
 * @version 1.0
 * @date 11/02/2023 15:18
 */
public class MainApp {
	private static final Logger LOGGER = LogManager.getLogger(MainApp.class);

	public static void main(String[] args) {
		boolean status = HttpUtils.init();

		if (status) {
			LOGGER.info("Staring application...");
			MoodleCrontab.start();
		} else {
			LOGGER.error("Error initialize application! STOPPING");
			System.exit(1);
		}
	}
}
