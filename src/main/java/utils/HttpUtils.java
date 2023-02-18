package utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import common.MoodleFunctions;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author haotn
 * @version 1.0
 * @date 11/02/2023 15:43
 */
public class HttpUtils {
	private static String TOKEN;

	// secret here
	private static final String MOODLE_USERNAME = "haotn";
	private static final String MOODLE_PASSWORD = "123456aB.";
	private static final String MOODLE_SERVICE_NAME = "";
	private static final String MOODLE_HOST = "https://moodle-110012-0.cloudclusters.net/%s";
	private static final String TOKEN_API = "login/token.php?username=%s&password=%s&service=%s";

	private static final Logger LOGGER;
	private static final CloseableHttpClient httpClient;

	private static final Gson gson;

	static {
		LOGGER = LogManager.getLogger(HttpUtils.class);
		httpClient = HttpClients.createDefault();
		gson = new Gson();
	}

	public static boolean init() {
		return _getAccessToken();
	}

	private static boolean _getAccessToken() {
		String apiPath = String.format(TOKEN_API, MOODLE_USERNAME, MOODLE_PASSWORD, MOODLE_SERVICE_NAME);
		String url = String.format(MOODLE_HOST, apiPath);

		String data = httpGet(url);
		JsonObject dataObject = gson.fromJson(data, JsonObject.class);

		if (dataObject != null) {
			TOKEN = dataObject.get("token").getAsString();
			return true;
		} else {
			TOKEN = "";
			LOGGER.error("Can not get access token");
			return false;
		}
	}

	public static String httpGet(String urlPath) {
		return httpGet(urlPath, null, MoodleFunctions.DEFAULT);
	}

	public static String httpGet(String urlPath, List<NameValuePair> params, String function) {

		if (Objects.isNull(params)) {
			params = new ArrayList<>();
		}

		params.add(new BasicNameValuePair("wstoken", TOKEN));
		params.add(new BasicNameValuePair("wsfunction", function));
		params.add(new BasicNameValuePair("moodlewsrestformat", "json"));

		try {
			ClassicHttpRequest req = new HttpGet(urlPath);
			req.setUri(new URIBuilder(req.getUri()).addParameters(params).build());
			CloseableHttpResponse response = httpClient.execute(req);

			return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
			return null;
		}
	}


	public static void main(String[] args) {

//		INSTANCE._getAccessToken();
	}
}
