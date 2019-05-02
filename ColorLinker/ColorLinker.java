package ColorLinker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

/**
 * @author Izhari Ishak Aksa
 */
public class ColorLinker {

    static final long TIME_OUT = 9500, TIME_OUT2 = 9750;
    static final long START_TIME = System.nanoTime();
    static final int[] dirRow = new int[]{1, -1, 0, 0};
    static final int[] dirCol = new int[]{0, 0, 1, -1};
    static int[][][] board, group, bestBoard, blankBoard;
    static HashMap<Integer, Integer> cellPosInArr;
    static int[][] initial;
    static int[] nColors;
    static Cell[] cells;
    static int N, score = Integer.MAX_VALUE, penalty;
    static boolean[] hapusArr;
    static int[][] arrCost;
    static int[] penScore = new int[6];
    PriorityQueue<Path> pathList = new PriorityQueue<Path>();
    int[] colorSeq;
    boolean noPenalty = true;
    LinkedList<Integer> rowList = new LinkedList<Integer>();
    LinkedList<Integer> colList = new LinkedList<Integer>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int gridSize = Integer.parseInt(sc.nextLine());
        int penalty = Integer.parseInt(sc.nextLine());
        String[] grid = new String[gridSize];
        for (int i = 0; i < gridSize; i++) {
            grid[i] = sc.nextLine();
        }
        ColorLinker cl = new ColorLinker();
        int[] ret = cl.link(grid, penalty);
        System.out.println(ret.length);
        for (int i = 0; i < ret.length; i++) {
            System.out.println(ret[i]);
        }
        System.out.flush();
    }

    final void init(String[] grid, int penalty) {
        N = grid.length;
        this.penalty = penalty;
        board = new int[N][N][6];
        bestBoard = new int[N][N][6];
        blankBoard = new int[N][N][6];
        group = new int[N][N][5];
        nColors = new int[5];
        arrCost = new int[N][N];
        for (int i = 0; i < 6; i++) {
            penScore[i] = i + i * (i - 1) * penalty;
        }
        int total = 0;
        char c;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                c = grid[i].charAt(j);
                if (c != '-') {
                    board[i][j][c - '0'] = 1;
                    board[i][j][5] = 1;
                    nColors[c - '0']++;
                    total++;
                }
            }
        }
        cells = new Cell[total];
        total = 0;
        initial = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                initial[i][j] = -1;
                for (int k = 0; k < 5; k++) {
                    group[i][j][k] = -1;
                }
                c = grid[i].charAt(j);
                if (c != '-') {
                    board[i][j][5] = 1;
                    group[i][j][c - '0'] = total;
                    cells[total] = new Cell(total, i, j, c - '0', N);
                    initial[i][j] = total;
                    total++;
                }
            }
        }
        int dist;
        for (int i = 0; i < total; i++) {
            dist = Integer.MAX_VALUE;
            for (int j = 0; j < total; j++) {
                if (i == j || cells[i].color != cells[j].color) {
                    continue;
                }
                dist = Math.min(dist, Math.abs(cells[i].row - cells[j].row) + Math.abs(cells[i].col - cells[j].col));
            }
            cells[i].dist = dist;
        }
        Arrays.sort(cells);
        cellPosInArr = new HashMap<Integer, Integer>();
        for (int i = 0; i < total; i++) {
            cellPosInArr.put(cells[i].id, i);
        }
    }

    final int getScore(int[][][] b) {
        int ret = 0, cUsed;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                cUsed = b[i][j][5];
                ret += penScore[cUsed];
            }
        }
        return ret;
    }

    int[] getResult(int[][][] b) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < 5; k++) {
                    if (b[i][j][k] > 0) {
                        list.add(i);
                        list.add(j);
                        list.add(k);
                    }
                }
            }
        }
        int[] ret = new int[list.size()];
        for (int i = 0; i < ret.length; i += 3) {
            ret[i] = list.get(i);
            ret[i + 1] = list.get(i + 1);
            ret[i + 2] = list.get(i + 2);
        }
        return ret;
    }

    int[] getResult() {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < 5; k++) {
                    if (bestBoard[i][j][k] > 0) {
                        list.add(i);
                        list.add(j);
                        list.add(k);
                    }
                }
            }
        }
        int[] ret = new int[list.size()];
        for (int i = 0; i < ret.length; i += 3) {
            ret[i] = list.get(i);
            ret[i + 1] = list.get(i + 1);
            ret[i + 2] = list.get(i + 2);
        }
        return ret;
    }

    final void trace(Cell cell, int sourceGroup) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                arrCost[i][j] = Integer.MAX_VALUE;
            }
        }
        arrCost[cell.row][cell.col] = 0;
        pathList.clear();
        Path path = new Path(N, cell.row, cell.col);
        pathList.add(path);
        int XX = cells.length;
        while (!pathList.isEmpty()) {
            Path curPath = pathList.poll();
            int curRow = curPath.curRow;
            int curCol = curPath.curCol;
            int index = curPath.index;
            if (group[curRow][curCol][cell.color] != sourceGroup && board[curRow][curCol][cell.color] == 1) {
                //mark the board with the best path and add best path to selected cell, mark the group of selected cell
                int a = group[curRow][curCol][cell.color];
                for (int i = 0; i < index; i++) {
                    int x = curPath.pathRow[i];
                    int y = curPath.pathCol[i];
                    group[x][y][cell.color] = sourceGroup;
                    if (board[x][y][cell.color] == 0) {
                        board[x][y][cell.color] = 1;
                        board[x][y][5]++;
                    }
                }
                //change the group of the cell
                for (int i = 0; i < XX; i++) {
                    if (cells[i].group == a) {
                        cells[i].group = sourceGroup;
                    }
                }
                //change the group of the board
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        if (group[i][j][cell.color] == a) {
                            group[i][j][cell.color] = sourceGroup;
                        }
                    }
                }
                break;
            }
            for (int i = 0; i < 4; i++) {
                int a = curRow + dirRow[i];
                int b = curCol + dirCol[i];
                if (a >= 0 && a < N && b >= 0 && b < N && !(initial[a][b] > -1 && board[a][b][cell.color] == 0)) {
                    int cUsed = board[a][b][5] + (board[a][b][cell.color] > 0 ? -1 : 1);
                    if (cUsed > 1 && noPenalty) {
                        continue;
                    }
                    int curCost = penScore[cUsed];
                    if (curCost + curPath.cost < arrCost[a][b]) {
                        arrCost[a][b] = curCost + curPath.cost;
                        Path p = new Path(N, a, b);
                        p.cost = curCost + curPath.cost;
                        for (int j = 0; j < index; j++) {
                            p.pathRow[j] = curPath.pathRow[j];
                            p.pathCol[j] = curPath.pathCol[j];
                        }
                        p.pathRow[index] = a;
                        p.pathCol[index] = b;
                        p.index = index + 1;
                        pathList.add(p);
                    }
                }
            }
        }
    }

    final void initialPlacement() {
        colorSeq = new int[5];
        int[] temp = new int[5];
        System.arraycopy(nColors, 0, temp, 0, 5);
        for (int i = 0; i < 5; i++) {
            int ind = 0, min = Integer.MAX_VALUE;
            for (int j = 0; j < 5; j++) {
                if (temp[j] < min) {
                    min = temp[j];
                    ind = j;
                }
            }
            colorSeq[i] = ind;
            temp[ind] = Integer.MAX_VALUE;
        }
        for (int x = 0; x < 5; x++) {
            int c = colorSeq[x];
            if (nColors[c] < 2) {
                continue;
            }
            for (int i = 0; i < cells.length; i++) {
                if (cells[i].color != c) {
                    continue;
                }
                trace(cells[i], cells[i].group);
            }
        }

        noPenalty = false;
        for (int x = 4; x >= 0; x--) {
            int c = colorSeq[x];
            if (nColors[c] < 2) {
                continue;
            }
            for (int i = 0; i < cells.length; i++) {
                if (cells[i].color != c) {
                    continue;
                }
                trace(cells[i], cells[i].group);
            }
        }
    }

    final void removeIntersection(int row, int col, int color) {
        rowList.clear();
        colList.clear();
        rowList.add(row);
        colList.add(col);
        while (!rowList.isEmpty()) {
            int curRow = rowList.poll();
            int curCol = colList.poll();
            if (board[curRow][curCol][color] == 1) {
                board[curRow][curCol][color] = 0;
                board[curRow][curCol][5]--;
            }
            group[curRow][curCol][color] = -1;
            for (int i = 0; i < 4; i++) {
                int a = curRow + dirRow[i];
                int b = curCol + dirCol[i];
                if (a >= 0 && a < N && b >= 0 && b < N && board[a][b][color] == 1) {
                    if (initial[a][b] == -1) {
                        rowList.add(a);
                        colList.add(b);
                    } else {
                        hapusArr[initial[a][b]] = true;
                    }
                }
            }
        }
    }

    void regroup() {
        //reset the group
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < 5; k++) {
                    group[i][j][k] = -1;
                }
            }
        }
        //regrouping
        int G = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                boolean grouped = false;
                int color = -1;
                for (int k = 0; k < 5; k++) {
                    if (group[i][j][k] != -1) {
                        grouped = true;
                    }
                    if (board[i][j][k] == 1) {
                        color = k;
                    }
                }
                if (color != -1 && !grouped) {
                    G++;
                    rowList.clear();
                    colList.clear();
                    rowList.add(i);
                    colList.add(j);
                    while (!rowList.isEmpty()) {
                        int curRow = rowList.poll();
                        int curCol = colList.poll();
                        group[curRow][curCol][color] = G;
                        //group the initial cells
                        if (initial[curRow][curCol] != -1) {
                            cells[cellPosInArr.get(initial[curRow][curCol])].group = G;
                        }
                        for (int ii = 0; ii < 4; ii++) {
                            int a = curRow + dirRow[ii];
                            int b = curCol + dirCol[ii];
                            if (a >= 0 && a < N && b >= 0 && b < N && board[a][b][color] == 1 && group[a][b][color] == -1) {
                                rowList.add(a);
                                colList.add(b);
                            }
                        }
                    }
                }
            }
        }
    }

    int[][][] copyBoard(int[][][] b) {
        int[][][] ret = new int[N][N][6];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.arraycopy(b[i][j], 0, ret[i][j], 0, 6);
            }
        }
        return ret;
    }

    void repath(Cell cell) {
        rowList.clear();
        colList.clear();
        rowList.add(cell.row);
        colList.add(cell.col);
        List<Integer> rowPath = new ArrayList<Integer>();
        List<Integer> colPath = new ArrayList<Integer>();
        boolean[][] added = new boolean[N][N];
        boolean bisa = true;
        while (!rowList.isEmpty()) {
            int curRow = rowList.poll();
            int curCol = colList.poll();
            int count = 0;
            for (int i = 0; i < 4; i++) {
                int a = curRow + dirRow[i];
                int b = curCol + dirCol[i];
                if (a >= 0 && a < N && b >= 0 && b < N && board[a][b][cell.color] == 1) {
                    count++;
                }
            }
            if ((initial[curRow][curCol] != -1 && count > 1) || count > 2) {
                break;
            }
            if (board[curRow][curCol][cell.color] == 1 && initial[curRow][curCol] == -1) {
                rowPath.add(curRow);
                colPath.add(curCol);
            }
            added[curRow][curCol] = true;
            for (int i = 0; i < 4; i++) {
                int a = curRow + dirRow[i];
                int b = curCol + dirCol[i];
                if (a >= 0 && a < N && b >= 0 && b < N && board[a][b][cell.color] == 1 && initial[a][b] == -1 && !added[a][b]) {
                    rowList.add(a);
                    colList.add(b);
                }
            }
        }
        if (bisa) {
            for (int i = 0; i < rowPath.size(); i++) {
                int a = rowPath.get(i);
                int b = colPath.get(i);
                if (board[a][b][cell.color] == 1 && initial[a][b] == -1) {
                    board[a][b][cell.color] = 0;
                    board[a][b][5]--;
                }
            }
            regroup();
            trace(cell, cell.group);
        }
    }

    public int[] link(String[] grid, int penalty) {
        setupError();
        init(grid, penalty);
        blankBoard = copyBoard(board);
        initialPlacement();
        score = getScore(board);
        bestBoard = copyBoard(board);
        System.err.println("Initial Score = " + score);
        List<Integer> cellList = new ArrayList<Integer>();
        for (int i = 0; i < cells.length; i++) {
            cellList.add(i);
        }

        //do placement by color permutation
        List<Integer> xxList = new ArrayList<Integer>();
        for (int i = 0; i < 5; i++) {
            if (nColors[i] > 0) {
                xxList.add(i);
            }
        }
        int[] xxArr = new int[xxList.size()];
        for (int i = 0; i < xxList.size(); i++) {
            xxArr[i] = xxList.get(i);
        }
        int len = 1;
        for (int i = 2; i <= xxArr.length; i++) {
            len *= i;
        }
        int[][] permutation = new int[len][xxArr.length];
        int counter = 0;
        do {
            System.arraycopy(xxArr, 0, permutation[counter], 0, xxArr.length);
            counter++;
        } while (next_permutation(xxArr));
        System.err.println("Permutation: " + permutation.length);
        //end permutation


        int hapus, x, curScore, steps = 1;
        boolean selesai;
        long seed = 1234567891011L;
        int XX = cells.length;
        while (System.nanoTime() - START_TIME < TIME_OUT * 1000000) {
            hapus = 0;
            hapusArr = new boolean[XX];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (board[i][j][5] > 1) {
                        for (int k = 4; k >= 0; k--) {
                            if (board[i][j][colorSeq[k]] == 1) {
                                removeIntersection(i, j, colorSeq[k]);
                            }
                        }
                        hapus++;
                    }
                }
            }
            if (hapus == 0) {
                int count = 0;
                board = copyBoard(blankBoard);
                while (count++ < XX && System.nanoTime() - START_TIME < TIME_OUT * 1000000) {
                    regroup();
                    Collections.shuffle(cellList, new Random(seed++));
                    selesai = true;
                    for (int k = 0; k < cellList.size(); k++) {
                        x = cellList.get(k);
                        trace(cells[x], cells[x].group);
                        if (System.nanoTime() - START_TIME > TIME_OUT * 1000000) {
                            selesai = false;
                            break;
                        }
                    }
                    curScore = getScore(board);
                    if (curScore < score && selesai) {
                        bestBoard = copyBoard(board);
                        score = curScore;
                    }
                    board = copyBoard(blankBoard);
                }
            } else {
                int[][][] temp = copyBoard(board);
                List<Integer> reTraceList = new ArrayList<Integer>();
                for (int k = 0; k < hapusArr.length; k++) {
                    if (hapusArr[k]) {
                        reTraceList.add(k);
                    }
                }
                //System.err.println("Hapus: " + hapus + " -> List: " + reTraceList.size());
                for (int i = 0; i < permutation.length; i++) {
                    if (System.nanoTime() - START_TIME > TIME_OUT * 1000000) {
                        break;
                    }
                    board = copyBoard(temp);
                    regroup();
                    selesai = true;
                    Collections.shuffle(reTraceList, new Random(seed++));
                    for (int j = 0; j < permutation[i].length; j++) {
                        for (int l = 0; l < reTraceList.size(); l++) {
                            x = cellPosInArr.get(reTraceList.get(l));
                            if (cells[x].color != permutation[i][j]) {
                                continue;
                            }
                            trace(cells[x], cells[x].group);
                            if (System.nanoTime() - START_TIME > TIME_OUT * 1000000) {
                                selesai = false;
                                break;
                            }
                        }
                        if (System.nanoTime() - START_TIME > TIME_OUT * 1000000) {
                            selesai = false;
                            break;
                        }
                    }
                    curScore = getScore(board);
                    System.err.println((steps++) + " Score = " + curScore + " bestScore = " + score);
                    if (curScore < score && selesai) {
                        bestBoard = copyBoard(board);
                        score = curScore;
                    }
                }
            }
            board = copyBoard(bestBoard);
        }
        System.err.println("Before Repath: " + getScore(bestBoard));
        board = copyBoard(bestBoard);
        for (int i = 0; i < N && System.nanoTime() - START_TIME < TIME_OUT2 * 1000000; i++) {
            for (int j = 0; j < N && System.nanoTime() - START_TIME < TIME_OUT2 * 1000000; j++) {
                if (initial[i][j] != -1 && System.nanoTime() - START_TIME < TIME_OUT2 * 1000000) {
                    x = cellPosInArr.get(initial[i][j]);
                    repath(cells[x]);
                }
            }
        }
        bestBoard = copyBoard(board);
        System.err.println("Final Score: " + getScore(bestBoard));
        System.err.println("Total Waktu: " + ((System.nanoTime() - START_TIME) / 1000000));
        return getResult();
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

    boolean next_permutation(int[] p) {
        for (int a = p.length - 2; a >= 0; --a) {
            if (p[a] < p[a + 1]) {
                for (int b = p.length - 1; ; --b) {
                    if (p[b] > p[a]) {
                        int t = p[a];
                        p[a] = p[b];
                        p[b] = t;
                        for (++a, b = p.length - 1; a < b; ++a, --b) {
                            t = p[a];
                            p[a] = p[b];
                            p[b] = t;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

class Cell implements Comparable<Cell> {

    int id, row, col, color, group, dist, N;

    public Cell(int id, int row, int col, int color, int N) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.color = color;
        this.group = id;
        this.N = N;
    }

    @Override
    public int compareTo(Cell o) {
        return dist - o.dist;
    }
}

class Path implements Comparable<Path> {

    int[] pathRow;
    int[] pathCol;
    int cost, index, curRow, curCol;

    public Path(int N, int curRow, int curCol) {
        int x = N << 4;
        pathRow = new int[x];
        pathCol = new int[x];
        this.curRow = curRow;
        this.curCol = curCol;
    }

    @Override
    public int compareTo(Path o) {
        if (cost == o.cost) {
            if (curRow == o.curRow) {
                return curCol - o.curCol;
            }
            return curRow - o.curRow;
        }
        return cost - o.cost;
    }
}