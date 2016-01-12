
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author izharishaksa
 */
public class CollageMaker {

    public int[] compose(int[] data) {
        setupError();
        int[] ret = new int[800];
        Arrays.fill(ret, 1);
        return ret;
    }

    void setupError() {
        try {
            PrintStream console = System.err;
            File file = new File("log.txt");
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);
            System.setErr(ps);
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int len = Integer.parseInt(sc.nextLine());
        int[] data = new int[len];
        for (int i = 0; i < len; i++) {
            data[i] = Integer.parseInt(sc.nextLine());
        }
        CollageMaker cm = new CollageMaker();
        int[] ret = cm.compose(data);
        for (int i = 0; i < 800; i++) {
            System.out.println(ret[i]);
        }
        System.out.flush();
    }
}
