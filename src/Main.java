/*
import java.util.ArrayList;

public class Main {
    public static final String[][] startGame = {
            {"0", "0", "0", "0"},
            {"0", "0", "0", "0"},
            {"0", "0", "0", "0"},
            {"0", "0", "0", "0"},
            {"0", "0", "0", "0"},
            {"0", "0", "0", "0"},
            {"0", "0", "0", "0"},
            {"1", "2", "3", "4"},
            {"2", "3", "4", "1"},
            {"3", "4", "1", "2"},
            {"4", "1", "2", "1"}};
    public static Node BFS(Node head) {
        ArrayList<Node> nodeList = new ArrayList<Node>();
        nodeList.add(head);
        while (nodeList.get(0).goal()) {
            nodeList.addAll(nodeList.get(0).suckNode());
            System.out.println("depth = "+ nodeList.get(0).getDepth() );
            nodeList.remove(0);
        }
        return nodeList.get(0);
    }
    public static Node DFS(Node head) {
        ArrayList<Node> nodeList = new ArrayList<Node>();
        nodeList.add(head);
        while (nodeList.get(0).getDepth()<100000) {
            if(nodeList.get(0).getDepth()>100) {

                nodeList.remove(0);
            }else {
                System.out.println(nodeList.get(0).getDepth() + "    " + nodeList.get(0).getState());
                Node myTemp = nodeList.remove(0);
                nodeList = switchPlaces(myTemp.suckNode(), nodeList);
            }
        }
        return nodeList.get(0);
    }
    public static ArrayList<Node> switchPlaces(ArrayList<Node> toAppend, ArrayList<Node> alreadyIn) {
        toAppend.addAll(alreadyIn);
        return toAppend;
    }
    public static void main(String[] args) {
        UpThrustGame newGame = new UpThrustGame(startGame);
        Node startingNode = new Node(newGame,null);
        Node finalNode = DFS(startingNode);
        System.out.println(finalNode.getDepth());
    }
}*/
