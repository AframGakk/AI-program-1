import java.util.List;

public class StateNode {
    private State state;
    // the action that led it here
    private Action action;
    private List<State> successors;

    public StateNode() {

    }

    public StateNode(State state, Action action) {
        this.state = state;
        this.action = action;
        this.successors = state.successorStates();
    }

    public List<State> getSuccessors() {
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
