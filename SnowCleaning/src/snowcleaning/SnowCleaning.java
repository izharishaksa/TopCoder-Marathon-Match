package snowcleaning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Izhari Ishak Aksa
 */
public class SnowCleaning {

    private int boardSize = 0;
    private int salary = 0;
    private int snowFine = 0;
    private int workerNumber = 0;
    private long start;
    /**
     * Represents worker in the board.
     */
    private boolean[][] board = null;
    /**
     * Represents board in each day.
     */
    private boolean[][] tempBoard = null;
    /**
     * Represents the list of command in each day.
     */
    private List<String> ret = null;
    /**
     * Represents workers that has been commanded.
     */
    private boolean[] used = null;
    /**
     * Represents workers.
     */
    private Worker[] workers = null;
    private int[][] workersOnBoard = null;

    private void moveUp(Worker w, int i) {
        ret.add("M " + w.id + " U");
        used[w.id] = true;
        board[w.curRow][w.curCol] = false;
        workers[i].curRow--;
        tempBoard[w.curRow][w.curCol] = false;
        board[w.curRow][w.curCol] = true;
    }

    private void moveDown(Worker w, int i) {
        ret.add("M " + w.id + " D");
        used[w.id] = true;
        board[w.curRow][w.curCol] = false;
        workers[i].curRow++;
        tempBoard[w.curRow][w.curCol] = false;
        board[w.curRow][w.curCol] = true;
    }

    private void moveLeft(Worker w, int i) {
        ret.add("M " + w.id + " L");
        used[w.id] = true;
        board[w.curRow][w.curCol] = false;
        workers[i].curCol--;
        tempBoard[w.curRow][w.curCol] = false;
        board[w.curRow][w.curCol] = true;
    }

    private void moveRight(Worker w, int i) {
        ret.add("M " + w.id + " R");
        used[w.id] = true;
        board[w.curRow][w.curCol] = false;
        workers[i].curCol++;
        tempBoard[w.curRow][w.curCol] = false;
        board[w.curRow][w.curCol] = true;
    }

    public int init(int boardSize, int salary, int snowFine) {
        this.start = System.currentTimeMillis();
        this.boardSize = boardSize;
        this.salary = salary;
        this.snowFine = snowFine;
        this.board = new boolean[boardSize][boardSize];
        this.tempBoard = new boolean[boardSize][boardSize];
        this.workers = new Worker[101];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = false;
                tempBoard[i][j] = false;
            }
        }
        workersOnBoard = new int[boardSize][boardSize];

        setupWorker();
        return 7;
    }
    int index = 0;

    private void setupWorker() {
        //There must be something to do with salary & snowFine
        double xFactor = 1.5;
        if (boardSize < 28) {
            xFactor = 1.111111;
        }
        if (boardSize > 27 && (snowFine - 10 <= salary)) {
            xFactor = 1.;
        }
        int expected = boardSize;
        double factor = snowFine * 1.0 / (salary * xFactor);
        if (salary >= curSnowFine - 10) {
            factor = 0.75;
        }
        expected *= factor;
        if (expected > 100) {
            expected = 100;
        }
        if (expected < 9) {
            expected = 9;
        }

        System.err.println("Expected: " + expected);

        //Spread worker in the board
        int x = 1;
        int y = 1;
        while (x * y < expected) {
            if (x != y) {
                y++;
            } else {
                x++;
            }
        }
        if (x * y > expected) {
            if (x > y) {
                x--;
            } else {
                y--;
            }
        }

        int xx = boardSize / x;
        int yy = boardSize / y;
        int sisaKolom = boardSize % y;
        int sisaBaris = boardSize % x;

        index = 0;
        for (int i = 0; i < boardSize; i += xx) {
            sisaKolom = boardSize % y;
            for (int j = 0; j < boardSize; j += yy) {
                try {
                    workers[index] = new Worker();
                    int a = i;
                    int b = j;
                    int ii = i + xx - 1 + (sisaBaris > 0 ? 1 : 0);
                    int jj = j + yy - 1 + (sisaKolom > 0 ? 1 : 0);
                    workers[index].aa = i;
                    workers[index].bb = j;
                    workers[index].cc = ii;
                    workers[index].dd = jj;

                    int cr = ((a - xx) + (ii + xx)) / 2;
                    int cc = ((b - yy) + (jj + yy)) / 2;
                    workers[index].centerRow = cr;
                    workers[index].centerCol = cc;

                    if (ii > boardSize) {
                        ii = boardSize - 1;
                        i = boardSize;
                    }
                    if (jj > boardSize) {
                        jj = boardSize - 1;
                        j = boardSize;
                    }
                    if (a - xx >= 0) {
                        a -= xx;
                    }
                    if (b - yy >= 0) {
                        b -= yy;
                    }
                    if (ii + xx < boardSize) {
                        ii += xx;
                    }
                    if (jj + yy < boardSize) {
                        jj += yy;
                    }
                    workers[index].a = a;
                    workers[index].b = b;
                    workers[index].c = ii;
                    workers[index].d = jj;
                    workers[index].id = -1;

                    index++;
                } catch (Exception e) {
                }
                if (sisaKolom > 0) {
                    sisaKolom--;
                    j++;
                }
            }
            if (sisaBaris > 0) {
                sisaBaris--;
                i++;
            }
        }
    }

    private void setup(int[] snowFalls) {
        for (int i = 0; i < snowFalls.length; i += 2) {
            tempBoard[snowFalls[i]][snowFalls[i + 1]] = true;
        }
        ret = new ArrayList<String>();
        used = new boolean[101];
        Arrays.fill(used, false);
    }

    private void assignWorkers() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (tempBoard[i][j] && workerNumber < workers.length) {
                    for (int k = 0; k < workers.length; k++) {
                        if (workers[k] == null) {
                            continue;
                        }
                        Worker w = workers[k];
                        if ((w.aa <= i && w.bb <= j && w.cc >= i && w.dd >= j && workers[k].id == -1)) {
                            if (workersOnBoard[i][j] > 0 && curSalary > curSnowFine) {
                                System.err.println("adad");
                                continue;
                            }
                            ret.add("H " + i + " " + j);
                            workers[k].curRow = i;
                            workers[k].curCol = j;

                            workers[k].id = workerNumber;
                            used[workerNumber++] = true;
                            tempBoard[workers[k].curRow][workers[k].curCol] = false;

                            for (int ii = w.a; ii <= w.c; ii++) {
                                for (int jj = w.b; jj <= w.d; jj++) {
                                    workersOnBoard[ii][jj]++;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //Get Score by counting number of connected component
    int getScore(Worker w, int row, int col, boolean[][] visited) {
        int score = -1;
        LinkedList<Integer> rowList = new LinkedList<Integer>();
        LinkedList<Integer> colList = new LinkedList<Integer>();
        rowList.add(row);
        colList.add(col);
        while (!rowList.isEmpty()) {
            int aRow = rowList.poll();
            int aCol = colList.poll();
            visited[aRow][aCol] = true;
            score++;
            if (aRow - 1 >= w.a && !visited[aRow - 1][aCol] && tempBoard[aRow - 1][aCol]) {
                rowList.add(aRow - 1);
                colList.add(aCol);
            }
            if (aRow + 1 <= w.c && !visited[aRow + 1][aCol] && tempBoard[aRow + 1][aCol]) {
                rowList.add(aRow + 1);
                colList.add(aCol);
            }
            if (aCol - 1 >= w.b && !visited[aRow][aCol - 1] && tempBoard[aRow][aCol - 1]) {
                rowList.add(aRow);
                colList.add(aCol - 1);
            }
            if (aCol + 1 <= w.d && !visited[aRow][aCol + 1] && tempBoard[aRow][aCol + 1]) {
                rowList.add(aRow);
                colList.add(aCol + 1);
            }
        }
        //System.err.println("Score : " + score);
        return score;
    }

    private void clearSnow() {
        for (int i = 0; i < workers.length; i++) {
            if (workers[i] == null) {
                continue;
            }
            Worker w = workers[i];
            if (w.id != -1 && !used[w.id]) {
                if (tempBoard[w.curRow][w.curCol]) {
                    tempBoard[w.curRow][w.curCol] = false;
                    continue;
                }

                //Check Scoring
                int up = 0, down = 0, left = 0, right = 0, maxScore = 0, direction = -1;
                boolean[][] visited = new boolean[boardSize][boardSize];
                if (w.curRow - 1 >= w.a && tempBoard[w.curRow - 1][w.curCol] && !visited[w.curRow - 1][w.curCol]) {
                    up = getScore(w, w.curRow - 1, w.curCol, visited);
                    if (up > maxScore) {
                        maxScore = up;
                        direction = 0;
                    }
                }
                if (w.curRow + 1 <= w.c && tempBoard[w.curRow + 1][w.curCol] && !visited[w.curRow + 1][w.curCol]) {
                    down = getScore(w, w.curRow + 1, w.curCol, visited);
                    if (down > maxScore) {
                        maxScore = down;
                        direction = 1;
                    }
                }
                if (w.curCol - 1 >= w.b && tempBoard[w.curRow][w.curCol - 1] && !visited[w.curRow][w.curCol - 1]) {
                    left = getScore(w, w.curRow, w.curCol - 1, visited);
                    if (left > maxScore) {
                        maxScore = left;
                        direction = 2;
                    }
                }
                if (w.curCol + 1 <= w.d && tempBoard[w.curRow][w.curCol + 1] && !visited[w.curRow][w.curCol + 1]) {
                    right = getScore(w, w.curRow, w.curCol + 1, visited);
                    if (right > maxScore) {
                        direction = 3;
                    }
                }
                //End Check Scoring

                if (direction == 0) {
                    moveUp(w, i);
                } else if (direction == 1) {
                    moveDown(w, i);
                } else if (direction == 2) {
                    moveLeft(w, i);
                } else if (direction == 3) {
                    moveRight(w, i);
                } else {
                    int expRow = -1, expCol = -1, distance = 1000;
                    for (int j = w.a; j <= w.c; j++) {
                        for (int k = w.b; k <= w.d; k++) {
                            if (tempBoard[j][k]) {
                                int curDistance = Math.abs(w.curRow - j) + Math.abs(w.curCol - k);
                                if (curDistance < distance) {
                                    distance = curDistance;
                                    expRow = j;
                                    expCol = k;
                                }
                            }
                        }
                    }
                    if (distance < 1000) {
                        if (expRow < w.curRow) {
                            moveUp(w, i);
                        } else if (expRow > w.curRow) {
                            moveDown(w, i);
                        } else if (expCol < w.curCol) {
                            moveLeft(w, i);
                        } else if (expCol > w.curCol) {
                            moveRight(w, i);
                        }
                    } else {
                        int er = w.centerRow;
                        int ec = w.centerCol;
                        if (er < w.curRow) {
                            moveUp(w, i);
                        } else if (ec > w.curCol) {
                            moveRight(w, i);
                        } else if (er > w.curRow) {
                            moveDown(w, i);
                        } else if (ec < w.curCol) {
                            moveLeft(w, i);
                        }
                    }
                }
            }
        }
    }
    private int curSalary = 0;
    private int curSnowFine = 0;
    private int periode = 0;

    public String[] nextDay(int[] snowFalls) {
        int totalSnow = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (tempBoard[i][j]) {
                    totalSnow++;
                }
            }
        }
        curSnowFine += totalSnow * snowFine;
        curSalary += workerNumber * salary;

        setup(snowFalls);
        if (snowFalls.length > 0 && workerNumber < workers.length) {
            assignWorkers();
        }
        clearSnow();


        /*System.err.println("Fine: " + curSnowFine);
         System.err.println("Salary: " + curSalary);*/

        return ret.toArray(new String[]{});
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int boardSize = sc.nextInt();
        int salary = sc.nextInt();
        int snowFine = sc.nextInt();
        SnowCleaning snowCleaning = new SnowCleaning();
        snowCleaning.init(boardSize, salary, snowFine);

        for (int i = 0; i < 2000; i++) {
            int count = sc.nextInt();
            int[] snowFalls = new int[2 * count];
            for (int j = 0; j < 2 * count; j++) {
                snowFalls[j] = sc.nextInt();
            }
            String[] ret = snowCleaning.nextDay(snowFalls);
            System.out.println(ret.length);
            for (int j = 0; j < ret.length; j++) {
                System.out.println(ret[j]);
            }
            System.out.flush();
        }
    }
}

class Worker {

    public int id;
    public int a;
    public int b;
    public int c;
    public int d;
    public int curRow;
    public int curCol;
    public int centerRow;
    public int centerCol;
    public int aa;
    public int bb;
    public int cc;
    public int dd;

    public Worker() {
    }
}