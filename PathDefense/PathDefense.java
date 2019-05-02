package PathDefense;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * @author izharishaksa
 */
public class PathDefense {

    int STEP;
    int SIZE;
    int[][] board;
    int CREEP_HEALTH;
    TowerType[] towerTypes;
    Base[] bases;
    Creep[] creeps;
    List<Tower> placedTowers;

    int nBase;
    final int PATH_CODE = 10;
    boolean[][][] activePaths;
    int[][][] distPaths;
    int[][] coverage;
    int[] power;
    int[][] dir = new int[][]{{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    void settingBase(String[] b) {
        nBase = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                char c = b[i].charAt(j);
                if (c >= '0' && c <= '9') {
                    nBase++;
                }
            }
        }
        activePaths = new boolean[SIZE][SIZE][nBase];
        distPaths = new int[SIZE][SIZE][nBase];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                for (int k = 0; k < nBase; k++) {
                    distPaths[i][j][k] = Integer.MAX_VALUE;
                }
            }
        }
        power = new int[nBase];
        bases = new Base[nBase];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                char c = b[i].charAt(j);
                if (c >= '0' && c <= '9') {
                    int id = c - '0';
                    bases[id] = new Base(id, 1000, i, j);
                    LinkedList<Integer> row = new LinkedList<Integer>();
                    LinkedList<Integer> col = new LinkedList<Integer>();
                    LinkedList<Integer> jar = new LinkedList<Integer>();
                    row.add(i);
                    col.add(j);
                    jar.add(1);
                    activePaths[i][j][id] = true;
                    while (!row.isEmpty()) {
                        int curRow = row.poll();
                        int curCol = col.poll();
                        int jarak = jar.poll();
                        for (int k = 0; k < 4; k++) {
                            int nextRow = dir[k][0] + curRow;
                            int nextCol = dir[k][1] + curCol;
                            if (nextRow >= 0 && nextRow < SIZE && nextCol >= 0 && nextCol < SIZE
                                    && board[nextRow][nextCol] == PATH_CODE && !activePaths[nextRow][nextCol][id]) {
                                row.add(nextRow);
                                col.add(nextCol);
                                jar.add(jarak + 1);
                                activePaths[nextRow][nextCol][id] = true;
                                distPaths[nextRow][nextCol][id] = jarak;
                            }
                        }
                    }
                    activePaths[i][j][id] = false;
                }
            }
        }
    }

    public int init(String[] b, int money, int creepHealth, int creepMoney, int[] towerTypes) {
        debug();
        this.SIZE = b.length;
        this.CREEP_HEALTH = creepHealth;
        this.warning = 6;
        this.STEP = 0;
        this.board = new int[SIZE][SIZE];
        this.coverage = new int[SIZE][SIZE];
        this.placedTowers = new ArrayList<>();
        this.creeps = new Creep[2001];

        //Init board, path, base and boundary
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                char c = b[i].charAt(j);
                board[i][j] = c >= '0' && c <= '9' ? (c - '0') : (c == '.' ? PATH_CODE : -1000);
            }
        }
        settingBase(b);
        //Init towerTypes
        this.towerTypes = new TowerType[towerTypes.length / 3];
        for (int i = 0; i < towerTypes.length / 3; i++) {
            this.towerTypes[i] = new TowerType(i, towerTypes[i * 3], towerTypes[i * 3 + 1], towerTypes[i * 3 + 2]);
        }
        return 7;
    }

    boolean inRange(int a, int b, int c, int d, int range) {
        return ((a - c) * (a - c)) + ((b - d) * (b - d)) <= (range * range);
    }

    int countRange(int a, int b, int c, int d) {
        return ((a - c) * (a - c)) + ((b - d) * (b - d));
    }

    int attack(Tower t, int id) {
        TowerType type = towerTypes[t.type];
        if (creeps[id] != null) {
            creeps[id].health -= type.damage;
            if (creeps[id].health < 1) {
                creeps[id] = null;
            }
        }
        return 0;
    }

    int attack(Tower t) {
        TowerType type = towerTypes[t.type];
        int minRange = type.range * type.range;
        int id = -1;
        for (Creep c : creeps) {
            if (c != null) {
                int cur = countRange(t.row, t.col, c.row, c.col);
                if (cur <= minRange && (id == -1 || c.id < id)) {
                    minRange = cur;
                    id = c.id;
                }
            }
        }
        if (id != -1) {
            creeps[id].health -= type.damage;
            if (creeps[id].health < 1) {
                creeps[id] = null;
            }
        }
        return 0;
    }

    int[] tempId;

    Creep[] nextCreeps;

    int nextAttack(Tower t) {
        TowerType type = towerTypes[t.type];
        int minRange = type.range * type.range;
        int id = -1;
        for (Creep c : nextCreeps) {
            if (c != null) {
                int cur = countRange(t.row, t.col, c.row, c.col);
                if (cur <= minRange && (id == -1 || c.id < id)) {
                    minRange = cur;
                    id = c.id;
                }
            }
        }
        if (id != -1) {
            if (nextCreeps[id].health - type.damage < 1) {
                creeps[id].cover = true;
                nextCreeps[id] = null;
            } else {
                nextCreeps[id].health -= type.damage;
            }
        }
        return 0;
    }

    int warning;

    Tower score(Cell cell, TowerType type, int baseId, int targetId) {
        Tower ret = null;
        int range = type.range;
        int score = 1, attackCreep = -1;

        int minRange = type.range * type.range;
        int coverage = 0;
        for (int i = 0; i < tempId.length; i++) {
            Creep creep = creeps[tempId[i]];
            if (creep != null && !creep.cover) {
                int curRange = countRange(cell.row, cell.col, creep.row, creep.col);
                if (curRange <= minRange && board[cell.row][cell.col] == 10) {
                    coverage++;
                }
                if (curRange <= minRange && (attackCreep == -1 || attackCreep > creep.id)) {
                    minRange = curRange;
                    attackCreep = creep.id;
                }
            }
        }
        if (attackCreep == targetId) {
            score *= CREEP_HEALTH - (creeps[attackCreep].health - type.damage);
            score *= (type.damage / type.cost);
            score -= Math.abs(cell.row - bases[baseId].row) + Math.abs(cell.col - bases[baseId].row);
            int a = cell.row - type.range + 1;
            a = a >= 0 ? a : 0;
            int b = cell.col - type.range + 1;
            b = b >= 0 ? b : 0;
            int c = cell.row + type.range - 1;
            c = c < SIZE ? c : SIZE - 1;
            int d = cell.col + type.range - 1;
            d = d < SIZE ? d : SIZE - 1;
            //fix this
            ret = new Tower(-1, type.id, cell.row, cell.col);
            ret.aCreep = attackCreep;
            ret.score = score;
            ret.baseId = baseId;
        }
        return ret;
    }

    public int[] placeTowers(int[] creep, int money, int[] baseHealth) {
        STEP++;
        if (creep.length == 0) {
            return new int[0];
        }
        if (STEP % 500 == 0) {
            this.CREEP_HEALTH *= 2;
            warning++;
        }
        tempId = new int[creep.length / 4];
        //Set the creeps
        int[][] nCreeps = new int[SIZE][SIZE];
        boolean waves = false;
        for (int i = 0, j = 0; i < creep.length; i += 4) {
            int id = creep[i];
            int health = creep[i + 1];
            int col = creep[i + 2];
            int row = creep[i + 3];
            nCreeps[row][col]++;
            if (nCreeps[row][col] >= 5) {
                waves = true;
            }
            if (creeps[id] == null) {
                creeps[id] = new Creep(id, row, col, health);
            } else {
                creeps[id].row = row;
                creeps[id].col = col;
                creeps[id].health = health;
                creeps[id].cover = false;
            }
            tempId[j++] = creep[i];
        }
        //Placed towers attack creeps
        //If still in range don't add new towers        
        for (Tower t : placedTowers) {
            attack(t);
        }
        /*int[] ddist = new int[nBase];
         int[] pressure = new int[nBase];
         for (Creep c : creeps) {
         if (c != null && !c.cover) {
         int minDist = Integer.MAX_VALUE;
         int ind = -1;
         for (int i = 0; i < nBase; i++) {
         if (distPaths[c.row][c.col][i] != 0
         && distPaths[c.row][c.col][i] < minDist) {
         minDist = distPaths[c.row][c.col][i];
         ind = i;
         }
         }
         if (ind != -1) {
         ddist[ind] = Math.min(ddist[ind], minDist);
         pressure[ind] += c.health;
         }
         }
         }*/
        boolean ok = false;
        List<Integer> list = new ArrayList<>();
        do {
            PriorityQueue<Tower> pq = new PriorityQueue<>();
            for (Creep c : creeps) {
                if (c != null && !c.cover) {
                    int minDist = Integer.MAX_VALUE;
                    int ind = -1;
                    for (int i = 0; i < nBase; i++) {
                        /*if ((power[i] > pressure[i]) && !waves) {
                         continue;
                         }*/
                        if (distPaths[c.row][c.col][i] != 0
                                && distPaths[c.row][c.col][i] <= warning
                                && distPaths[c.row][c.col][i] < minDist) {
                            minDist = distPaths[c.row][c.col][i];
                            ind = i;
                        }
                    }
                    if (ind == -1) {
                        continue;
                    }
                    int aa = c.row - 5;
                    aa = aa >= 0 ? aa : 0;
                    int bb = c.col - 5;
                    bb = bb >= 0 ? bb : 0;
                    int cc = c.row + 5;
                    cc = cc < SIZE ? cc : SIZE - 1;
                    int dd = c.col + 5;
                    dd = dd < SIZE ? dd : SIZE - 1;
                    for (int p = aa; p <= cc; p++) {
                        for (int q = bb; q <= dd; q++) {
                            for (int t = 0; t < towerTypes.length; t++) {
                                if (board[p][q] != -1000 || money < towerTypes[t].cost) {
                                    continue;
                                }
                                Tower newTower = score(new Cell(p, q), towerTypes[t], ind, creeps[c.id].id);
                                if (newTower != null && newTower.aCreep > 0) {
                                    pq.add(newTower);
                                }
                            }
                        }
                    }
                }
            }

            ok = false;
            while (!pq.isEmpty()) {
                Tower cur = pq.poll();
                if (board[cur.row][cur.col] == -1000 && money >= towerTypes[cur.type].cost) {
                    list.add(cur.col);
                    list.add(cur.row);
                    list.add(cur.type);
                    board[cur.row][cur.col] = cur.type + 11;
                    money -= towerTypes[cur.type].cost;
                    placedTowers.add(cur);
                    attack(cur, cur.aCreep);
                    power[cur.baseId] += towerTypes[cur.type].damage;
                    break;
                }
            }
        } while (ok);

        int[] ret = new int[list.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = list.get(i);
        }
        return ret;
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
        int N = sc.nextInt();
        int money = sc.nextInt();
        sc.nextLine();
        String[] board = new String[N];
        for (int i = 0; i < N; i++) {
            board[i] = sc.nextLine();
        }
        int creepHealth = sc.nextInt();
        int creepMoney = sc.nextInt();
        int NT = sc.nextInt();
        int[] towerTypes = new int[NT];
        for (int i = 0; i < NT; i++) {
            towerTypes[i] = sc.nextInt();
        }
        PathDefense pd = new PathDefense();
        pd.init(board, money, creepHealth, creepMoney, towerTypes);
        for (int i = 0; i < 2000; i++) {
            money = sc.nextInt();
            int NC = sc.nextInt();
            int[] creep = new int[NC];
            for (int j = 0; j < creep.length; j++) {
                creep[j] = sc.nextInt();
            }
            int B = sc.nextInt();
            int[] baseHealth = new int[B];
            for (int j = 0; j < baseHealth.length; j++) {
                baseHealth[j] = sc.nextInt();
            }
            int[] ret = pd.placeTowers(creep, money, baseHealth);
            System.out.println(ret.length);
            for (int res : ret) {
                System.out.println(res);
            }
            System.out.flush();
        }
    }

}

class Creep {

    int id, row, col, health;
    boolean cover;

    public Creep(int id, int row, int col, int health) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.health = health;
    }

}

class Tower implements Comparable<Tower> {

    int id, type, row, col, score, dist, aCreep, baseId;

    public Tower(int id, int type, int row, int col) {
        this.id = id;
        this.type = type;
        this.row = row;
        this.col = col;
    }

    @Override
    public int compareTo(Tower o) {
        return o.score - score;
    }

    @Override
    public String toString() {
        return id + " => " + row + "," + col + " : " + type + " score = " + score;
    }
}

class Base {

    int id, health, row, col;

    public Base(int id, int health, int row, int col) {
        this.id = id;
        this.health = health;
        this.row = row;
        this.col = col;
    }
}

class TowerType {

    int id, range, damage, cost;

    public TowerType(int id, int range, int damage, int cost) {
        this.id = id;
        this.range = range;
        this.damage = damage;
        this.cost = cost;
    }
}

class Cell {

    int row, col, dist;
    List<Integer> creepId;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    void reset() {
        this.creepId = new ArrayList<>();
    }
}
