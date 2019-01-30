import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class State {
    private Position position;
    private Position home;
    private boolean on;
    private Orientation orientation;
    private int dirtCount;
    private int score;
    private int atDepth;

    private Map map;

    public State() {}

    public State(Position position, Orientation orientation,
                 Map map, int dirtCount) {
        this.position = position;
        this.home = position;
        this.orientation = orientation;
        this.map = map;
        this.score = 0;
        this.dirtCount = dirtCount;
        this.on = false;
    }


    public State(
            Position position,
            Position home,
            boolean on,
            Orientation orientation,
            int dirtCount,
            int score,
            Map map,
            int atDepth
    )
    {
        this.position = new Position(position.getX(), position.getY());
        this.home = new Position(home.getX(), home.getY());
        this.on = on;
        this.orientation = orientation;
        this.dirtCount = dirtCount;
        this.score = score;
        this.map = new Map(map.getDirtLocations(), map.getObstacleLocations(), map.getSizeDimension());
        this.atDepth = atDepth;
    }

    public State(State newState) {
        this.position = new Position(newState.getPosition().getX(), newState.getPosition().getY());
        this.home = new Position(newState.getHome().getX(), newState.getHome().getY());
        this.on = newState.isOn();
        this.orientation = newState.orientation;
        this.dirtCount = newState.getDirtCount();
        this.score = newState.getScore();
        this.map = new Map(newState.getMap().getDirtLocations(),
                newState.getMap().getObstacleLocations(),
                new Position(newState.getMap().getSizeDimension().getX(), newState.getMap().getSizeDimension().getY()));
        this.atDepth = newState.getDepth();
    }

    /*
    *   GETTERS & SETTERS
    * */
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
    public void setMap(Map map) {
        this.map = map;
    }
    public Orientation getOrientation() {
        return orientation;
    }
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
    public Map getMap() {
        return map;
    }
    public int getDirtCount() {
        return dirtCount;
    }
    public boolean isOn() {
        return this.on;
    }
    public int getScore() {
        return score;
    }
    public int getDepth() { return atDepth; }

    public boolean goalTest() {
        return (this.map.getDirtLocations().size() == 0) && (position.getX() == home.getX()) && (position.getY() == home.getY()) && (on == false);
    }

    // executes an action within the state
    public void executeMove(Action action) {
        switch (action) {
            case SUCK:
                if(this.map.cleanUpDirt(this.position)) {
                    dirtCount--;
                    this.score++;
                    break;
                } else {
                    this.score += 5;
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
        atDepth++;
    }

    public void printStateCheck() {
        System.out.println("Is on: " + this.isOn());
        System.out.println("Dirt Count: " + this.getDirtCount());
        System.out.println("Dirt Count List: " + this.map.getDirtLocations().size());
        System.out.println("Position: (" + this.position.getX() + ", " + this.position.getY() + ")");
        System.out.println("Home: " + this.getHome());
        System.out.println("Orientation: " + this.orientation);
        System.out.println("Depth: " + atDepth);
        printMap();
    }

    public void printMap() {
        /*
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[i].length - 1; i++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
        */
        /*
        this.map[this.position.getY()][this.position.getX()] = 8;

        for (int[] i : map) {
            for (int j : i) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
        */
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        State other = (State) obj;
        if (position.getX() != other.getPosition().getX() || position.getY() != other.getPosition().getY())
            return false;
        if (on != other.isOn())
            return false;
        if (orientation != other.getOrientation())
            return false;
        if (this.getMap().getDirtLocations().size() != other.getMap().getDirtLocations().size())
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.getPosition().getX();
        result = prime * result + this.getPosition().getY();
        result = prime * result + this.getMap().getDirtLocations().hashCode();
        result = prime * result + this.getOrientation().hashCode();
        if (this.isOn()) {
            result = prime * result + 1;
        } else {
            result = prime * result + 0;
        }

        return result;
    }
}
