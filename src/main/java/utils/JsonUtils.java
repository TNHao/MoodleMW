package utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author haotn
 * @version 1.0
 * @date 18/02/2023 13:21
 */
public class JsonUtils {

	private static final Logger LOGGER = LogManager.getLogger(JsonUtils.class);

	private static final Gson gson = new Gson();

	public static JsonObject toObject(String json) {
		return gson.fromJson(json, JsonObject.class);
	}

	public static List<JsonObject> toList(String json) {
		try {
			Type listType = new TypeToken<ArrayList<JsonObject>>() {
			}.getType();
			List<JsonObject> items = gson.fromJson(json, listType);
			return items;
		} catch (Exception ex) {
			LOGGER.error("[Error parse json] - json: " + json, ex);
			return null;
		}
	}

	public static List<JsonObject> toList(JsonArray json){
		Type listType = new TypeToken<ArrayList<JsonObject>>() {
		}.getType();
		return gson.fromJson(json, listType);
	}
}
