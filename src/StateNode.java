import java.util.ArrayList;
import java.util.List;

public class StateNode {
    private State state;
    // the action that led it here
    private Action action;

    public StateNode() {

    }

    public StateNode(State state) {
        this.state = state;
    }

    public StateNode(State state, Action action) {
        this.state = state;
        this.action = action;
    }

    public List<StateNode> successorStates() {
        List<StateNode> successors = new ArrayList<>();
        StateNode nStateNode;
        State nState;


        if (!this.state.isOn()) {
            nState = new State(this.state);
            nState.executeMove(Action.TURN_ON);
            nStateNode = new StateNode(nState, Action.TURN_ON);

            successors.add(nStateNode);
            return successors;
        }

        this.state.printStateCheck();


        // If position contains dirt
        if (this.state.getMap()[this.state.getPosition().getX()][this.state.getPosition().getY()] == 1) {
            nState = new State(this.state);
            nState.executeMove(Action.SUCK);
            nStateNode = new StateNode(nState, Action.SUCK);
            successors.add(nStateNode);
            return successors;
        }

        switch (this.state.getOrientation()) {
            case EAST:
                // if position on x axis is NOT at the end of the environment
                if(this.state.getPosition().getX() + 1 < this.state.getMap().length) {
                    if(this.state.getMap()[this.state.getPosition().getX() + 1][this.state.getPosition().getY()] != 2) {
                        nState = new State(this.state);
                        nState.executeMove(Action.GO);
                        nStateNode = new StateNode(nState, Action.GO);
                        successors.add(nStateNode);
                    }
                }
                break;
            case WEST:
                if(this.state.getPosition().getX() > 0 &&
                        this.state.getMap()[this.state.getPosition().getX() - 1][this.state.getPosition().getY()] != 2) {
                    nState = new State(this.state);
                    nState.executeMove(Action.GO);
                    nStateNode = new StateNode(nState, Action.GO);
                    successors.add(nStateNode);
                }
                break;
            case NORTH:
                if(this.state.getPosition().getY() + 1 < this.state.getMap()[0].length ) {
                    if((this.state.getMap()[this.state.getPosition().getX()][this.state.getPosition().getY() + 1] != 2)) {
                        nState = new State(this.state);
                        nState.executeMove(Action.GO);
                        nStateNode = new StateNode(nState, Action.GO);
                        successors.add(nStateNode);
                    }
                }
                break;
            case SOUTH:
                if(this.state.getPosition().getY() > 0 &&
                        this.state.getMap()[this.state.getPosition().getX()][this.state.getPosition().getY() - 1] != 2) {
                    nState = new State(this.state);
                    nState.executeMove(Action.GO);
                    nStateNode = new StateNode(nState, Action.GO);
                    successors.add(nStateNode);
                }
                break;
            default:
                System.out.println("Invalid orientation");
        }

        nState = new State(this.state);
        nState.executeMove(Action.TURN_OFF);
        nStateNode = new StateNode(nState, Action.TURN_OFF);
        successors.add(nStateNode);

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


}
