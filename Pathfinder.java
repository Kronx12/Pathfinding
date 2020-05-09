/////////// PATHFINDING :
         // Guillaume
         // BAUD
/////////// 42 Lyon

class Node {
    public Node PARENT;
    public double G;
    public double H;
    public double F;
    public int x;
    public int y;

    public Node(int px, int py, Node pPARENT, double pG, double pH) {
        this.PARENT = pPARENT;
        this.G = pG;
        this.H = pH;
        this.F = pG + pH;
        this.x = px;
        this.y = py;
    }

    public boolean compare(Node node) {
        if (this.x == node.x && this.y == node.y)
            return true;
        return false;
    }

    public void describe() {
        System.err.println("======");
        System.err.println("X=" + x + " Y=" + y);
        System.err.println("G=" + G + " H=" + H + " F=" + F);
    }
}

class Pathfinder {
    public ArrayList<Node> OPEN_LIST = new ArrayList<Node>();
    public ArrayList<Node> CLOSE_LIST = new ArrayList<Node>();
    public ArrayList<Node> PATH;
    public String[] map;
    public Node CURRENT_NODE;
    public Node START;
    public Node STOP;

    public Pathfinder(String[] pmap, int start_x, int start_y, int stop_x, int stop_y) {
        map = pmap;
        STOP = new Node(stop_x, stop_y, null, 0, 0);

        START = new Node(start_x, start_y, null, 0, dist(start_x, start_y, stop_x, stop_y));
        OPEN_LIST.add(START);

        PATH = process();
    }

    public ArrayList<Node> process() {
        // MAIN LOOP
        while (OPEN_LIST.size() > 0) {
            CURRENT_NODE = get_lower_f();
            swap_list(CURRENT_NODE);

            add_side_to_open_list();
            if (is_in_open_list(STOP)) {
                ArrayList<Node> path = new ArrayList<Node>();
                Node tmp = get_lower_f();
                while (!tmp.PARENT.compare(START)) {
                    path.add(tmp);
                    tmp = tmp.PARENT;
                }
                System.err.println("PATH FIND");
                return (path);
            }
        }

        // LAST CHECK
        if (is_in_open_list(STOP)) {
            ArrayList<Node> path = new ArrayList<Node>();
            Node tmp = get_lower_f();
            while (!tmp.PARENT.compare(START)) {
                path.add(tmp);
                tmp = tmp.PARENT;
            }
            System.err.println("PATH FIND");
            return (path);
        }
        System.err.println("NO PATH");
        for (int i = 0; i < CLOSE_LIST.size(); i++)
            CLOSE_LIST.get(i).describe();
        return null;
    }

    public void swap_list(Node node) {
        for (int i = 0; i < OPEN_LIST.size(); i++)
            if (OPEN_LIST.get(i).compare(node)) {
                OPEN_LIST.remove(i);
                break;
            }
        CLOSE_LIST.add(node);
    }

    public Node get_lower_f() {
        double tmp = -1;
        Node node = null;
        for (int i = 0; i < OPEN_LIST.size(); i++)
            if (tmp == -1 || OPEN_LIST.get(i).F < tmp) {
                tmp = OPEN_LIST.get(i).F;
                node = OPEN_LIST.get(i);
            }
        return node;
    }

    public void add_side_to_open_list() {
        int[] sides_x = {CURRENT_NODE.x - 1, CURRENT_NODE.x + 1, CURRENT_NODE.x, CURRENT_NODE.x};
        int[] sides_y = {CURRENT_NODE.y, CURRENT_NODE.y, CURRENT_NODE.y - 1, CURRENT_NODE.y + 1};

        for (int i = 0; i < sides_x.length; i++) {
            
            /* TOP BOTTOM */
            if (sides_y[i] < 0 || sides_y[i] >= map.length)
                continue;
            
            /* LEFT RIGHT */
            if (sides_x[i] == -1) sides_x[i] = map[0].length() - 1;
            else if (sides_x[i] == map[0].length()) sides_x[i] = 0;
            else if (sides_x[i] < -1 || sides_x[i] > map[0].length())
                continue;
            
            /* WALL */
            if (map[sides_y[i]].charAt(sides_x[i]) == '#')
                continue;

            Node tmp = new Node(sides_x[i], sides_y[i], CURRENT_NODE, CURRENT_NODE.G + 1, dist(sides_x[i], sides_y[i], STOP.x, STOP.y));
            if (!is_in_close_list(tmp)) // Si pas dans la close_list
                OPEN_LIST.add(tmp);
        }
    }

    public boolean is_in_close_list(Node node) {
        for (int j = 0; j < CLOSE_LIST.size(); j++)
                if ((CLOSE_LIST.get(j).compare(node))) // Si pas dans la close_list
                    return true;
        return false;
    }

    public boolean is_in_open_list(Node node) {
        for (int j = 0; j < OPEN_LIST.size(); j++)
                if ((OPEN_LIST.get(j).compare(node))) // Si pas dans la close_list
                    return true;
        return false;
    }

    public double dist(double x1, double y1, double x2, double y2) {
        return (Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
    }

}