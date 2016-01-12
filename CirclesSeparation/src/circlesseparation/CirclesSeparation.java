package circlesseparation;

import static java.lang.Math.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * @author Izhari Ishak Aksa
 */
public class CirclesSeparation {

    final long TIME_OUT = 9300, TIME_OUT2 = 9700;
    final long START_TIME = System.nanoTime();
    double SEPARATOR = 1e-9;
    double globalScore = 0, counter = 0;
    int SIZE, start;
    Circle[] circle;
    Circle[] bestConf;
    double[] sinRad = new double[361];
    double[] cosRad = new double[361];
    int N = 5;
    //-->6
    /*final int[][] permutations = new int[][]{{0, 1, 2, 3, 4, 5}, {0, 1, 2, 3, 5, 4}, {0, 1, 2, 4, 3, 5}, {0, 1, 2, 4, 5, 3}, {0, 1, 2, 5, 3, 4}, {0, 1, 2, 5, 4, 3}, {0, 1, 3, 2, 4, 5}, {0, 1, 3, 2, 5, 4}, {0, 1, 3, 4, 2, 5}, {0, 1, 3, 4, 5, 2}, {0, 1, 3, 5, 2, 4}, {0, 1, 3, 5, 4, 2}, {0, 1, 4, 2, 3, 5}, {0, 1, 4, 2, 5, 3}, {0, 1, 4, 3, 2, 5}, {0, 1, 4, 3, 5, 2}, {0, 1, 4, 5, 2, 3}, {0, 1, 4, 5, 3, 2}, {0, 1, 5, 2, 3, 4}, {0, 1, 5, 2, 4, 3}, {0, 1, 5, 3, 2, 4}, {0, 1, 5, 3, 4, 2}, {0, 1, 5, 4, 2, 3}, {0, 1, 5, 4, 3, 2}, {0, 2, 1, 3, 4, 5}, {0, 2, 1, 3, 5, 4}, {0, 2, 1, 4, 3, 5}, {0, 2, 1, 4, 5, 3}, {0, 2, 1, 5, 3, 4}, {0, 2, 1, 5, 4, 3}, {0, 2, 3, 1, 4, 5}, {0, 2, 3, 1, 5, 4}, {0, 2, 3, 4, 1, 5}, {0, 2, 3, 4, 5, 1}, {0, 2, 3, 5, 1, 4}, {0, 2, 3, 5, 4, 1}, {0, 2, 4, 1, 3, 5}, {0, 2, 4, 1, 5, 3}, {0, 2, 4, 3, 1, 5}, {0, 2, 4, 3, 5, 1}, {0, 2, 4, 5, 1, 3}, {0, 2, 4, 5, 3, 1}, {0, 2, 5, 1, 3, 4}, {0, 2, 5, 1, 4, 3}, {0, 2, 5, 3, 1, 4}, {0, 2, 5, 3, 4, 1}, {0, 2, 5, 4, 1, 3}, {0, 2, 5, 4, 3, 1}, {0, 3, 1, 2, 4, 5}, {0, 3, 1, 2, 5, 4}, {0, 3, 1, 4, 2, 5}, {0, 3, 1, 4, 5, 2}, {0, 3, 1, 5, 2, 4}, {0, 3, 1, 5, 4, 2}, {0, 3, 2, 1, 4, 5}, {0, 3, 2, 1, 5, 4}, {0, 3, 2, 4, 1, 5}, {0, 3, 2, 4, 5, 1}, {0, 3, 2, 5, 1, 4}, {0, 3, 2, 5, 4, 1}, {0, 3, 4, 1, 2, 5}, {0, 3, 4, 1, 5, 2}, {0, 3, 4, 2, 1, 5}, {0, 3, 4, 2, 5, 1}, {0, 3, 4, 5, 1, 2}, {0, 3, 4, 5, 2, 1}, {0, 3, 5, 1, 2, 4}, {0, 3, 5, 1, 4, 2}, {0, 3, 5, 2, 1, 4}, {0, 3, 5, 2, 4, 1}, {0, 3, 5, 4, 1, 2}, {0, 3, 5, 4, 2, 1}, {0, 4, 1, 2, 3, 5}, {0, 4, 1, 2, 5, 3}, {0, 4, 1, 3, 2, 5}, {0, 4, 1, 3, 5, 2}, {0, 4, 1, 5, 2, 3}, {0, 4, 1, 5, 3, 2}, {0, 4, 2, 1, 3, 5}, {0, 4, 2, 1, 5, 3}, {0, 4, 2, 3, 1, 5}, {0, 4, 2, 3, 5, 1}, {0, 4, 2, 5, 1, 3}, {0, 4, 2, 5, 3, 1}, {0, 4, 3, 1, 2, 5}, {0, 4, 3, 1, 5, 2}, {0, 4, 3, 2, 1, 5}, {0, 4, 3, 2, 5, 1}, {0, 4, 3, 5, 1, 2}, {0, 4, 3, 5, 2, 1}, {0, 4, 5, 1, 2, 3}, {0, 4, 5, 1, 3, 2}, {0, 4, 5, 2, 1, 3}, {0, 4, 5, 2, 3, 1}, {0, 4, 5, 3, 1, 2}, {0, 4, 5, 3, 2, 1}, {0, 5, 1, 2, 3, 4}, {0, 5, 1, 2, 4, 3}, {0, 5, 1, 3, 2, 4}, {0, 5, 1, 3, 4, 2}, {0, 5, 1, 4, 2, 3}, {0, 5, 1, 4, 3, 2}, {0, 5, 2, 1, 3, 4}, {0, 5, 2, 1, 4, 3}, {0, 5, 2, 3, 1, 4}, {0, 5, 2, 3, 4, 1}, {0, 5, 2, 4, 1, 3}, {0, 5, 2, 4, 3, 1}, {0, 5, 3, 1, 2, 4}, {0, 5, 3, 1, 4, 2}, {0, 5, 3, 2, 1, 4}, {0, 5, 3, 2, 4, 1}, {0, 5, 3, 4, 1, 2}, {0, 5, 3, 4, 2, 1}, {0, 5, 4, 1, 2, 3}, {0, 5, 4, 1, 3, 2}, {0, 5, 4, 2, 1, 3}, {0, 5, 4, 2, 3, 1}, {0, 5, 4, 3, 1, 2}, {0, 5, 4, 3, 2, 1}, {1, 0, 2, 3, 4, 5}, {1, 0, 2, 3, 5, 4}, {1, 0, 2, 4, 3, 5}, {1, 0, 2, 4, 5, 3}, {1, 0, 2, 5, 3, 4}, {1, 0, 2, 5, 4, 3}, {1, 0, 3, 2, 4, 5}, {1, 0, 3, 2, 5, 4}, {1, 0, 3, 4, 2, 5}, {1, 0, 3, 4, 5, 2}, {1, 0, 3, 5, 2, 4}, {1, 0, 3, 5, 4, 2}, {1, 0, 4, 2, 3, 5}, {1, 0, 4, 2, 5, 3}, {1, 0, 4, 3, 2, 5}, {1, 0, 4, 3, 5, 2}, {1, 0, 4, 5, 2, 3}, {1, 0, 4, 5, 3, 2}, {1, 0, 5, 2, 3, 4}, {1, 0, 5, 2, 4, 3}, {1, 0, 5, 3, 2, 4}, {1, 0, 5, 3, 4, 2}, {1, 0, 5, 4, 2, 3}, {1, 0, 5, 4, 3, 2}, {1, 2, 0, 3, 4, 5}, {1, 2, 0, 3, 5, 4}, {1, 2, 0, 4, 3, 5}, {1, 2, 0, 4, 5, 3}, {1, 2, 0, 5, 3, 4}, {1, 2, 0, 5, 4, 3}, {1, 2, 3, 0, 4, 5}, {1, 2, 3, 0, 5, 4}, {1, 2, 3, 4, 0, 5}, {1, 2, 3, 4, 5, 0}, {1, 2, 3, 5, 0, 4}, {1, 2, 3, 5, 4, 0}, {1, 2, 4, 0, 3, 5}, {1, 2, 4, 0, 5, 3}, {1, 2, 4, 3, 0, 5}, {1, 2, 4, 3, 5, 0}, {1, 2, 4, 5, 0, 3}, {1, 2, 4, 5, 3, 0}, {1, 2, 5, 0, 3, 4}, {1, 2, 5, 0, 4, 3}, {1, 2, 5, 3, 0, 4}, {1, 2, 5, 3, 4, 0}, {1, 2, 5, 4, 0, 3}, {1, 2, 5, 4, 3, 0}, {1, 3, 0, 2, 4, 5}, {1, 3, 0, 2, 5, 4}, {1, 3, 0, 4, 2, 5}, {1, 3, 0, 4, 5, 2}, {1, 3, 0, 5, 2, 4}, {1, 3, 0, 5, 4, 2}, {1, 3, 2, 0, 4, 5}, {1, 3, 2, 0, 5, 4}, {1, 3, 2, 4, 0, 5}, {1, 3, 2, 4, 5, 0}, {1, 3, 2, 5, 0, 4}, {1, 3, 2, 5, 4, 0},
     {1, 3, 4, 0, 2, 5}, {1, 3, 4, 0, 5, 2}, {1, 3, 4, 2, 0, 5}, {1, 3, 4, 2, 5, 0}, {1, 3, 4, 5, 0, 2}, {1, 3, 4, 5, 2, 0}, {1, 3, 5, 0, 2, 4}, {1, 3, 5, 0, 4, 2}, {1, 3, 5, 2, 0, 4}, {1, 3, 5, 2, 4, 0}, {1, 3, 5, 4, 0, 2}, {1, 3, 5, 4, 2, 0}, {1, 4, 0, 2, 3, 5}, {1, 4, 0, 2, 5, 3}, {1, 4, 0, 3, 2, 5}, {1, 4, 0, 3, 5, 2}, {1, 4, 0, 5, 2, 3}, {1, 4, 0, 5, 3, 2}, {1, 4, 2, 0, 3, 5}, {1, 4, 2, 0, 5, 3}, {1, 4, 2, 3, 0, 5}, {1, 4, 2, 3, 5, 0}, {1, 4, 2, 5, 0, 3}, {1, 4, 2, 5, 3, 0}, {1, 4, 3, 0, 2, 5}, {1, 4, 3, 0, 5, 2}, {1, 4, 3, 2, 0, 5}, {1, 4, 3, 2, 5, 0}, {1, 4, 3, 5, 0, 2}, {1, 4, 3, 5, 2, 0}, {1, 4, 5, 0, 2, 3}, {1, 4, 5, 0, 3, 2}, {1, 4, 5, 2, 0, 3}, {1, 4, 5, 2, 3, 0}, {1, 4, 5, 3, 0, 2}, {1, 4, 5, 3, 2, 0}, {1, 5, 0, 2, 3, 4}, {1, 5, 0, 2, 4, 3}, {1, 5, 0, 3, 2, 4}, {1, 5, 0, 3, 4, 2}, {1, 5, 0, 4, 2, 3}, {1, 5, 0, 4, 3, 2}, {1, 5, 2, 0, 3, 4}, {1, 5, 2, 0, 4, 3}, {1, 5, 2, 3, 0, 4}, {1, 5, 2, 3, 4, 0}, {1, 5, 2, 4, 0, 3}, {1, 5, 2, 4, 3, 0}, {1, 5, 3, 0, 2, 4}, {1, 5, 3, 0, 4, 2}, {1, 5, 3, 2, 0, 4}, {1, 5, 3, 2, 4, 0}, {1, 5, 3, 4, 0, 2}, {1, 5, 3, 4, 2, 0}, {1, 5, 4, 0, 2, 3}, {1, 5, 4, 0, 3, 2}, {1, 5, 4, 2, 0, 3}, {1, 5, 4, 2, 3, 0}, {1, 5, 4, 3, 0, 2}, {1, 5, 4, 3, 2, 0}, {2, 0, 1, 3, 4, 5}, {2, 0, 1, 3, 5, 4}, {2, 0, 1, 4, 3, 5}, {2, 0, 1, 4, 5, 3}, {2, 0, 1, 5, 3, 4}, {2, 0, 1, 5, 4, 3}, {2, 0, 3, 1, 4, 5}, {2, 0, 3, 1, 5, 4}, {2, 0, 3, 4, 1, 5}, {2, 0, 3, 4, 5, 1}, {2, 0, 3, 5, 1, 4}, {2, 0, 3, 5, 4, 1}, {2, 0, 4, 1, 3, 5}, {2, 0, 4, 1, 5, 3}, {2, 0, 4, 3, 1, 5}, {2, 0, 4, 3, 5, 1}, {2, 0, 4, 5, 1, 3}, {2, 0, 4, 5, 3, 1}, {2, 0, 5, 1, 3, 4}, {2, 0, 5, 1, 4, 3}, {2, 0, 5, 3, 1, 4}, {2, 0, 5, 3, 4, 1}, {2, 0, 5, 4, 1, 3}, {2, 0, 5, 4, 3, 1}, {2, 1, 0, 3, 4, 5}, {2, 1, 0, 3, 5, 4}, {2, 1, 0, 4, 3, 5}, {2, 1, 0, 4, 5, 3}, {2, 1, 0, 5, 3, 4}, {2, 1, 0, 5, 4, 3}, {2, 1, 3, 0, 4, 5}, {2, 1, 3, 0, 5, 4}, {2, 1, 3, 4, 0, 5}, {2, 1, 3, 4, 5, 0}, {2, 1, 3, 5, 0, 4}, {2, 1, 3, 5, 4, 0}, {2, 1, 4, 0, 3, 5}, {2, 1, 4, 0, 5, 3}, {2, 1, 4, 3, 0, 5}, {2, 1, 4, 3, 5, 0}, {2, 1, 4, 5, 0, 3}, {2, 1, 4, 5, 3, 0}, {2, 1, 5, 0, 3, 4}, {2, 1, 5, 0, 4, 3}, {2, 1, 5, 3, 0, 4}, {2, 1, 5, 3, 4, 0}, {2, 1, 5, 4, 0, 3}, {2, 1, 5, 4, 3, 0}, {2, 3, 0, 1, 4, 5}, {2, 3, 0, 1, 5, 4}, {2, 3, 0, 4, 1, 5}, {2, 3, 0, 4, 5, 1}, {2, 3, 0, 5, 1, 4}, {2, 3, 0, 5, 4, 1}, {2, 3, 1, 0, 4, 5}, {2, 3, 1, 0, 5, 4}, {2, 3, 1, 4, 0, 5}, {2, 3, 1, 4, 5, 0}, {2, 3, 1, 5, 0, 4}, {2, 3, 1, 5, 4, 0}, {2, 3, 4, 0, 1, 5}, {2, 3, 4, 0, 5, 1}, {2, 3, 4, 1, 0, 5}, {2, 3, 4, 1, 5, 0}, {2, 3, 4, 5, 0, 1}, {2, 3, 4, 5, 1, 0}, {2, 3, 5, 0, 1, 4}, {2, 3, 5, 0, 4, 1}, {2, 3, 5, 1, 0, 4}, {2, 3, 5, 1, 4, 0}, {2, 3, 5, 4, 0, 1}, {2, 3, 5, 4, 1, 0}, {2, 4, 0, 1, 3, 5}, {2, 4, 0, 1, 5, 3}, {2, 4, 0, 3, 1, 5}, {2, 4, 0, 3, 5, 1}, {2, 4, 0, 5, 1, 3}, {2, 4, 0, 5, 3, 1}, {2, 4, 1, 0, 3, 5}, {2, 4, 1, 0, 5, 3}, {2, 4, 1, 3, 0, 5}, {2, 4, 1, 3, 5, 0}, {2, 4, 1, 5, 0, 3}, {2, 4, 1, 5, 3, 0}, {2, 4, 3, 0, 1, 5}, {2, 4, 3, 0, 5, 1}, {2, 4, 3, 1, 0, 5}, {2, 4, 3, 1, 5, 0}, {2, 4, 3, 5, 0, 1}, {2, 4, 3, 5, 1, 0}, {2, 4, 5, 0, 1, 3}, {2, 4, 5, 0, 3, 1}, {2, 4, 5, 1, 0, 3}, {2, 4, 5, 1, 3, 0}, {2, 4, 5, 3, 0, 1}, {2, 4, 5, 3, 1, 0}, {2, 5, 0, 1, 3, 4}, {2, 5, 0, 1, 4, 3}, {2, 5, 0, 3, 1, 4}, {2, 5, 0, 3, 4, 1}, {2, 5, 0, 4, 1, 3}, {2, 5, 0, 4, 3, 1}, {2, 5, 1, 0, 3, 4}, {2, 5, 1, 0, 4, 3}, {2, 5, 1, 3, 0, 4}, {2, 5, 1, 3, 4, 0}, {2, 5, 1, 4, 0, 3}, {2, 5, 1, 4, 3, 0}, {2, 5, 3, 0, 1, 4}, {2, 5, 3, 0, 4, 1}, {2, 5, 3, 1, 0, 4}, {2, 5, 3, 1, 4, 0}, {2, 5, 3, 4, 0, 1}, {2, 5, 3, 4, 1, 0}, {2, 5, 4, 0, 1, 3}, {2, 5, 4, 0, 3, 1}, {2, 5, 4, 1, 0, 3}, {2, 5, 4, 1, 3, 0}, {2, 5, 4, 3, 0, 1}, {2, 5, 4, 3, 1, 0},
     {3, 0, 1, 2, 4, 5}, {3, 0, 1, 2, 5, 4}, {3, 0, 1, 4, 2, 5}, {3, 0, 1, 4, 5, 2}, {3, 0, 1, 5, 2, 4}, {3, 0, 1, 5, 4, 2}, {3, 0, 2, 1, 4, 5}, {3, 0, 2, 1, 5, 4}, {3, 0, 2, 4, 1, 5}, {3, 0, 2, 4, 5, 1}, {3, 0, 2, 5, 1, 4}, {3, 0, 2, 5, 4, 1}, {3, 0, 4, 1, 2, 5}, {3, 0, 4, 1, 5, 2}, {3, 0, 4, 2, 1, 5}, {3, 0, 4, 2, 5, 1}, {3, 0, 4, 5, 1, 2}, {3, 0, 4, 5, 2, 1}, {3, 0, 5, 1, 2, 4}, {3, 0, 5, 1, 4, 2}, {3, 0, 5, 2, 1, 4}, {3, 0, 5, 2, 4, 1}, {3, 0, 5, 4, 1, 2}, {3, 0, 5, 4, 2, 1}, {3, 1, 0, 2, 4, 5}, {3, 1, 0, 2, 5, 4}, {3, 1, 0, 4, 2, 5}, {3, 1, 0, 4, 5, 2}, {3, 1, 0, 5, 2, 4}, {3, 1, 0, 5, 4, 2}, {3, 1, 2, 0, 4, 5}, {3, 1, 2, 0, 5, 4}, {3, 1, 2, 4, 0, 5}, {3, 1, 2, 4, 5, 0}, {3, 1, 2, 5, 0, 4}, {3, 1, 2, 5, 4, 0}, {3, 1, 4, 0, 2, 5}, {3, 1, 4, 0, 5, 2}, {3, 1, 4, 2, 0, 5}, {3, 1, 4, 2, 5, 0}, {3, 1, 4, 5, 0, 2}, {3, 1, 4, 5, 2, 0}, {3, 1, 5, 0, 2, 4}, {3, 1, 5, 0, 4, 2}, {3, 1, 5, 2, 0, 4}, {3, 1, 5, 2, 4, 0}, {3, 1, 5, 4, 0, 2}, {3, 1, 5, 4, 2, 0}, {3, 2, 0, 1, 4, 5}, {3, 2, 0, 1, 5, 4}, {3, 2, 0, 4, 1, 5}, {3, 2, 0, 4, 5, 1}, {3, 2, 0, 5, 1, 4}, {3, 2, 0, 5, 4, 1}, {3, 2, 1, 0, 4, 5}, {3, 2, 1, 0, 5, 4}, {3, 2, 1, 4, 0, 5}, {3, 2, 1, 4, 5, 0}, {3, 2, 1, 5, 0, 4}, {3, 2, 1, 5, 4, 0}, {3, 2, 4, 0, 1, 5}, {3, 2, 4, 0, 5, 1}, {3, 2, 4, 1, 0, 5}, {3, 2, 4, 1, 5, 0}, {3, 2, 4, 5, 0, 1}, {3, 2, 4, 5, 1, 0}, {3, 2, 5, 0, 1, 4}, {3, 2, 5, 0, 4, 1}, {3, 2, 5, 1, 0, 4}, {3, 2, 5, 1, 4, 0}, {3, 2, 5, 4, 0, 1}, {3, 2, 5, 4, 1, 0}, {3, 4, 0, 1, 2, 5}, {3, 4, 0, 1, 5, 2}, {3, 4, 0, 2, 1, 5}, {3, 4, 0, 2, 5, 1}, {3, 4, 0, 5, 1, 2}, {3, 4, 0, 5, 2, 1}, {3, 4, 1, 0, 2, 5}, {3, 4, 1, 0, 5, 2}, {3, 4, 1, 2, 0, 5}, {3, 4, 1, 2, 5, 0}, {3, 4, 1, 5, 0, 2}, {3, 4, 1, 5, 2, 0}, {3, 4, 2, 0, 1, 5}, {3, 4, 2, 0, 5, 1}, {3, 4, 2, 1, 0, 5}, {3, 4, 2, 1, 5, 0}, {3, 4, 2, 5, 0, 1}, {3, 4, 2, 5, 1, 0}, {3, 4, 5, 0, 1, 2}, {3, 4, 5, 0, 2, 1}, {3, 4, 5, 1, 0, 2}, {3, 4, 5, 1, 2, 0}, {3, 4, 5, 2, 0, 1}, {3, 4, 5, 2, 1, 0}, {3, 5, 0, 1, 2, 4}, {3, 5, 0, 1, 4, 2}, {3, 5, 0, 2, 1, 4}, {3, 5, 0, 2, 4, 1}, {3, 5, 0, 4, 1, 2}, {3, 5, 0, 4, 2, 1}, {3, 5, 1, 0, 2, 4}, {3, 5, 1, 0, 4, 2}, {3, 5, 1, 2, 0, 4}, {3, 5, 1, 2, 4, 0}, {3, 5, 1, 4, 0, 2}, {3, 5, 1, 4, 2, 0}, {3, 5, 2, 0, 1, 4}, {3, 5, 2, 0, 4, 1}, {3, 5, 2, 1, 0, 4}, {3, 5, 2, 1, 4, 0}, {3, 5, 2, 4, 0, 1}, {3, 5, 2, 4, 1, 0}, {3, 5, 4, 0, 1, 2}, {3, 5, 4, 0, 2, 1}, {3, 5, 4, 1, 0, 2}, {3, 5, 4, 1, 2, 0}, {3, 5, 4, 2, 0, 1}, {3, 5, 4, 2, 1, 0}, {4, 0, 1, 2, 3, 5}, {4, 0, 1, 2, 5, 3}, {4, 0, 1, 3, 2, 5}, {4, 0, 1, 3, 5, 2}, {4, 0, 1, 5, 2, 3}, {4, 0, 1, 5, 3, 2}, {4, 0, 2, 1, 3, 5}, {4, 0, 2, 1, 5, 3}, {4, 0, 2, 3, 1, 5}, {4, 0, 2, 3, 5, 1}, {4, 0, 2, 5, 1, 3}, {4, 0, 2, 5, 3, 1}, {4, 0, 3, 1, 2, 5}, {4, 0, 3, 1, 5, 2}, {4, 0, 3, 2, 1, 5}, {4, 0, 3, 2, 5, 1}, {4, 0, 3, 5, 1, 2}, {4, 0, 3, 5, 2, 1}, {4, 0, 5, 1, 2, 3}, {4, 0, 5, 1, 3, 2}, {4, 0, 5, 2, 1, 3}, {4, 0, 5, 2, 3, 1}, {4, 0, 5, 3, 1, 2}, {4, 0, 5, 3, 2, 1}, {4, 1, 0, 2, 3, 5}, {4, 1, 0, 2, 5, 3}, {4, 1, 0, 3, 2, 5}, {4, 1, 0, 3, 5, 2}, {4, 1, 0, 5, 2, 3}, {4, 1, 0, 5, 3, 2}, {4, 1, 2, 0, 3, 5}, {4, 1, 2, 0, 5, 3}, {4, 1, 2, 3, 0, 5}, {4, 1, 2, 3, 5, 0}, {4, 1, 2, 5, 0, 3}, {4, 1, 2, 5, 3, 0}, {4, 1, 3, 0, 2, 5}, {4, 1, 3, 0, 5, 2}, {4, 1, 3, 2, 0, 5}, {4, 1, 3, 2, 5, 0}, {4, 1, 3, 5, 0, 2}, {4, 1, 3, 5, 2, 0}, {4, 1, 5, 0, 2, 3}, {4, 1, 5, 0, 3, 2}, {4, 1, 5, 2, 0, 3}, {4, 1, 5, 2, 3, 0}, {4, 1, 5, 3, 0, 2}, {4, 1, 5, 3, 2, 0}, {4, 2, 0, 1, 3, 5}, {4, 2, 0, 1, 5, 3}, {4, 2, 0, 3, 1, 5}, {4, 2, 0, 3, 5, 1}, {4, 2, 0, 5, 1, 3}, {4, 2, 0, 5, 3, 1}, {4, 2, 1, 0, 3, 5}, {4, 2, 1, 0, 5, 3}, {4, 2, 1, 3, 0, 5}, {4, 2, 1, 3, 5, 0}, {4, 2, 1, 5, 0, 3}, {4, 2, 1, 5, 3, 0},
     {4, 2, 3, 0, 1, 5}, {4, 2, 3, 0, 5, 1}, {4, 2, 3, 1, 0, 5}, {4, 2, 3, 1, 5, 0}, {4, 2, 3, 5, 0, 1}, {4, 2, 3, 5, 1, 0}, {4, 2, 5, 0, 1, 3}, {4, 2, 5, 0, 3, 1}, {4, 2, 5, 1, 0, 3}, {4, 2, 5, 1, 3, 0}, {4, 2, 5, 3, 0, 1}, {4, 2, 5, 3, 1, 0}, {4, 3, 0, 1, 2, 5}, {4, 3, 0, 1, 5, 2}, {4, 3, 0, 2, 1, 5}, {4, 3, 0, 2, 5, 1}, {4, 3, 0, 5, 1, 2}, {4, 3, 0, 5, 2, 1}, {4, 3, 1, 0, 2, 5}, {4, 3, 1, 0, 5, 2}, {4, 3, 1, 2, 0, 5}, {4, 3, 1, 2, 5, 0}, {4, 3, 1, 5, 0, 2}, {4, 3, 1, 5, 2, 0}, {4, 3, 2, 0, 1, 5}, {4, 3, 2, 0, 5, 1}, {4, 3, 2, 1, 0, 5}, {4, 3, 2, 1, 5, 0}, {4, 3, 2, 5, 0, 1}, {4, 3, 2, 5, 1, 0}, {4, 3, 5, 0, 1, 2}, {4, 3, 5, 0, 2, 1}, {4, 3, 5, 1, 0, 2}, {4, 3, 5, 1, 2, 0}, {4, 3, 5, 2, 0, 1}, {4, 3, 5, 2, 1, 0}, {4, 5, 0, 1, 2, 3}, {4, 5, 0, 1, 3, 2}, {4, 5, 0, 2, 1, 3}, {4, 5, 0, 2, 3, 1}, {4, 5, 0, 3, 1, 2}, {4, 5, 0, 3, 2, 1}, {4, 5, 1, 0, 2, 3}, {4, 5, 1, 0, 3, 2}, {4, 5, 1, 2, 0, 3}, {4, 5, 1, 2, 3, 0}, {4, 5, 1, 3, 0, 2}, {4, 5, 1, 3, 2, 0}, {4, 5, 2, 0, 1, 3}, {4, 5, 2, 0, 3, 1}, {4, 5, 2, 1, 0, 3}, {4, 5, 2, 1, 3, 0}, {4, 5, 2, 3, 0, 1}, {4, 5, 2, 3, 1, 0}, {4, 5, 3, 0, 1, 2}, {4, 5, 3, 0, 2, 1}, {4, 5, 3, 1, 0, 2}, {4, 5, 3, 1, 2, 0}, {4, 5, 3, 2, 0, 1}, {4, 5, 3, 2, 1, 0}, {5, 0, 1, 2, 3, 4}, {5, 0, 1, 2, 4, 3}, {5, 0, 1, 3, 2, 4}, {5, 0, 1, 3, 4, 2}, {5, 0, 1, 4, 2, 3}, {5, 0, 1, 4, 3, 2}, {5, 0, 2, 1, 3, 4}, {5, 0, 2, 1, 4, 3}, {5, 0, 2, 3, 1, 4}, {5, 0, 2, 3, 4, 1}, {5, 0, 2, 4, 1, 3}, {5, 0, 2, 4, 3, 1}, {5, 0, 3, 1, 2, 4}, {5, 0, 3, 1, 4, 2}, {5, 0, 3, 2, 1, 4}, {5, 0, 3, 2, 4, 1}, {5, 0, 3, 4, 1, 2}, {5, 0, 3, 4, 2, 1}, {5, 0, 4, 1, 2, 3}, {5, 0, 4, 1, 3, 2}, {5, 0, 4, 2, 1, 3}, {5, 0, 4, 2, 3, 1}, {5, 0, 4, 3, 1, 2}, {5, 0, 4, 3, 2, 1}, {5, 1, 0, 2, 3, 4}, {5, 1, 0, 2, 4, 3}, {5, 1, 0, 3, 2, 4}, {5, 1, 0, 3, 4, 2}, {5, 1, 0, 4, 2, 3}, {5, 1, 0, 4, 3, 2}, {5, 1, 2, 0, 3, 4}, {5, 1, 2, 0, 4, 3}, {5, 1, 2, 3, 0, 4}, {5, 1, 2, 3, 4, 0}, {5, 1, 2, 4, 0, 3}, {5, 1, 2, 4, 3, 0}, {5, 1, 3, 0, 2, 4}, {5, 1, 3, 0, 4, 2}, {5, 1, 3, 2, 0, 4}, {5, 1, 3, 2, 4, 0}, {5, 1, 3, 4, 0, 2}, {5, 1, 3, 4, 2, 0}, {5, 1, 4, 0, 2, 3}, {5, 1, 4, 0, 3, 2}, {5, 1, 4, 2, 0, 3}, {5, 1, 4, 2, 3, 0}, {5, 1, 4, 3, 0, 2}, {5, 1, 4, 3, 2, 0}, {5, 2, 0, 1, 3, 4}, {5, 2, 0, 1, 4, 3}, {5, 2, 0, 3, 1, 4}, {5, 2, 0, 3, 4, 1}, {5, 2, 0, 4, 1, 3}, {5, 2, 0, 4, 3, 1}, {5, 2, 1, 0, 3, 4}, {5, 2, 1, 0, 4, 3}, {5, 2, 1, 3, 0, 4}, {5, 2, 1, 3, 4, 0}, {5, 2, 1, 4, 0, 3}, {5, 2, 1, 4, 3, 0}, {5, 2, 3, 0, 1, 4}, {5, 2, 3, 0, 4, 1}, {5, 2, 3, 1, 0, 4}, {5, 2, 3, 1, 4, 0}, {5, 2, 3, 4, 0, 1}, {5, 2, 3, 4, 1, 0}, {5, 2, 4, 0, 1, 3}, {5, 2, 4, 0, 3, 1}, {5, 2, 4, 1, 0, 3}, {5, 2, 4, 1, 3, 0}, {5, 2, 4, 3, 0, 1}, {5, 2, 4, 3, 1, 0}, {5, 3, 0, 1, 2, 4}, {5, 3, 0, 1, 4, 2}, {5, 3, 0, 2, 1, 4}, {5, 3, 0, 2, 4, 1}, {5, 3, 0, 4, 1, 2}, {5, 3, 0, 4, 2, 1}, {5, 3, 1, 0, 2, 4}, {5, 3, 1, 0, 4, 2}, {5, 3, 1, 2, 0, 4}, {5, 3, 1, 2, 4, 0}, {5, 3, 1, 4, 0, 2}, {5, 3, 1, 4, 2, 0}, {5, 3, 2, 0, 1, 4}, {5, 3, 2, 0, 4, 1}, {5, 3, 2, 1, 0, 4}, {5, 3, 2, 1, 4, 0}, {5, 3, 2, 4, 0, 1}, {5, 3, 2, 4, 1, 0}, {5, 3, 4, 0, 1, 2}, {5, 3, 4, 0, 2, 1}, {5, 3, 4, 1, 0, 2}, {5, 3, 4, 1, 2, 0}, {5, 3, 4, 2, 0, 1}, {5, 3, 4, 2, 1, 0}, {5, 4, 0, 1, 2, 3}, {5, 4, 0, 1, 3, 2}, {5, 4, 0, 2, 1, 3}, {5, 4, 0, 2, 3, 1}, {5, 4, 0, 3, 1, 2}, {5, 4, 0, 3, 2, 1}, {5, 4, 1, 0, 2, 3}, {5, 4, 1, 0, 3, 2}, {5, 4, 1, 2, 0, 3}, {5, 4, 1, 2, 3, 0}, {5, 4, 1, 3, 0, 2}, {5, 4, 1, 3, 2, 0}, {5, 4, 2, 0, 1, 3}, {5, 4, 2, 0, 3, 1}, {5, 4, 2, 1, 0, 3}, {5, 4, 2, 1, 3, 0}, {5, 4, 2, 3, 0, 1}, {5, 4, 2, 3, 1, 0}, {5, 4, 3, 0, 1, 2}, {5, 4, 3, 0, 2, 1}, {5, 4, 3, 1, 0, 2}, {5, 4, 3, 1, 2, 0}, {5, 4, 3, 2, 0, 1}, {5, 4, 3, 2, 1, 0}};*/
    //-->5
    final int[][] permutations = new int[][]{{1, 2, 3, 4, 0}, {2, 1, 3, 4, 0}, {2, 3, 1, 4, 0}, {3, 2, 1, 4, 0}, {1, 3, 2, 4, 0}, {3, 1, 2, 4, 0}, {3, 2, 4, 1, 0}, {2, 3, 4, 1, 0}, {2, 4, 3, 1, 0}, {4, 2, 3, 1, 0}, {3, 4, 2, 1, 0}, {4, 3, 2, 1, 0}, {1, 3, 4, 2, 0}, {3, 1, 4, 2, 0}, {3, 4, 1, 2, 0}, {4, 3, 1, 2, 0}, {1, 4, 3, 2, 0}, {4, 1, 3, 2, 0}, {1, 2, 4, 3, 0}, {2, 1, 4, 3, 0}, {2, 4, 1, 3, 0}, {4, 2, 1, 3, 0}, {1, 4, 2, 3, 0}, {4, 1, 2, 3, 0}, {4, 2, 3, 0, 1}, {2, 4, 3, 0, 1}, {2, 3, 4, 0, 1}, {3, 2, 4, 0, 1}, {4, 3, 2, 0, 1}, {3, 4, 2, 0, 1}, {3, 2, 0, 4, 1}, {2, 3, 0, 4, 1}, {2, 0, 3, 4, 1}, {0, 2, 3, 4, 1}, {3, 0, 2, 4, 1}, {0, 3, 2, 4, 1}, {4, 3, 0, 2, 1}, {3, 4, 0, 2, 1}, {3, 0, 4, 2, 1}, {0, 3, 4, 2, 1}, {4, 0, 3, 2, 1}, {0, 4, 3, 2, 1}, {4, 2, 0, 3, 1}, {2, 4, 0, 3, 1}, {2, 0, 4, 3, 1}, {0, 2, 4, 3, 1}, {4, 0, 2, 3, 1}, {0, 4, 2, 3, 1}, {1, 4, 3, 0, 2}, {4, 1, 3, 0, 2}, {4, 3, 1, 0, 2}, {3, 4, 1, 0, 2}, {1, 3, 4, 0, 2}, {3, 1, 4, 0, 2}, {3, 4, 0, 1, 2}, {4, 3, 0, 1, 2}, {4, 0, 3, 1, 2}, {0, 4, 3, 1, 2}, {3, 0, 4, 1, 2}, {0, 3, 4, 1, 2}, {1, 3, 0, 4, 2}, {3, 1, 0, 4, 2}, {3, 0, 1, 4, 2}, {0, 3, 1, 4, 2}, {1, 0, 3, 4, 2}, {0, 1, 3, 4, 2}, {1, 4, 0, 3, 2}, {4, 1, 0, 3, 2}, {4, 0, 1, 3, 2}, {0, 4, 1, 3, 2}, {1, 0, 4, 3, 2}, {0, 1, 4, 3, 2}, {1, 2, 4, 0, 3}, {2, 1, 4, 0, 3}, {2, 4, 1, 0, 3}, {4, 2, 1, 0, 3}, {1, 4, 2, 0, 3}, {4, 1, 2, 0, 3}, {4, 2, 0, 1, 3}, {2, 4, 0, 1, 3}, {2, 0, 4, 1, 3}, {0, 2, 4, 1, 3}, {4, 0, 2, 1, 3}, {0, 4, 2, 1, 3}, {1, 4, 0, 2, 3}, {4, 1, 0, 2, 3}, {4, 0, 1, 2, 3}, {0, 4, 1, 2, 3}, {1, 0, 4, 2, 3}, {0, 1, 4, 2, 3}, {1, 2, 0, 4, 3}, {2, 1, 0, 4, 3}, {2, 0, 1, 4, 3}, {0, 2, 1, 4, 3}, {1, 0, 2, 4, 3}, {0, 1, 2, 4, 3}, {1, 2, 3, 0, 4}, {2, 1, 3, 0, 4}, {2, 3, 1, 0, 4}, {3, 2, 1, 0, 4}, {1, 3, 2, 0, 4}, {3, 1, 2, 0, 4}, {3, 2, 0, 1, 4}, {2, 3, 0, 1, 4}, {2, 0, 3, 1, 4}, {0, 2, 3, 1, 4}, {3, 0, 2, 1, 4}, {0, 3, 2, 1, 4}, {1, 3, 0, 2, 4}, {3, 1, 0, 2, 4}, {3, 0, 1, 2, 4}, {0, 3, 1, 2, 4}, {1, 0, 3, 2, 4}, {0, 1, 3, 2, 4}, {1, 2, 0, 3, 4}, {2, 1, 0, 3, 4}, {2, 0, 1, 3, 4}, {0, 2, 1, 3, 4}, {1, 0, 2, 3, 4}, {0, 1, 2, 3, 4}};
    //-->4
    //final int[][] permutations = new int[][]{{1, 2, 3, 0}, {2, 1, 3, 0}, {2, 3, 1, 0}, {3, 2, 1, 0}, {1, 3, 2, 0}, {3, 1, 2, 0}, {3, 2, 0, 1}, {2, 3, 0, 1}, {2, 0, 3, 1}, {0, 2, 3, 1}, {3, 0, 2, 1}, {0, 3, 2, 1}, {1, 3, 0, 2}, {3, 1, 0, 2}, {3, 0, 1, 2}, {0, 3, 1, 2}, {1, 0, 3, 2}, {0, 1, 3, 2}, {1, 2, 0, 3}, {2, 1, 0, 3}, {2, 0, 1, 3}, {0, 2, 1, 3}, {1, 0, 2, 3}, {0, 1, 2, 3}};
    //-->3
    //final int[][] permutations = new int[][]{{1, 2, 0}, {2, 1, 0}, {2, 0, 1}, {0, 2, 1}, {1, 0, 2}, {0, 1, 2}};

    void init(double[] x, double[] y, double[] r, double[] m) {
        for (int i = 0; i < 361; i++) {
            sinRad[i] = sin(i * PI / 180);
            cosRad[i] = cos(i * PI / 180);
        }
        circle = new Circle[SIZE];
        double lr = 0;
        for (int i = 0; i < SIZE; i++) {
            circle[i] = new Circle(i, x[i], y[i], r[i], m[i], 0, 0, 0, false, 0);
            lr = max(lr, r[i]);
        }
        Arrays.sort(circle, new ByMass(lr));
        //counter = lr * 0.4;
        counter = 1;
    }

    void getInitialScore() {
        int index = 0;
        do {
            boolean overlap = false;
            int total = 0;
            for (int i = index - 1; i >= 0; i--) {
                if (isOverlapped(circle[i], circle[index].getX(), circle[index].getY(), circle[index].getR())) {
                    overlap = true;
                    break;
                }
            }
            if (overlap) {
                double indScore = 10e5;
                double px = 0, py = 0;
                int afiliasi = 0;
                Neighbor[] nbr = new Neighbor[index];
                for (int i = 0; i < index; i++) {
                    double dist = distance(circle[i].getXX(), circle[i].getYY(), circle[index].getX(), circle[index].getY());
                    nbr[i] = new Neighbor(i, dist);
                }
                Arrays.sort(nbr, new ByDist());

                for (int ii = 0; ii < index && indScore > 10e5 - 1; ii++) {
                    int i = nbr[ii].id;
                    double curPX = circle[i].getXX() + circle[i].getR() + circle[index].getR() + SEPARATOR;
                    double curPY = circle[i].getYY();
                    for (double j = 0; j < 360; j += counter) {
                        //System.err.println(j);
                        double[] newPoints = rotate(curPX, curPY, circle[i].getXX(), circle[i].getYY(), j);
                        double score = cost(circle[index].getX(), circle[index].getY(), newPoints[0], newPoints[1], circle[index].getMass());
                        if (score < indScore) {
                            boolean overlapping = false;
                            for (int k = 0; k < index; k++) {
                                if (isOverlapped(circle[k], newPoints[0], newPoints[1], circle[index].getR())) {
                                    overlapping = true;
                                    break;
                                }
                            }
                            if (!overlapping) {
                                indScore = score;
                                px = newPoints[0];
                                py = newPoints[1];
                                afiliasi = i;
                            }
                        }
                    }
                }
                globalScore += indScore;
                circle[index] = new Circle(circle[index].getId(), circle[index].getX(), circle[index].getY(), circle[index].getR(), circle[index].getMass(),
                        px, py, indScore, false, afiliasi);
            } else {
                circle[index] = new Circle(circle[index].getId(), circle[index].getX(), circle[index].getY(), circle[index].getR(), circle[index].getMass(),
                        circle[index].getX(), circle[index].getY(), 0, false, 0);
            }
        } while (++index < SIZE);
    }

    double intersect(double x1, double y1, double r1, double x2, double y2, double r2) {
        return (r1 + r2) - Math.abs(x1 - x2) - Math.abs(y1 - y2);
    }

    double distance(double x1, double y1, double x2, double y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    double cost(double x1, double y1, double x2, double y2, double m) {
        return m * Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    boolean isOverlapped(double x1, double y1, double r1, double x2, double y2, double r2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) < (r1 + r2) * (r1 + r2);
    }

    boolean isOverlapped(Circle c1, double x, double y, double r) {
        return (c1.getXX() - x) * (c1.getXX() - x) + (c1.getYY() - y) * (c1.getYY() - y) < (c1.getR() + r) * (c1.getR() + r);
    }

    double[] rotate(double px, double py, double ox, double oy, double theta) {
        //return new double[]{cosRad[theta] * (px - ox) - sinRad[theta] * (py - oy) + ox, sinRad[theta] * (px - ox) + cosRad[theta] * (py - oy) + oy};
        return new double[]{cos(theta * PI / 180) * (px - ox) - sin(theta * PI / 180) * (py - oy) + ox, sin(theta * PI / 180) * (px - ox) + cos(theta * PI / 180) * (py - oy) + oy};
    }

    double getNewPlacement(int index, HashMap<Integer, Boolean> pass) {
        double bestScore = Double.MAX_VALUE;
        boolean overlap = false;
        for (int i = 0; i < SIZE; i++) {
            if (i == index || (pass.containsKey(i) && pass.get(i))) {
                continue;
            }
            if (isOverlapped(circle[i], circle[index].getX(), circle[index].getY(), circle[index].getR())) {
                overlap = true;
                break;
            }
        }
        if (overlap) {
            Neighbor[] nbr = new Neighbor[SIZE];
            for (int i = 0; i < SIZE; i++) {
                double dist = distance(circle[i].getXX(), circle[i].getYY(), circle[index].getX(), circle[index].getY());
                nbr[i] = new Neighbor(i, dist);
            }
            Arrays.sort(nbr, new ByDist());

            double px = 0;
            double py = 0;
            int afiliasi = 0;
            for (int ii = 0; ii < SIZE; ii++) {
                int i = nbr[ii].id;
                if (index == i || (pass.containsKey(i) && pass.get(i))) {
                    continue;
                }
                double curPX = this.circle[i].getXX() + this.circle[i].getR() + this.circle[index].getR() + SEPARATOR;
                double curPY = this.circle[i].getYY();
                for (int j = start; j < 360; j++) {
                    double[] newPoints = rotate(curPX, curPY, this.circle[i].getXX(), this.circle[i].getYY(), j);
                    double score = cost(this.circle[index].getX(), this.circle[index].getY(), newPoints[0], newPoints[1], this.circle[index].getMass());
                    if (score < bestScore) {
                        boolean overlapping = false;
                        for (int k = 0; k < SIZE; k++) {
                            if (index == k || (pass.containsKey(k) && pass.get(k))) {
                                continue;
                            }
                            if (isOverlapped(this.circle[k], newPoints[0], newPoints[1], this.circle[index].getR())) {
                                overlapping = true;
                                break;
                            }
                        }
                        if (!overlapping) {
                            bestScore = score;
                            px = newPoints[0];
                            py = newPoints[1];
                            afiliasi = i;
                        }
                    }
                }
            }
            circle[index] = new Circle(circle[index].getId(), circle[index].getX(), circle[index].getY(), circle[index].getR(), circle[index].getMass(), px, py, bestScore, false, afiliasi);
        } else {
            circle[index] = new Circle(circle[index].getId(), circle[index].getX(), circle[index].getY(), circle[index].getR(), circle[index].getMass(),
                    circle[index].getX(), circle[index].getY(), 0, false, 0);
            return 0;
        }
        return bestScore;
    }

    public double[] minimumWork(double[] x, double[] y, double[] r, double[] m) {
        setupError();
        SIZE = x.length;
        double[] ret = new double[SIZE * 2];
        init(x, y, r, m);

        //===Assigning unmoved Circle===========================================
        getInitialScore();
        System.err.println("Waktu pertama: " + (System.nanoTime() - START_TIME) / 1000000);
        System.err.println("First Best Score: " + globalScore);
        PriorityQueue<Configuration> list = new PriorityQueue<Configuration>();
        //Arrays.sort(circle, new ByMass());
        Configuration conf = new Configuration(this.circle);
        conf.score = globalScore;
        conf.afiliasi = 0;
        list.add(conf);
        bestConf = this.circle.clone();
        //===End================================================================

        /*List<Configuration> tempList = new ArrayList<Configuration>();
        int step = 0, vv = 0;
        while (System.nanoTime() - START_TIME < TIME_OUT * 1000000 && !list.isEmpty()) {
            Configuration item = list.poll();
            Circle[] temp = item.getCircle();

            double score = item.score;
            for (int i = 0; i < SIZE && System.nanoTime() - START_TIME < TIME_OUT * 1000000; i++) {
                this.circle = temp.clone();
                Neighbor[] nbr = new Neighbor[SIZE - 1];
                for (int j = 0, k = 0; j < SIZE; j++) {
                    if (i == j) {
                        continue;
                    }
                    double dist = distance(temp[i].getXX(), temp[i].getYY(), temp[j].getXX(), temp[j].getYY());
                    nbr[k++] = new Neighbor(j, dist);
                }
                Arrays.sort(nbr, new ByDist());
                int[] arrID = new int[N];
                for (int j = 0; j < N; j++) {
                    arrID[j] = nbr[j].id;
                    circle[arrID[j]] = new Circle(arrID[j], circle[arrID[j]].getX(), circle[arrID[j]].getY(),
                            circle[arrID[j]].getR(), circle[arrID[j]].getMass(), 0, 0, 0, false, circle[arrID[j]].getAfiliasi());
                }
                //System.err.println("Circle: " + i + " >> " + score + " >> ");
                for (int j = vv; j < permutations.length && System.nanoTime() - START_TIME < TIME_OUT * 1000000; j++) {
                    int[] perm = permutations[j];
                    this.circle = temp.clone();
                    HashMap<Integer, Boolean> pass = new HashMap<Integer, Boolean>();
                    for (int k = 0; k < N; k++) {
                        pass.put(arrID[perm[k]], true);
                    }
                    boolean complete = true;
                    double initScoreTotal = 0;
                    double lastScoreTotal = 0;
                    for (int k = 0; k < N; k++) {
                        if (System.nanoTime() - START_TIME > TIME_OUT * 1000000 && k < N) {
                            complete = false;
                            break;
                        }
                        pass.put(arrID[perm[k]], false);
                        initScoreTotal += circle[arrID[perm[k]]].getScore();
                        double newScore = getNewPlacement(arrID[perm[k]], pass);
                        lastScoreTotal += newScore;
                    }
                    double lastScore = score + lastScoreTotal - initScoreTotal;
                    //System.err.println("Permutasi: " + j + " = " + lastScore);
                    if (lastScore < globalScore && complete) {
                        Configuration newConf = new Configuration(this.circle);
                        newConf.score = lastScore;
                        globalScore = lastScore;
                        bestConf = circle.clone();
                        //System.err.println("LEBIH BAGUS: " + globalScore);
                        tempList.add(newConf);
                        vv = 0;
                        break;
                    }
                }
            }
            Collections.sort(tempList, new ByScore());
            list.add(tempList.get(0));
            vv++;
            if (vv == SIZE) {
                vv = 0;
            }
        }*/
        System.err.println("Second Best Score: " + globalScore);

        this.circle = bestConf.clone();
        for (int i = 0; i < SIZE; i++) {
            if (System.nanoTime() - START_TIME > TIME_OUT2 * 1000000) {
                break;
            }
            double initScoreTotal = circle[i].getScore();
            double newScore = getNewPlacement(i, new HashMap<Integer, Boolean>());
            globalScore += newScore - initScoreTotal;
        }

        Arrays.sort(circle, new ByCost());
        for (int i = 0; i < SIZE; i++) {
            Circle c = circle[i];
            int id = c.getId();
            ret[id * 2] = c.getXX();
            ret[id * 2 + 1] = c.getYY();
        }

        System.err.println("Last Best Score: " + globalScore);
        System.err.println("Waktu akhir: " + (System.nanoTime() - START_TIME) / 1000000);
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
        int N = Integer.parseInt(sc.nextLine());
        double[] x = new double[N];
        double[] y = new double[N];
        double[] r = new double[N];
        double[] m = new double[N];
        for (int i = 0; i < N; i++) {
            x[i] = Double.parseDouble(sc.nextLine());
        }
        for (int i = 0; i < N; i++) {
            y[i] = Double.parseDouble(sc.nextLine());
        }
        for (int i = 0; i < N; i++) {
            r[i] = Double.parseDouble(sc.nextLine());
        }
        for (int i = 0; i < N; i++) {
            m[i] = Double.parseDouble(sc.nextLine());
        }
        double[] ret;
        CirclesSeparation cs = new CirclesSeparation();
        ret = cs.minimumWork(x, y, r, m);
        for (int i = 0; i < N * 2; i++) {
            System.out.println(ret[i]);
        }
        System.out.flush();
    }
}

final class Configuration implements Comparable<Configuration> {

    final private Circle[] circle;
    double score;
    int afiliasi;

    public Configuration(Circle[] circle) {
        this.circle = circle.clone();
    }

    Circle[] getCircle() {
        return this.circle;
    }

    public int compareTo(Configuration o) {
        if (score < o.score) {
            return -1;
        }
        return 1;
    }
}

final class Circle {

    final private int id, afiliasi;
    final private double x, y, r, m, score, xx, yy;
    boolean modified;

    public Circle(int id, double x, double y, double r, double m, double xx, double yy, double score, boolean modified, int afiliasi) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.r = r;
        this.m = m;
        this.xx = xx;
        this.yy = yy;
        this.score = score;
        this.modified = modified;
        this.afiliasi = afiliasi;
    }

    int getAfiliasi() {
        return this.afiliasi;
    }

    double getScore() {
        return this.score;
    }

    double getXX() {
        return this.xx;
    }

    double getYY() {
        return this.yy;
    }

    double getX() {
        return this.x;
    }

    double getY() {
        return this.y;
    }

    int getId() {
        return this.id;
    }

    double getMass() {
        return this.m;
    }

    double getR() {
        return this.r;
    }
}

class ByRadi implements Comparator<Circle> {

    public int compare(Circle o1, Circle o2) {
        if (o1.getR() < o2.getR()) {
            return -1;
        }
        return 1;
    }
}

class ByMass implements Comparator<Circle> {

    double lr;

    public ByMass(double lr) {
        this.lr = lr;
    }

    public int compare(Circle o1, Circle o2) {
        /*if (o1.getMass() * (o1.getR() > lr ? (o1.getR() * o1.getR()) : (lr * lr))
         > o2.getMass() * (o2.getR() > lr ? (o2.getR() * o2.getR()) : (lr * lr))) {
         return -1;
         }*/
        if (o1.getMass() * min(lr / o1.getR(), 1.75) > o2.getMass() * min(lr / o2.getR(), 1.75)) {
            return -1;
        }
        return 1;
    }
}

class ByCost implements Comparator<Circle> {

    public int compare(Circle o1, Circle o2) {
        if (o1.getScore() > o2.getScore()) {
            return -1;
        }
        return 1;
    }
}

class Neighbor {

    int id = 0;
    double dist = 0;

    public Neighbor(int id, double dist) {
        this.id = id;
        this.dist = dist;
    }
}

class ByDist implements Comparator<Neighbor> {

    public int compare(Neighbor o1, Neighbor o2) {
        if (o1.dist < o2.dist) {
            return -1;
        }
        return 1;
    }
}

class ByScore implements Comparator<Configuration> {

    public int compare(Configuration o1, Configuration o2) {
        if (o1.score < o2.score) {
            return -1;
        }
        return 1;
    }
}
