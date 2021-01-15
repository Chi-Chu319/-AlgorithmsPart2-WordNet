import edu.princeton.cs.algs4.In;

import java.util.Hashtable;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Hashtable<Integer, Integer> h = new Hashtable<>();
        h.put(20, 12);
        System.out.println(h.contains(12));
    }
}
