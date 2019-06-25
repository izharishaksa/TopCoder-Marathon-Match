package MovingNQueens;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * @author izharishaksa
 */
public class MovingNQueens {

    static final long TIME_OUT = 9700;
    static final long START_TIME = System.nanoTime();
    int N, MAX_MOVES;
    int[][] dirCursor = new int[][]{{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};

    int threatenNumber(Queen[] queens) {
        int ret = 0;
        for (int i = 0; i < queens.length; i++) {
            queens[i].threats = 0;
        }
        for (int i = 0; i < queens.length; i++) {
            Queen q1 = queens[i];
            for (int j = i + 1; j < queens.length; j++) {
                Queen q2 = queens[j];
                if (q1.row == q2.row
                        || q1.col == q2.col
                        || Math.abs(q1.row - q2.row) == Math.abs(q1.col - q2.col)) {
                    ret++;
                    queens[i].threats++;
                    queens[j].threats++;
                }
            }
        }
        return ret;
    }

    boolean isValidMove(int dr, int dc) {
        return dr == dc || dr == -dc;
    }

    public String[] rearrange(int[] row, int[] col) throws CloneNotSupportedException {
        log();
        N = row.length;
        MAX_MOVES = 8 * N;

        Queen[] queens = new Queen[N];
        for (int i = 0; i < N; i++) {
            queens[i] = new Queen(i, row[i], col[i], 0);
        }
        String[] initMove = new String[MAX_MOVES];
        Arrays.fill(initMove, "");
        State state = new State(0, threatenNumber(queens), 0, initMove, queens);

        PriorityQueue<State> q = new PriorityQueue<State>();
        q.add(state);

        Map<State, Boolean> stateMap = new HashMap<State, Boolean>();
        stateMap.put(state, true);

        State bestState = (State) state.clone();
        int iteration = 0, found = 0, batas = 101 / N;
        while (!q.isEmpty() && System.nanoTime() - START_TIME < TIME_OUT * 1000000) {
            iteration++;
            State curState = q.poll();
            if (curState.moveCount >= MAX_MOVES && curState.threatScore > 0) {
                continue;
            }
            if (curState.threatScore == 0) {
                found++;
                if (curState.score < bestState.score || bestState.score == 0) {
                    bestState = (State) curState.clone();
                }
                continue;
            }
            Queen[] curQueen = curState.queens;
            Arrays.sort(curQueen);
            for (int index = 0; index < N; index++) {
                int i = curQueen[index].index;
                if (curQueen[i].threats == 0) {
                    continue;
                }
                int curRow = curQueen[i].row;
                int curCol = curQueen[i].col;
                boolean[] canMove = new boolean[8];
                Arrays.fill(canMove, true);
                Cell[] canMoveList = new Cell[8];
                for (int j = 0; j < 8; j++) {
                    canMoveList[j] = new Cell(0, 0, curQueen[i].threats, Integer.MAX_VALUE);
                }
                int move = 1;
                do {
                    for (int j = 0; j < 8; j++) {
                        if (!canMove[j]) {
                            continue;
                        }
                        int candidateRow = curRow + (move * dirCursor[j][0]);
                        int candidateCol = curCol + (move * dirCursor[j][1]);
                        int nThreats = 0;
                        for (int k = 0; k < N && canMove[j]; k++) {
                            if (i == k) {
                                continue;
                            }
                            nThreats += isValidMove(candidateRow - curQueen[k].row, candidateCol - curQueen[k].col) ? 1 : 0;
                            nThreats += candidateRow == curQueen[k].row ? 1 : 0;
                            nThreats += candidateCol == curQueen[k].col ? 1 : 0;
                            if (candidateRow == curQueen[k].row && candidateCol == curQueen[k].col) {
                                canMove[j] = false;
                            }
                        }
                        if (canMove[j]) {
                            if (nThreats < canMoveList[j].threats) {
                                canMoveList[j].threats = nThreats;
                                canMoveList[j].move = move;
                                canMoveList[j].row = candidateRow;
                                canMoveList[j].col = candidateCol;
                            }
                            if (nThreats == 0) {
                                canMove[j] = false;
                            }
                        }
                    }
                } while (move++ < N);

                Arrays.sort(canMoveList);
                for (int ii = 0, jj = 0; ii < 8 && jj < batas; ii++) {
                    if (canMoveList[ii].threats == curQueen[i].threats) {
                        continue;
                    }
                    Cell cell = canMoveList[ii];
                    State newState = (State) curState.clone();
                    newState.score += cell.move;
                    newState.queens[i].row = cell.row;
                    newState.queens[i].col = cell.col;
                    StringBuilder gg = new StringBuilder();
                    gg.append(curQueen[i].index).append(" ").append(cell.row).append(" ").append(cell.col);
                    newState.moves[newState.moveCount] = gg.toString();
                    newState.moveCount++;
                    newState.threatScore = threatenNumber(newState.queens);

                    if (!stateMap.containsKey(newState) && (newState.score < bestState.score || bestState.score == 0)) {
                        stateMap.put(newState, true);
                        q.add(newState);
                        jj++;
                    }
                }
            }
        }

        System.err.println("================");
        System.err.println("Best Score: " + bestState.score);
        System.err.println("N: " + N);
        System.err.println("MoveCount: " + bestState.moveCount);
        System.err.println("Found: " + found);
        System.err.println("Total Iterasi: " + iteration);
        System.err.println("Unprocessed: " + q.size());
        String[] ret = new String[bestState.moveCount];
        System.arraycopy(bestState.moves, 0, ret, 0, ret.length);

        return ret;
    }

    void log() {
        try {
            PrintStream console = System.err;
            File file = new File("log.txt");
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);
            System.setErr(ps);
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] row = new int[n];
        for (int i = 0; i < n; i++) {
            row[i] = sc.nextInt();
        }
        int m = sc.nextInt();
        int[] col = new int[m];
        for (int i = 0; i < m; i++) {
            col[i] = sc.nextInt();
        }
        MovingNQueens mnq = new MovingNQueens();
        String[] ret = mnq.rearrange(row, col);
        System.out.println(ret.length);
        for (int i = 0; i < ret.length; i++) {
            System.out.println(ret[i]);
        }
        System.out.flush();
    }
}

class State implements Comparable<State>, Cloneable {

    int score;
    int moveCount;
    int threatScore;
    String[] moves;
    Queen[] queens;
    int max;

    public State(int score, int threatScore, int moveCount, String[] moves, Queen[] queens) {
        this.queens = queens;
        this.threatScore = threatScore;
        this.score = score;
        this.moveCount = moveCount;
        this.moves = moves;
        this.max = queens.length * queens.length;
    }

    @Override
    public int compareTo(State o) {
        if (threatScore < o.threatScore) {
            return -1;
        } else if (threatScore > o.threatScore) {
            return 1;
        }
        if (score < o.score) {
            return -1;
        } else if (score > o.score) {
            return 1;
        }
        if (moveCount < o.moveCount) {
            return -1;
        }
        return 1;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        State state = (State) super.clone();
        state.queens = new Queen[state.queens.length];
        for (int i = 0; i < state.queens.length; i++) {
            state.queens[i] = (Queen) this.queens[i].clone();
        }
        state.moves = new String[moves.length];
        System.arraycopy(moves, 0, state.moves, 0, moves.length);
        return state;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Score : ").append(score).append("\n");
        sb.append("MoveCount: ").append(moveCount).append("\n");
        sb.append("ThreatScore: ").append(threatScore).append("\n");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof State)) {
            return false;
        }
        State obj = (State) o;
        for (int i = 0; i < queens.length; i++) {
            if (!(queens[i].row == obj.queens[i].row && queens[i].col == obj.queens[i].col)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Arrays.deepHashCode(this.queens);
        return hash;
    }
}

class Queen implements Cloneable, Comparable<Queen> {

    int row, col, index, threats;

    public Queen(int index, int row, int col, int threats) {
        this.index = index;
        this.row = row;
        this.col = col;
        this.threats = threats;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(index).append(" => ").append(row).append(",").append(col);
        return sb.toString();
    }

    @Override
    public int compareTo(Queen o) {
        return threats - o.threats;
    }
}

class Cell implements Comparable<Cell> {

    int row, col, threats, move;

    public Cell(int row, int col, int threats, int move) {
        this.row = row;
        this.col = col;
        this.threats = threats;
        this.move = move;
    }

    @Override
    public int compareTo(Cell o) {
        if (threats < o.threats) {
            return -1;
        }
        if (threats > o.threats) {
            return 1;
        }
        return move - o.move;
    }
}