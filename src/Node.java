import java.awt.Color;
import java.util.ArrayList;

import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;

public class Node extends GraphicsGroup{
    private final static int SIZE = 80;

    private String letter;
    private Ellipse circle;
    private GraphicsText label;
    private ArrayList<Node> connections;
    
    public Node(String letter, double xPosition, double yPosition) {
        this.letter = letter;
        connections = new ArrayList<>();

        circle = new Ellipse(xPosition, yPosition, SIZE, SIZE);
        circle.setCenter(xPosition, yPosition);
        circle.setFillColor(Color.WHITE);
        label = new GraphicsText(letter, xPosition, yPosition);
        label.setFontSize(30);
        label.setCenter(xPosition, yPosition);
        super.add(circle);
        super.add(label);
    }

    public String getLetter() {
        return letter;
    }

    public boolean testIfHit(double x, double y) {
        return circle.testHit(x, y);
    }

    public ArrayList<Node> getConnections() {
        return connections;
    }

    public void addConnection(Node node) {
        connections.add(node);
    }

    public void setConnections(ArrayList<Node> nodes) {
        connections = nodes;
    }

    public boolean hasConnection(Node connection) {
        for (Node node : connections) {
            if (connection.equals(node)) {
                return true;
            }
        }
        return false;
    }
}
