import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.Queue;

import java.util.*;
import java.util.stream.Collectors;

public class SAP {
    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G){
        this.G = G;
    }



    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w){
        //if the same v
        if (w == v) return 0;

        int[][] visited = new int[this.G.V()][2];
        visited[v] = new int[]{1, 0};
        visited[w] = new int[]{1, 1};

        Queue<int[]> q = new Queue<>();
        q.enqueue(new int[]{v, 0});
        q.enqueue(new int[]{w, 1});

        int[] distTo = new int[this.G.V()];
        int length = 0;

        while (!q.isEmpty() && length == 0){
            int[] vertex = q.dequeue();
            for (int adj:this.G.adj(vertex[0]))
            {
                // if visited and by BFS result of another queried vertex
                if (visited[adj][0] == 1 && visited[adj][1] != vertex[1]){
                    length = distTo[adj] + distTo[vertex[0]] + 1;
                    break;
                }
                else if (visited[adj][0] != 1){
                    // not visited.
                    q.enqueue(new int[]{adj, vertex[1]});
                    visited[adj] = new int[]{1, vertex[1]};
                    distTo[adj] = distTo[vertex[0]] + 1;
                }
            }
        }


        if (length == 0) return -1;
        return length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w){
        if (w == v) return w;

        // do a BSF
        // use hash table to store visited vertex BFS result from which vertex
        int[][] visited = new int[this.G.V()][2];
        visited[v] = new int[]{1, 0};
        visited[w] = new int[]{1, 1};
        Queue<int[]> q = new Queue<>();
        q.enqueue(new int[]{v, 0});
        q.enqueue(new int[]{w, 1});
        while (!q.isEmpty()){
            int[] vertex = q.dequeue();
            for (int adj:this.G.adj(vertex[0]))
            {
                if (visited[adj][0] == 1) {
                    if (visited[adj][1] != vertex[1]) return adj;
                }
                else {
                    q.enqueue(new int[]{adj, vertex[1]});
                    visited[adj] = new int[]{1, vertex[1]};
                }
            }
        }
        return -1;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        // initialize parents {parent if not -1, 0 if the BFS result of v -1 if from w}
        int[][] visited = new int[this.G.V()][2];
        Queue<int[]> q = new Queue<>();

        for (int v1 : v){ q.enqueue(new int[]{v1, 0}); visited[v1] = new int[]{1, 0};}
        for (int w1 : w){ q.enqueue(new int[]{w1, 1});
            if (visited[w1][0] == 1){ return 0; }
            visited[w1] = new int[]{1, 1};}

        int[] distTo = new int[this.G.V()];
        int length = 0;

        while (!q.isEmpty() && length == 0){
            int[] vertex = q.dequeue();
            for (int adj:this.G.adj(vertex[0]))
            {
                // if visited and by BFS result of another queried vertex
                if (visited[adj][0] == 1 && visited[adj][1] != vertex[1]){
                    length = distTo[adj] + distTo[vertex[0]] + 1;
                    break;
                }
                else if (visited[adj][0] != 1){
                    // not visited.
                    q.enqueue(new int[]{adj, vertex[1]});
                    visited[adj] = new int[]{1, vertex[1]};
                    distTo[adj] = distTo[vertex[0]] + 1;
                    // keep track of the longest path of BFS.
                }
            }

        }


        if (length == 0) return -1;
        else return length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
        // do a BSF
        // use int array (1 for visited) to store visited vertex BFS result from which vertex
        int[][] visited = new int[this.G.V()][2];
        Queue<int[]> q = new Queue<>();
        for (int v1: v){
            visited[v1] = new int[]{1, 0};
            q.enqueue(new int[]{v1, 0});
        }
        for (int w1: w){
            if (visited[w1][0] == 1) return w1;
            visited[w1] = new int[] {1, 1};
            q.enqueue(new int[]{w1, 1});
        }

        while (!q.isEmpty()){
            int[] vertex = q.dequeue();
            for (int adj:this.G.adj(vertex[0]))
            {
                if (visited[adj][0] == 1) {
                    if (visited[adj][1] != vertex[1]) return adj;
                }
                else {
                    q.enqueue(new int[]{adj, vertex[1]});
                    visited[adj] = new int[]{1, vertex[1]};
                }
            }
        }
        return -1;
    }

    // do unit testing of this class
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
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
//            int v = StdIn.readInt();
//            int w = StdIn.readInt();
//            int length   = sap.length(v, w);
//            int ancestor = sap.ancestor(v, w);
            List<Integer> vs = Arrays.stream(StdIn.readLine().split(" ")).map(Integer::parseInt).collect(Collectors.toList());
            List<Integer> ws = Arrays.stream(StdIn.readLine().split(" ")).map(Integer::parseInt).collect(Collectors.toList());

            int length   = sap.length(vs, ws);
            int ancestor = sap.ancestor(vs, ws);

            StdOut.printf("length = %d, ancestor = %d\n",length,ancestor);
        }

    }
}