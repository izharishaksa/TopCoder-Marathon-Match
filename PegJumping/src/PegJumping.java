
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * @author izharishaksa
 */
public class PegJumping {

    static long TIME_LIMIT = 14500;
    long TIMEOUT = System.currentTimeMillis() + TIME_LIMIT;
    int SIZE;
    int[][] b;
    int[] value;

    Sequence doIt(int i, int j, int lastRow, int lastCol, String paths, int sc) {
        Sequence ret = new Sequence();
        ret.row = i;
        ret.col = j;
        ret.val = b[i][j];
        ret.score = sc;
        ret.path = paths;
        ret.lastRow = lastRow;
        ret.lastCol = lastCol;

        int[][] visit = new int[SIZE][SIZE];
        for (int ii = 0; ii < SIZE; ii++) {
            System.arraycopy(b[ii], 0, visit[ii], 0, SIZE);
        }

        int cRow = i, cCol = j;
        for (int ii = 0; ii < paths.length(); ii++) {
            visit[cRow][cCol] = -1;
            char c = paths.charAt(ii);
            if (c == 'U') {
                visit[cRow - 1][cCol] = -1;
                cRow -= 2;
            } else if (c == 'R') {
                visit[cRow][cCol + 1] = -1;
                cCol += 2;
            } else if (c == 'D') {
                visit[cRow + 1][cCol] = -1;
                cRow += 2;
            } else {
                visit[cRow][cCol - 1] = -1;
                cCol -= 2;
            }
            visit[cRow][cCol] = ret.val;
        }

        Stack<Integer> row = new Stack<>();
        Stack<Integer> col = new Stack<>();
        Stack<Integer> score = new Stack<>();
        Stack<int[][]> used = new Stack<>();
        Stack<StringBuffer> path = new Stack<>();
        Stack<Integer> len = new Stack<>();
        row.add(cRow);
        col.add(cCol);
        score.add(sc);
        visit[cRow][cCol] = -1;
        used.add(visit);
        path.add(new StringBuffer(paths));
        len.add(paths.length());

        while (!row.isEmpty() && System.currentTimeMillis() < TIMEOUT) {
            int curRow = row.pop();
            int curCol = col.pop();
            int[][] curVisit = used.pop();
            int curScore = score.pop();
            StringBuffer curPath = path.pop();
            int curLen = len.pop();

            boolean ada = false;
            for (int x = 0; x < 4; x++) {
                int nextRow = curRow + dir[x][0];
                int nextCol = curCol + dir[x][1];
                int overRow = curRow + prev[x][0];
                int overCol = curCol + prev[x][1];
                if (nextRow >= 0 && nextRow < SIZE && nextCol >= 0 && nextCol < SIZE
                        && curVisit[overRow][overCol] > -1 && curVisit[nextRow][nextCol] < 0) {
                    row.add(nextRow);
                    col.add(nextCol);

                    int culScore = curScore + value[b[overRow][overCol]];
                    score.add(culScore);
                    int[][] tempVisit = new int[SIZE][SIZE];
                    for (int ii = 0; ii < SIZE; ii++) {
                        System.arraycopy(curVisit[ii], 0, tempVisit[ii], 0, SIZE);
                    }
                    tempVisit[nextRow][nextCol] = -1;
                    tempVisit[overRow][overCol] = -1;
                    used.add(tempVisit);
                    len.add(curLen + 1);
                    path.add(new StringBuffer(curPath).append(new StringBuffer().append(SIGN[x])));
                    ada = true;
                }
            }
            if (!ada) {
                int vval = curScore * curLen;
                if (vval > ret.score * ret.path.length()) {
                    ret.score = curScore;
                    ret.path = curPath.toString();
                    ret.lastRow = curRow;
                    ret.lastCol = curCol;
                }
            }
        }
        return ret;
    }

    int nPeg = 0;

    void init(int[] v, String[] board) {
        value = new int[v.length];
        System.arraycopy(v, 0, value, 0, value.length);
        SIZE = board.length;
        b = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                char c = board[i].charAt(j);
                b[i][j] = c == '.' ? -1 : (c - '0');
                if (b[i][j] > -1) {
                    nPeg++;
                }
            }
        }
    }

    void printB(int[][] bb) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.err.print((bb[i][j] > -1 ? " " : "") + bb[i][j] + " ");
            }
            System.err.println();
        }
    }

    int[][] dir = new int[][]{{-2, 0}, {0, 2}, {2, 0}, {0, -2}};
    int[][] prev = new int[][]{{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    final char[] SIGN = new char[]{'U', 'R', 'D', 'L'};

    void modify(int curRow, int curCol, int[][] tempB, Sequence seq, State curStates) {
        for (int i = 0; i < 4; i++) {
            int prevRow = curRow + prev[i][0];
            int prevCol = curCol + prev[i][1];
            int nextRow = curRow + dir[i][0];
            int nextCol = curCol + dir[i][1];
            if (prevRow >= 0 && prevRow < SIZE && prevCol >= 0 && prevCol < SIZE
                    && nextRow >= 0 && nextRow < SIZE && nextCol >= 0 && nextCol < SIZE
                    && tempB[prevRow][prevCol] > -1 && tempB[nextRow][nextCol] > -1) {
                int[][] temp = new int[][]{{1, 0, -1, 0}, {0, -1, 0, 1}, {-1, 0, 1, 0}, {0, 1, 0, -1}};
                for (int j = 0; j < 4; j++) {
                    int aa = nextRow + temp[j][0];
                    int bb = nextCol + temp[j][1];
                    int cc = nextRow + temp[j][2];
                    int dd = nextCol + temp[j][3];

                    if (aa >= 0 && aa < SIZE && bb >= 0 && bb < SIZE
                            && cc >= 0 && cc < SIZE && dd >= 0 && dd < SIZE
                            && b[aa][bb] > -1 && b[cc][dd] < 0
                            && b[aa][bb] == tempB[aa][bb]
                            && b[cc][dd] == tempB[cc][dd]) {
                        int[][] tempBoard = new int[SIZE][SIZE];
                        for (int xx = 0; xx < SIZE; xx++) {
                            System.arraycopy(b[xx], 0, tempBoard[xx], 0, SIZE);
                        }
                        int tempVal = value[tempBoard[nextRow][nextCol]];
                        tempBoard[nextRow][nextCol] = -1;
                        tempBoard[cc][dd] = tempB[aa][bb];
                        tempBoard[aa][bb] = -1;

                        State tempState = new State(tempBoard);
                        if (!map.containsKey(tempState)) {
                            map.put(tempState, true);
                            tempState.setSequence(seq);
                            tempState.score = curStates.score + tempVal;
                            tempState.pScore = seq.score * seq.path.length();
                            tempState.moves = new ArrayList<>();
                            for (String s : curStates.moves) {
                                tempState.moves.add(s);
                            }
                            tempState.moves.add(aa + " " + bb + " " + SIGN[j]);
                            tempState.rScore = (tempState.pScore + tempState.score) / tempState.moves.size();
                            newStates.offer(tempState);
                        }
                    }
                }
                for (int j = 0; j < 4; j++) {
                    int prevRow2 = nextRow + prev[j][0];
                    int prevCol2 = nextCol + prev[j][1];
                    int nextRow2 = nextRow + dir[j][0];
                    int nextCol2 = nextCol + dir[j][1];

                    if (nextRow2 == curRow && nextCol2 == curCol) {
                        continue;
                    }
                    if (prevRow2 >= 0 && prevRow2 < SIZE && prevCol2 >= 0 && prevCol2 < SIZE
                            && nextRow2 >= 0 && nextRow2 < SIZE && nextCol2 >= 0 && nextCol2 < SIZE
                            && b[prevRow2][prevCol2] > -1 && b[nextRow2][nextCol2] < 0
                            && tempB[prevRow2][prevCol2] > -1 && tempB[nextRow2][nextCol2] < 0) {
                        int[][] tempBoard = new int[SIZE][SIZE];
                        for (int xx = 0; xx < SIZE; xx++) {
                            System.arraycopy(b[xx], 0, tempBoard[xx], 0, SIZE);
                        }
                        int tempVal = value[tempBoard[prevRow2][prevCol2]];
                        tempBoard[nextRow2][nextCol2] = tempBoard[nextRow][nextCol];
                        tempBoard[nextRow][nextCol] = -1;
                        tempBoard[prevRow2][prevCol2] = -1;
                        State tempState = new State(tempBoard);
                        if (!map.containsKey(tempState)) {
                            map.put(tempState, true);
                            tempState.setSequence(seq);
                            tempState.score = curStates.score + tempVal;
                            tempState.pScore = seq.score * seq.path.length();
                            tempState.moves = new ArrayList<>();
                            for (String s : curStates.moves) {
                                tempState.moves.add(s);
                            }
                            tempState.moves.add(nextRow + " " + nextCol + " " + SIGN[j]);
                            tempState.rScore = (tempState.pScore + tempState.score) / tempState.moves.size();
                            //newStates.offer(tempState);
                        }
                    }
                }
            }
            if (prevRow >= 0 && prevRow < SIZE && prevCol >= 0 && prevCol < SIZE
                    && tempB[prevRow][prevCol] < 0) {
                for (int j = 0; j < 4; j++) {
                    int prevRow2 = prevRow + prev[j][0];
                    int prevCol2 = prevCol + prev[j][1];
                    int nextRow2 = prevRow + dir[j][0];
                    int nextCol2 = prevCol + dir[j][1];

                    if (prevRow2 >= 0 && prevRow2 < SIZE && prevCol2 >= 0 && prevCol2 < SIZE
                            && nextRow2 >= 0 && nextRow2 < SIZE && nextCol2 >= 0 && nextCol2 < SIZE
                            && b[prevRow][prevCol] == tempB[prevRow][prevCol]
                            && b[prevRow2][prevCol2] > -1 && b[nextRow2][nextCol2] > -1
                            && tempB[prevRow2][prevCol2] > -1 && tempB[nextRow2][nextCol2] > -1) {

                        int[][] tempBoard = new int[SIZE][SIZE];
                        for (int xx = 0; xx < SIZE; xx++) {
                            System.arraycopy(b[xx], 0, tempBoard[xx], 0, SIZE);
                        }
                        int tempVal = value[tempBoard[prevRow2][prevCol2]];
                        tempBoard[prevRow][prevCol] = tempBoard[nextRow2][nextCol2];
                        tempBoard[prevRow2][prevCol2] = -1;
                        tempBoard[nextRow2][nextCol2] = -1;

                        State tempState = new State(tempBoard);
                        if (!map.containsKey(tempState)) {
                            map.put(tempState, true);
                            tempState.setSequence(seq);
                            tempState.score = curStates.score + tempVal;
                            tempState.pScore = seq.score * seq.path.length();
                            tempState.moves = new ArrayList<>();
                            for (String s : curStates.moves) {
                                tempState.moves.add(s);
                            }
                            char[] sign2 = new char[]{'D', 'L', 'U', 'R'};
                            tempState.moves.add(nextRow2 + " " + nextCol2 + " " + sign2[j]);
                            tempState.rScore = (tempState.pScore + tempState.score) / tempState.moves.size();
                            newStates.offer(tempState);
                        }
                    }
                }
            }
        }
    }

    PriorityQueue<State> newStates;
    Map<State, Boolean> map;

    public String[] getMoves(int[] v, String[] board) {
        debug();
        init(v, board);

        PriorityQueue<State> states = new PriorityQueue<>();
        State st = new State(b);
        states.add(st);

        State bestState = new State(b);

        int width = 41;
        if (nPeg % 2 == 0) {
            width = 42;
        }
        int batas = width;
        map = new HashMap<>();
        int STEP = 0;
        while (!states.isEmpty() && System.currentTimeMillis() < TIMEOUT) {
            newStates = new PriorityQueue<>();
            while (!states.isEmpty() && System.currentTimeMillis() < TIMEOUT) {
                State curStates = states.poll();
                for (int i = 0; i < SIZE; i++) {
                    System.arraycopy(curStates.board[i], 0, this.b[i], 0, SIZE);
                }
                Sequence curSeq = curStates.seq;

                PriorityQueue<Sequence> pq = new PriorityQueue<>();
                if (curSeq != null) {
                    //System.err.println(curStates.pScore + " " + curStates.score);
                    Sequence seq2 = doIt(curSeq.row, curSeq.col, curSeq.lastRow, curSeq.lastCol, curSeq.path, curSeq.score);
                    if (seq2 != null && seq2.path.length() > batas) {
                        pq.add(seq2);
                    }
                }
                if (pq.isEmpty()) {
                    for (int i = 0; i < SIZE; i++) {
                        for (int j = 0; j < SIZE; j++) {
                            if (b[i][j] > -1) {
                                Sequence seq = doIt(i, j, i, j, "", 0);
                                if (seq.score > 0) {
                                    pq.add(seq);
                                }
                            }
                        }
                    }
                    batas--;
                    if (batas < 1) {
                        batas = 1;
                    }
                }

                int it = 0;
                while (!pq.isEmpty() && System.currentTimeMillis() < TIMEOUT && it++ < width) {
                    Sequence seq = pq.poll();
                    int curRow = seq.row;
                    int curCol = seq.col;

                    int[][] tempB = new int[SIZE][SIZE];
                    for (int i = 0; i < SIZE; i++) {
                        System.arraycopy(b[i], 0, tempB[i], 0, SIZE);
                    }

                    StringBuffer path = new StringBuffer();
                    for (int ii = 0; ii < seq.path.length(); ii++) {
                        tempB[curRow][curCol] = -1;
                        char c = seq.path.charAt(ii);
                        path = path.append(c);
                        if (c == 'U') {
                            tempB[curRow - 1][curCol] = -1;
                            curRow -= 2;
                        } else if (c == 'R') {
                            tempB[curRow][curCol + 1] = -1;
                            curCol += 2;
                        } else if (c == 'D') {
                            tempB[curRow + 1][curCol] = -1;
                            curRow += 2;
                        } else {
                            tempB[curRow][curCol - 1] = -1;
                            curCol -= 2;
                        }
                        tempB[curRow][curCol] = seq.val;
                    }

                    int[][] temp = new int[SIZE][SIZE];
                    for (int i = 0; i < SIZE; i++) {
                        System.arraycopy(tempB[i], 0, temp[i], 0, SIZE);
                    }
                    State tempState = new State(temp);

                    tempB[curRow][curCol] = -1;
                    modify(curRow, curCol, tempB, seq, curStates);

                    if (!map.containsKey(tempState)) {
                        map.put(tempState, true);
                        tempState.score = curStates.score + (seq.score * seq.path.length());
                        tempState.moves = new ArrayList<>();
                        for (String s : curStates.moves) {
                            tempState.moves.add(s);
                        }
                        tempState.moves.add(seq.row + " " + seq.col + " " + path);
                        tempState.rScore = (tempState.pScore + tempState.score) / tempState.moves.size();
                        newStates.offer(tempState);
                    }
                }
            }
            int count = 0, pScore = 0;
            boolean ada = false;
            int[][] cal = new int[SIZE][SIZE];
            while (!newStates.isEmpty() && count < width) {
                State curState = newStates.poll();

                if (curState.score > bestState.score) {
                    bestState = new State(curState.board);
                    for (String s : curState.moves) {
                        bestState.moves.add(s);
                    }
                    bestState.score = curState.score;
                }
                if ((pScore == 0 || curState.pScore > pScore / 1.5) && curState.seq != null && cal[curState.seq.row][curState.seq.col] < 4) {
                    if (count == 0) {
                        pScore = curState.pScore;
                    }
                    states.offer(curState);
                    count++;
                    cal[curState.seq.row][curState.seq.col]++;
                }
            }
            if (count == 0) {
                State nS = new State(bestState.board);
                for (String s : bestState.moves) {
                    nS.moves.add(s);
                }
                nS.score = bestState.score;
                states.offer(nS);
            }
            //System.err.println("---------------------");
        }
        for (String s : bestState.moves) {
            //System.err.println(s);
        }
        System.err.println(bestState.score);
        return bestState.moves.toArray(new String[0]);
    }

    void debug() {
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
        int M = Integer.parseInt(sc.nextLine());
        int[] value = new int[M];
        for (int i = 0; i < M; i++) {
            value[i] = Integer.parseInt(sc.nextLine());
        }
        int N = Integer.parseInt(sc.nextLine());
        String[] board = new String[N];
        for (int i = 0; i < N; i++) {
            board[i] = sc.nextLine();
        }
        PegJumping pj = new PegJumping();
        String[] ret = pj.getMoves(value, board);
        System.out.println(ret.length);
        for (String s : ret) {
            System.out.println(s);
        }
        System.out.flush();
    }

}

class Sequence implements Comparable<Sequence> {

    int score, row, col, val, lastRow, lastCol;
    String path;

    public Sequence() {
        path = "";
    }

    @Override
    public int compareTo(Sequence o) {
        return o.score - score;
    }

    @Override
    public String toString() {
        return row + "," + col + " => " + score + " => " + path;
    }

}

class State implements Comparable<State> {

    int score, pScore, rScore;
    int[][] board;
    List<String> moves;
    Sequence seq;

    public State(int[][] b) {
        seq = null;
        moves = new ArrayList<>();
        board = new int[b.length][b.length];
        for (int i = 0; i < b.length; i++) {
            System.arraycopy(b[i], 0, board[i], 0, b.length);
        }
    }

    void setSequence(Sequence s) {
        seq = new Sequence();
        seq.row = s.row;
        seq.col = s.col;
        seq.val = s.val;
        seq.path = s.path;
        seq.score = s.score;
        seq.lastRow = s.lastRow;
        seq.lastCol = s.lastCol;
    }

    @Override
    public int compareTo(State o) {
        return o.rScore - rScore;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof State)) {
            return false;
        }
        State state = (State) obj;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (!(board[i][j] > -1 && state.board[i][j] > -1)) {
                    return false;
                }
            }
        }
        return true;
    }

}
