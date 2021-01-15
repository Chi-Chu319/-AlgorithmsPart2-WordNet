import edu.princeton.cs.algs4.In;

import java.util.Hashtable;

public class Main {

    public static void main(String[] args) {
	// write your code here
        int[][] parents = new int[20][2];
        System.out.println(parents.length);
        for (int i = 0; i<parents.length; i++) parents[i][0] = -1;
    }
}
