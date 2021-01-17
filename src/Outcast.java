import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet w;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.w = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns){
        String result = "";
//        String ancestor = "";
        int dMax = 0;
        int temp;
        int d = 0;
        for(String noun:nouns){
            for(String n:nouns){
                temp = this.w.distance(noun, n);
                d += temp;
//                ancestor = this.w.sap(noun, n);
//                System.out.printf("distance from %s (%d) to %s (%d) is %d, ancestor is %s (%d)\n", noun,
//                        this.w.lookup(noun), n, this.w.lookup(n), temp, ancestor, this.w.lookup(ancestor.split(" ")[0]));
            }
            if (d > dMax){
                dMax = d;
                result = noun;
            }
//            System.out.printf("noun: %s, distance: %d\n", noun, d);
            d = 0;
        }
        return result;
    }

    // see test client below
    public static void main(String[] args){
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}