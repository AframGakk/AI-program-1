import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;

public class State {
    private Position position;
    private Position home;
    private boolean on;
    private Orientation orientation;
    /**
     * 0 = empty location
     * 1 = dirt location
     * 2 = obstacle location
     * */
    private int map[][];
    private int dirtCount;
    private int score;

    public State() {}

    public State(Position position, Orientation orientation,
                 int map[][], int dirtCount) {
        this.position = position;
        this.home = position;
        this.orientation = orientation;
        this.map = map;
        this.dirtCount = dirtCount;
        this.score = 0;
        this.on = false;
    }

    public State(State newState) {
        this.position = newState.position;
        this.home = newState.home;
        this.on = newState.on;
        this.orientation = newState.orientation;
        this.map = newState.map;
        this.dirtCount = newState.dirtCount;
        this.score = newState.score;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getHome() {
        return home;
    }

    public void setHome(Position home) {
        this.home = home;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public int[][] getMap() {
        return map;
    }

    public int getDirtCount() {
        return dirtCount;
    }

    public boolean goalTest() {
        return (dirtCount == 0) && (position == home) && (on == false);
    }

    public boolean isOn() {
        return this.on;
    }

    public List<State> successorStates() {
        List<State> successors = new ArrayList<>();
        State nState;

        if (!this.on) {
            nState = new State(this);
            nState.executeMove(Action.TURN_ON);
            successors.add(nState);
            return successors;
        }

        // If position contains dirt
        if (map[position.getX()][position.getY()] == 1) {
            nState = new State(this);
            nState.executeMove(Action.SUCK);
            successors.add(nState);
        }

        switch (this.orientation) {
            case EAST:
                // if position on x axis is NOT at the end of the environment
                if(position.getX() < map.length && map[position.getX() + 1][position.getY()] != 2) {
                    nState = new State(this);
                    nState.executeMove(Action.GO);
                    successors.add(nState);
                }
                break;
            case WEST:
                if(position.getX() > 0 && map[position.getX() - 1][position.getY()] != 2) {
                    nState = new State(this);
                    nState.executeMove(Action.GO);
                    successors.add(nState);
                }
                break;
            case NORTH:
                if(position.getY() > 0 && map[position.getX()][position.getY() + 1] != 2) {
                    nState = new State(this);
                    nState.executeMove(Action.GO);
                    successors.add(nState);
                }
                break;
            case SOUTH:
                if(position.getY() < map[0].length && map[position.getX()][position.getY() - 1] != 2) {
                    nState = new State(this);
                    nState.executeMove(Action.GO);
                    successors.add(nState);
                }
                break;
            default:
                System.out.println("Invalid orientation");
        }

        nState = new State(this);
        nState.executeMove(Action.TURN_OFF);
        successors.add(nState);

        nState = new State(this);
        nState.executeMove(Action.TURN_LEFT);
        successors.add(nState);
        nState = new State(this);
        nState.executeMove(Action.TURN_RIGHT);
        successors.add(nState);

        return successors;
    }

    // executes an action within the state
    public void executeMove(Action action) {
        switch (action) {
            case SUCK:
                if(this.map[position.getX()][position.getY()] != 1) {
                    this.score += 5;
                    break;
                } else {
                    this.map[position.getX()][position.getY()] = 0;
                    this.dirtCount--;
                    this.score++;
                }
                break;
            case GO:
                switch (this.orientation) {
                    case EAST:
                        this.position.incrX();
                        break;
                    case WEST:
                        this.position.decrX();
                        break;
                    case NORTH:
                        this.position.incrY();
                        break;
                    case SOUTH:
                        this.position.decrY();
                        break;
                    default:
                        System.out.println("Invalid orientation");
                }
                this.score++;
                break;
            case TURN_ON:
                this.on = true;
                this.score++;
                break;
            case TURN_OFF:
                this.on = false;
                if (this.dirtCount > 0) {
                    this.score += (50 * this.dirtCount) + 1;
                } else if (this.home != this.position) {
                    this.score += (50 * this.dirtCount) + 100;
                } else {
                    this.score++;
                }
                break;
            case TURN_LEFT:
                switch (this.orientation) {
                    case SOUTH:
                        this.orientation = Orientation.EAST;
                        break;
                    case NORTH:
                        this.orientation = Orientation.WEST;
                        break;
                    case EAST:
                        this.orientation = Orientation.NORTH;
                        break;
                    case WEST:
                        this.orientation = Orientation.SOUTH;
                        break;
                    default:
                        System.out.println("Invalid orientation in TURN_LEFT");
                }
                this.score++;
                break;
            case TURN_RIGHT:
                switch (this.orientation) {
                    case SOUTH:
                        this.orientation = Orientation.WEST;
                        break;
                    case NORTH:
                        this.orientation = Orientation.EAST;
                        break;
                    case EAST:
                        this.orientation = Orientation.SOUTH;
                        break;
                    case WEST:
                        this.orientation = Orientation.NORTH;
                        break;
                    default:
                        System.out.println("Invalid orientation in TURN_RIGHT");

                }
                this.score++;
                break;
            default:
                System.out.println("Invalid action");
        }
    }

    public void printStateCheck() {
        System.out.println("Dirt Count: " + this.dirtCount);
        System.out.println("Position: (" + this.position.getX() + ", " + this.position.getY() + ")");
    }
}
