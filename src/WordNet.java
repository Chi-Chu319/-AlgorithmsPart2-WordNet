import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;


public class WordNet {
    // Dynamic array of set of strings
    private Hashtable<String, Integer> lookup;
    private ArrayList<String> strings;
    private ArrayList<String[]> synsets;
    private ArrayList<String> glossary;
    private LinkedList<Integer>[] hypernyms;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms){
        this.lookup = new Hashtable<>();
        this.synsets = new ArrayList<>(1);
        this.glossary = new ArrayList<String>(1);
        this.strings = new ArrayList<>();
        int length = 0;

        In in = new In(synsets);
        while (!in.isEmpty()){
            length ++;
            String[] data = in.readLine().split(",");
            int idx = Integer.parseInt(data[0]);
            for (String s : data[1].split(" ")){this.lookup.put(s, idx); this.strings.add(s);}
            this.synsets.add(data[1].split(" "));
            this.glossary.add(data[2]);
        }

        this.hypernyms = new LinkedList[length];

        in = new In(hypernyms);

        while (!in.isEmpty()) {
            String[] data = in.readLine().split(",");
            int idx = Integer.parseInt(data[0]);
            LinkedList<Integer> hypernym = new LinkedList<>();
            for(int i = 1; i < data.length;i++){
                hypernym.add(Integer.parseInt(data[i]));
            }
            this.hypernyms[idx] = hypernym;
        }

    }

    // returns all WordNet nouns
    public Iterable<String> nouns(){
        return strings;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word){
        return lookup.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB){
        if (nounA.equals(nounB)) return 0;
        int v = lookup.get(nounA);
        int w = lookup.get(nounB);

        int[][] visited = new int[synsets.size()][2];
        visited[v] = new int[]{1, 0};
        visited[w] = new int[]{1, 1};

        Queue<int[]> q = new Queue<>();
        q.enqueue(new int[]{v, 0});
        q.enqueue(new int[]{w, 1});

        int[] distTo = new int[synsets.size()];
        int length = 0;

        while (!q.isEmpty() && length == 0){
            int[] vertex = q.dequeue();
            for (int adj:hypernyms[vertex[0]])
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

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB)
    {
        if (nounA.equals(nounB)) return nounA;

        int v = lookup.get(nounA);
        int w = lookup.get(nounB);

        int[][] visited = new int[synsets.size()][2];
        visited[v] = new int[]{1, 0};
        visited[w] = new int[]{1, 1};

        Queue<int[]> q = new Queue<>();
        q.enqueue(new int[]{v, 0});
        q.enqueue(new int[]{w, 1});


        while (!q.isEmpty()){
            int[] vertex = q.dequeue();
            for (int adj:hypernyms[vertex[0]])
            {
                // if visited and by BFS result of another queried vertex
                if (visited[adj][0] == 1 && visited[adj][1] != vertex[1]){
                    return String.join(" ", synsets.get(adj));
                }
                else if (visited[adj][0] != 1){
                    // not visited.
                    q.enqueue(new int[]{adj, vertex[1]});
                    visited[adj] = new int[]{1, vertex[1]};
                }
            }
        }

        return "";
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