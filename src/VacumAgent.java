import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VacumAgent implements Agent {
    private Queue<Action> actions;
    StateNode root;

    public void init(Collection<String> percepts) {
        State initState = new State();

        int initMap[][] = new int[100][100];
        int dirtCount = 0;
        Position position = new Position(0,0);
        Orientation orientation = Orientation.NORTH;
        List<Position> dirtList = new ArrayList<>();
        List<Position> obstacleList = new ArrayList<>();


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
                    // TODO: brjóta niður DIRT, OBSTACLE
                    if (m.matches()) {
                        System.out.println("robot is at " + m.group(1) + "," + m.group(2));

                        position = new Position(Integer.parseInt(m.group(1)) - 1, Integer.parseInt(m.group(2)) - 1);
                    }
                }
                    if(perceptName.equals("SIZE")) {
                        Matcher m2 = Pattern.compile("\\(\\s*SIZE\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
                        if(m2.matches()) {
                            System.out.println("size is " + m2.group(1) + "," + m2.group(2));
                            // TODO: init stærð kortsinns
                            int x = Integer.parseInt(m2.group(1));
                            int y = Integer.parseInt(m2.group(2));
                            initMap = new int[x][y];
                            for (int i = 0; i < x; i++) {
                                for (int j = 0; j < y; j++) {
                                    initMap[i][j] = 0;
                                }
                            }
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
                            initMap[Integer.parseInt(m4.group(1))][Integer.parseInt(m4.group(2))] = 2;
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

        for(Position pos : dirtList) {
            initMap[pos.getX() - 1][pos.getY() - 1] = 1;
        }

        for(Position pos : obstacleList) {
            initMap[pos.getX() - 1][pos.getY() - 1] = 2;
        }

        root = new StateNode(new State(position, orientation, initMap, dirtCount));
        root.setPathCost(0);
        actions = new LinkedList<>();

        root.getState().printMap();
        //System.out.println(root.getState().getMap()[0][1]);



        // TODO: setja inn algorithma
        BFdumbSearch();

    }

    public void BFdumbSearch() {
        LinkedList<StateNode> frontier = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        boolean done = false;

        frontier.add(root);

        while (!frontier.isEmpty()) {
            StateNode tmpNode = frontier.remove();

            System.out.println("XXXXXXXXXXXXXXXXXXXX");
            for(StateNode i : frontier) {
                System.out.println(i.getAction() + " ");
            }
            System.out.println("XXXXXXXXXXXXXXXXXXXX");

            System.out.println("====================");
            System.out.println("       Expanding    ");
            System.out.println(tmpNode.getAction());
            tmpNode.getState().printStateCheck();

            visited.add(tmpNode.hashCode());





            //tmpNode.getState().printStateCheck();

            for (StateNode child: tmpNode.successorStates()) {
                System.out.println("--------------------");
                System.out.println(child.getAction());
                System.out.println(child.getState().getOrientation());
                child.getState().printStateCheck();

                if(!visited.contains(child.hashCode()) && !frontier.contains(child)) {
                    if(child.getState().goalTest()) {
                        // TODO: returna action lista
                        this.actions.add(child.getAction());
                        while (tmpNode.getParent() != null) {
                            this.actions.add(tmpNode.getAction());
                            tmpNode = tmpNode.getParent();
                        }
                        return;
                    }
                    child.setParent(tmpNode);
                    frontier.add(child);
                }
            }
            System.out.println("=====================");

        }
    }

    public void DFS() {
        LinkedList<StateNode> frontier = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        boolean done = false;

        frontier.add(root);

        while (!frontier.isEmpty()) {
            StateNode tmpNode = frontier.remove();

            visited.add(tmpNode.hashCode());

            for (StateNode child: tmpNode.successorStates()) {

                if(!visited.contains(child.hashCode()) && !frontier.contains(child)) {
                    if(child.getState().goalTest()) {
                        this.actions.add(child.getAction());
                        while (tmpNode.getParent() != null) {
                            this.actions.add(tmpNode.getAction());
                            tmpNode = tmpNode.getParent();
                        }
                        return;
                    }
                    child.setParent(tmpNode);
                    frontier.addFirst(child);
                }
            }
        }
    }

    public void uniformSearch() {
        PriorityQueue<StateNode> frontier = new PriorityQueue<>();
        Set<Integer> visited = new HashSet<>();
        boolean done = false;

        frontier.add(root);

        while(!frontier.isEmpty()) {
            StateNode tmpNode = frontier.remove();
            if(tmpNode.getState().goalTest()) {
                this.actions.add(tmpNode.getAction());
                while (tmpNode.getParent() != null) {
                    this.actions.add(tmpNode.getAction());
                    tmpNode = tmpNode.getParent();
                }
                return;
            }

            visited.add(tmpNode.hashCode());

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



        }
    }



    public String nextAction(Collection<String> percepts) {
        Action next = this.actions.remove();

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
