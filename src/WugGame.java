import java.util.ArrayList;
import java.awt.Color;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.ui.TextField;

/**
 * Yayyyyyyy
 */
public class WugGame extends GraphicsGroup {
    private CanvasWindow canvas;
    private TextField input;
    private GraphicsText words;
    private ArrayList<String> wordsList;
    //Hard coding so I don't have to think too hard
    //start nodes
    private Node nodeA;
    private Node nodeW;
    private Node nodeU;
    //first row
    private Node nodeS;
    private Node nodeE;
    private Node nodeL;
    //second row
    private Node nodeV;
    private Node nodeN;
    private Node nodeP;
    //end nodes
    private Node nodeT;
    private Node nodeY;
    private Node[] nodes;

    //So sorry about this mess, this is an extremely rough prototype
    private ArrayList<Wire> allWires;
    private Wire lastClickedWire;
    private Wire wireAS;
    private Wire wireAE;
    private Wire wireWE;
    private Wire wireSV;
    private Wire wireEV;
    private Wire wireUL;
    private Wire wireLE;
    private Wire wireEN;
    private Wire wireNV;
    private Wire wireLN;
    private Wire wireVT;
    private Wire wireNP;
    private Wire wireVY;
    private Wire wireNY;

    public WugGame () {
        canvas = new CanvasWindow("Prototype", 1000, 500);
        canvas.setBackground(new Color(102,179,255));
        allWires = new ArrayList<>();
        wordsList = new ArrayList<>();

        createNodes();
        addConnections();
        for (Wire wire : allWires) {
            wire.addBoundingBox(canvas);
        }

        

        input = new TextField();
        input.setBackground(new Color(102,179,255));
        input.setAnchor(800,40);
        input.setText("");
        input.setPosition(800, 40);
        canvas.add(input);

        words = new GraphicsText("", 0, 0);
        words.setFontSize(15);
        words.setFillColor(Color.WHITE);
        words.setPosition(800, 55);
        canvas.add(words);

        canvas.animate((dt) -> {
            checkKeyTyped();
            nodeForces();
            updateVelocities();
            updatePositions();
            decayWires();
        });

        canvas.onMouseDown((mouse) -> {
            for (Wire wire : allWires) {
                if (wire.isClicked(mouse.getPosition().getX(), mouse.getPosition().getY()) && !wire.equals(lastClickedWire)) {
                    wire.decrimentWidth();
                    lastClickedWire = wire;
                    return;
                }
            }
        });
    }

    //the force directed graph thing has an electric (inverse square based on distance) force between all nodes
    //and a spring (-proportional to displacement from the resting length) force. forces just are acceleration,
    //and we add these forces instead of setting them because we zero out acc + then calculate the force from the
    //boundaries in update positions (too much interlinked stuff but this is a prototype)
    public void nodeForces() {
        for (int i = 0; i<nodes.length; i++) {
            for (int j = i+1; j<nodes.length; j++) {
                double distance = nodes[i].getNodePosition().distance(nodes[j].getNodePosition());
                double force = -1/(distance*distance/15 + 1);
                Point forceVec = (nodes[i].getNodePosition().subtract(nodes[j].getNodePosition())).scale(force);
                nodes[i].addAcc(-forceVec.getX(), -forceVec.getY());
                nodes[j].addAcc(forceVec.getX(), forceVec.getY());
            }
        }
        for (Wire wire : allWires) {
            Node start = wire.getStart();
            Node end = wire.getEnd();
            double thickness = Math.min(wire.getThickness(),9);
            thickness = thickness*thickness*thickness/3; //emphasizes the effect of thickness
            double displacement = end.getNodePosition().distance(start.getNodePosition())-(200-thickness);
            if (displacement < 0) { //so the wires don't act like springs, they don't push the nodes away if its closer than resting
                displacement = 0;
            }
            Point forceVec = (end.getNodePosition().subtract(start.getNodePosition())).scale(displacement/1000000*wire.getThickness());
            start.addAcc(forceVec.getX(), forceVec.getY());
            end.addAcc(-forceVec.getX(), -forceVec.getY());
        }
    }

    //this is a prototype so if you don't want to play singleplayer you have to comment out decaywires
    //because the wire's decriment doesn't remove it from wires so it crashes when it tries to decay the wire
    public void decayWires() {
        for (Wire wire : allWires) {
            wire.decay();
            if (wire.getThickness()<=0) {
                allWires.remove(wire);
                break;
            }
        }
    }

    public void updateVelocities() {
        for (Node node : nodes) {
            node.updateVel();
        }
    }

    public void updatePositions() {
        for (Node node : nodes) {
            node.updatePos();
            node.gravity(); //gravity is poorly named it's really an electric force i realized that works better to keep them in bounds
        }
        for (Wire wire : allWires) {
            wire.reposition();
        }
    }

    public void checkKeyTyped() { //assumes input is all lower case
        String text = input.getText().toLowerCase();
        if (text.length()<3) { //we only accept 2 letter words, and you press space to submit, so it has to be at least 2 letters and space
            return;
        }
        if (text.charAt(text.length()-1) == ' ') {
            text = text.substring(0, text.length()-1);
        } else {
            return;
        }

        for (int i = 0; i < text.length()-1; i++) { // loops through every pair
            char firstChar = text.charAt(i);
            char secondChar = text.charAt(i+1);
            Node firstNode = getNodeByLetter(firstChar);
            Node secondNode = getNodeByLetter(secondChar);
            if (secondNode == null || firstNode == null) { //both characters have to have a node
                input.setText("");
                return;
            } 
            Wire connection = getConnectingWire(firstNode, secondNode);
            if (connection == null) { //and they have to have a wire
                input.setText("");
                return;
            }
        }

        if (!wordsList.contains(text) && text.length() <= 8){
            words.setText(words.getText() + "\n" + text);
            increaseChosenLines(text);
            wordsList.add(text);
        } else {
            input.setText("");
        }
    }


    private void increaseChosenLines(String word) {
        for (int i = 1; i < word.length(); i++) {
            Wire connection = getConnectingWire(getNodeByLetter(word.charAt(i-1)), getNodeByLetter(word.charAt(i)));
            if (connection != null) {
                connection.increaseWidth();
            }
        }
        canvas.draw();
    }

    private Wire getConnectingWire(Node firstNode, Node secondNode) {
        if (firstNode.equals(nodeA)) {
            if (secondNode.equals(nodeS)) {
                return wireAS;
            } else if (secondNode.equals(nodeE)) {
                return wireAE;
            }
        } else if (firstNode.equals(nodeW) && secondNode.equals(nodeE)) {
            return wireWE;
        } else if (firstNode.equals(nodeU) && secondNode.equals(nodeL)) {
            return wireUL;
        } else if (firstNode.equals(nodeS)) {
            if (secondNode.equals(nodeA)) {
                return wireAS;
            } else if (secondNode.equals(nodeV)) {
                return wireSV;
            }
        } else if (firstNode.equals(nodeE)) {
            if (secondNode.equals(nodeA)) {
                return wireAE;
            } else if (secondNode.equals(nodeL)) {
                return wireLE;
            } else if (secondNode.equals(nodeV)) {
                return wireEV;
            } else if (secondNode.equals(nodeN)) {
                return wireEN;
            } else if (secondNode.equals(nodeW)) {
                return wireWE;
            }
        } else if (firstNode.equals(nodeL)) {
            if (secondNode.equals(nodeE)) {
                return wireLE;
            } else if (secondNode.equals(nodeN)) {
                return wireLN;
            } else if (secondNode.equals(nodeU)) {
                return wireUL;
            }
        } else if (firstNode.equals(nodeV)) {
            if (secondNode.equals(nodeS)) {
                return wireSV;
            } else if (secondNode.equals(nodeE)) {
                return wireEV;
            } else if (secondNode.equals(nodeN)) {
                return wireNV;
            } else if (secondNode.equals(nodeT)) {
                return wireVT;
            } else if (secondNode.equals(nodeY)) {
                return wireVY;
            }
        } else if (firstNode.equals(nodeN)) {
            if (secondNode.equals(nodeV)) {
                return wireNV;
            } else if (secondNode.equals(nodeE)) {
                return wireEN;
            } else if (secondNode.equals(nodeL)) {
                return wireLN;
            } else if (secondNode.equals(nodeP)) {
                return wireNP;
            } else if (secondNode.equals(nodeY)) {
                return wireNY;
            }
        } else if (firstNode.equals(nodeT)) {
            if (secondNode.equals(nodeV)) {
                return wireVT;
            }
        } else if (firstNode.equals(nodeY)) {
            if (secondNode.equals(nodeV)) {
                return wireVY;
            } else if (secondNode.equals(nodeN)) {
                return wireNY;
            }
        } else if (firstNode.equals(nodeP)) {
            if (secondNode.equals(nodeN)) {
                return wireNP;
            }
        }
        return null;
    }

    // yikes of hard coding lol
    private Node getNodeByLetter(Character ch) {
        if (ch.compareTo('a') == 0) {
            return nodeA;
        } else if (ch.compareTo('w') == 0) {
            return nodeW;
        } else if (ch.compareTo('u') == 0) {
            return nodeU;
        } else if (ch.compareTo('s') == 0) {
            return nodeS;
        } else if (ch.compareTo('e') == 0) {
            return nodeE;
        } else if (ch.compareTo('l') == 0) {
            return nodeL;
        } else if (ch.compareTo('v') == 0) {
            return nodeV;
        } else if (ch.compareTo('n') == 0) {
            return nodeN;
        } else if (ch.compareTo('p') == 0) {
            return nodeP;
        } else if (ch.compareTo('t') == 0) {
            return nodeT;
        } else if (ch.compareTo('y') == 0) {
            return nodeY;
        } 
        return null;
    }

    private void addConnections() {
        wireAS = new Wire(nodeA, nodeS);
        allWires.add(wireAS);

        wireAE = new Wire(nodeA, nodeE);
        allWires.add(wireAE);

        wireWE = new Wire(nodeW, nodeE);
        allWires.add(wireWE);

        wireSV = new Wire(nodeS, nodeV);
        allWires.add(wireSV);

        wireEV = new Wire(nodeE, nodeV);
        allWires.add(wireEV);

        wireUL = new Wire(nodeU, nodeL);
        allWires.add(wireUL);

        wireLE = new Wire(nodeL, nodeE);
        allWires.add(wireLE);

        wireEN = new Wire(nodeE, nodeN);
        allWires.add(wireEN);

        wireNV = new Wire(nodeN, nodeV);
        allWires.add(wireNV);

        wireLN = new Wire(nodeL, nodeN);
        allWires.add(wireLN);

        wireVT = new Wire(nodeV, nodeT);
        allWires.add(wireVT);

        wireNP = new Wire(nodeN, nodeP);
        allWires.add(wireNP);

        wireVY = new Wire(nodeV, nodeY);
        allWires.add(wireVY);

        wireNY = new Wire(nodeN, nodeY);
        allWires.add(wireNY);

        for (Wire wire : allWires) {
            canvas.add(wire);
        }

        addNodes();
    }

    private void createNodes() {
        nodeA = new Node("a", 100, 100);
        nodeW = new Node("w", 100, 250);
        nodeU = new Node("u", 100, 400);
        
        nodeS = new Node("s", 300, 100);
        nodeE = new Node("e", 300, 250);
        nodeL = new Node("l", 300, 400);
        
        nodeV = new Node("v", 500, 175);
        nodeN = new Node("n", 500, 325);
       
        nodeT = new Node("t", 700, 100);
        nodeY = new Node("y", 700, 250);
        nodeP = new Node("p", 700, 400);
        nodes = new Node[]{nodeA, nodeW, nodeU, nodeS, nodeE, nodeL, nodeV, nodeN, nodeP, nodeT, nodeY};
        }

    private void addNodes() {
        canvas.add(nodeA);
        canvas.add(nodeW);
        canvas.add(nodeU);
        canvas.add(nodeS);
        canvas.add(nodeE);
        canvas.add(nodeL);
        canvas.add(nodeV);
        canvas.add(nodeN);
        canvas.add(nodeT);
        canvas.add(nodeY);
        canvas.add(nodeP);
    }

    public static void main(String[] args) {
        new WugGame();
    }
}


