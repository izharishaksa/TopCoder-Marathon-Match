package GraphReconstruction;

import java.io.*;

public class GraphReconstruction {

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            int N = Integer.parseInt(br.readLine());
            double C = Double.parseDouble(br.readLine());
            int K = Integer.parseInt(br.readLine());
            int NumPaths = Integer.parseInt(br.readLine());
            String[] paths = new String[NumPaths];
            for (int i = 0; i < NumPaths; i++) paths[i] = br.readLine();

            GraphReconstruction prog = new GraphReconstruction();
            String[] ret = prog.findSolution(N, C, K, paths);

            System.out.println(ret.length);
            for (int i = 0; i < ret.length; i++) {
                System.out.println(ret[i]);
            }
        } catch (Exception e) {
        }
    }

    public String[] findSolution(int N, double C, int K, String[] paths) {
        String[] out = new String[N];
        for (int i = 0; i < N; i++) {
            out[i] = "";
            for (int k = 0; k < N; k++) {
                out[i] += "1";
            }
        }
        return out;
    }

}