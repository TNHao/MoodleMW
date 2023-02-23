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
}
