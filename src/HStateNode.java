import java.util.ArrayList;
import java.util.List;

public class HStateNode implements Comparable<HStateNode> {
    private State state;
    // the action that led it here
    private Action action;
    private HStateNode parent;
    private int pathCost;
    private int manhattanDistance;


    public HStateNode() {

    }

    public HStateNode(State state) {
        this.state = state;
    }

    public HStateNode(State state, Action action) {
        this.state = state;
        this.action = action;
    }

    public HStateNode(State state, Action action, HStateNode parent) {
        this.state = state;
        this.action = action;
        this.parent = parent;
    }

    public HStateNode(State state, Action action, HStateNode parent, int cost) {
        this.state = state;
        this.action = action;
        this.parent = parent;
        this.pathCost = cost;
    }

    public List<Action> successors() {
        List<Action> successors = new ArrayList<>();

        if (!this.state.isOn()) {
            successors.add(Action.TURN_ON);
            return successors;
        }

        if (this.state.getMap().getDirtLocations().size() == 0 && this.state.getPosition().compare(this.state.getHome())) {
            successors.add(Action.TURN_OFF);
        }

        // If position contains dirt
        if (this.state.getMap().locationContainsDirt(this.state.getPosition())) {
            successors.add(Action.SUCK);
        }

        if(this.state.getMap().nextIsAvailable(this.state.getPosition(), this.state.getOrientation())) {
            successors.add(Action.GO);
        }

        successors.add(Action.TURN_LEFT);
        successors.add(Action.TURN_RIGHT);

        return successors;
    }

    private void printNodeCheck(String header, HStateNode node) {
        System.out.println("=============================");
        System.out.println("          " + header + "    ");
        //System.out.println("Ori before: " + node.getParent().getState().getOrientation());
        if(node.getParent() != null) {
            System.out.println("Parent Action: " + node.getParent().getAction());
        } else {
            System.out.println("Parent Action: NULL");
        }
        System.out.println("Action: " + node.getAction());
        node.getState().printStateCheck();
        node.getState().getMap().printEnvironment(node.getState().getPosition());
        System.out.println("=============================");
    }


    public void setState(State state) {
        this.state = state;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public State getState() {
        return state;
    }

    public void setParent(HStateNode parent) {
        this.parent = parent;
    }

    public HStateNode getParent() {
        return parent;
    }

    public void setPathCost(int pathCost) {
        this.pathCost = pathCost;
    }

    public int getPathCost() {
        return pathCost;
    }

    public int getManhattanDistance() {
        return manhattanDistance;
    }

    public void setManhattanDistance(int manhattanDistance) {
        this.manhattanDistance = manhattanDistance;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.getState().getPosition().getX();
        result = prime * result + this.getState().getPosition().getY();
        if(this.getState().getDirtCount() == 0) {
            result = prime * result + this.pathCost;
        } else {
            result = prime * result + this.getState().getDirtCount();
        }
        result = prime * result + this.state.getOrientation().hashCode();
        if(this.action == null) {
            result = prime * result + 1;
        } else {
            result = prime * result + this.action.hashCode();
        }

        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }
        if(!(obj instanceof HStateNode)){
            return false;
        }
        HStateNode other = (HStateNode)obj;
        return (this.getState().equals(other.getState()));
    }

    @Override
    public int compareTo(HStateNode other) {
        if(this.manhattanDistance > other.manhattanDistance) return 1;
        else if(this.manhattanDistance < other.manhattanDistance) return -1;
        return 0;
    }

}
