public class Outcast {
    private WordNet w;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.w = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns){
        String result = "";
        int d_min = Integer.MAX_VALUE;
        int d = 0;
        for(String noun:nouns){
            for(String n:nouns){
                d += this.w.distance(noun, n);
            }
            if (d < d_min){
                d_min = d;
                result = noun;
            }
        }

        return result;
    }

    // see test client below
    public static void main(String[] args){

    }
}