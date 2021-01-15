import edu.princeton.cs.algs4.In;

import java.util.*;
import edu.princeton.cs.algs4.In;


public class WordNet {
    // Dynamic array of set of strings
    private Hashtable<String, Integer> lookup;
    private ArrayList<String> strings;
    private ArrayList<Set<String>> synsets;
    private ArrayList<String> glossary;
    private LinkedList<Integer>[] hypernyms;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms){
        this.lookup = new Hashtable<>();
        this.synsets = new ArrayList<Set<String>>(1);
        this.glossary = new ArrayList<String>(1);
        this.strings = new ArrayList<>();
        int length = 0;

        In in = new In(synsets);
        while (!in.isEmpty()){
            length ++;
            String[] data = in.readLine().split(",");
            int idx = Integer.parseInt(data[0]);
            for (String s : data[1].split(" ")){this.lookup.put(s, idx); this.strings.add(s);}
            this.synsets.add(new HashSet<>(Arrays.asList(data[1].split(" "))));
            this.glossary.add(data[2]);
        }

        this.hypernyms = new LinkedList[length];

        in = new In(hypernyms);

        while (!in.isEmpty()) {
            String[] data = in.readLine().split(",");
            int idx = Integer.parseInt(data[0]);
            LinkedList<Integer> hypernym = new LinkedList<>();
            for(int i = 1; i < data.length-1;i++){hypernym.add(Integer.parseInt(data[i]));}
            this.hypernyms[idx] = hypernym;
        }

    }


    // returns all WordNet nouns
    public Iterable<String> nouns(){
        return strings;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word){
        return lookup.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB){
        return 1;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB){
        return "";
    }

    // do unit testing of this class
    public static void main(String[] args){
        WordNet w = new WordNet("./wordnet_test/synsets.txt", "./wordnet_test/hypernyms.txt");
        for(String s: w.nouns()) System.out.println(s);

    }
}