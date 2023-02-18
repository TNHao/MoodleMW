package model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import common.Constant;
import common.MoodleFunctions;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.HttpUtils;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author haotn
 * @version 1.0
 * @date 12/02/2023 15:43
 */
public class MoodleModel {
	private static final Logger LOGGER = LogManager.getLogger(MoodleModel.class);

	public static final MoodleModel INSTANCE = new MoodleModel();

	private MoodleModel() {
	}

	public JsonObject getUserDetail(String email) {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("field", "email"));
		params.add(new BasicNameValuePair("values[0]", email));

		String result = HttpUtils.httpGet(Constant.CORE_API, params, MoodleFunctions.USER_DETAIL);
		List<JsonObject> users = JsonUtils.toList(result);

		if (Objects.isNull(users) || users.isEmpty()) {
			LOGGER.error("[Can not get user detail] - email: " + email);
			return null;
		}

		return users.get(0);
	}

	public List<JsonObject> getUserEnrolledCourses(String userId) {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("userid", userId));

		String result = HttpUtils.httpGet(Constant.CORE_API, params, MoodleFunctions.USER_COURSES);
		List<JsonObject> courses = JsonUtils.toList(result);

		if (Objects.isNull(courses) || courses.isEmpty()) {
			LOGGER.error("[Can not get user courses] - userId: " + userId);
			return null;
		}

		return courses;
	}

	public List<JsonObject> getCourseAssignment(String courseId) {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("courseids[0]", courseId));
		// param to get course info even this account isn't  enrolled to
		params.add(new BasicNameValuePair("includenotenrolledcourses", "1"));

		String result = HttpUtils.httpGet(Constant.CORE_API, params, MoodleFunctions.COURSE_ASSIGNMENT);
		JsonObject detail = JsonUtils.toObject(result);

		if (Objects.isNull(detail)){
			LOGGER.error("[Can not get course assignments] - courseId: " + courseId);
			return null;
		}

		JsonObject course = detail.getAsJsonArray("courses").get(0).getAsJsonObject();
		JsonArray assignments = course.getAsJsonArray("assignments");

		return JsonUtils.toList(assignments);
	}

	public List<JsonObject> getSubmissions(String assignmentId) {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("assignmentids[0]", assignmentId));

		String result = HttpUtils.httpGet(Constant.CORE_API, params, MoodleFunctions.ASSIGNMENT_SUBMISSIONS);
		JsonObject detail = JsonUtils.toObject(result);

		if (Objects.isNull(detail)){
			LOGGER.error("[Can not get submissions] - assignmentId: " + assignmentId);
			return null;
		}

		JsonObject assignment = detail.getAsJsonArray("assignments").get(0).getAsJsonObject();
		JsonArray submissions = assignment.getAsJsonArray("submissions");

		return JsonUtils.toList(submissions);
	}

	public static void main(String[] args) {
		INSTANCE.getUserDetail("vuagio.2402@gmail.com");
	}
}
