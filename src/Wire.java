import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Random;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Line;
import edu.macalester.graphics.Rectangle;

public class Wire extends Line{
    private int width;
    private Rectangle boundingBox;
    
    public Wire(Node nodeA, Node nodeB) {
        super(nodeA.getCenter(), nodeB.getCenter());
        super.setStrokeColor(Color.MAGENTA);
        width = 2*(new Random().nextInt(4) + 2);
        super.setStrokeWidth(width);
        boundingBox = new Rectangle(getX1(), getY1(), Point2D.distance(getX1(), getY1(), getX2(), getY2()), 18);
        boundingBox.setCenter(getCenter());
        double rotation = Math.toDegrees(Math.acos((getX1()-getX2())/Point2D.distance(getX1(), getY1(), getX2(), getY2())));
        if (getY2() > getY1()) {
            rotation = rotation * -1;
        }
        boundingBox.setRotation(rotation);
        boundingBox.setStroked(false);
    }

    public boolean isClicked(double x, double y) {
        return boundingBox.testHit(x, y);
    }

    public void addBoundingBox(CanvasWindow canvas) {
        canvas.add(boundingBox);
    }

    public int getThickness() {
        return width;
    }

    public void increaseWidth() {
        if (width < 18) {
            width += 2;
        }
        super.setStrokeWidth(width);
    }

    public void decrimentWidth() {
        if (width > 2) {
            width -= 2;
            super.setStrokeWidth(width);
        } else {
            super.getCanvas().remove(this);
        }
    }
}
