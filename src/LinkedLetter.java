import java.util.HashMap;
import java.util.ArrayList;

public class LinkedLetter {
    private char letter;
    private HashMap<Character, Integer> edgesOut = new HashMap<>();

    public LinkedLetter(char letter) {
        this.letter = letter;
    }

    public char getLetter() {
        return letter;
    }

    public void addEdge(Character to, int weight) {
        edgesOut.put(to, weight);
    }

    public int getEdgeWeight(Character to) {
        return edgesOut.get(to);
    }

    public boolean hasEdge(Character to) {
        return edgesOut.containsKey(to);
    }

    public boolean hasEdges() {
        return !edgesOut.isEmpty();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Character next : edgesOut.keySet()) {
            sb.append(letter);
            sb.append(':');
            sb.append(next);
            sb.append(" [");
            sb.append(edgesOut.get(next));
            sb.append("]\n");
        }
        return sb.toString();
    }

    public void increment(Character to) {
        if (edgesOut.containsKey(to)) {
            edgesOut.put(to, edgesOut.get(to) + 1);
        } else {
            edgesOut.put(to, 1);
        }
    }

    public void decrement(Character to) {
        if (edgesOut.containsKey(to)) {
            edgesOut.put(to, edgesOut.get(to) - 1);
        }
        if (edgesOut.get(to) <= 0) {
            edgesOut.remove(to);
        }
    }

    public void decrementAll() {
        ArrayList<Character> toRemove = new ArrayList<>();
        for (Character next : edgesOut.keySet()) {
            toRemove.add(next);
        }
        for (Character next : toRemove) {
            this.decrement(next);
        }
    }

}
