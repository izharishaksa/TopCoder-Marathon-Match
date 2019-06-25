package SnowCleaning;

import java.io.PrintWriter;

/**
 * @author Izhari Ishak Aksa
 */
public class CreatingFile {

    public static void main(String[] args) throws Exception {
        for (int i = 11; i <= 100; i++) {
            PrintWriter writer = new PrintWriter("run" + i + ".bat", "UTF-8");

            String temp = "java -jar SnowCleaningVis.jar -exec \"java -jar SnowCleaning.jar\" -seed " + i;

            writer.println("del \"SnowCleaning.jar\"");
            writer.println("copy \"D:\\TopCoder\\Marathon Match\\SnowCleaning\\dist\\SnowCleaning.jar\" \"D:\\TopCoder\\Marathon Match\\SnowCleaning\\\"");
            writer.println(temp);
            writer.close();
        }
    }
}
