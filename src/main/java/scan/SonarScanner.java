package scan;

import com.google.gson.JsonObject;
import common.Constant;
import crontab.MoodleCrontab;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import utils.HttpUtils;
import utils.JsonUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class SonarScanner {
    public static boolean scanProject(String projectPath,String projectKey,String token)  {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "d: && cd "+projectPath+" && sonar-scanner.bat -D\"sonar.projectKey=" +projectKey+
                "\" -D\"sonar.sources=.\" -D\"sonar.host.url=http://localhost:9000\" -D\"sonar.login=" + token + "\"");
        builder.redirectErrorStream(true);
        Process p = null;
        try {
            p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) { break; }
                System.out.println(line);
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static String generateToken(String token){
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("name", token));

        String result = HttpUtils.httpPost(Constant.GENERATETOKENAPI, params);
        System.out.println(result);
        JsonObject tokenInfo = JsonUtils.toObject(result);

        if(tokenInfo.has("token")){
            return tokenInfo.get("token").toString();
        }else{
            return "";
        }
    }

    public static void getResult(String projectKey, int page){
        String apiPath = String.format(Constant.GETRESULTAPI, Constant.PAGESIZE, projectKey, Constant.PAGESIZE, page);
        String result = HttpUtils.httpGet(apiPath, null);

        JsonObject resultJsonObject = JsonUtils.toObject(result);

        System.out.println(resultJsonObject.get("total"));
        System.out.println(resultJsonObject.get("issues").getAsJsonArray().size());
    }
    public static void main(String[] args) {
        String token = generateToken("1234");
        scanProject("D:\\sonarqube\\testPy","TestPy",token);
        getResult("FirstTest",2);

    }
}
