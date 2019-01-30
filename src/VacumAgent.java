import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.abs;

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

        Environment newMap = new Environment(dirtList, obstacleList, size);
        root = new StateNode(new State(position, orientation, newMap, dirtList.size()));
        root.setPathCost(0);
        actions = new LinkedList<>();


        printNodeCheck("ROOT", root);

        // Uncomment the search algoritham you want to use!!
        //BFS();
        //DFS();
        //uniformSearch();
        Astar();
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

            List<Action> availableActions = tmpNode.successors();

            for (Action action : availableActions) {

                State childState = new State (
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
                    frontier.add(child);
                }            
            }
        }
    }

    public void Astar() {

        // setup for A*
        //HashMap<MapNode,MapNode> parentMap = new HashMap<MapNode,MapNode>();
        Set<Integer> visited = new HashSet<>();

        //Map<MapNode, Double> distances = initializeAllToInfinity();
        Map<Integer, Integer> distances = new HashMap<Integer, Integer>();

        //Queue<MapNode> priorityQueue = initQueue();
        PriorityQueue<StateNode> frontier = new PriorityQueue<>();

        //  enque StartNode, with distance 0
        //startNode.setDistanceToStart(new Double(0));
        // distances.put(startNode, new Double(0));
        //priorityQueue.add(startNode);
        StateNode current = null;

        //  enque StartNode, with distance 0
        root.setDistanceToStart(0);
        distances.put(root.hashCode(), 0);
        frontier.add(root);




        while (!frontier.isEmpty()) {
            current = frontier.remove();

            if (!visited.contains(current.getState().hashCode()) ){
                visited.add(current.getState().hashCode());

                // goal test
                if (current.getState().goalTest()) {
                    reconstructActionList(current);
                    return;
                }

                List<Action> availableActions = current.successors();



                for (Action action : availableActions) {

                    State childState = new State (
                            current.getState().getPosition(),
                            current.getState().getHome(),
                            current.getState().isOn(),
                            current.getState().getOrientation(),
                            current.getState().getDirtCount(),
                            current.getState().getScore(),
                            current.getState().getMap(),
                            current.getState().getDepth()
                    );

                    childState.executeMove(action);
                    StateNode child = new StateNode(childState, action);
                    child.setParent(current);

                    if (!visited.contains(childState.hashCode()) ){

                        /*
                        // calculate predicted distance to the end node
                        double predictedDistance = neighbor.getLocation().distance(endNode.getLocation());

                        // 1. calculate distance to neighbor. 2. calculate dist from start node
                        double neighborDistance = current.calculateDistance(neighbor);
                        double totalDistance = current.getDistanceToStart() + neighborDistance + predictedDistance;
                        */

                        int heuristic = manhattanHeuristic(childState.getPosition(),
                                childState.getHome(), childState.getMap().getDirtLocations().size());

                        /*
                        // check if distance smaller
                        if(totalDistance < distances.get(neighbor) ){
                            // update n's distance
                            distances.put(neighbor, totalDistance);
                            // used for PriorityQueue
                            neighbor.setDistanceToStart(totalDistance);
                            neighbor.setPredictedDistance(predictedDistance);
                            // set parent
                            parentMap.put(neighbor, current);
                            // enqueue
                            priorityQueue.add(neighbor);
                        }
                        */

                        if(!distances.containsKey(child.hashCode())) {
                            distances.put(child.hashCode(), Integer.MAX_VALUE);
                        }

                        if (heuristic < distances.get(child.hashCode())) {
                            // update distance
                            distances.put(child.hashCode(), heuristic);
                            // set parent
                            child.setParent(current);
                            // frontier
                            frontier.add(child);
                        }
                    }
                }
            }
        }

    }

    private int manhattanHeuristic(Position position, Position home, int dirtCount) {
        return abs(position.getX() - home.getX()) + abs(position.getY() - home.getY()) + dirtCount;
    }

    private void reconstructActionList(StateNode end) {
        this.actions.add(end.getAction());
        StateNode iter = end;

        while (iter.getParent() != null) {
            this.actions.add(iter.getAction());
            iter = iter.getParent();
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
        node.getState().getMap().printEnvironment(node.getState().getPosition());
        System.out.println("=============================");
    }


}
