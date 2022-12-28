import java.util.ArrayList;
import java.util.Arrays;

public class Node {
    private static final String[][] goal = {
            {"1","2","3","4"},
            {"2","3","4","1"},
            {"3","4","1","2"},
            {"4","1","2","1"},
            {"0","0","0","0"},
            {"0","0","0","0"},
            {"0","0","0","0"},
            {"0","0","0","0"},
            {"0","0","0","0"},
            {"0","0","0","0"},
            {"0","0","0","0"}};
    private UpThrustGame state;
    private int depth;
    private Node parentNode;

    @Override
    public String toString() {
        return "Node{" +
                "state=" + state +
                ", depth=" + depth +
                ", parentNode=" + parentNode +
                '}';
    }

    public boolean goal(){
        if(!Arrays.equals(state.getGameMatrix(), goal)){
            return true;
        }
        return false;
    }
    public Node(UpThrustGame state, Node parantNode) {
        this.state = state;
        this.parentNode = parantNode;
        if(parentNode == null){
            depth = 0;
        }
        else{
            depth = parentNode.getDepth() + 1;
        }
    }

    public ArrayList<Node> suckNode(){
        ArrayList<Node> sucked = new ArrayList<>();
        ArrayList<UpThrustGame> states = state.successor();
        for(UpThrustGame state : states){
            Node node = new Node(state, this);
            sucked.add(node);
        }
        return sucked;
    }


    public UpThrustGame getState() {
        return state;
    }

    public void setState(UpThrustGame state) {
        this.state = state;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }
}

