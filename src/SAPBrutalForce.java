import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import javax.xml.crypto.dom.DOMCryptoContext;
import java.io.FileOutputStream;
import java.util.Arrays;

public class SAPBrutalForce {
    private final int V;
    private final Digraph G;

    public SAPBrutalForce(Digraph G){
        this.G = G;
        this.V = G.V();
    }

    private int pathTo(int v, int w){
        // if there is no such path, return infinity
        if (v == w) return 0;
        int depth = 0;
        int[] distTo = new int[V];
        Arrays.fill(distTo, -1);
        distTo[v] = 0;
        Queue<Integer> q = new Queue<>();
        q.enqueue(v);
        boolean found = false;

        while(!q.isEmpty() && !found){
            int vertex = q.dequeue();
            depth = distTo[vertex] + 1;
            for (int adj: this.G.adj(vertex)){
                if (adj == w){
                    found = true;
                    break;
                }
                else if (distTo[adj] == -1){
                    q.enqueue(adj);
                    distTo[adj] = distTo[vertex]+1;
                }
            }
        }

        if (!found) return Integer.MAX_VALUE;
        return depth;
    }

    public int ancestor(int v, int w)
    {
        int ancestor = -1;
        int length = Integer.MAX_VALUE;
        int temp;
        int temp1;
        int temp2;

        for(int i = 0; i < V; i++){
            temp1 = pathTo(v, i);
            temp2 = pathTo(w, i);
            if ((temp1+temp2) < length && (temp1 != Integer.MAX_VALUE && temp2 != Integer.MAX_VALUE)){
                length = temp1 + temp2;
                ancestor = i;
            }
        }
        if (length == Integer.MAX_VALUE) return -1;
        return ancestor;
    }

    public int length(int v, int w)
    {
        int ancestor = -1;
        int length = Integer.MAX_VALUE;
        int temp;
        int temp1;
        int temp2;

        for(int i = 0; i < V; i++){
            temp1 = pathTo(v, i);
            temp2 = pathTo(w, i);
            if ((temp1+temp2) < length && (temp1 != Integer.MAX_VALUE && temp2 != Integer.MAX_VALUE)){
                length = temp1 + temp2;
                ancestor = i;
            }
        }
        if (length == Integer.MAX_VALUE) return -1;
        return length;
    }

    public static void main(String[] args) {
        //        In in = new In(args[0]);
//        Digraph G = new Digraph(in);
//        SAP sap = new SAP(G);
//        while (!StdIn.isEmpty()) {
//            int v = StdIn.readInt();
//            int w = StdIn.readInt();
//            int length   = sap.length(v, w);
//            int ancestor = sap.ancestor(v, w);
//            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
//        }

        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAPBrutalForce sap = new SAPBrutalForce(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);

//            List<Integer> vs = Arrays.stream(StdIn.readLine().split(" ")).map(Integer::parseInt).collect(Collectors.toList());
//            List<Integer> ws = Arrays.stream(StdIn.readLine().split(" ")).map(Integer::parseInt).collect(Collectors.toList());
//
//            int length   = sap.length(vs, ws);
//            int ancestor = sap.ancestor(vs, ws);

            StdOut.printf("length = %d, ancestor = %d\n",length,ancestor);
        }

    }
}
