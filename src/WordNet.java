import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


import java.util.Collections;
import java.util.Hashtable;


public class WordNet {
    /*
    * The wordNet is a rooted graph which means
    * given two vertex in the wordNet, there must be an common ancestor for both.
    * as every vertex in the graph is guaranteed to have a parent and all are children to the root,
    * except for the root.
    * */
    // Dynamic array of set of strings
    private Hashtable<String, Integer> lookup;
    private Hashtable<Integer, String> lookupNoun;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms){
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        this.lookup = new Hashtable<>();
        this.lookupNoun = new Hashtable<>();
        int V = 0;

        In in = new In(synsets);
        while (!in.isEmpty()){
            String[] data = in.readLine().split(",");
            int idx = Integer.parseInt(data[0]);
            for (String s : data[1].split(" ")){this.lookup.put(s, idx);}
            this.lookupNoun.put(idx, data[1]);
            V++;
        }

        Digraph G = new Digraph(V);
        in = new In(hypernyms);

        while (!in.isEmpty()) {
            String[] data = in.readLine().split(",");
            int v = Integer.parseInt(data[0]);
            for(int i = 1; i < data.length ;i++){
                int w = Integer.parseInt(data[i]);
                G.addEdge(v, w);
            }
        }
        this.sap = new SAP(G);
        // todo make sure the graph is rooted.
    }

    // returns all WordNet nouns
    public Iterable<String> nouns(){
        return Collections.list(lookup.keys());
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word){
        if (word == null) throw new IllegalArgumentException();
        return lookup.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB){
        if (nounA == null || nounB == null) throw new IllegalArgumentException();

        int v = lookup(nounA);
        int w = lookup(nounB);

        if (w == v) return 0;

        return sap.length(v,w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB)
    {
        if (nounA ==null || nounB == null) throw new IllegalArgumentException();

        int v = lookup(nounA);
        int w = lookup(nounB);

        if (w == v) return nounA;

        int ancestor = sap.ancestor(v,w);

        return lookup(ancestor);
    }

    private int lookup(String noun){
        if (!lookup.containsKey(noun)) throw  new IllegalArgumentException();
        return lookup.get(noun);
    }

    private String lookup(int idx){
        return lookupNoun.get(idx);
    }


    // do unit testing of this class
    public static void main(String[] args){
        WordNet w = new WordNet("./wordnet_test/synsets.txt", "./wordnet_test/hypernyms.txt");

        while(!StdIn.isEmpty()){
            String nounA = StdIn.readLine();
            String nounB = StdIn.readLine();
            int length   = w.distance(nounA, nounB);
            String ancestor = w.sap(nounA, nounB);

            StdOut.printf("length = %d, ancestor = %s\n",length, ancestor);
        }
    }
}