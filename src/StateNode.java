import java.util.ArrayList;
import java.util.List;

public class StateNode implements Comparable<StateNode> {
    private State state;
    // the action that led it here
    private Action action;
    private StateNode parent;
    private int pathCost;
    private int distanceToStart;


    public StateNode() {

    }

    public StateNode(State state) {
        this.state = state;
    }

    public StateNode(State state, Action action) {
        this.state = state;
        this.action = action;
    }

    public StateNode(State state, Action action, StateNode parent) {
        this.state = state;
        this.action = action;
        this.parent = parent;
    }

    public StateNode(State state, Action action, StateNode parent, int cost) {
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

    private void printNodeCheck(String header, StateNode node) {
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

    public void setParent(StateNode parent) {
        this.parent = parent;
    }

    public StateNode getParent() {
        return parent;
    }

    public void setPathCost(int pathCost) {
        this.pathCost = pathCost;
    }

    public int getPathCost() {
        return pathCost;
    }

    public int getDistanceToStart() {
        return distanceToStart;
    }

    public void setDistanceToStart(int distanceToStart) {
        this.distanceToStart = distanceToStart;
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
        if(!(obj instanceof StateNode)){
            return false;
        }
        StateNode other = (StateNode)obj;
        return (this.getState().equals(other.getState()));
    }

    @Override
    public int compareTo(StateNode other) {
        return Integer.compare(pathCost, other.getPathCost());
    }

}
