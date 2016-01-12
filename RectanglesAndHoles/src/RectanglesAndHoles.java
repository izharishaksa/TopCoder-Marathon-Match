
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

/**
 *
 * @author Izhari Ishak Aksa
 */
public class RectanglesAndHoles {

    int N, holes;
    List<Rectangle> added = new ArrayList<Rectangle>();

    boolean isOverlap(Rectangle A) {
        for (Rectangle r : added) {
            if (isOverlap(A, r)) {
                return true;
            }
        }
        return false;
    }

    boolean isOverlap(Rectangle A, Rectangle B) {
        return Math.max(A.LX, B.LX) < Math.min(A.RX, B.RX) && Math.max(A.LY, B.LY) < Math.min(A.RY, B.RY);
    }

    int[] place(int[] A, int[] B) {
        N = A.length;
        Rectangle[] rect = new Rectangle[N];
        for (int i = 0; i < N; i++) {
            rect[i] = new Rectangle(i, A[i], B[i], 0);
            if (rect[i].alas > rect[i].tinggi) {
                rect[i].rotate();
            }
            rect[i].setDefault();
        }
        LinkedList<Rectangle> pool = new LinkedList<Rectangle>();
        for (int i = 0; i < N; i++) {
            pool.add(rect[i]);
        }
        Collections.sort(pool, new SortByRatio());

        Queue<Rectangle> list = new ArrayDeque<Rectangle>();
        Rectangle rct = pool.poll();
        rct.setPosition(0, 0);
        rct.fixed = true;
        list.add(rct);

        List<Rectangle> temp = new ArrayList<Rectangle>();
        int batas = 3, count = 0;
        added.add(rct);
        while (!pool.isEmpty()) {
            Collections.sort(pool, new SortByRatio());
            Rectangle cur = pool.poll();

            if (count >= batas) {
                list.offer(cur);
                continue;
            }

            List<Rectangle> temp2 = new ArrayList<Rectangle>();
            State state = new State(null, -1, -1, -1, -1, -1, 0);
            while (!list.isEmpty() && count < batas) {
                Rectangle partner1 = list.poll();
                if (partner1.A == -1 || partner1.B == -1 || partner1.C == -1 || partner1.D == -1) {

                    if (partner1.A == -1) {
                        cur.setPosition(partner1.LX - cur.alas, partner1.RY);
                        Rectangle partner2 = null, partner3 = null, partner4 = null, partner5 = null;
                        double size = 0;
                        double score = 1000 - Math.abs(cur.alas - partner1.tinggi);
                        score += 1000 - Math.abs(cur.tinggi - partner1.alas);

                        boolean first = true;
                        if (partner1.C != -1) {
                            partner2 = partner1.C;
                            score += 2000 - Math.abs(cur.alas - partner2.alas);

                            if (partner2.A != null) {
                                partner3 = partner2.A;
                                score += 3000 - Math.abs(cur.alas - partner3.tinggi);

                                cur.setPosition(cur.LX, Math.min(partner1.RY, partner3.RY));
                                if (!isOverlap(cur) && (!partner3.fixed || Math.max(cur.LX, partner2.LX) == partner3.LX)) {
                                    partner3.backUp();
                                    partner3.setPosition(Math.max(cur.LX, partner2.LX), partner3.LY);
                                    if (!isOverlap(partner3)) {
                                        size = Math.min(cur.alas, partner2.alas) * Math.min(partner1.tinggi, partner3.tinggi);
                                    } else {
                                        partner3.rollback();
                                    }
                                }
                                score += size;
                            }
                        } else if (partner1.B != null) {
                            first = false;
                            partner2 = partner1.B;
                            score += 2000 - Math.abs(cur.tinggi - partner2.tinggi);

                            if (partner2.A != null) {
                                partner3 = partner2.A;
                                score += 3000 - Math.abs(cur.tinggi - partner3.alas);

                                cur.setPosition(Math.max(partner1.LX, partner3.LX), cur.LY);
                                if (!isOverlap(cur) && (!partner3.fixed || Math.max(cur.RY, partner2.RY) == partner3.LY)) {
                                    partner3.backUp();
                                    partner3.setPosition(partner3.LX, Math.max(cur.RY, partner2.RY));
                                    if (!isOverlap(partner3)) {
                                        size = Math.min(cur.alas, partner2.alas) * Math.min(partner1.tinggi, partner3.tinggi);
                                    } else {
                                        partner3.rollback();
                                    }
                                }
                                score += size;
                            }
                        }
                        if (isOverlap(cur)) {
                            score = 0;
                        }
                        //System.err.println(partner1.id + " A : " + partner1.LX + " , " + partner1.LY + " == " + cur.id + " : " + cur.LX + " , " + cur.LY + " >> score : " + score);
                        if (score > state.score) {
                            state.score = score;
                            state.cur = new Rectangle(cur.id, cur.alas, cur.tinggi, cur.dir);
                            state.cur.setPosition(cur.LX, cur.LY);
                            state.partner1 = new Rectangle(partner1.id, partner1.alas, partner1.tinggi, partner1.dir);
                            state.partner1.setPosition(partner1.LX, partner1.LY);
                            state.cur.D = state.partner1;
                            state.partner1.A = state.cur;
                            if (partner2 != null) {
                                state.partner2 = new Rectangle(partner2.id, partner2.alas, partner2.tinggi, partner2.dir);
                                state.partner2.setPosition(partner2.LX, partner2.LY);
                            }
                            if (partner3 != null) {
                                state.cur.fixed = true;
                                state.partner1.fixed = true;
                                state.partner2.fixed = true;

                                state.partner3 = new Rectangle(partner3.id, partner3.alas, partner3.tinggi, partner3.dir);
                                state.partner3.setPosition(partner3.LX, partner3.LY);
                                state.partner3.fixed = true;

                                if (first) {
                                    state.cur.C = state.partner3;
                                    state.partner3.B = state.cur;
                                } else {
                                    state.cur.B = state.partner3;
                                    state.partner3.C = state.cur;
                                }
                                partner3.rollback();
                            }
                        }
                    }


                    if (partner1.B == null) {
                        cur.setPosition(partner1.RX, partner1.RY);
                        Rectangle partner2 = null, partner3 = null, partner4 = null, partner5 = null;
                        double size = 0;
                        double score = 1000 - Math.abs(cur.alas - partner1.tinggi);
                        score += 1000 - Math.abs(cur.tinggi - partner1.alas);

                        if (partner1.D != null) {
                            partner2 = partner1.D;
                            score += 2000 - Math.abs(cur.alas - partner2.alas);

                            if (partner2.B != null) {
                                partner3 = partner2.B;
                                score += 3000 - Math.abs(cur.alas - partner3.tinggi);

                                cur.setPosition(cur.LX, cur.LY - (partner1.RY - partner3.RY));
                                if (!isOverlap(cur) && (!partner3.fixed || Math.min(cur.RX, partner2.RX) == partner3.LX)) {
                                    partner3.backUp();
                                    partner3.setPosition(Math.min(cur.LX, partner2.LX), partner3.LY);
                                    if (!isOverlap(partner3)) {
                                        size = Math.min(cur.alas, partner2.alas) * Math.min(partner1.tinggi, partner3.tinggi);
                                    } else {
                                        partner3.rollback();
                                    }
                                }
                                score += size;
                            }
                        }
                        if (isOverlap(cur)) {
                            score = 0;
                        }
                        //System.err.println(partner1.id + " B : " + partner1.LX + " , " + partner1.LY + " == " + cur.id + " : " + cur.LX + " , " + cur.LY + " >> score : " + score);                        
                        if (score > state.score) {
                            state.score = score;
                            state.cur = new Rectangle(cur.id, cur.alas, cur.tinggi, cur.dir);
                            state.cur.setPosition(cur.LX, cur.LY);
                            state.partner1 = new Rectangle(partner1.id, partner1.alas, partner1.tinggi, partner1.dir);
                            state.partner1.setPosition(partner1.LX, partner1.LY);
                            state.cur.C = state.partner1;
                            state.partner1.B = state.cur;
                            if (partner2 != null) {
                                state.partner2 = new Rectangle(partner2.id, partner2.alas, partner2.tinggi, partner2.dir);
                                state.partner2.setPosition(partner2.LX, partner2.LY);
                            }
                            if (partner3 != null) {
                                state.cur.fixed = true;
                                state.partner1.fixed = true;
                                state.partner2.fixed = true;

                                state.partner3 = new Rectangle(partner3.id, partner3.alas, partner3.tinggi, partner3.dir);
                                state.partner3.setPosition(partner3.LX, partner3.LY);
                                state.partner3.fixed = true;
                                state.cur.D = state.partner3;
                                state.partner3.A = state.cur;
                                partner3.rollback();
                            }
                        }
                    }


                    if (partner1.C == null) {
                        cur.setPosition(partner1.LX - cur.alas, partner1.LY - cur.tinggi);
                        Rectangle partner2 = null, partner3 = null, partner4 = null, partner5 = null;
                        double size = 0;
                        double score = 1000 - Math.abs(cur.tinggi - partner1.alas);
                        score += 1000 - Math.abs(cur.alas - partner1.tinggi);

                        if (partner1.A != null) {
                            partner2 = partner1.A;
                            score += 2000 - Math.abs(cur.alas - partner2.alas);

                            if (partner2.C != null) {
                                partner3 = partner2.C;
                                score += 3000 - Math.abs(cur.tinggi - partner3.alas);

                                //cur.setPosition(cur.LX + Math.abs(partner1.LX - partner3.LX), cur.LY);
                                cur.setPosition(cur.LX, cur.LY + Math.abs(partner1.tinggi - partner3.tinggi));
                                if (!isOverlap(cur) && (!partner3.fixed || Math.max(cur.LX, partner2.LX) == partner3.LX)) {
                                    partner3.backUp();
                                    partner3.setPosition(partner2.LX, Math.max(cur.LX, partner2.LX));
                                    if (!isOverlap(partner3)) {
                                        size = Math.min(cur.alas, partner2.alas) * Math.min(partner1.tinggi, partner3.tinggi);
                                    } else {
                                        partner3.rollback();
                                    }
                                }
                                score += size;
                            }
                        }
                        if (isOverlap(cur)) {
                            score = 0;
                        }
                        //System.err.println(partner1.id + " C : " + partner1.LX + " , " + partner1.LY + " == " + cur.id + " : " + cur.LX + " , " + cur.LY + " >> score : " + score);                        
                        if (score > state.score) {
                            state.score = score;
                            state.cur = new Rectangle(cur.id, cur.alas, cur.tinggi, cur.dir);
                            state.cur.setPosition(cur.LX, cur.LY);
                            state.partner1 = new Rectangle(partner1.id, partner1.alas, partner1.tinggi, partner1.dir);
                            state.partner1.setPosition(partner1.LX, partner1.LY);
                            state.cur.B = state.partner1;
                            state.partner1.C = state.cur;
                            if (partner2 != null) {
                                state.partner2 = new Rectangle(partner2.id, partner2.alas, partner2.tinggi, partner2.dir);
                                state.partner2.setPosition(partner2.LX, partner2.LY);
                            }
                            if (partner3 != null) {
                                state.cur.fixed = true;
                                state.partner1.fixed = true;
                                state.partner2.fixed = true;

                                state.partner3 = new Rectangle(partner3.id, partner3.alas, partner3.tinggi, partner3.dir);
                                state.partner3.setPosition(partner3.LX, partner3.LY);
                                state.partner3.fixed = true;
                                state.cur.A = state.partner3;
                                state.partner3.D = state.cur;
                                partner3.rollback();
                            }
                        }
                    }


                    if (partner1.D == null) {
                        cur.setPosition(partner1.RX, partner1.LY - cur.tinggi);
                        Rectangle partner2 = null, partner3 = null, partner4 = null, partner5 = null;
                        double size = 0;
                        double score = 1000 - Math.abs(cur.alas - partner1.tinggi);
                        score += 1000 - Math.abs(cur.tinggi - partner1.alas);

                        if (partner1.B != null) {
                            partner2 = partner1.B;
                            score += 2000 - Math.abs(cur.alas - partner2.alas);

                            if (partner2.D != null) {
                                partner3 = partner2.D;
                                score += 3000 - Math.abs(cur.alas - partner3.tinggi);

                                cur.setPosition(cur.LX, cur.LY + Math.abs((partner1.LY - partner3.LY)));
                                if (!isOverlap(cur) && (!partner3.fixed || Math.min(cur.RX, partner2.RX) == partner3.LX)) {
                                    partner3.backUp();
                                    partner3.setPosition(Math.min(Math.min(cur.RX, partner2.RX), partner2.LX), partner3.LY);
                                    if (!isOverlap(partner3)) {
                                        size = Math.min(cur.alas, partner2.alas) * Math.min(partner1.tinggi, partner3.tinggi);
                                    } else {
                                        partner3.rollback();
                                    }
                                }
                                score += size;
                            }
                        }
                        if (isOverlap(cur)) {
                            score = 0;
                        }
                        //System.err.println(partner1.id + " D : " + partner1.LX + " , " + partner1.LY + " == " + cur.id + " : " + cur.LX + " , " + cur.LY + " >> score : " + score);                        
                        if (score > state.score) {
                            state.score = score;
                            state.cur = new Rectangle(cur.id, cur.alas, cur.tinggi, cur.dir);
                            state.cur.setPosition(cur.LX, cur.LY);
                            state.partner1 = new Rectangle(partner1.id, partner1.alas, partner1.tinggi, partner1.dir);
                            state.partner1.setPosition(partner1.LX, partner1.LY);
                            state.cur.A = state.partner1;
                            state.partner1.D = state.cur;
                            if (partner2 != null) {
                                state.partner2 = new Rectangle(partner2.id, partner2.alas, partner2.tinggi, partner2.dir);
                                state.partner2.setPosition(partner2.LX, partner2.LY);
                            }
                            if (partner3 != null) {
                                state.cur.fixed = true;
                                state.partner1.fixed = true;
                                state.partner2.fixed = true;

                                state.partner3 = new Rectangle(partner3.id, partner3.alas, partner3.tinggi, partner3.dir);
                                state.partner3.setPosition(partner3.LX, partner3.LY);
                                state.partner3.fixed = true;
                                state.cur.B = state.partner3;
                                state.partner3.C = state.cur;
                                partner3.rollback();
                            }
                        }
                    }
                }

                System.err.println(partner1.id + " >> " + partner1.A + "  " + partner1.B + " " + partner1.C + " " + partner1.D);
                temp2.add(partner1);
            }

            System.err.println("=======");
            Map<Integer, Rectangle> set = new HashMap<Integer, Rectangle>();
            if (state.score > 0) {
                //System.err.println(state.cur.id + " = " + state.score + " :: " + state.cur.LX + "," + state.cur.LY);
                //System.err.println(state.cur.A + " " + state.cur.B + " " + state.cur.C + " " + state.cur.D);
                list.offer(state.cur);
                pool.addAll(temp);
                temp.clear();
                count++;
                added = new ArrayList<Rectangle>();
                added.add(state.cur);
                if (state.partner1 != null) {
                    set.put(state.partner1.id, state.partner1);
                }
                if (state.partner2 != null) {
                    set.put(state.partner1.id, state.partner1);
                }
                if (state.partner3 != null) {
                    set.put(state.partner1.id, state.partner1);
                }
            } else {
                cur.setDefault();
                temp.add(cur);
            }
            for (Rectangle r : temp2) {
                if (set.containsKey(r.id)) {
                    Rectangle xxx = set.get(r.id);
                    Rectangle yyy = new Rectangle(xxx.id, xxx.alas, xxx.tinggi, xxx.dir);
                    yyy.setPosition(xxx.LX, xxx.LY);
                    yyy.fixed = xxx.fixed;
                    yyy.A = xxx.A;
                    yyy.B = xxx.B;
                    yyy.C = xxx.C;
                    yyy.D = xxx.D;
                    list.add(yyy);
                    added.add(yyy);
                    System.err.println("masuk");

                } else {
                    list.add(r);
                    added.add(r);
                }
            }
        }

        int[] ret = new int[3 * A.length];
        while (!list.isEmpty()) {
            Rectangle r = list.poll();
            int id = r.id;
            ret[3 * id] = r.LX;
            ret[3 * id + 1] = r.LY;
            ret[3 * id + 2] = r.dir;
        }
        return ret;
    }

    Rectangle getCopy(Rectangle ori) {
        Rectangle ret = new Rectangle(ori.id, ori.alas, ori.tinggi, ori.dir);
        ret.setPosition(ori.LX, ori.LY);
        ret.fixed = ori.fixed;

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
        RectanglesAndHoles rh = new RectanglesAndHoles();
        rh.setupError();
        int N = Integer.parseInt(sc.nextLine());
        int[] A = new int[N];
        for (int i = 0; i < N; i++) {
            A[i] = Integer.parseInt(sc.nextLine());
        }
        int[] B = new int[N];
        for (int i = 0; i < N; i++) {
            B[i] = Integer.parseInt(sc.nextLine());
        }
        int[] ret = rh.place(A, B);

        for (int i = 0; i < 3 * N; i++) {
            System.out.println(ret[i]);
        }
        System.out.flush();
    }
}

class Rectangle {

    int id, alas, tinggi, LX, LY, RX, RY, dir;
    int oldX, oldY, bestX, bestY;
    Rectangle A, B, C, D;
    boolean fixed, oldFixed;

    public Rectangle(int id, int a, int b, int dir) {
        this.id = id;
        this.alas = a;
        this.tinggi = b;
        this.dir = dir;
    }

    void rotate() {
        dir = dir == 0 ? 1 : 0;
        alas ^= tinggi;
        tinggi ^= alas;
        alas ^= tinggi;
        setPosition(LX, LY);
    }

    void backUp() {
        oldFixed = fixed;
        oldX = LX;
        oldY = LY;
    }

    void rollback() {
        fixed = oldFixed;
        setPosition(oldX, oldY);
    }

    void setPosition(int x, int y) {
        LX = x;
        LY = y;
        RX = LX + alas;
        RY = LY + tinggi;
    }

    void setDefault() {
        setPosition(-10000, -10000);
    }
}

class SortByRatio implements Comparator<Rectangle> {

    @Override
    public int compare(Rectangle o1, Rectangle o2) {
        return ((o2.alas * o2.tinggi) / (o2.tinggi / o2.alas)) - ((o1.alas * o1.tinggi) / (o1.tinggi / o1.alas));
    }
}

class State {

    Rectangle cur;
    int partner1, partner2, partner3, partner4, partner5;
    double score;

    public State(Rectangle cur, int A, int B, int C, int D, int E, double score) {
        this.cur = cur;
        this.partner1 = A;
        this.partner2 = B;
        this.partner3 = C;
        this.partner4 = D;
        this.partner5 = E;
        this.score = score;
    }
}