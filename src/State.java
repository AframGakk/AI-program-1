import java.util.ArrayList;
import java.util.List;

public class State {
    private int posX;
    private int posY;
    private boolean on;
    private Orientation orientation;
    private int map[][];
    private int dirtCount;
    private int score;

    public State(int posX, int posY, Orientation orientation,
                 int map[][], int dirtCount) {
        this.posX = posX;
        this.posY = posY;
        this.orientation = orientation;
        this.map = map;
        this.dirtCount = dirtCount;
        this.score = 0;
    }

    public State(State newState) {
        this.posY = newState.posY;
        this.posX = newState.posX;
        this.on = newState.on;
        this.orientation = newState.orientation;
        this.map = newState.map;
        this.dirtCount = newState.dirtCount;
        this.score = newState.score;
    }

    public List<State> successorStates() {
        List<State> successors = new ArrayList<>();

        // If position contains dirt
        if (map[posX][posY] == 1) {
            // TODO: implement
            State nState = new State(this);
            nState
            successors.add(new State())
        }


        switch (this.orientation) {
            case EAST:

        }
        return null;
    }

    // TODO: implement
    public void executeMove() {

    }
}
