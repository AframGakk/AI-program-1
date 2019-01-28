import java.util.ArrayList;
import java.util.List;

public class StateNode implements Comparable<StateNode> {
    private State state;
    // the action that led it here
    private Action action;
    private StateNode parent;
    private int pathCost;

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

    public List<StateNode> successorStates() {
        List<StateNode> successors = new ArrayList<>();
        StateNode nStateNode;
        State nState;


        if (this.state.goalTest()) {
            nState = new State(this.state);
            nState.executeMove(Action.TURN_OFF);
            nStateNode = new StateNode(nState, Action.TURN_OFF);
            successors.add(nStateNode);
            return successors;
        }

        if (!this.state.isOn()) {
            nState = new State(this.state);
            nState.executeMove(Action.TURN_ON);
            nStateNode = new StateNode(nState, Action.TURN_ON);

            successors.add(nStateNode);
            return successors;
        }


        // If position contains dirt
        System.out.println(this.state.getMap()[this.state.getPosition().getY()][this.state.getPosition().getX()]);
        if (this.state.getMap()[this.state.getPosition().getY()][this.state.getPosition().getX()] == 1) {
            System.out.println("---------- SHOULD SUCK ---------");
            nState = new State(this.state);
            nState.executeMove(Action.SUCK);
            nStateNode = new StateNode(nState, Action.SUCK);
            successors.add(nStateNode);
        }

        switch (this.state.getOrientation()) {
            case EAST:
                // if position on x axis is NOT at the end of the environment
                if(this.state.getPosition().getX() > 0) {
                    if(this.state.getMap()[this.state.getPosition().getY()][this.state.getPosition().getX() - 1] != 2) {
                        nState = new State(this.state);
                        nState.executeMove(Action.GO);
                        nStateNode = new StateNode(nState, Action.GO);
                        successors.add(nStateNode);
                    }
                }
                break;
            case WEST:
                if(this.state.getPosition().getX() < this.state.getMap().length) {
                    if (this.state.getMap()[this.state.getPosition().getY()][this.state.getPosition().getX() + 1] != 2) {
                        nState = new State(this.state);
                        nState.executeMove(Action.GO);
                        nStateNode = new StateNode(nState, Action.GO);
                        successors.add(nStateNode);
                    }
                }
                break;
            case NORTH:
                if(this.state.getPosition().getY() + 1 < this.state.getMap().length ) {
                    if((this.state.getMap()[this.state.getPosition().getY() + 1][this.state.getPosition().getX()] != 2)) {
                        nState = new State(this.state);
                        nState.executeMove(Action.GO);
                        nStateNode = new StateNode(nState, Action.GO);
                        successors.add(nStateNode);
                    }
                }
                break;
            case SOUTH:
                if(this.state.getPosition().getY() > 0) {

                    if (this.state.getMap()[this.state.getPosition().getY() - 1][this.state.getPosition().getX()] != 2) {
                        nState = new State(this.state);
                        nState.executeMove(Action.GO);
                        nStateNode = new StateNode(nState, Action.GO);
                        successors.add(nStateNode);
                    }
                }
                break;
            default:
                System.out.println("Invalid orientation");
        }

        nState = new State(this.state);
        nState.executeMove(Action.TURN_LEFT);
        nStateNode = new StateNode(nState, Action.TURN_LEFT);
        successors.add(nStateNode);
        nState = new State(this.state);
        nState.executeMove(Action.TURN_RIGHT);
        nStateNode = new StateNode(nState, Action.TURN_RIGHT);
        successors.add(nStateNode);

        return successors;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.getState().getPosition().getX();
        result = prime * result + this.getState().getPosition().getY();
        result = prime * result + this.getState().getDirtCount();
        result = prime * result + this.state.getOrientation().hashCode();



        /*
        switch (this.state.getOrientation()) {
            case NORTH:
                result = prime * result + 2;
                break;
            case SOUTH:
                result = prime * result + 3;
                break;
            case WEST:
                result = prime * result + 4;
                break;
            default:
                result = prime * result + 5;
        }
        */

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StateNode other = (StateNode) obj;
        if (state != other.getState())
            return false;
        if (action != other.getAction())
            return false;
        return true;
    }

    @Override
    public int compareTo(StateNode other) {
        return Integer.compare(pathCost, other.getPathCost());
    }

}
