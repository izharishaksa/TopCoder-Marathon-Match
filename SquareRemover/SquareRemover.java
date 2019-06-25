package SquareRemover;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Izhari Ishak Aksa
 */
public class SquareRemover {

    final long TIME_OUT = 29000;
    final long TIME_OUT1 = 28000;
    final long TIME_OUT2 = 29000;
    final long START_TIME = System.nanoTime();
    final int MOD = Integer.MAX_VALUE;
    final int MUL = 48271;
    final int BUFFER_SIZE = 100;
    int N, total;
    int COLORS;
    int[][] board;
    Random rand;
    PriorityQueue<State> candidate;
    PriorityQueue<State> newCan;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int colors = Integer.parseInt(sc.nextLine());
        int N = Integer.parseInt(sc.nextLine());
        String[] board = new String[N];
        for (int i = 0; i < N; i++) {
            board[i] = sc.nextLine();
        }
        int startSeed = Integer.parseInt(sc.nextLine());
        SquareRemover sr = new SquareRemover();
        int[] ret = sr.playIt(colors, board, startSeed);
        for (int i : ret) {
            System.out.println(i);
        }
        System.out.flush();
    }

    void init(int c, String[] b) {
        N = b.length;
        total = N * N * 2 - (2 * N);
        COLORS = c;
        rand = new Random(12346789);
        board = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = b[i].charAt(j) - '0';
            }
        }
        candidate = new PriorityQueue<State>();
    }

    State adjustScore(int step, int[][] board, int row, int col, int dir, int score, long seed, int[] result) {
        if (dir == 0) {
            board[row][col] ^= board[row - 1][col];
            board[row - 1][col] ^= board[row][col];
            board[row][col] ^= board[row - 1][col];
        } else if (dir == 1) {
            board[row][col] ^= board[row][col + 1];
            board[row][col + 1] ^= board[row][col];
            board[row][col] ^= board[row][col + 1];
        } else if (dir == 2) {
            board[row][col] ^= board[row + 1][col];
            board[row + 1][col] ^= board[row][col];
            board[row][col] ^= board[row + 1][col];
        } else if (dir == 3) {
            board[row][col] ^= board[row][col - 1];
            board[row][col - 1] ^= board[row][col];
            board[row][col] ^= board[row][col - 1];
        }
        int ret = 0;
        while (true) {
            boolean find = false;
            for (int i = 0; i < N - 1 && !find; i++) {
                for (int j = 0; j < N - 1 && !find; j++) {
                    int a = board[i][j];
                    int b = board[i][j + 1];
                    int c = board[i + 1][j];
                    int d = board[i + 1][j + 1];
                    if (a == b && a == c && a == d) {
                        find = true;
                        ret++;
                        board[i][j] = (int) seed % COLORS;
                        seed = (seed * MUL) % MOD;
                        board[i][j + 1] = (int) seed % COLORS;
                        seed = (seed * MUL) % MOD;
                        board[i + 1][j] = (int) seed % COLORS;
                        seed = (seed * MUL) % MOD;
                        board[i + 1][j + 1] = (int) seed % COLORS;
                        seed = (seed * MUL) % MOD;
                    }
                }
            }
            if (!find) {
                break;
            }
        }
        if (step > -1) {
            result[step * 3] = row;
            result[step * 3 + 1] = col;
            result[step * 3 + 2] = dir;
        }
        State state = new State(score + ret, seed, board, result);
        return state;
    }

    int adjustScore(int[][] cBoard, int row, int col, int dir, long cSeed) {
        if (dir == 0) {
            cBoard[row][col] ^= cBoard[row - 1][col];
            cBoard[row - 1][col] ^= cBoard[row][col];
            cBoard[row][col] ^= cBoard[row - 1][col];
        } else if (dir == 1) {
            cBoard[row][col] ^= cBoard[row][col + 1];
            cBoard[row][col + 1] ^= cBoard[row][col];
            cBoard[row][col] ^= cBoard[row][col + 1];
        } else if (dir == 2) {
            cBoard[row][col] ^= cBoard[row + 1][col];
            cBoard[row + 1][col] ^= cBoard[row][col];
            cBoard[row][col] ^= cBoard[row + 1][col];
        } else if (dir == 3) {
            cBoard[row][col] ^= cBoard[row][col - 1];
            cBoard[row][col - 1] ^= cBoard[row][col];
            cBoard[row][col] ^= cBoard[row][col - 1];
        }
        int ret = 0;
        while (true) {
            boolean find = false;
            for (int i = 0; i < N - 1 && !find; i++) {
                for (int j = 0; j < N - 1 && !find; j++) {
                    int a = cBoard[i][j];
                    int b = cBoard[i][j + 1];
                    int c = cBoard[i + 1][j];
                    int d = cBoard[i + 1][j + 1];
                    if (a == b && a == c && a == d) {
                        find = true;
                        ret += 500;
                        cBoard[i][j] = (int) cSeed % COLORS;
                        cSeed = (cSeed * MUL) % MOD;
                        cBoard[i][j + 1] = (int) cSeed % COLORS;
                        cSeed = (cSeed * MUL) % MOD;
                        cBoard[i + 1][j] = (int) cSeed % COLORS;
                        cSeed = (cSeed * MUL) % MOD;
                        cBoard[i + 1][j + 1] = (int) cSeed % COLORS;
                        cSeed = (cSeed * MUL) % MOD;
                    }
                }
            }
            if (!find) {
                break;
            }
        }
        return ret;
    }

    int[][] copyBoard(int[][] board) {
        int[][] ret = new int[N][N];
        for (int i = 0; i < N; i++) {
            System.arraycopy(board[i], 0, ret[i], 0, N);
        }
        return ret;
    }

    Prospect[] play(int[][] board, long seed) {
        Prospect[] ret = new Prospect[total];
        int counter = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                //kiri kanan
                if (j + 1 < N && board[i][j] != board[i][j + 1]) {
                    int cur = 0, cur2 = 0, cur3 = 0;
                    int aa = i - 1 >= 0 && j - 1 >= 0 ? board[i - 1][j - 1] : -1;
                    int bb = i - 1 >= 0 ? board[i - 1][j] : -1;
                    int cc = j - 1 >= 0 ? board[i][j - 1] : -1;
                    int kiri = board[i][j + 1];
                    int ee = i + 1 < N && j - 1 >= 0 ? board[i + 1][j - 1] : -1;
                    int ff = i + 1 < N ? board[i + 1][j] : -1;
                    int gg = i - 1 >= 0 ? board[i - 1][j + 1] : -1;
                    int hh = i - 1 >= 0 && j + 2 < N ? board[i - 1][j + 2] : -1;
                    int kanan = board[i][j];
                    int jj = j + 2 < N ? board[i][j + 2] : -1;
                    int kk = i + 1 < N ? board[i + 1][j + 1] : -1;
                    int ll = i + 1 < N && j + 2 < N ? board[i + 1][j + 2] : -1;

                    //abgh
                    //cdij
                    //efkl
                    //complete
                    if ((kiri == aa && kiri == bb && kiri == cc) || (kanan == gg && kanan == hh && kanan == jj)
                            || (kiri == cc && kiri == ee && kiri == ff) || (kanan == jj && kanan == kk && kanan == ll)) {
                        cur += adjustScore(copyBoard(board), i, j, 1, seed);
                    } else {
                        //1 move left
                        if ((kiri == aa && kiri == bb && kiri == ee) || (kiri == ee && kiri == ff && kiri == aa)
                                || (kiri == bb && kiri == gg && kiri == kk) || (kiri == ff && kiri == kk && kiri == gg)
                                || (kiri == bb && kiri == gg && kiri == jj) || (kiri == ff && kiri == kk && kiri == jj)
                                || (kiri == aa && kiri == cc && kiri == gg) || (kiri == cc && kiri == ee && kiri == kk)) {
                            cur += 90;
                        }
                        //1 move left
                        if ((kanan == gg && kanan == hh && kanan == ll) || (kanan == ll && kanan == hh && kanan == kk)
                                || (kanan == bb && kanan == gg && kanan == ff) || (kanan == ff && kanan == kk && kanan == bb)
                                || (kanan == cc && kanan == ff && kanan == kk) || (kanan == bb && kanan == gg && kanan == cc)
                                || (kanan == bb && kanan == hh && kanan == jj) || (kanan == ff && kanan == ll && kanan == jj)) {
                            cur += 90;
                        }
                        //2 move left
                        if ((kiri == aa && kiri == bb && kiri == ff) || (kiri == ee && kiri == ff && kiri == bb)
                                || (kiri == aa && kiri == cc && kiri == ff) || (kiri == ee && kiri == cc && kiri == bb)
                                || (kiri == bb && kiri == gg && kiri == hh) || (kiri == ff && kiri == kk && kiri == ll)) {
                            cur += 29;
                        }
                        //2 move left
                        if ((kanan == gg && kanan == hh && kanan == kk) || (kanan == gg && kanan == kk && kanan == ll)
                                || (kanan == gg && kanan == jj && kanan == ll) || (kanan == hh && kanan == jj && kanan == kk)
                                || (kanan == aa && kanan == bb && kanan == gg) || (kanan == ee && kanan == ff && kanan == kk)) {
                            cur += 29;
                        }
                        //3 move left
                        if ((kiri == aa && kiri == bb) || (kiri == ee && kiri == ff)) {
                            cur += 1;
                        }
                        //3 move left
                        if ((kanan == gg && kanan == hh) || (kanan == kk && kanan == ll)) {
                            cur += 1;
                        }
                    }
                    int res = cur + cur2 + cur3;
                    ret[counter++] = new Prospect(res, i, j, 1);
                } else if (j + 1 < N) {
                    ret[counter++] = new Prospect(0, i, j, 1);
                }

                //atas bawah
                if (i + 1 < N && board[i][j] != board[i + 1][j]) {
                    int cur = 0, cur2 = 0, cur3 = 0;
                    int aa = i - 1 >= 0 && j - 1 >= 0 ? board[i - 1][j - 1] : -1;
                    int bb = j - 1 >= 0 ? board[i][j - 1] : -1;
                    int cc = i - 1 >= 0 ? board[i - 1][j] : -1;
                    int atas = board[i + 1][j];
                    int ee = i - 1 >= 0 && j + 1 < N ? board[i - 1][j + 1] : -1;
                    int ff = j + 1 < N ? board[i][j + 1] : -1;
                    int gg = j - 1 >= 0 ? board[i + 1][j - 1] : -1;
                    int hh = i + 2 < N && j - 1 >= 0 ? board[i + 2][j - 1] : -1;
                    int bawah = board[i][j];
                    int jj = i + 2 < N ? board[i + 2][j] : -1;
                    int kk = j + 1 < N ? board[i + 1][j + 1] : -1;
                    int ll = i + 2 < N && j + 1 < N ? board[i + 2][j + 1] : -1;

                    //a c e
                    //b d f
                    //g i k
                    //h j l
                    if ((atas == aa && atas == cc && atas == bb) || (atas == cc && atas == ee && atas == ff)
                            || (bawah == gg && bawah == jj && bawah == hh) || (bawah == jj && bawah == kk && bawah == ll)) {
                        cur += adjustScore(copyBoard(board), i, j, 2, seed);
                    } else {
                        //1 move left
                        if ((atas == aa && atas == bb && atas == ee) || (atas == aa && atas == ee && atas == ff)
                                || (atas == bb && atas == gg && atas == jj) || (atas == ff && atas == kk && atas == jj)
                                || (atas == aa && atas == cc && atas == gg) || (atas == cc && atas == ee && atas == kk)
                                || (atas == bb && atas == gg && atas == kk) || (atas == ff && atas == kk && atas == bb)) {
                            cur += 90;
                        }
                        //1 move left
                        if ((bawah == gg && bawah == hh && bawah == ll) || (bawah == kk && bawah == ll && bawah == hh)
                                || (bawah == bb && bawah == gg && bawah == cc) || (bawah == ff && bawah == kk && bawah == cc)
                                || (bawah == hh && bawah == jj && bawah == bb) || (bawah == jj && bawah == ll && bawah == ff)
                                || (bawah == bb && bawah == gg && bawah == ff) || (bawah == ff && bawah == kk && bawah == bb)) {
                            cur += 90;
                        }
                        //2 move left
                        if ((atas == aa && atas == bb && atas == ff) || (atas == ee && atas == ff && atas == bb)
                                || (atas == aa && atas == cc && atas == ff) || (atas == bb && atas == cc && atas == ee)
                                || (atas == bb && atas == gg && atas == hh) || (atas == ff && atas == kk && atas == ll)) {
                            cur += 29;
                        }
                        //2 move left
                        if ((bawah == hh && bawah == gg && bawah == kk) || (bawah == kk && bawah == ll && bawah == gg)
                                || (bawah == hh && bawah == kk && bawah == jj) || (bawah == gg && bawah == jj && bawah == ll)
                                || (bawah == aa && bawah == bb && bawah == gg) || (bawah == ee && bawah == ff && bawah == kk)) {
                            cur += 29;
                        }
                        //3 move left
                        if ((atas == aa && atas == bb) || (atas == ee && atas == ff)) {
                            cur += 1;
                        }
                        //3 move left
                        if ((bawah == gg && bawah == hh) || (bawah == kk && bawah == ll)) {
                            cur += 1;
                        }
                    }
                    int res = cur + cur2 + cur3;
                    ret[counter++] = new Prospect(res, i, j, 2);
                } else if (i + 1 < N) {
                    ret[counter++] = new Prospect(0, i, j, 2);
                }
            }
        }
        return ret;
    }

    int[] copyResult(int[] result) {
        int[] ret = new int[30000];
        System.arraycopy(result, 0, ret, 0, result.length);
        return ret;
    }

    int[] playIt(int c, String[] b, int s) {
        setupError();
        init(c, b);
        State cur = adjustScore(-1, board, -1, -1, -1, 0, s, new int[30000]);
        System.err.println("Score awal: " + cur.score);
        candidate.add(cur);
        int batas = 8;
        int batas2 = 2;
        if (N > 15) {
            batas = 7;
        }
        if (N == 8) {
            batas = 9;
        }
        for (int i = 0; i < 10000; i++) {
            int cnt = 0;
            newCan = new PriorityQueue<State>();
            long xx = (System.nanoTime() - START_TIME) / 1000000;
            if (xx > TIME_OUT2) {
                batas = 1;
                batas2 = 1;
            } else if (xx > TIME_OUT1) {
                batas = 2;
            }
            while (!candidate.isEmpty() && cnt < batas) {
                cnt++;
                State state = candidate.poll();
                Prospect[] lp = play(state.board, state.seed);
                Arrays.sort(lp);

                Prospect pp = lp[0];
                if (pp.score < 29) {
                    int x = 0;
                    while (x < batas2) {
                        int rowRand = rand.nextInt(N - 1);
                        int colRand = rand.nextInt(N - 1);
                        if (rowRand == 0) {
                            rowRand++;
                        }
                        if (colRand == 0) {
                            colRand++;
                        }
                        int dir = rand.nextInt(10) % 4;
                        boolean ok = true;
                        if ((dir == 0 && state.board[rowRand][colRand] == state.board[rowRand - 1][colRand])
                                || (dir == 1 && state.board[rowRand][colRand] == state.board[rowRand][colRand + 1])
                                || (dir == 2 && state.board[rowRand][colRand] == state.board[rowRand + 1][colRand])
                                || (dir == 3 && state.board[rowRand][colRand] == state.board[rowRand][colRand - 1])) {
                            ok = false;
                        }
                        if (ok) {
                            State newState = adjustScore(i, copyBoard(state.board), rowRand, colRand, dir, state.score, state.seed, copyResult(state.result));
                            newCan.add(newState);
                            x++;
                        }
                    }
                    continue;
                }

                for (int j = 0; j < batas2; j++) {
                    Prospect p = lp[j];
                    State newState = adjustScore(i, copyBoard(state.board), p.row, p.col, p.dir, state.score, state.seed, copyResult(state.result));
                    newCan.add(newState);
                }
            }
            candidate.clear();
            candidate.addAll(newCan);
        }
        State ret = candidate.poll();
        int score = ret.score;
        System.err.println("Usage Time: " + (System.nanoTime() - START_TIME) / 1000000);
        System.err.println("Score: " + score);
        return ret.result;
    }

    void printBoard(int[][] board) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.err.print(board[i][j] + " ");
            }
            System.err.println();
        }
        System.err.println("==================");
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
}

class State implements Comparable<State> {

    int score;
    long seed;
    int[][] board;
    int[] result;

    public State(int score, long seed, int[][] board, int[] result) {
        this.score = score;
        this.seed = seed;
        this.board = board;
        this.result = result;
    }

    @Override
    public int compareTo(State o) {
        if (score > o.score) {
            return -1;
        }
        return 1;
    }
}

class Prospect implements Comparable<Prospect> {

    int score, row, col, dir;

    public Prospect(int score, int row, int col, int dir) {
        this.score = score;
        this.row = row;
        this.col = col;
        this.dir = dir;
    }

    @Override
    public int compareTo(Prospect o) {
        if (score > o.score) {
            return -1;
        } else if (score < o.score) {
            return 1;
        }
        return 0;
    }
}