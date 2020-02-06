import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Izhari Ishak Aksa
 */
public class Collaboration {

    private static final long TIME_LIMIT = 1789L;
    private static final long START_TIME = System.nanoTime();

    public Collaboration() {
        this.setupError();

        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        Job[] jobs = new Job[N];
        for (int i = 1; i <= N; i++) {
            jobs[i - 1] = new Job(i, sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt());
        }
        Arrays.sort(jobs);
        for (Job j : jobs) {
            System.out.println(j);
        }

        Queue<Solution> solutions = new PriorityQueue<>();
        solutions.add(new Solution());

        Solution best = new Solution();

        System.err.println(TimeUnit.SECONDS.toSeconds(System.nanoTime() - START_TIME));
        long counter = 1L;
        while (TimeUnit.SECONDS.toSeconds(System.nanoTime() - START_TIME) < TIME_LIMIT) {
            System.err.println(counter++ / 1000000 + "s");
        }

        for (Worker i : best.workers) {
            System.out.println(i);
        }
        System.out.flush();
    }

    public static void main(String[] args) {
        Collaboration col = new Collaboration();
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

class Solution implements Comparable<Solution> {
    int profit;
    List<Worker> workers = new ArrayList<>();

    public Solution() {
        this.profit = 0;
    }

    @Override
    public int compareTo(Solution o) {
        return this.profit - o.profit;
    }
}

class Job implements Comparable<Job> {
    int id, x, y, d, p, l, h, reward, profit, credit;
    boolean isDone = false;

    public Job(int id, int x, int y, int d, int p, int l, int h) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.d = d;
        this.p = p;
        this.l = l;
        this.h = l;
        this.reward = this.d * this.p * (this.p + 5);
        this.credit = this.p * (240 + this.d);
        this.profit = this.reward - credit;
    }

    public void setStatus(boolean status) {
        this.isDone = status;
    }

    public String toString() {
        return id + " " + x + "," + y + " reward = " + reward + " credit = " + credit + " profit = " + profit;
    }

    @Override
    public int compareTo(Job o) {
        return o.profit - this.profit;
    }
}

class Worker {

    int credit, location;

    List<StringBuilder> instructions = new ArrayList<>();

    public Worker(int moment) {
        this.credit = 240;
        this.location = 1;

        StringBuilder sb = new StringBuilder("start ").append(moment).append(" ").append(1);
        this.instructions.add(sb);
    }

    public void pay(int credit) {
        this.credit += credit;
    }

    public void move(int moment, int location) {
        StringBuilder sb = new StringBuilder("arrive ").append(moment).append(" ").append(location);
        this.instructions.add(sb);
        this.location = location;
    }

    public void work(int start, int end, int location) {
        StringBuilder sb = new StringBuilder("work ").append(start).append(" ").append(end).append(" ").append(location);
        this.instructions.add(sb);
        this.location = location;
    }

    public void end() {
        StringBuilder sb = new StringBuilder("end");
        this.instructions.add(sb);
        this.location = 1;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (StringBuilder s : instructions) {
            ret.append(s).append("\n");
        }
        return ret.toString();
    }
}