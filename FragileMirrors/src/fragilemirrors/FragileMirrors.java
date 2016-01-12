package fragilemirrors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * @author Izhari Ishak Aksa
 */
public class FragileMirrors {

    byte size;
    short bestId;
    byte[][] board, bestBoard, temp, nodeRC;
    int res;
    short[][] nodeID;
    int[][] expected;
    boolean isLast = false;
    int best = Integer.MIN_VALUE;
    final long START = System.currentTimeMillis();
    boolean[] skip;
    ArrayList<Short> node;

    byte[][] getCopy() {
        byte[][] ret = new byte[size][size];
        for (byte i = 0; i < size; i++) {
            System.arraycopy(this.temp[i], 0, ret[i], 0, size);
        }
        return ret;
    }

    ArrayList<Short> last() {
        ArrayList<Short> list = new ArrayList<Short>();
        this.expected[size][0] = 0;
        while (this.res > 0) {
            best = Integer.MIN_VALUE;
            int score = 0;
            for (short i = 0; i < nodeRC.length; i++) {
                score = getScore(nodeRC[i][0], nodeRC[i][1], i);
            }
            this.res -= best;
            for (byte i = 0; i < size; i++) {
                System.arraycopy(bestBoard[i], 0, this.board[i], 0, size);
            }
            list.add(this.bestId);
        }
        return list;
    }

    int getScore(byte row, byte col, short id) {
        if (skip[id]) {
            return 0;
        }
        byte a = row;
        byte b = col;
        this.temp = new byte[size][size];
        for (byte i = 0; i < size; i++) {
            System.arraycopy(board[i], 0, temp[i], 0, size);
        }

        //down = 0, right = 1, up = 2, left = 3
        byte dir = 0;
        if (row == size) {
            dir = 2;
        } else if (col == -1) {
            dir = 1;
        } else if (col == size) {
            dir = 3;
        }

        int score = 0;
        node = new ArrayList<Short>();
        ArrayList<Byte> tempNode = new ArrayList<Byte>();
        do {
            if (dir == 0) {
                while (++row < size) {
                    if (temp[row][col] == 1) {
                        score++;
                        dir = 3;
                        temp[row][col] = 0;
                        tempNode.add(row);
                        tempNode.add(col);
                        break;
                    } else if (temp[row][col] == 2) {
                        score++;
                        dir = 1;
                        temp[row][col] = 0;
                        tempNode.add(row);
                        tempNode.add(col);
                        break;
                    }
                }
            } else if (dir == 1) {
                while (++col < size) {
                    if (temp[row][col] == 1) {
                        score++;
                        dir = 2;
                        temp[row][col] = 0;
                        tempNode.add(row);
                        tempNode.add(col);
                        break;
                    } else if (temp[row][col] == 2) {
                        score++;
                        dir = 0;
                        temp[row][col] = 0;
                        tempNode.add(row);
                        tempNode.add(col);
                        break;
                    }
                }
            } else if (dir == 2) {
                while (--row >= 0) {
                    if (temp[row][col] == 1) {
                        score++;
                        dir = 1;
                        temp[row][col] = 0;
                        tempNode.add(row);
                        tempNode.add(col);
                        break;
                    } else if (temp[row][col] == 2) {
                        score++;
                        dir = 3;
                        temp[row][col] = 0;
                        tempNode.add(row);
                        tempNode.add(col);
                        break;
                    }
                }
            } else {
                while (--col >= 0) {
                    if (temp[row][col] == 1) {
                        score++;
                        dir = 0;
                        temp[row][col] = 0;
                        tempNode.add(row);
                        tempNode.add(col);
                        break;
                    } else if (temp[row][col] == 2) {
                        score++;
                        dir = 2;
                        temp[row][col] = 0;
                        tempNode.add(row);
                        tempNode.add(col);
                        break;
                    }
                }
            }
        } while (row >= 0 && row < size && col >= 0 && col < size);
        for (int i = 0; i < tempNode.size(); i += 2) {
            this.node.add(nodeID[tempNode.get(i)][tempNode.get(i + 1)]);
        }
        if (score == 0) {
            skip[id] = true;
            return 0;
        }
        if (isLast && score >= best) {
            this.bestId = id;
            this.best = score;
            this.bestBoard = getCopy();
        }
        return score;
    }

    boolean checkOverlapped(BoardRes a, BoardRes b) {
        for (int i : a.node) {
            boolean ada = false;
            for (int j : b.node) {
                if (i == j) {
                    ada = true;
                    break;
                }
            }
            if (!ada) {
                return false;
            }
        }
        return true;
    }

    PriorityQueue<BoardRes> generate(ArrayList<Short> curList, int id) {
        int score;
        PriorityQueue<BoardRes> boardRes = new PriorityQueue<BoardRes>();
        ArrayList<BoardRes> tempList = new ArrayList<BoardRes>();
        for (int i = 0; i < size; i++) {
            score = getScore(nodeRC[i][0], nodeRC[i][1], (short) i);
            if (score > this.expected[size][0]) {
                BoardRes br = new BoardRes(size, this.res - score);
                br.board = getCopy();
                br.list.addAll(curList);
                br.list.add((short) i);
                br.node = this.node;
                br.id = id == -1 ? i : id;
                br.score = score;
                tempList.add(br);
            }
        }
        for (int i = size; i < size * 2; i++) {
            score = getScore(nodeRC[i][0], nodeRC[i][1], (short) i);
            if (score > this.expected[size][0]) {
                BoardRes br = new BoardRes(size, this.res - score);
                br.board = getCopy();
                br.list.addAll(curList);
                br.list.add((short) i);
                br.node = this.node;
                br.id = id == -1 ? i : id;
                br.score = score;
                tempList.add(br);
            }
        }
        for (int i = size * 2; i < size * 3; i++) {
            score = getScore(nodeRC[i][0], nodeRC[i][1], (short) i);
            if (score > this.expected[size][0]) {
                BoardRes br = new BoardRes(size, this.res - score);
                br.board = getCopy();
                br.list.addAll(curList);
                br.list.add((short) i);
                br.node = this.node;
                br.id = id == -1 ? i : id;
                br.score = score;
                tempList.add(br);
            }
        }
        for (int i = size * 3; i < size * 4; i++) {
            score = getScore(nodeRC[i][0], nodeRC[i][1], (short) i);
            if (score > this.expected[size][0]) {
                BoardRes br = new BoardRes(size, this.res - score);
                br.board = getCopy();
                br.list.addAll(curList);
                br.list.add((short) i);
                br.node = this.node;
                br.id = id == -1 ? i : id;
                br.score = score;
                tempList.add(br);
            }
        }
        for (BoardRes i : tempList) {
            i.skip = this.skip.clone();
        }
        boardRes.addAll(tempList);
        return boardRes;
    }

    boolean eval(int batas) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] > 0) {
                    int count = 0;
                    for (int k = j - 1; k >= 0; k--) {
                        if (board[i][k] > 0) {
                            count++;
                            break;
                        }
                    }
                    for (int k = j + 1; k < size; k++) {
                        if (board[i][k] > 0) {
                            count++;
                            break;
                        }
                    }
                    for (int k = i - 1; k >= 0; k--) {
                        if (board[k][j] > 0) {
                            count++;
                            break;
                        }
                    }
                    for (int k = i + 1; k < size; k++) {
                        if (board[k][j] > 0) {
                            count++;
                            break;
                        }
                    }
                    if (count < 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int[] destroy(String[] s) {
        setupError();

        ArrayList<Short> list = new ArrayList<Short>();
        this.size = (byte) s.length;
        board = new byte[size][size];
        bestBoard = new byte[size][size];
        nodeID = new short[size][size];
        short index = 0;
        for (byte i = 0; i < size; i++) {
            for (byte j = 0; j < size; j++) {
                board[i][j] = (byte) (s[i].charAt(j) == 'L' ? 1 : 2);
                nodeID[i][j] = index++;
            }
        }
        //Storing id
        index = 0;
        nodeRC = new byte[size * 4][2];
        for (byte i = 0; i < size; i++) {
            nodeRC[index][0] = -1;
            nodeRC[index++][1] = i;
        }
        for (byte i = 0; i < size; i++) {
            nodeRC[index][0] = size;
            nodeRC[index++][1] = i;
        }
        for (byte i = 0; i < size; i++) {
            nodeRC[index][0] = i;
            nodeRC[index++][1] = -1;
        }
        for (byte i = 0; i < size; i++) {
            nodeRC[index][0] = i;
            nodeRC[index++][1] = size;
        }
        //End storing id

        this.res = size * size;
        this.skip = new boolean[size * 4];
        this.expected = new int[101][2];
        for (int i = 50; i < 101; i++) {
            this.expected[i][0] = 10;
            this.expected[i][1] = 10;
        }
        //end init

        PriorityQueue<BoardRes> boardRes = new PriorityQueue<BoardRes>();
        do {
            this.expected[size][0]--;
            boardRes = generate(new ArrayList<Short>(), -1);
        } while (boardRes.size() < size);

        System.err.println("Ukuran papan: " + this.size);
        System.err.println("Langkah 0: " + boardRes.size());
        BoardRes bestStep = null;
        if (!boardRes.isEmpty()) {
            bestStep = boardRes.element();
        }

        PriorityQueue<BoardRes> boardResTemp = new PriorityQueue<BoardRes>();
        this.expected[size][0] = 10;
        int batas = 125 - size;
        int batas1 = size - 15;
        if (size >= 80) {
            this.expected[size][0] = 20;
        }
        boolean unchecked = true;
        do {
            //System.err.println("Langkah: " + (step++) + " Ukuran: " + boardRes.size() + " Expected score: " + this.expected[size][0]);
            ArrayList<BoardRes> tempList = new ArrayList<BoardRes>();
            boardResTemp = new PriorityQueue<BoardRes>();
            while (!boardRes.isEmpty() && System.currentTimeMillis() - START < 9000) {
                BoardRes br = boardRes.poll();
                //System.err.println(br.id + " >> " + br.res + " score: " + br.score);
                this.res = br.res;
                this.board = br.board;
                boolean ev = eval(1);
                if (!ev && unchecked) {
                    continue;
                }
                this.skip = br.skip;
                boardResTemp.add(br);
                tempList.addAll(generate(br.list, br.id));
            }
            Collections.sort(tempList, new Comparation1());
            int ind = 0;
            while (!tempList.isEmpty() && ind++ < batas) {
                boardRes.add(tempList.remove(0));
            }
            if (batas > 10) {
                batas--;
            }
            if (this.expected[size][0] < batas1) {
                this.expected[size][0]++;
            }
            if (!boardRes.isEmpty()) {
                bestStep = boardRes.element();
            } else {
                unchecked = false;
                boardRes.addAll(boardResTemp);
                this.expected[size][0] = 0;
                batas = 10;
            }
        } while (System.currentTimeMillis() - START < 9000 && this.res > 0);

        list.addAll(bestStep.list);
        System.err.println("Waktu: " + (System.currentTimeMillis() - START));
        System.err.println("Sementara: " + bestStep.list.size());
        System.err.println("Sisa: " + bestStep.res);
        this.res = bestStep.res;
        this.board = bestStep.board;
        isLast = true;
        list.addAll(last());

        int[] ret = new int[list.size() * 2];
        for (int i = 0, j = 0; i < list.size() * 2; i += 2) {
            int x = list.get(j++);
            ret[i] = nodeRC[x][0];
            ret[i + 1] = nodeRC[x][1];
        }
        System.err.println("Total langkah: " + (ret.length / 2));
        System.err.println("Score: " + (size * 1.0 / (ret.length / 2)));

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
        int n = Integer.parseInt(sc.nextLine());
        String[] board = new String[n];
        for (int i = 0; i < n; i++) {
            board[i] = sc.nextLine();
        }
        FragileMirrors fm = new FragileMirrors();
        int[] ret = fm.destroy(board);
        System.out.println(ret.length);
        for (int i = 0; i < ret.length; i++) {
            System.out.println(ret[i]);
        }
        System.out.flush();
    }
}

class BoardRes implements Comparable<BoardRes> {

    byte[][] board;
    int id, batas, res, score;
    ArrayList<Short> list;
    boolean[] skip;
    ArrayList<Short> node;

    public BoardRes(byte size, int res) {
        this.board = new byte[size][size];
        this.res = res;
        this.score = 0;
        this.list = new ArrayList<Short>();
        this.batas = 500;
        this.skip = new boolean[4 * size];
        this.node = new ArrayList<Short>();
    }

    public int compareTo(BoardRes o) {
        if (this.res < o.res) {
            return -1;
        }
        return 1;
    }
}

class Comparation1 implements Comparator<BoardRes> {

    public int compare(BoardRes o1, BoardRes o2) {
        if (o1.res < o2.res) {
            return -1;
        }
        return 1;
    }
}