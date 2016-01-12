package snowcleaning;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 *
 * @author Izhari Ishak Aksa
 */
public class RunTestCases {

    public static void main(String[] args) throws Exception {
        PrintWriter writer = new PrintWriter("Score.txt", "UTF-8");
        for (int i = 1; i < 101; i++) {
            long start = System.currentTimeMillis();
            Process process = Runtime.getRuntime().exec("run" + i + ".bat");
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while (System.currentTimeMillis() - start < 200) {
                System.out.println(System.currentTimeMillis() - start);
                line = br.readLine();
                if (line.contains("Score")) {
                    String x = "";
                    for (int j = 0; j < line.length(); j++) {
                        if (line.charAt(j) >= '0' && line.charAt(j) <= '9') {
                            x += line.charAt(j);
                        }
                    }
                    writer.println(x);
                    writer.flush();
                }
            }
            process.destroy();
        }
        writer.close();
    }
}
