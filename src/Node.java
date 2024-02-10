import java.awt.Color;
import java.util.ArrayList;

import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Point;

public class Node extends GraphicsGroup{
    private final static int SIZE = 80;

    private String letter;
    private Ellipse circle;
    private GraphicsText label;
    private ArrayList<Node> connections;
    private double xPosition;
    private double yPosition;
    private double xAcc = 0;
    private double yAcc = 0;
    private double xVel = 0;
    private double yVel = 0; 
    
    public Node(String letter, double xPosition, double yPosition) {
        this.letter = letter;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
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

    public void addAcc(double xAcc, double yAcc) {
        this.xAcc += xAcc;
        this.yAcc += yAcc;
    }

    public void resetAcc() {
        xAcc = 0;
        yAcc = 0;
    }

    public void gravity() {
        xAcc = -electricForce(800-xPosition) + electricForce(xPosition);
        yAcc = -electricForce(500-yPosition) + electricForce(yPosition);
    }

    private double electricForce(double x) {
        return .25/(x*x/1000 + 1);
    }

    public void updateVel() {
        this.xVel += xAcc;
        this.yVel += yAcc;
    }

    public void updatePos() {
        this.xPosition += xVel;
        this.yPosition += yVel;
        if (xPosition < 0) {
            xPosition = 1;
            xVel = Math.abs(xVel);
        } else if (xPosition >= 800) {
            xPosition = 799;
            xVel = -Math.abs(xVel);
        }
        if (yPosition < 0) {
            yPosition = 1;
            yVel = Math.abs(yVel);
        } else if (yPosition > 500) {
            yPosition = 499;
            yVel = -Math.abs(yVel);
        }
        circle.setCenter(xPosition, yPosition);
        label.setCenter(xPosition, yPosition);
    }

    public boolean testIfHit(double x, double y) {
        return circle.testHit(x, y);
    }

    public ArrayList<Node> getConnections() {
        return connections;
    }

    public Point getNodePosition() {
        return new Point(xPosition, yPosition);
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
