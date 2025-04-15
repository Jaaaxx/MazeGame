import java.awt.*;
import java.util.*;

import tester.*;
import javalib.impworld.*;
import javalib.worldimages.*;

// represents the image rendering of the maze;

class MazeImage {
  Image image; // is the image that represents the maze

  // Constructor that takes in an image and sets it to the image field

  MazeImage(Image image) {
    this.image = image;
  }
}

// represents the weight of the edge between two nodes

class EdgeWeight {
  int weight; // the random weigh of the edge
  Node a; // one node in the edge
  Node b; // the other node in the edge

  // THE ORDER OF THE NODES DOESNT MATTER A -> B IS HE SAME AS B -> A

  // Constructor that takes two nodes and sets them to the a and b fields
  // as well as setting the weight of the edge to a random value between 1 - 100

  EdgeWeight(Node a, Node b) {
    this.a = a;
    this.b = b;
    this.weight = new Random().nextInt(100);
  }

  // Overrides the equals method so that edges that have the same weight and
  // contain the same two nodes (order non dependent) are equal

//  @Override

//  public boolean equals(Object o) {
//    if (o instanceof EdgeWeight) {
//    EdgeWeight other = (EdgeWeight) o;
//      return ((this.a == other.a && this.b == other.b)
//             || (this.a == other.a && this.b == other.a)
//              || (this.a == other.b && this.b == other.b)
//              || (this.a == other.b && this.b == other.a));
//    } else {
//     return false;
//    }
//  }

  // overrides the hashCode method because equals was overridden to set the code
  // to be the product of the hashCodes of a, b and the edge weight

//  @Override

//  public int hashCode() {
//    return this.a.hashCode() * this.b.hashCode();
// }

  int compare(EdgeWeight other) {
    return this.weight - other.weight;
  }

}

// represents a square in a maze

class Node {
  Node left; // left neighbor
  Node right; // right neighbor
  Node up; // up neighbor
  Node down; // down neighbor
  EdgeWeight leftWeight; // the edge connecting this node to its left neighbor
  EdgeWeight rightWeight; // the edge connecting this nodes to its right neighbor
  EdgeWeight upWeight; // the edge connecting this node to its up neighbor
  EdgeWeight downWeight; // the edge connecting this node to its down neighbor
  boolean wallLeft = true; // if this is a wall in the left direction
  boolean wallRight = true; // if there is a wall in the right direction
  boolean wallUp = true; // if there is a wall in the up direction
  boolean wallDown = true; // if there is a wall in the down direction
  Color color = Color.WHITE; // color of a square in the maze
  Color wallColor = Color.BLACK; // color of walls in the maze
  Posn posn; // position of square in the maze (might get rid of)

  // constructor that takes nothing and sets nothing

  Node() {
  }

  // sets the color of the node to be the given value

  void setColor(Color color) {
    this.color = color;
  }

  // sets the color of the wall to be the given value

  void setWallColor(Color wallColor) {
    this.wallColor = wallColor;
  }

  // sets the node using only its neighbor and leaving everything
  // else null (unless already set)

  Node(Node left, Node right, Node up, Node down) {
    this.left = left;
    if (left != null) {
      left.right = this;
    }
    this.right = right;
    if (right != null) {
      right.left = this;
    }
    this.up = up;
    if (up != null) {
      up.down = this;
    }
    this.down = down;
    if (down != null) {
      down.up = this;
    }
  }

  // constructor that takes all values except for edges and colors

  Node(Node left, Node right, Node up, Node down, boolean wallLeft, boolean wallRight,
       boolean wallUp, boolean wallDown) {
    this(left, right, up, down);

    if (left != null) {
      left.wallRight = wallLeft;
    }
    if (right != null) {
      right.wallLeft = wallRight;
    }
    if (up != null) {
      up.wallDown = wallUp;
    }
    if (down != null) {
      down.wallUp = wallDown;
    }

    this.wallLeft = wallLeft;
    this.wallRight = wallRight;
    this.wallUp = wallUp;
    this.wallDown = wallDown;
  }

  // gets the image representation of the maze

  WorldImage getImage() {
    int imageWidth = MazeGame.DISPLAY_WIDTH / MazeGame.GRID_WIDTH;
    int imageHeight = MazeGame.DISPLAY_HEIGHT / MazeGame.GRID_HEIGHT;
    WorldImage baseImage = new RectangleImage(imageWidth, imageHeight,
            "solid", this.color);
    WorldImage wallImageHorizontal = new RectangleImage(imageWidth, imageHeight / 15,
            "solid", this.wallColor);
    WorldImage wallImageVertical = new RectangleImage(imageWidth / 15, imageHeight,
            "solid", this.wallColor);
    if (this.wallUp) {
      baseImage = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.TOP,
              wallImageHorizontal, 0, 0, baseImage);
    }
    if (this.wallDown) {
      baseImage = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM,
              wallImageHorizontal, 0, 0, baseImage);
    }
    if (this.wallLeft) {
      baseImage = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.MIDDLE,
              wallImageVertical, 0, 0, baseImage);
    }
    if (this.wallRight) {
      baseImage = new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.MIDDLE,
              wallImageVertical, 0, 0, baseImage);
    }

    return baseImage;
  }

  // EFFECT: sets the up neighbor to be the given and sets the down neighbor of the given to be
  // the current node, as well as creating a new edge between the up and current node

  // sets the up neighbor and makes an edge between them

  void setUp(Node up) {
    up.down = this;
    this.up = up;
  }

  // EFFECT: sets the down neighbor to be the given and sets the up neighbor of the given to be
  // the current node, as well as creating a new edge between the down and current node

  // sets the down neighbor and makes an edge between them

  void setDown(Node down) {
    down.up = this;
    this.down = down;
  }

  // EFFECT: sets the left neighbor to be the given and sets the right neighbor of the given to be
  // the current node, as well as creating a new edge between the left and current node

  // sets the up neighbor and makes an edge between them

  void setLeft(Node left) {
    left.right = this;
    this.left = left;
  }

  // EFFECT: sets the right neighbor to be the given and sets the left neighbor of the given to be
  // the current node, as well as creating a new edge between the right and current node

  // sets the right neighbor and makes an edge between them

  void setRight(Node right) {
    right.left = this;
    this.right = right;
  }

  void setEdgeUp() {
    EdgeWeight weight = new EdgeWeight(this, this.up);
    this.upWeight = weight;
    this.up.downWeight = weight;
  }

  void setEdgeDown() {
    EdgeWeight weight = new EdgeWeight(this, this.down);
    this.downWeight = weight;
    this.down.upWeight = weight;
  }

  void setEdgeLeft() {
    EdgeWeight weight = new EdgeWeight(this, this.left);
    this.leftWeight = weight;
    this.left.rightWeight = weight;
  }

  void setEdgeRight() {
    EdgeWeight weight = new EdgeWeight(this, this.right);
    this.rightWeight = weight;
    this.right.leftWeight = weight;
  }

  // EFFECT: sets the wallUp field to be true

  void wallUp() {
    if (this.up != null) {
      this.up.wallDown = true;
    }
    this.wallUp = true;
  }

  // EFFECT: sets the wallUp field to be true

  void wallUp(boolean b) {
    this.wallUp = b;
    this.up.wallDown = b;
  }

  // EFFECT: sets the wallDown field to be true

  void wallDown() {
    if (this.down != null) {
      this.down.wallUp = true;
    }
    this.wallDown = true;
  }

  // EFFECT: sets the wallDown field to be true

  void wallDown(boolean b) {
    this.wallDown = b;
    this.down.wallUp = b;
  }

  // EFFECT: sets the wallLeft field to be true

  void wallLeft() {
    if (this.left != null) {
      this.left.wallRight = true;
    }
    this.wallLeft = true;
  }

  // EFFECT: sets the wallLeft field to be true

  void wallLeft(boolean b) {
    this.wallLeft = b;
    this.left.wallRight = b;
  }


  // EFFECT: sets the wallRight field to be true

  void wallRight() {
    if (this.right != null) {
      this.right.wallLeft = true;
    }
    this.wallRight = true;
  }


  // EFFECT: sets the wallRight field to be true

  void wallRight(boolean b) {
    this.wallRight = b;
    this.right.wallLeft = b;
  }



}

// represents an attempted path to finish a maze

class Path {
  boolean reachesEnd; // true if the path reaches the end point of the maze
  final ArrayList<String> moves; // a list of strings that represent the directions of the
  // moves in the path
  final HashSet<Node> visited = new HashSet<>(); // will be 2 more than moves (start and
  // end nodes
  final Stack<Node> pathDFS = new Stack<Node>(); // the path found by a DFS
  final Deque<Node> pathBFS = new ArrayDeque<Node>(); // the path found by a BFS

  // constructor that takes no parameters and initializes the moves ArrayList
  // as well as the reachesEnd to false and

  Path() {
    this.reachesEnd = false;
    this.moves = new ArrayList<>();
  }

  // Constructor that takes a boolean that represents whether or not the end has been reached
  // and the string list of moves and initializes them

  Path(boolean reachesEnd, ArrayList<String> moves) {
    this.reachesEnd = reachesEnd;
    this.moves = moves;
  }

  // EFFECT: adds a string move to the moves ArrayList field

  // add a move to the path

  void addMove(String move) {
    this.moves.add(move);
  }

  // EFFECT: adds a given Node to the visited field

  // adds a visited node to the HashSet of visited nodes in the path

  void addMoveNode(Node moveNode) {
    this.visited.add(moveNode);
  }
}

// represents the functionality of a maze game;

class MazeGame {
  static int GRID_WIDTH = 110; // width of the cells
  static int GRID_HEIGHT = 65; // height of the cells
  static int DISPLAY_WIDTH = 2200; // width of the display
  static int DISPLAY_HEIGHT = 1300; // height of the display

  ArrayList<ArrayList<Node>> maze; // the array list that sets the links for the
  // maze
  int r_val = 0; // the red color value (used for random color generation)
  int g_val = 0; // the green color value (used for random color generation)
  int b_val = 0; // the blue color value (used for random color generation)

  static WorldImage getImage(ArrayList<ArrayList<Node>> maze) {
    Node rowPointer = getBottomLeft(maze);
    WorldImage image = null;

    // Start: last row of the grid
    // End: the first row of the grid
    // What Changes: the row pointer goes up a row
    // becoming the left-most node in the next upwards row
    // Why: so that each row can be represented as an image that sits above the
    // previous

    for (int row = GRID_HEIGHT - 1; row >= 0; row--) {
      Node curr = rowPointer;
      WorldImage rowImage = rowPointer.getImage();

      // Start: the left most column index of the grid
      // End: the right most index of the grid
      // What Changes: cur is set to its right neighbor and a new beside image
      // is added to the row image that was initialized to be the rendered value of
      // the first node in the row
      // Why: so a complete row of the maze can be rendered

      for (int col = 0; col < GRID_WIDTH - 1; col++) {
        curr = curr.right;
        rowImage = new BesideImage(rowImage, curr.getImage());
      }

      if (image == null) {
        image = rowImage;
      } else {
        image = new AboveImage(rowImage, image);
      }

      rowPointer = rowPointer.up;
    }

    return image;
  }

  // gets the completed image of the maze

  WorldImage getImage() {
    return getImage(this.maze);
  }

  // EFFECT: sets the maze field top be the result of the initializeMaze static method

  // initializes a random maze and sets the color of the nodes to be
  // generally similar pale color to get a cool look

  void loadLevel(String genMethod) {
    this.maze = initializeMaze(GRID_WIDTH, GRID_HEIGHT, genMethod);
    this.randomizeMazeColors();
  }

  // Creates a maze, setting its links (neighbors, edges, walls), creating its boarders,
  // and setting up its walls

  // CAN USE FUNCTION OBJECTS HERE TO ABSTRACT IMPLEMENTATION FOR RANDOM AND CORRECT MAZES
  // GO BACK *******

  static ArrayList<ArrayList<Node>> initializeMaze(int width, int height,
                                                   String genMethod) {
    ArrayList<ArrayList<Node>> maze = createGrid(width, height);
    setInitialLinks(maze);
    if (genMethod.equals("random")) {
      createMazeBruteForce(maze, 5);
    } else {
      createTrueMaze(maze, algoForTrueMaze(getHashMapNodes(maze),
              makeEdgeGraph(maze),
              width * height));
    }
    setMazeBorders(maze);
    return maze;
  }

  // static method that creates a grid of cells in a maze with a given width and height

  static ArrayList<ArrayList<Node>> createGrid(int width, int height) {
    ArrayList<ArrayList<Node>> maze = new ArrayList<>();

    // Start: a row index of 0
    // End: height - 1
    // What Changes: a new empty row is added to the maze
    // Why: so that a 2d ArrayList of nodes can be created to represent to
    // maze and links can be set from there

    for (int row = 0; row < height; row++) {
      maze.add(new ArrayList<>());

      // Start: a column index of 0
      // End: width - 1
      // What Changes: a new node is made with the position (column, row)
      // and this is added to given row of the maze
      // Why: so that each row of the maze can be created in nodes


      for (int col = 0; col < width; col++) {
        Node n = new Node();
        n.posn = new Posn(col, row);
        maze.get(row).add(n);
      }
    }

    return maze;
  }

  // gets a random order of directions for the brute force solving method

  static ArrayList<String> getRandomDirectionOrder() {
    ArrayList<String> dirs = new ArrayList<>();
    dirs.add("left");
    dirs.add("right");
    dirs.add("up");
    dirs.add("down");
    Collections.shuffle(dirs);
    return dirs;
  }

  // the helper method for the DFS through the maze
  // Starts trying the left, right, up, down (in that order)
  // and if none of those has a valid node next to it (no wall and not
  // already visited) then it goes back to the last node with valid neighbors
  // to traverse through

  // when one path is taken it recursively does the same algorithm with the new current
  // set and the path getting added to until it must go back and try a new path
  // (in which things are removed from the path) or it reaches the end in
  // which it returns the path


  private Path pathDFSHelper(Path path, Node current, Node goal) {
    if (current == goal) {
      return path;
    }

    if (!current.wallLeft && current.left != null && !path.visited.contains(current.left)) {
      path.visited.add(current.left);
      path.pathDFS.push(current.left);
      path.addMove("left");

      Path res = pathDFSHelper(path, current.left, goal);
      if (res != null) {
        return res;
      }
      path.moves.remove(path.moves.size() - 1);
      path.pathDFS.pop();
    }

    if (!current.wallRight && current.right != null && !path.visited.contains(current.right)) {
      path.visited.add(current.right);
      path.pathDFS.push(current.right);
      path.addMove("right");

      Path res = pathDFSHelper(path, current.right, goal);
      if (res != null) {
        return res;
      }
      path.moves.remove(path.moves.size() - 1);
      path.pathDFS.pop();
    }

    if (!current.wallUp && current.up != null && !path.visited.contains(current.up)) {
      path.visited.add(current.up);
      path.pathDFS.push(current.up);
      path.addMove("up");

      Path res = pathDFSHelper(path, current.up, goal);
      if (res != null) {
        return res;
      }
      path.moves.remove(path.moves.size() - 1);
      path.pathDFS.pop();
    }

    if (!current.wallDown && current.down != null && !path.visited.contains(current.down)) {
      path.visited.add(current.down);
      path.pathDFS.push(current.down);
      path.addMove("down");

      Path res = pathDFSHelper(path, current.down, goal);
      if (res != null) {
        return res;
      }
      path.moves.remove(path.moves.size() - 1);
      path.pathDFS.pop();
    }

    return null;
  }

  // helper method for reconstructing the found path for bfs search and removing non-path nodes
  private Path reconstructPathBFS(Path path, Map<Node, Node> cameFrom, Node start, Node end) {
    LinkedList<Node> nodeOrder = new LinkedList<>();
    Node current = end;

    while (current != null) {
      nodeOrder.addFirst(current);
      if (current == start) {
        break;
      }
      current = cameFrom.get(current);
    }

    for (int i = 0; i < nodeOrder.size() - 1; i++) {
      Node n1 = nodeOrder.get(i);
      Node n2 = nodeOrder.get(i + 1);
      if (n2 == n1.left) {
        path.addMove("left");
      } else if (n2 == n1.right) {
        path.addMove("right");
      } else if (n2 == n1.up) {
        path.addMove("up");
      } else if (n2 == n1.down) {
        path.addMove("down");
      }
    }

    for (Node n : nodeOrder) {
      path.addMoveNode(n);
    }

    path.reachesEnd = true;
    return path;
  }


  // moves a player by setting returning the node in the
  // given direction if it is valid

  Node movePlayerPath(Node pos, String dir) {
    if (dir.equals("left")) {
      if (!pos.wallLeft) {
        pos = pos.left;
      }
    } else if (dir.equals("right")) {
      if (!pos.wallRight) {
        pos = pos.right;
      }
    } else if (dir.equals("up")) {
      if (!pos.wallUp) {
        pos = pos.up;
      }
    } else if (dir.equals("down")) {
      if (!pos.wallDown) {
        pos = pos.down;
      }
    }
    return pos;
  }

  // calls the depth first search helper with the top left as the current,
  // and a path with only current in its visited as the inputed path

  Path getValidPathDepthFirst() {
    Node start = getTopLeft(this.maze);
    Node goal = getBottomRight(this.maze);

    Path path = new Path();
    path.addMoveNode(start);

    return pathDFSHelper(path, start, goal);
  }

  void pathFindAndColor(String algo) {
    Path foundPath;
    if (algo.equals("breadthFirst")) {
      foundPath = getValidPathBreadthFirst();
    } else if (algo.equals("depthFirst")) {
      foundPath = getValidPathDepthFirst();
    } else {
      // Default to brute force pathfinding
      foundPath = getValidPathBruteForce();
    }
    this.colorPath(foundPath);
  }

  // gets the valid breadth first path
  Path getValidPathBreadthFirst() {
    Node start = getTopLeft(this.maze);
    Node end = getBottomRight(this.maze);

    Path path = new Path();
    path.visited.add(start);
    path.pathBFS.offer(start);

    Map<Node, Node> cameFrom = new HashMap<>();

    while (!path.pathBFS.isEmpty()) {
      Node current = path.pathBFS.getFirst();
      path.pathBFS.removeFirst();

      if (current == end) {
        return reconstructPathBFS(path, cameFrom, start, end);
      }

      if (!current.wallLeft && current.left != null && !path.visited.contains(current.left)) {
        path.visited.add(current.left);
        cameFrom.put(current.left, current);
        path.pathBFS.add(current.left);
      }
      if (!current.wallRight && current.right != null && !path.visited.contains(current.right)) {
        path.visited.add(current.right);
        cameFrom.put(current.right, current);
        path.pathBFS.add(current.right);
      }
      if (!current.wallUp && current.up != null && !path.visited.contains(current.up)) {
        path.visited.add(current.up);
        cameFrom.put(current.up, current);
        path.pathBFS.add(current.up);
      }
      if (!current.wallDown && current.down != null && !path.visited.contains(current.down)) {
        path.visited.add(current.down);
        cameFrom.put(current.down, current);
        path.pathBFS.add(current.down);
      }
    }

    return null;
  }

  // tries 10000 times to get a valid path via brute force by using
  // randomized directions (not part of the assignment we just thought it was
  // cool so it will be a little less marked up)

  Path getValidPathBruteForce() {
    Node cur = getTopLeft(this.maze);
    int foundPaths = 0;
    Path path;

    while (foundPaths++ < 100000 && cur != getBottomRight(this.maze)) {
      path = new Path();
      cur = getTopLeft(this.maze);
      while (cur != getBottomRight(this.maze)) {
        path.addMoveNode(cur);
        ArrayList<String> dirs = getRandomDirectionOrder();

        boolean stuck = true;
        while (!dirs.isEmpty() && stuck) {
          String dir = dirs.remove(dirs.size() - 1);
          if (dir.equals("left")) {
            if (!cur.wallLeft && !path.visited.contains(cur.left)) {
              cur = cur.left;
              path.addMove(dir);
              stuck = false;
            }
          } else if (dir.equals("right")) {
            if (!cur.wallRight && !path.visited.contains(cur.right)) {
              cur = cur.right;
              path.addMove(dir);
              stuck = false;
            }
          } else if (dir.equals("up")) {
            if (!cur.wallUp && !path.visited.contains(cur.up)) {
              cur = cur.up;
              path.addMove(dir);
              stuck = false;
            }
          } else if (dir.equals("down")) {
            if (!cur.wallDown && !path.visited.contains(cur.down)) {
              cur = cur.down;
              path.addMove(dir);
              stuck = false;
            }
          }
        }
        if (stuck) {
          break;
        }
      }
      if (cur == getBottomRight(maze)) {
        path.reachesEnd = true;
        return path;
      }
    }
    return null;
  }

  // sets the colors of the nodes and walls in a maze to be all red and light pink
  // if no valid path could be found and sets the nodes in the path to be red if one
  // is found

  void colorPath(Path path) {
    if (path == null) {
      setNodeColors(this.maze, 150, 100, 100);
      setWallColors(this.maze, 120, 0, 0);
    } else {
      ArrayList<String> dirs = path.moves;
      Node cur = getTopLeft(this.maze);
      cur.setColor(Color.YELLOW);
      while (!dirs.isEmpty()) {
        String dir = dirs.remove(0);
        if (dir.equals("left")) {
          cur = cur.left;
        } else if (dir.equals("right")) {
          cur = cur.right;
        } else if (dir.equals("up")) {
          cur = cur.up;
        } else if (dir.equals("down")) {
          cur = cur.down;
        }
        cur.setColor(Color.RED);
      }
      cur.setColor(Color.GREEN);
    }
  }

  // resets the path to its original colors and the wall colors to be black
  // not private, because path reset is a function of a key
  void resetPath() {
    setRandomColors(this.maze, this.r_val, this.g_val, this.b_val);
    setWallColors(this.maze, 0, 0, 0);
  }

  // Sets custom, randomized, light background colors to differentiate mazes

  private void randomizeMazeColors() {
    int r = new Random().nextInt(105) + 125;
    int g = new Random().nextInt(105) + 125;
    int b = new Random().nextInt(105) + 125;

    this.r_val = r;
    this.g_val = g;
    this.b_val = b;

    setRandomColors(maze, r, g, b);
  }

  // Sets custom, randomized, light background colors to differentiate mazes

  private static void setRandomColors(ArrayList<ArrayList<Node>> maze, int r, int g, int b) {
    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = maze.get(row).get(col);
        if (row == 0 && col == 0) {
          cur.setColor(Color.YELLOW);
        } else if (row == GRID_HEIGHT - 1 && col == GRID_WIDTH - 1) {
          cur.setColor(Color.GREEN);
        } else {
          cur.setColor(new Color(new Random().nextInt(15) + r,
                  new Random().nextInt(15) + g, new Random().nextInt(15) + b));
        }
      }
    }
  }

  // sets the colors of all of the nodes in the maze

  private static void setNodeColors(ArrayList<ArrayList<Node>> maze, int r, int g, int b) {
    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = maze.get(row).get(col);
        cur.setColor(new Color(r, g, b));
      }
    }
  }

  // sets the colors of all of the walls in the maze

  private static void setWallColors(ArrayList<ArrayList<Node>> maze, int r, int g, int b) {
    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = maze.get(row).get(col);
        cur.setWallColor(new Color(r, g, b));
      }
    }
  }

  // sets the borders of the maze (the walls on the outside)

  private static void setMazeBorders(ArrayList<ArrayList<Node>> maze) {
    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = maze.get(row).get(col);
        if (row == 0) {
          cur.wallUp();
        }
        if (col == 0) {
          cur.wallLeft();
        }
        if (row == GRID_HEIGHT - 1) {
          cur.wallDown();
        }
        if (col == GRID_WIDTH - 1) {
          cur.wallRight();
        }
      }
    }
  }

  private static void clearWalls(ArrayList<ArrayList<Node>> maze) {
    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = maze.get(row).get(col);
        cur.wallUp = false;
        cur.wallLeft = false;
        cur.wallDown = false;
        cur.wallRight = false;
      }
    }
  }

  // creates a random maze (not well formed but used to test our rendering and basic
  // functionality)

  private static void createMazeBruteForce(ArrayList<ArrayList<Node>> maze, int randomness) {
    clearWalls(maze);
    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = maze.get(row).get(col);
        if (new Random().nextInt(randomness) == 1) {
          cur.wallUp();
        }
        if (new Random().nextInt(randomness) == 1) {
          cur.wallLeft();
        }
        if (new Random().nextInt(randomness) == 1) {
          cur.wallDown();
        }
        if (new Random().nextInt(randomness) == 1) {
          cur.wallRight();
        }
      }
    }
  }

  private static void createTrueMaze(ArrayList<ArrayList<Node>> maze, HashSet<EdgeWeight> edges) {
    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = maze.get(row).get(col);
        if (edges.contains(cur.upWeight)) {
          cur.wallUp(false);
        }
        if (edges.contains(cur.leftWeight)) {
          cur.wallLeft(false);
        }
        if (edges.contains(cur.downWeight)) {
          cur.wallDown(false);
        }
        if (edges.contains(cur.rightWeight)) {
          cur.wallRight(false);
        }
      }
    }
  }

  private static HashSet<EdgeWeight> algoForTrueMaze(HashMap<Node, Node> nodes,
                                             ArrayList<EdgeWeight> edges,
                                             int numNodes) {
    HashSet<EdgeWeight> finalSet = new HashSet<EdgeWeight>();
    int i = 1;
    while (i < numNodes) {
      EdgeWeight e = edges.get(0);
      if (nodes.containsKey(e.a) && nodes.get(e.a).equals(e.a)) {
        nodes.replace(e.a, e.a, e.b);
        finalSet.add(e);
        i++;
      }
      edges.remove(0);
    }

    return finalSet;
  }

  // EFFECT: sets the left, right, up, and down fields of the nodes in the maze
  // creates and sets the edge weights

  // sets the neighbors and edge weights of all of the nodes in the maze

  private static void setInitialLinks(ArrayList<ArrayList<Node>> maze) {
    // sets horizontal links
    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 1; col < GRID_WIDTH - 1; col++) {
        Node cur = maze.get(row).get(col);
        cur.setLeft(maze.get(row).get(col - 1));
        cur.setRight(maze.get(row).get(col + 1));
      }
    }

    // sets vertical links
    for (int row = 1; row < GRID_HEIGHT - 1; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = maze.get(row).get(col);
        cur.setUp(maze.get(row - 1).get(col));
        cur.setDown(maze.get(row + 1).get(col));
      }
    }

    // edge weights
    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = maze.get(row).get(col);
        if (row < GRID_HEIGHT - 1) {
          cur.setEdgeDown();
        }
        if (col < GRID_WIDTH - 1) {
          cur.setEdgeRight();
        }
      }
    }

  }

  // gets all of the unique edge weights in the maze, sorts them, and returns them as a
  // HashMap with identical pairings

  private static ArrayList<EdgeWeight> makeEdgeGraph(ArrayList<ArrayList<Node>> maze) {

    // gets all of the unique pairings *********************************************

    ArrayList<EdgeWeight> edges = new ArrayList<EdgeWeight>();

    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = maze.get(row).get(col);
        if (row < GRID_HEIGHT - 1) {
          edges.add(cur.downWeight);
        }
        if (col < GRID_WIDTH - 1) {
          edges.add(cur.rightWeight);
        }
      }
    }

    // sorts them from lowest to highest using sort **************************

    edges.sort(new CompareEdgeWeight());

    return edges;
  }

  private static HashMap<Node, Node> getHashMapNodes(ArrayList<ArrayList<Node>> maze) {
    HashMap<Node, Node> nodes = new HashMap<Node, Node>();

    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = maze.get(row).get(col);
        nodes.put(cur, cur);
      }
    }
    return nodes;
  }

  // gets the bottom left node in the maze

  private static Node getBottomLeft(ArrayList<ArrayList<Node>> maze) {
    return maze.get(GRID_HEIGHT - 1).get(0);
  }

  // gets the top left node in the maze

  static Node getTopLeft(ArrayList<ArrayList<Node>> maze) {
    return maze.get(0).get(0);
  }

  // gets the bottom right node in the maze

  static Node getBottomRight(ArrayList<ArrayList<Node>> maze) {
    return maze.get(GRID_HEIGHT - 1).get(GRID_WIDTH - 1);
  }

}

// handles the big bang aspects of the maze game

class MazeWorld extends World {
  MazeGame mazeGame; // the maze game that holds all of the game info and
  // functionality
  Node playerPos; // current node of the player

  // constructor

  MazeWorld() {
    this.mazeGame = new MazeGame(); // makes a new mazeGame to play
    this.mazeGame.loadLevel("not random"); // loads the level to a starting state
    this.playerPos = MazeGame.getTopLeft(this.mazeGame.maze); // puts the player in the
    // top left corner of the maze
    this.playerPos.setColor(Color.CYAN); // sets the players node color to be blue
  }

  // makes a world scene that displays the current image representation of the maze\

  @Override
  public WorldScene makeScene() {
    WorldImage image = this.mazeGame.getImage();

    WorldScene ws = new WorldScene(MazeGame.DISPLAY_WIDTH, MazeGame.DISPLAY_HEIGHT);
    ws.placeImageXY(image, MazeGame.DISPLAY_WIDTH / 2, MazeGame.DISPLAY_HEIGHT / 2);
    return ws;
  }

  // key handler (I will comment more in the actual method as to what each thing does)

  @Override
  public void onKeyEvent(String s) {
    if (s.equals(" ")) {
      this.mazeGame.loadLevel("not random"); // every time space is hit it loads a new level

      this.playerPos = MazeGame.getTopLeft(this.mazeGame.maze);
      this.playerPos.setColor(Color.CYAN);
    }
    if (s.equals("k")) {
      this.mazeGame.loadLevel("random");

      this.playerPos = MazeGame.getTopLeft(this.mazeGame.maze);
      this.playerPos.setColor(Color.CYAN);
    }
    if (s.equals("b")) {
      this.mazeGame.resetPath();
      this.mazeGame.pathFindAndColor("breadthFirst");
    }
    if (s.equals("r")) {
      this.mazeGame.resetPath();
      this.mazeGame.pathFindAndColor("breadthFirst");
    }
    if (s.equals("d")) {
      this.mazeGame.resetPath();
      this.mazeGame.pathFindAndColor("breadthFirst");
    }
    if (s.equals("right") || s.equals("down") || s.equals("left") || s.equals("up")) {
      this.mazeGame.resetPath(); // gets rid of the old players path so that it is no longer blue
      this.playerPos = this.mazeGame.movePlayerPath(this.playerPos, s);

      if (this.playerPos == MazeGame.getBottomRight(this.mazeGame.maze)) {
        this.mazeGame.colorPath(null); // shows a final scene
      } else {
        this.playerPos.setColor(Color.CYAN);
      }
    }
  }
}

// comparator for edge weights used to quickSort

class CompareEdgeWeight implements Comparator<EdgeWeight> {

  // if negative than a < b
  // if 0 then they are of the same weight
  // if positive a > b

  public int compare(EdgeWeight a, EdgeWeight b) {
    return a.compare(b);
  }

}

class Examples {
  public void testGame(Tester t) {
    World mw = new MazeWorld();
    mw.bigBang(MazeGame.DISPLAY_WIDTH, MazeGame.DISPLAY_HEIGHT, 1);
  }
}

