import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VacumAgent implements Agent {
    private LinkedList<Action> actions;
    StateNode root;

    public void init(Collection<String> percepts) {
        State initState = new State();

        Position size = new Position(0, 0);
        int dirtCount = 0;
        Position position = new Position(0,0);
        Orientation orientation = Orientation.NORTH;
        Set<Position> dirtList = new HashSet<>();
        Set<Position> obstacleList = new HashSet<>();


		/*
			Possible percepts are:
			- "(SIZE x y)" denoting the size of the environment, where x,y are integers
			- "(HOME x y)" with x,y >= 1 denoting the initial position of the robot
			- "(ORIENTATION o)" with o in {"NORTH", "SOUTH", "EAST", "WEST"} denoting the initial orientation of the robot
			- "(AT o x y)" with o being "DIRT" or "OBSTACLE" denoting the position of a dirt or an obstacle
			Moving north increases the y coordinate and moving east increases the x coordinate of the robots position.
			The robot is turned off initially, so don't forget to turn it on.
		*/
        Pattern perceptNamePattern = Pattern.compile("\\(\\s*(([^\\s]+)\\s*[^\\s\\d]+).*\\)");
        for (String percept:percepts) {
            Matcher perceptNameMatcher = perceptNamePattern.matcher(percept);
            if (perceptNameMatcher.matches()) {
                String perceptName = perceptNameMatcher.group(1);
                String preceptOrientation = perceptNameMatcher.group(2);
                if (perceptName.equals("HOME")) {
                    Matcher m = Pattern.compile("\\(\\s*HOME\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);

                    if (m.matches()) {
                        System.out.println("robot is at " + m.group(1) + "," + m.group(2));

                        position = new Position(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
                    }
                }
                    if(perceptName.equals("SIZE")) {
                        Matcher m2 = Pattern.compile("\\(\\s*SIZE\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
                        if(m2.matches()) {
                            System.out.println("size is " + m2.group(1) + "," + m2.group(2));

                            size = new Position(Integer.parseInt(m2.group(1)), Integer.parseInt(m2.group(2)));
                        }
                    }
                    if(perceptName.equals("AT DIRT")) {
                        Matcher m3 = Pattern.compile("\\(\\s*AT DIRT\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
                        if(m3.matches()) {
                            System.out.println("dirt is at " + m3.group(1) + "," + m3.group(2));

                            dirtList.add(new Position(Integer.parseInt(m3.group(1)), Integer.parseInt(m3.group(2))));
                            dirtCount++;
                        }

                    }
                    if(perceptName.equals("AT OBSTACLE")) {
                        Matcher m4 = Pattern.compile("\\(\\s*AT OBSTACLE\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
                        if(m4.matches()) {
                            System.out.println("obstacle is at " + m4.group(1) + "," + m4.group(2));

                            obstacleList.add(new Position(Integer.parseInt(m4.group(1)), Integer.parseInt(m4.group(2))));
                        }

                    }
                    if(preceptOrientation.equals("ORIENTATION")) {
                        Matcher m5 = Pattern.compile("\\(\\s*ORIENTATION+\\s*([^\\s]+).*\\)").matcher(percept);
                        if(m5.matches()) {
                            System.out.println("orientation is " + m5.group(1));
                            //adda inni eitthvað
                            switch (m5.group(1)) {
                                case "NORTH":
                                    orientation = Orientation.NORTH;
                                    break;
                                case "SOUTH":
                                    orientation = Orientation.SOUTH;
                                    break;
                                case "EAST":
                                    orientation = Orientation.EAST;
                                    break;
                                default:
                                    orientation = Orientation.WEST;
                            }
                        }
                        
                    }
                }
        }

        Map newMap = new Map(dirtList, obstacleList, size);
        root = new StateNode(new State(position, orientation, newMap, dirtList.size()));
        root.setPathCost(0);
        actions = new LinkedList<>();


        printNodeCheck("ROOT", root);

        // TODO: setja inn algorithma
        DFS();

    }

    public void BFS() {
        LinkedList<StateNode> frontier = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        boolean done = false;

        frontier.add(root);

        while (!frontier.isEmpty()) {
            StateNode tmpNode = frontier.remove();

            visited.add(tmpNode.getState().hashCode());

            List<Action> availableActions = tmpNode.successors();

            for (Action action : availableActions) {

                State childState = new State(
                        tmpNode.getState().getPosition(),
                        tmpNode.getState().getHome(),
                        tmpNode.getState().isOn(),
                        tmpNode.getState().getOrientation(),
                        tmpNode.getState().getDirtCount(),
                        tmpNode.getState().getScore(),
                        tmpNode.getState().getMap(),
                        tmpNode.getState().getDepth()
                );

                childState.executeMove(action);
                StateNode child = new StateNode(childState, action);
                child.setParent(tmpNode);
                if(!visited.contains(childState.hashCode()) && !frontier.contains(child)) {

                    if(childState.goalTest()) {
                        this.actions.add(action);
                        StateNode iter = tmpNode;

                        while (iter.getParent() != null) {
                            this.actions.add(iter.getAction());
                            iter = iter.getParent();
                        }

                        return;
                    }
                    frontier.add(child);
                }
            }
        }

        /**
         * GOOD PRINTS
         * */

        /*
        if(tmpNode.getState().getPosition().getX() == 2 && tmpNode.getState().getPosition().getY() == 3 &&
            tmpNode.getState().getOrientation() == Orientation.WEST)
        {
            printNodeCheck("Child", child);
        }
                    */

        /*
                System.out.println("========    CHILD   ========");
                childState.printStateCheck();
                */

        /*
        System.out.println("======  ACTIONS ======");
        for (Action action : availableActions) {
            System.out.println(action);

        }
        */

        /*
            if(tmpNode.getState().getPosition().getX() == 2 && tmpNode.getState().getPosition().getY() == 3 &&
                    tmpNode.getState().getOrientation() == Orientation.WEST)
            {
                printNodeCheck("Expanding", tmpNode);
            }
            */

            /*
            if(tmpNode.getParent() != null) {
                if(tmpNode.getParent().getAction() == Action.SUCK) {
                    printNodeCheck("Expandee", tmpNode);
                }
            }
            */
    }

    public void DFS() {
        LinkedList<StateNode> frontier = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        frontier.add(root);

        while (!frontier.isEmpty()) {
            StateNode tmpNode = frontier.remove();

            visited.add(tmpNode.getState().hashCode());

            List<Action> availableActions = tmpNode.successors();

            for (Action action : availableActions) {

                State childState = new State(
                        tmpNode.getState().getPosition(),
                        tmpNode.getState().getHome(),
                        tmpNode.getState().isOn(),
                        tmpNode.getState().getOrientation(),
                        tmpNode.getState().getDirtCount(),
                        tmpNode.getState().getScore(),
                        tmpNode.getState().getMap(),
                        tmpNode.getState().getDepth()
                );

                childState.executeMove(action);
                StateNode child = new StateNode(childState, action);
                child.setParent(tmpNode);
                if (!visited.contains(childState.hashCode()) && !frontier.contains(child)) {

                    if (childState.goalTest()) {
                        this.actions.add(action);
                        StateNode iter = tmpNode;

                        while (iter.getParent() != null) {
                            this.actions.add(iter.getAction());
                            iter = iter.getParent();
                        }

                        return;
                    }
                    frontier.addFirst(child);
                }
            }
        }
    }

    public void uniformSearch() {
        PriorityQueue<StateNode> frontier = new PriorityQueue<>();
        Set<Integer> visited = new HashSet<>();

        frontier.add(root);

        while(!frontier.isEmpty()) {
            StateNode tmpNode = frontier.poll();

            visited.add(tmpNode.getState().hashCode());

            if(tmpNode.getState().goalTest()) {
                this.actions.add(tmpNode.getAction());
                while (tmpNode.getParent() != null) {
                    this.actions.add(tmpNode.getAction());
                    tmpNode = tmpNode.getParent();
                }
                return;
            }


/*
            for (StateNode child : tmpNode.successorStates()) {
                if(!frontier.contains(child) && !visited.contains(child.hashCode())) {
                    frontier.add(child);
                } else if (frontier.contains(child)) {
                    for(StateNode node : frontier) {
                        if(node == child) {
                            if (child.getPathCost() < node.getPathCost()) {
                                frontier.remove(node);
                                frontier.add(child);
                                break;
                            }
                        }
                    }
                }

            }
                                */

        }
    }



    public String nextAction(Collection<String> percepts) {
        Action next = this.actions.removeLast();

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

    private void printNodeCheck(String header, StateNode node) {
        System.out.println("=============================");
        System.out.println("          " + header + "    ");
        //System.out.println("Ori before: " + node.getParent().getState().getOrientation());
        System.out.println(node.hashCode());
        if(node.getParent() != null) {
            System.out.println("Parent Action: " + node.getParent().getAction());
        } else {
            System.out.println("Parent Action: NULL");
        }
        System.out.println("Action: " + node.getAction());
        node.getState().printStateCheck();
        node.getState().getMap().printMap(node.getState().getPosition());
        System.out.println("=============================");
    }


}
