import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SAP {
    private final Digraph G;
    private final int V;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G){
        if (G == null) throw new IllegalArgumentException();
        this.G = new Digraph(G);
        this.V = G.V();
    }

    private void validateInput(int v, int w){
        // throw exception if not valid
        // check if the inputs are with in range.
        if (v > V - 1 || v < 0|| w > V - 1 || w < 0) throw new IllegalArgumentException();
    }

    private void validateInput(Iterable<Integer> v, Iterable<Integer> w){
        // throw exception if not valid
        // check if the inputs are null.
        if (v == null || w == null) throw new IllegalArgumentException();
        // check if the inputs are with in range and not null.
        for(Integer v1:v){
            if (v1 == null) throw new IllegalArgumentException();
            if (v1 > V - 1 || v1 < 0) throw new IllegalArgumentException();
        }
        for(Integer w1:w){
            if (w1 == null) throw new IllegalArgumentException();
            if (w1 > V - 1 || w1 < 0) throw new IllegalArgumentException();
        }
    }

    private int lengthByQueue(Queue<int[]> q, int[][] visited){
        // return common ancestor with shortest path by the elements stored in queue
        int[] distTo = new int[this.G.V()];
        // if there is ANY COMMON ANCESTOR. (not guaranteed to be with the shortest path)
        int length = Integer.MAX_VALUE;
        // ANY COMMON ANCESTOR. (not guaranteed to be with the shortest path)
        int ancestor = -1;
        int depth;
        // if it's set there is GUARANTEED TO BE A COMMON ANCESTOR WITH SHORTEST PATH
        // could be not set even the sap is found.
        boolean found = false;

        // keep track of the current depth of BFS = distTo[vertex[0]] + 1
        // when the first common ancestor is found, keep track of the length and depth.
        // wait until the current depth > length of first discovered common ancestor (mean while updated the length.)

        while (!q.isEmpty() && !found) {
            int[] vertex = q.dequeue();
            // problem: how to indicate depth of BFS; distTo[] stored the shortest path to a vertex. but inserting a common ancestor may break the relation.
            // solution: overwrite the vertex dist if the vertex is found by BFS of another origin
            // proof of safety:
            // the solution is safe since the distTo[vertex] is only overwritten if at least one common ancestor is found.
            // to find a common ancestor with short path. the BFS will repeat the search path done by BFS of another origin.
            // which is guaranteed to be marked.
            // cons: depth of the BFS will be always correct.
            depth = distTo[vertex[0]] + 1;
            for (int adj : this.G.adj(vertex[0])) {
                // if visited and by BFS result of another queried vertex
                if (visited[adj][0] == 1 && visited[adj][1] != vertex[1]) {
                    // common ancestor, not guaranteed to have the shortest path
                    if (distTo[adj] + depth < length){ length = distTo[adj] + depth; ancestor = adj;}
                    if (length < depth) {// break out of the loop
                        found = true;
                    }
                    // push it to the queue.
                    q.enqueue(new int[]{adj, vertex[1]});
                    distTo[adj] = distTo[vertex[0]] + 1;
                } else if (visited[adj][0] != 1) {
                    // not visited.
                    q.enqueue(new int[]{adj, vertex[1]});
                    visited[adj] = new int[]{1, vertex[1]};
                    distTo[adj] = distTo[vertex[0]] + 1;
                }
            }
        }

        if (ancestor == -1) return -1;
        return length;
    }

    private int ancestorByQueue(Queue<int[]> q, int[][] visited){
        // return length to common ancestor with shortest path by the elements stored in queue
        // return common ancestor with shortest path by the elements stored in queue
        int[] distTo = new int[this.G.V()];
        int length = Integer.MAX_VALUE;
        int ancestor = -1;
        int depth;

        boolean found = false;

        while (!q.isEmpty() && !found) {
            int[] vertex = q.dequeue();
            depth = distTo[vertex[0]] + 1;
            for (int adj : this.G.adj(vertex[0])) {
                // if visited and by BFS result of another queried vertex
                if (visited[adj][0] == 1 && visited[adj][1] != vertex[1]) {
                    // common ancestor, not guaranteed to have the shortest path
                    if (distTo[adj] + depth < length) {length = distTo[adj] + depth; ancestor = adj;}
                    if (length < depth) {// break out of the loop
                        found = true;
                    }
                    // push it to the queue.
                    q.enqueue(new int[]{adj, vertex[1]});
                    distTo[adj] = distTo[vertex[0]] + 1;
                } else if (visited[adj][0] != 1) {
                    // not visited.
                    q.enqueue(new int[]{adj, vertex[1]});
                    visited[adj] = new int[]{1, vertex[1]};
                    distTo[adj] = distTo[vertex[0]] + 1;
                }
            }
        }

        return ancestor;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w){
        validateInput(v, w);

        //if the same v
        if (w == v) return 0;

        int[][] visited = new int[V][2];
        visited[v] = new int[]{1, 0};
        visited[w] = new int[]{1, 1};

        Queue<int[]> q = new Queue<>();
        q.enqueue(new int[]{v, 0});
        q.enqueue(new int[]{w, 1});

        return lengthByQueue(q, visited);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w){
        validateInput(v, w);

        // corner case
        if (w == v) return w;

        // do a BSF
        // use array of arrays to store {visited vertex, BFS result from which vertex}
        int[][] visited = new int[this.G.V()][2];
        visited[v] = new int[]{1, 0};
        visited[w] = new int[]{1, 1};
        Queue<int[]> q = new Queue<>();
        q.enqueue(new int[]{v, 0});
        q.enqueue(new int[]{w, 1});

        return ancestorByQueue(q, visited);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        validateInput(v, w);

        int[][] visited = new int[V][2];
        Queue<int[]> q = new Queue<>();

        for (int v1 : v){
            q.enqueue(new int[]{v1, 0});
            visited[v1] = new int[]{1, 0};
        }
        for (int w1 : w){
            // if same vertex in the queried iterables.
            if (visited[w1][0] == 1){ return 0; }
            visited[w1] = new int[]{1, 1};
            q.enqueue(new int[]{w1, 1});
        }

        return lengthByQueue(q, visited);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
        validateInput(v, w);


        // do a BSF
        // use int array (1 for visited) to store visited vertex BFS result from which vertex
        int[][] visited = new int[V][2];
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

        return ancestorByQueue(q, visited);
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