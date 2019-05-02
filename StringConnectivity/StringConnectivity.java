package StringConnectivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *
 * @author Izhari Ishak Aksa
 */
public class StringConnectivity {

    final long START = System.currentTimeMillis();
    final long TIMEOUT = 19000;
    int[] ret;
    boolean[][] visited;
    char[][] board;
    int N, index;
    String s;

    void simulate(int row, int col, int pos, int dir) {
        while (index - 1 < s.length()) {
            visited[row][col] = true;
            if (dir == 0 && col + 1 < N && !visited[row][col + 1]) {
                col++;
                board[row][col] = s.charAt(index - 1);
                ret[index++] = 0;
            } else if (dir == 1 && row - 1 >= 0 && !visited[row - 1][col]) {
                row--;
                board[row][col] = s.charAt(index - 1);
                ret[index++] = 1;
            } else if (dir == 2 && col - 1 >= 0 && !visited[row][col - 1]) {
                col--;
                board[row][col] = s.charAt(index - 1);
                ret[index++] = 2;
            } else if (dir == 3 && row + 1 < N && !visited[row + 1][col]) {
                row++;
                board[row][col] = s.charAt(index - 1);
                ret[index++] = 3;
            } else {
                dir++;
                dir %= 4;
            }
        }
    }

    void simulate2(int row, int col, int pos, int dir) {
        while (index - 1 < s.length()) {
            visited[row][col] = true;
            if (dir == 0 && col + 1 < N && !visited[row][col + 1]) {
                col++;
                board[row][col] = s.charAt(index - 1);
                ret[index++] = 0;
            } else if (dir == 0 && col + 1 == N) {
                dir = 3;
                continue;
            }
            if (dir == 2 && col - 1 >= 0 && !visited[row][col - 1]) {
                col--;
                board[row][col] = s.charAt(index - 1);
                ret[index++] = 2;
            } else if (dir == 2 && col - 1 < 0) {
                dir = 3;
                continue;
            }
            if (dir == 3 && row + 1 < N && !visited[row + 1][col]) {
                row++;
                board[row][col] = s.charAt(index - 1);
                ret[index++] = 3;
                if (col == 0) {
                    dir = 0;
                }
                if (col == N - 1) {
                    dir = 2;
                }
            }
        }
    }

    void simulate3(int row, int col, int pos, int dir) {
        while (index - 1 < s.length()) {
            visited[row][col] = true;
            if (dir == 0 && col + 1 < N && !visited[row][col + 1]) {
                col++;
                board[row][col] = s.charAt(index - 1);
                ret[index++] = 0;
                if (row + 1 < N && !visited[row + 1][col]) {
                    dir = 3;
                }
            } else if (dir == 1 && row - 1 >= 0 && !visited[row - 1][col]) {
                row--;
                board[row][col] = s.charAt(index - 1);
                ret[index++] = 1;
                if (col + 1 < N && !visited[row][col + 1]) {
                    dir = 0;
                }
            } else if (dir == 2 && col - 1 >= 0 && !visited[row][col - 1]) {
                col--;
                board[row][col] = s.charAt(index - 1);
                ret[index++] = 2;
                if (row - 1 >= 0 && !visited[row - 1][col]) {
                    dir = 1;
                }
            } else if (dir == 3 && row + 1 < N && !visited[row + 1][col]) {
                row++;
                board[row][col] = s.charAt(index - 1);
                ret[index++] = 3;
                if (col - 1 >= 0 && !visited[row][col - 1]) {
                    dir = 2;
                }
            } else {
                dir++;
                dir %= 4;
            }
        }
    }

    void simulate4(int row, int col, int pos, int dir) {
        boolean bawah = true;
        while (index - 1 < s.length()) {
            visited[row][col] = true;
            if (dir == 0 && col + 1 < N && !visited[row][col + 1]) {
                col++;
                board[row][col] = s.charAt(index - 1);
                ret[index++] = 0;
                if (bawah && row + 1 < N && !visited[row + 1][col]) {
                    dir = 3;
                    bawah = false;
                } else if (!bawah && row - 1 >= 0 && !visited[row - 1][col]) {
                    dir = 1;
                    bawah = true;
                }
            } else if (dir == 1 && row - 1 >= 0 && !visited[row - 1][col]) {
                row--;
                board[row][col] = s.charAt(index - 1);
                ret[index++] = 1;
                if (row == 0 && col + 1 < N && !visited[row][col + 1]) {
                    dir = 0;
                    bawah = true;
                }
            } else if (dir == 3 && row + 1 < N && !visited[row + 1][col]) {
                row++;
                board[row][col] = s.charAt(index - 1);
                ret[index++] = 3;
                if (col - 1 >= 0 && !visited[row][col - 1]) {
                    dir = 2;
                }
            } else if (dir == 2 && col - 1 >= 0 && !visited[row][col - 1]) {
                col--;
                board[row][col] = s.charAt(index - 1);
                ret[index++] = 2;
                if (col == 0 && row + 1 < N && !visited[row + 1][col]) {
                    row++;
                    board[row][col] = s.charAt(index - 1);
                    ret[index++] = 3;
                    dir = 0;
                    bawah = false;
                }
            }
        }
    }

    void simulate5(int row, int col, int pos, int dir) {
        while (index - 1 < s.length()) {
            visited[row][col] = true;
            if (dir == 0 && col + 1 < N && !visited[row][col + 1]) {
                col++;
                board[row][col] = s.charAt(index - 1);
                ret[index++] = 0;
            } else if (dir == 1 && row - 1 >= 0 && !visited[row - 1][col]) {
                row--;
                board[row][col] = s.charAt(index - 1);
                ret[index++] = 1;
            } else if (dir == 2 && col - 1 >= 0 && !visited[row][col - 1]) {
                col--;
                board[row][col] = s.charAt(index - 1);
                ret[index++] = 2;
            } else if (dir == 3 && row + 1 < N && !visited[row + 1][col]) {
                row++;
                board[row][col] = s.charAt(index - 1);
                ret[index++] = 3;
            } else {
                dir++;
                dir %= 4;
            }
        }        
    }

    public int[] placeString(String s) {
        setupError();
        this.s = s;
        //System.err.println(s);
        //System.exit(0);
        N = (int) Math.sqrt(s.length());
        System.err.println("Ukuran: " + N);
        int best = N * N;
        int[] bestRet = new int[N * N + 1];

        //1
        reset(0, 0);
        simulate(0, 0, 1, 0);
        int curScore = score();
        System.err.println(">>1: " + curScore);
        if (curScore < best) {
            best = curScore;
            bestRet = ret.clone();
        }

        //2
        reset(0, 0);
        simulate2(0, 0, 1, 0);
        curScore = score();
        System.err.println(">>2: " + curScore);
        if (curScore < best) {
            best = curScore;
            bestRet = ret.clone();
        }

        //3
        for (int i = 0; i < N / 2; i++) {
            reset(N / 2 - i, N / 2 - i);
            simulate3(N / 2 - i, N / 2 - i, 1, 0);
            curScore = score();
            System.err.println(">>3: " + curScore);
            if (curScore < best) {
                best = curScore;
                bestRet = ret.clone();
            }
        }

        //4
        for (int i = 0; i < N / 2; i++) {
            reset(N / 2 + i, N / 2 + i);
            simulate3(N / 2 + i, N / 2 + i, 1, 0);
            curScore = score();
            System.err.println(">>4: " + curScore);
            if (curScore < best) {
                best = curScore;
                bestRet = ret.clone();
            }
        }

        //5
        reset(0, 0);
        simulate4(0, 0, 1, 0);
        curScore = score();
        System.err.println(">>5: " + curScore);
        if (curScore < best) {
            best = curScore;
            bestRet = ret.clone();
        }
        
        
        //6
        int maxCon = count();
        System.err.println("seq: " + maxCon);
        System.err.println("sisa: " + N % maxCon);
        reset(0, 0);
        simulate5(0, 0, 1, 3);
        curScore = score();
        System.err.println(">>6: " + curScore);
        if (curScore < best) {
            best = curScore;
            bestRet = ret.clone();
        }

        return bestRet;
    }

    int count() {
        int res = 0;
        int x = 1;
        for (int i = 1; i < N; i++) {
            if (s.charAt(i) == s.charAt(i - 1)) {
                x++;
            } else {
                res = Math.max(res, x);
                x = 1;
            }
        }
        return res;
    }

    void reset(int row, int col) {
        ret = new int[N * N + 1];
        ret[0] = row;
        ret[1] = col;
        index = 2;
        visited = new boolean[N][N];
        visited[ret[0]][ret[1]] = true;
        board = new char[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = '.';
            }
        }
        board[ret[0]][ret[1]] = s.charAt(0);
    }

    void print() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.err.print(board[i][j]);
            }
            System.err.println();
        }
    }

    int score() {
        int total = 0;
        boolean[][] checked = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (!checked[i][j]) {
                    total++;
                    char c = board[i][j];
                    LinkedList<Integer> row = new LinkedList<Integer>();
                    LinkedList<Integer> col = new LinkedList<Integer>();
                    row.add(i);
                    col.add(j);
                    while (!row.isEmpty()) {
                        int curRow = row.poll();
                        int curCol = col.poll();
                        checked[curRow][curCol] = true;
                        if (curRow - 1 >= 0 && !checked[curRow - 1][curCol] && board[curRow - 1][curCol] == c) {
                            row.add(curRow - 1);
                            col.add(curCol);
                        }
                        if (curRow + 1 < N && !checked[curRow + 1][curCol] && board[curRow + 1][curCol] == c) {
                            row.add(curRow + 1);
                            col.add(curCol);
                        }
                        if (curCol - 1 >= 0 && !checked[curRow][curCol - 1] && board[curRow][curCol - 1] == c) {
                            row.add(curRow);
                            col.add(curCol - 1);
                        }
                        if (curCol + 1 < N && !checked[curRow][curCol + 1] && board[curRow][curCol + 1] == c) {
                            row.add(curRow);
                            col.add(curCol + 1);
                        }
                    }
                }
            }
        }
        return total;
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        StringConnectivity obj = new StringConnectivity();
        int[] ret = obj.placeString(s);
        for (int i : ret) {
            System.out.println(i);
        }
        System.out.flush();
    }
}
