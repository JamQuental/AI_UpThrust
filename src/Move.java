public class Move {
    private String action;
    private NodeGameAB state;

    public Move(String action, NodeGameAB state) {
        this.action = action;
        this.state = state;
    }

    @Override
    public String toString() {
        return "Play - " +action + "\n" + state.toString();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public NodeGameAB getState() {
        return state;
    }

    public void setState(NodeGameAB state) {
        this.state = state;
    }
}
