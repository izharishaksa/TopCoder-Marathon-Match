package circlesseparation;

public class Permutation {

    // print N! permutation of the characters of the string s (in order)
    public static void perm1(String s) {
        perm1("", s);
    }

    static int step = 0;
    private static void perm1(String prefix, String s) {
        int N = s.length();
        if (N == 0) {
            //System.out.println(prefix);
            step++;
            System.out.print("{");
            for (int i = 0; i < prefix.length() - 1; i++) {
                System.out.print(prefix.charAt(i) + ", ");
            }
            System.out.print(prefix.charAt(prefix.length() - 1));
            System.out.print("}, ");
            if (step % 180 == 0) {
                System.out.println();
            }
            
        } else {
            for (int i = 0; i < N; i++) {
                perm1(prefix + s.charAt(i), s.substring(0, i) + s.substring(i + 1, N));
            }
        }

    }

    // print N! permutation of the elements of array a (not in order)
    public static void perm2(String s) {
        int N = s.length();
        char[] a = new char[N];
        for (int i = 0; i < N; i++) {
            a[i] = s.charAt(i);
        }
        perm2(a, N);
    }

    private static void perm2(char[] a, int n) {
        if (n == 1) {
            //System.out.print(a);
            System.out.print("{");
            for (int i = 0; i < a.length - 1; i++) {
                System.out.print(a[i] + ", ");
            }
            System.out.print(a[a.length - 1]);
            System.out.print("}, ");
            if (step++ % 100 == 0) {
                System.err.println();
            }
            return;
        }
        for (int i = 0; i < n; i++) {
            swap(a, i, n - 1);
            perm2(a, n - 1);
            swap(a, i, n - 1);
        }
    }

    // swap the characters at indices i and j
    private static void swap(char[] a, int i, int j) {
        char c;
        c = a[i];
        a[i] = a[j];
        a[j] = c;
    }

    public static void main(String[] args) {
        int N = 7;
        String alphabet = "0123456";
        String elements = alphabet.substring(0, N);
        //perm1(elements);
        //System.out.println(step);
        perm2(elements);
    }
}