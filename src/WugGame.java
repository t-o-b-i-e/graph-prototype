import java.util.ArrayList;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.ui.TextField;

/**
 * Yayyyyyyy
 */
public class WugGame extends GraphicsGroup {
    private CanvasWindow canvas;
    private TextField input;
    private GraphicsText words;
    private String currentWord;
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


    public WugGame () {
        canvas = new CanvasWindow("Prototype", 1000, 500);
        allWires = new ArrayList<>();
        wordsList = new ArrayList<>();

        createNodes();
        addConnections();
        for (Wire wire : allWires) {
            wire.addBoundingBox(canvas);
        }

        canvas.onMouseDown((mouse) -> {
            checkKeyTyped();
            for (Wire wire : allWires) {
                if (wire.isClicked(mouse.getPosition().getX(), mouse.getPosition().getY()) && !wire.equals(lastClickedWire)) {
                    wire.decrimentWidth();
                    lastClickedWire = wire;
                    canvas.draw();
                }
            }
        });

        canvas.onMouseMove((m) -> {checkKeyTyped();});

        input = new TextField();
        input.setCenter(850, 50);
        canvas.add(input);
        currentWord = "";

        words = new GraphicsText("", 0, 0);
        words.setCenter(850, 80);
        canvas.add(words);
    }

    public void checkKeyTyped() { //assumes input is all lower case
        if (input.getText().isEmpty()) {
            return;
        }
        Character ch = input.getText().charAt(input.getText().length() - 1);
        if (ch.compareTo('t') == 0 || ch.compareTo('y') == 0 || ch.compareTo('p') == 0){
            currentWord = input.getText();
            if (!wordsList.contains(currentWord)) {
                words.setText(words.getText() + "\n" + currentWord);
                increaseChosenLines(currentWord);
                wordsList.add(currentWord);
            }
            input.setText("");
            canvas.draw();
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
            }
        } else if (firstNode.equals(nodeL)) {
            if (secondNode.equals(nodeE)) {
                return wireLE;
            } else if (secondNode.equals(nodeN)) {
                return wireLN;
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
        return nodeA;
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

        for (Wire wire : allWires) {
            canvas.add(wire);
        }

        addNodes();
    }

    private void createNodes() {
        nodeA = new Node("A", 100, 100);
        nodeW = new Node("W", 100, 250);
        nodeU = new Node("U", 100, 400);
        
        nodeS = new Node("S", 300, 100);
        nodeE = new Node("E", 300, 250);
        nodeL = new Node("L", 300, 400);
        
        nodeV = new Node("V", 500, 175);
        nodeN = new Node("N", 500, 325);
       
        nodeT = new Node("T", 700, 100);
        nodeY = new Node("Y", 700, 250);
        nodeP = new Node("P", 700, 400);
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


