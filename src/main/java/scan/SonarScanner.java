package scan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SonarScanner {
    public static boolean scanProject(String projectPath,String projectKey,String token)  {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "e: && cd "+projectPath+" && sonar-scanner.bat -D\"sonar.projectKey=" +projectKey+
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
}
