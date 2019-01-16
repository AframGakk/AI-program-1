import java.util.Collection;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VacumAgent {
    private Stack<Action> actions;

    public void init(Collection<String> percepts) {
        StateNode root;

		/*
			Possible percepts are:
			- "(SIZE x y)" denoting the size of the environment, where x,y are integers
			- "(HOME x y)" with x,y >= 1 denoting the initial position of the robot
			- "(ORIENTATION o)" with o in {"NORTH", "SOUTH", "EAST", "WEST"} denoting the initial orientation of the robot
			- "(AT o x y)" with o being "DIRT" or "OBSTACLE" denoting the position of a dirt or an obstacle
			Moving north increases the y coordinate and moving east increases the x coordinate of the robots position.
			The robot is turned off initially, so don't forget to turn it on.
		*/
        Pattern perceptNamePattern = Pattern.compile("\\(\\s*([^\\s]+).*");
        for (String percept:percepts) {
            Matcher perceptNameMatcher = perceptNamePattern.matcher(percept);
            if (perceptNameMatcher.matches()) {
                String perceptName = perceptNameMatcher.group(1);
                if (perceptName.equals("HOME")) {
                    Matcher m = Pattern.compile("\\(\\s*HOME\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
                    if (m.matches()) {



                        System.out.println("robot is at " + m.group(1) + "," + m.group(2));
                        /*
                        State firstState = new State(
                                new Position(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))),
                                Orientation.EAST,

                        );

                        */
                        root = new StateNode();

                        /*
                        public State(Position position, Orientation orientation,
                        int map[][], int dirtCount) {
                            this.position = position;
                            this.home = position;
                            this.orientation = orientation;
                            this.map = map;
                            this.dirtCount = dirtCount;
                            this.score = 0;
                        }
                        */



                    }
                } else {
                    System.out.println("other percept:" + percept);
                }
            } else {
                System.err.println("strange percept that does not match pattern: " + percept);
            }
        }
    }

    public String nextAction(Collection<String> percepts) {
        Action next = this.actions.pop();

        switch (next) {
            case TURN_RIGHT:
                return "TURN_RIGHT";
            case TURN_LEFT:
                return "TURN_LEFT";
            case TURN_OFF:
                return "TURN_OFF";
            case TURN_ON:
                return "TURN_ON";
            case SUCK:
                return "SUCK";
            default:
                return "GO";
        }

        /*
        System.out.print("perceiving:");
        for(String percept:percepts) {
            System.out.print("'" + percept + "', ");
        }
        System.out.println("");
        String[] actions = { "TURN_ON", "TURN_OFF", "TURN_RIGHT", "TURN_LEFT", "GO", "SUCK" };
        return actions[random.nextInt(actions.length)];
        */
    }
}