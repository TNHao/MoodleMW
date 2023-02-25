package common;

/**
 * @author haotn
 * @version 1.0
 * @date 15/02/2023 23:27
 */
public class Constant {
	public static final String MOODLE_HOST = "https://moodle-112112-0.cloudclusters.net";

	public static final String CORE_API = MOODLE_HOST + "/webservice/rest/server.php";
	public static final String TOKEN_API = "/login/token.php?username=%s&password=%s&service=%s";

	// Sonarqube
	public static final String SONARQUBE_HOST = "http://localhost:9000";
	public static final int PAGESIZE = 500;
	public static final String GENERATETOKENAPI = SONARQUBE_HOST + "/api/user_tokens/generate";
	public static final String GETRESULTAPI = SONARQUBE_HOST + "/api/issues/search?pageSize=%s&componentKeys=%s&ps=%s&p=%s";
	public static final String USERNAME = "admin";
	public static final String PASSWORD = "123456";
}
