import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.ArrayList;

public class App {
    private ArrayList<String> words = new ArrayList<>();
    private HashMap<Character, LinkedLetter> letters = new HashMap<>();
    private HashSet<Character> starts = new HashSet<>();
    private HashSet<Character> ends = new HashSet<>();

    /**
     * App has a list of words you've found, a map from the "language's" letters to the nodes
     * and a set of starting / ending letters. every other round the game should decrease the
     * stength of all the edges and then randomly add new edges. The game ends if you make a
     * word that isn't in the graph, if its too long, or if you already made that word. 
     */
    
    public static void main(String[] args) {
        App app = new App();
        while (app.round()) {
            for (Character c : app.letters.keySet()) {
                if (app.words.size() %2 == 0) {
                    app.letters.get(c).decrementAll();
                    app.prune();
                }
                if (Math.random() < 0.125) {
                    app.generateEdges(c);
                }
            }
        }
        System.out.println("Game over!");
        System.out.print("You made " + app.words.size() + " words:");
        for (String word : app.words) {
            System.out.print(" " + word);
        }
    }

    public App() {
        String alphabest = "eeeeeeeeeeeetttttttttaaaaaaaaooooooiiiiiiinnnnnnsssssshhhhhhrrrrrrddddllllcccuuuummmwwffggyyppbbvkjxqz"; //approximate english frequency
        for (int i = 0; i < 11; i++) { // generates the set of letters in the language
            char c;
            do {
                int r = (int) Math.floor(Math.random()*alphabest.length());
                c = alphabest.charAt(r);
            } while (letters.containsKey(c));
            letters.put(c, new LinkedLetter(c));
            if (i < 2) {
                starts.add(c); //creates 2 starting letters
            }
            if (i > 8) {
                ends.add(c); //and 2 ending letters
            }
        }
        for (Character c : letters.keySet()) {
            generateEdges(c); //
        }
        prune();
    }

    public void prune() { //we should make this recursively delete if we go with random generation
        ArrayList<Character> toRemove = new ArrayList<>();
        for (Character c : letters.keySet()) {
            if (starts.contains(c) || ends.contains(c)) {
                continue;
            }
            if (!letters.get(c).hasEdges()) {
                toRemove.add(c);
            }
        }
        for (Character c : toRemove) {
            letters.remove(c);
        }
    }

    public void generateEdges(Character c) { //doesn't make an edge to itself or to a starting letter 
        if (ends.contains(c)) {              //or starting with an ending letter
            return;
        }
        int numEdges = (int) Math.ceil(Math.random()*5); //tries to make 1-5 edges for c, but this counts fails so it might be less
        for (Character next : letters.keySet()) {
            if (numEdges <= 0) {
                return;
            }
            numEdges--;
            if (c == next || starts.contains(next)) {
                continue;
            }
            letters.get(c).addEdge(next, (int) Math.ceil(Math.random()*3)); // gives the edge a weight of 1-3
        }
    }

    public boolean round() {
        System.out.println(" ");
        for (String word : words) {
            System.out.println(word);
        }
        for (Character c : starts) {
            System.out.println("*:" + c);
        } 
        for (Character c : letters.keySet()) {
            System.out.println(letters.get(c).toString());
        }
        for (Character c : ends) {
            System.out.println(c + ":*");
        }
        Scanner sc = new Scanner(System.in);
        String word = sc.nextLine();
        if (checkWord(word)) {
            words.add(word);
            for (int i = 0; i < word.length()-2; i++) {
                letters.get(word.charAt(i)).increment(word.charAt(i + 1));
            }
            return true;
        }
        return false;
    }

    public boolean checkWord(String word) {
        for (String w : words) {
            if (w.equals(word)) {
                return false;
            }
        }
        if (word.length() > 10) {
            return false;
        }
        if (!starts.contains(word.charAt(0))) {
            return false;
        }
        for(int i = 0; i < word.length()-1; i++) {
            char letter = word.charAt(i);
            if (!letters.containsKey(letter)|| !letters.get(letter).hasEdge(word.charAt(i + 1))) {
                return false;
            }
        }
        return ends.contains(word.charAt(word.length() - 1));
    }

}
