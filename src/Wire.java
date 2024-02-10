import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Random;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Line;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.Rectangle;

public class Wire extends Line{
    private int width;
    private Rectangle boundingBox;
    private Node nodeA;
    private Node nodeB;
    private CanvasWindow canvas;
    
    public Wire(Node nodeA, Node nodeB) {
        super(nodeA.getCenter(), nodeB.getCenter());
        super.setStrokeColor(new Color(255,66,66));
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        width = 2*(new Random().nextInt(4) + 2);
        super.setStrokeWidth(width);
        boundingBox = new Rectangle(getX1(), getY1(), Point2D.distance(getX1(), getY1(), getX2(), getY2()), 18);
        reposition();
        boundingBox.setStroked(false);
    }

    public void reposition() {
        Point start = nodeA.getNodePosition();
        Point end = nodeB.getNodePosition();
        double angle = end.subtract(start).angle();
        double length = end.distance(start);
        this.setStartPosition(start);
        this.setEndPosition(end);
        boundingBox.setSize(length, 18);
        boundingBox.setCenter(this.getCenter());
        boundingBox.setRotation(angle*57.296);
    }

    public boolean isClicked(double x, double y) {
        return boundingBox.testHit(x, y);
    }

    public void addBoundingBox(CanvasWindow canvas) {
        canvas.add(boundingBox);
        this.canvas = canvas;
    }

    public int getThickness() {
        return width;
    }

    public void increaseWidth() {
        if (width < 18) {
            width += 4;
        }
        this.setStrokeWidth(width);
    }

    public Node getStart() {
        return nodeA;
    }

    public Node getEnd() {
        return nodeB;
    }

    public void decrimentWidth() {
        if (width > 2) {
            width -= 2;
            this.setStrokeWidth(width);
        } else {
            canvas.remove(boundingBox);
            canvas.remove(this);
        }
    }
}
