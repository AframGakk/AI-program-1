import javafx.geometry.Pos;

import java.util.HashSet;
import java.util.Set;

public class Environment {
    private Set<Position> dirtLocations;
    private Set<Position> obstacleLocations;
    private Position sizeDimension;

    public Environment() {}

    public Environment(Set<Position> dirt, Set<Position> obstacle, Position dimen) {
        this.obstacleLocations = new HashSet<>();
        this.dirtLocations = new HashSet<>();

        this.obstacleLocations.addAll(obstacle);
        this.dirtLocations.addAll(dirt);
        this.sizeDimension = dimen;
    }

    /*
    *   GETTERS & SETTERS
    * */
    public Position getSizeDimension() {
        return sizeDimension;
    }
    public void setDirtLocations(Set<Position> dirtLocations) {
        this.dirtLocations = dirtLocations;
    }
    public Set<Position> getDirtLocations() {
        return dirtLocations;
    }
    public Set<Position> getObstacleLocations() {
        return obstacleLocations;
    }
    public void setObstacleLocations(Set<Position> obstacleLocations) {
        this.obstacleLocations = obstacleLocations;
    }
    public void setSizeDimension(Position sizeDimension) {
        this.sizeDimension = sizeDimension;
    }

    public boolean nextIsAvailable(Position position, Orientation orientation) {
        Position next;

        switch (orientation) {
            case SOUTH:
                next = new Position(position.getX(), position.getY() - 1);
                return !(obstacleLocations.contains(next) || next.getY() < 1);
            case NORTH:
                next = new Position(position.getX(), position.getY() + 1);
                return !(obstacleLocations.contains(next) || next.getY() > sizeDimension.getY());
            case EAST:
                next = new Position(position.getX() + 1, position.getY());
                return !(obstacleLocations.contains(next) || next.getX() > sizeDimension.getX());
            case WEST:
                next = new Position(position.getX() - 1, position.getY());
                return !(obstacleLocations.contains(next) || next.getX() < 1);
            default:
                System.out.println("Illegal orientation");
                return false;
        }
    }

    public boolean locationContainsDirt(Position position) {
        return dirtLocations.contains(position);
    }

    public boolean cleanUpDirt(Position position) {
        return dirtLocations.remove(position);
    }

    public void printEnvironment(Position position) {
        System.out.println("========= Environment ==========");
        for (int y = sizeDimension.getY(); y >= 1; y--) {
            for (int x = 1; x <= sizeDimension.getX(); x++) {
                Position tmp = new Position(x, y);

                if (locationContainsDirt(tmp)) {
                    System.out.print("X ");
                } else if (obstacleLocations.contains(tmp)) {
                    System.out.print("Æ ");
                } else if (position.getY() == tmp.getY() && position.getX() == tmp.getX()) {
                    System.out.print("T ");
                } else {
                    System.out.print("O ");
                }
            }
            System.out.println("");
        }
        System.out.println("========================");
    }

}
