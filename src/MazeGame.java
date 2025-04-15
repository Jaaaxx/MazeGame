import java.awt.*;
import java.util.*;

import tester.*;
import javalib.impworld.*;
import javalib.worldimages.*;


// represents the weight of the edge between two nodes
class EdgeWeight {
  private final int weight; // the random weight of the edge

  public final Node a; // one node in the edge
  public final Node b; // the other node in the edge


  // Constructor that takes two nodes and sets them to the a and b fields
  // as well as setting the weight of the edge to a random value between 1 - 100
  // THE ORDER OF THE NODES DOESNT MATTER A -> B IS HE SAME AS B -> A
  EdgeWeight(Node a, Node b) {
    this.a = a;
    this.b = b;
    this.weight = new Random().nextInt(100);
  }

  // Compares the nodes against each other
  public int compare(EdgeWeight other) {
    return this.weight - other.weight;
  }
}

// represents a square in a maze
class Node {
  private Node left; // left neighbor (private b/c we want developers to use custom setter)
  private Node right; // right neighbor (private b/c we want developers to use custom setter)
  private Node up; // up neighbor (private b/c we want developers to use custom setter)
  private Node down; // down neighbor (private b/c we want developers to use custom setter)
  private boolean wallLeft = true; // if this is a wall in the left direction
  private boolean wallRight = true; // if there is a wall in the right direction
  private boolean wallUp = true; // if there is a wall in the up direction
  private boolean wallDown = true; // if there is a wall in the down direction
  private EdgeWeight leftWeight; // the edge connecting this node to its left neighbor
  private EdgeWeight rightWeight; // the edge connecting this nodes to its right neighbor
  private EdgeWeight upWeight; // the edge connecting this node to its up neighbor
  private EdgeWeight downWeight; // the edge connecting this node to its down neighbor

  public Color color = Color.WHITE; // color of a square in the maze
  public Color wallColor = Color.BLACK; // color of walls in the maze


  // gets the image representation of the maze
  public WorldImage getImage() {
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

  // Gets the up node
  public Node getUp() {
    return this.up;
  }

  // Gets the right node
  public Node getRight() {
    return this.right;
  }

  // Gets the down node
  public Node getDown() {
    return this.down;
  }

  // Gets the left node
  public Node getLeft() {
    return this.left;
  }

  // Gets the inverse of wallLeft
  public boolean noWallLeft() {
    return !this.wallLeft;
  }

  // Gets the inverse of wallUp
  public boolean noWallUp() {
    return !this.wallUp;
  }

  // Gets the inverse of wallDown
  public boolean noWallDown() {
    return !this.wallDown;
  }

  // Gets the inverse of wallRight
  public boolean noWallRight() {
    return !this.wallRight;
  }

  // Gets the leftWeight
  public EdgeWeight getLeftWeight() {
    return this.leftWeight;
  }

  // Gets the rightWeight
  public EdgeWeight getRightWeight() {
    return this.rightWeight;
  }

  // Gets the downWeight
  public EdgeWeight getDownWeight() {
    return this.downWeight;
  }

  // Gets the upWeight
  public EdgeWeight getUpWeight() {
    return this.upWeight;
  }

  // EFFECT: sets the up neighbor to be the given and sets the down neighbor of the given to be
  // the current node, as well as creating a new edge between the up and current node
  public void setUp(Node up) {
    if (up != null) {
      up.down = this;
    }
    this.up = up;
  }

  // EFFECT: sets the down neighbor to be the given and sets the up neighbor of the given to be
  // the current node, as well as creating a new edge between the down and current node
  public void setDown(Node down) {
    if (down != null) {
      down.up = this;
    }
    this.down = down;
  }

  // EFFECT: sets the left neighbor to be the given and sets the right neighbor of the given to be
  // the current node, as well as creating a new edge between the left and current node
  public void setLeft(Node left) {
    if (left != null) {
      left.right = this;
    }
    this.left = left;
  }

  // EFFECT: sets the right neighbor to be the given and sets the left neighbor of the given to be
  // the current node, as well as creating a new edge between the right and current node
  public void setRight(Node right) {
    if (right != null) {
      right.left = this;
    }
    this.right = right;
  }

  // Sets the weight of the down edge
  public void setEdgeDown() {
    EdgeWeight weight = new EdgeWeight(this, this.down);
    this.downWeight = weight;
    if (this.down != null) {
      this.down.upWeight = weight;
    }
  }

  // Sets the weight of the right edge
  public void setEdgeRight() {
    EdgeWeight weight = new EdgeWeight(this, this.right);
    this.rightWeight = weight;
    if (this.right != null) {
      this.right.leftWeight = weight;
    }
  }


  // EFFECT: sets the wallUp field to b
  public void wallUp(boolean b) {
    this.wallUp = b;
    if (this.up != null) {
      this.up.wallDown = b;
    }
  }

  // EFFECT: sets the wallRight field to b
  public void wallRight(boolean b) {
    this.wallRight = b;
    if (this.right != null) {
      this.right.wallLeft = b;
    }
  }

  // EFFECT: sets the wallDown field to b
  public void wallDown(boolean b) {
    this.wallDown = b;
    if (this.down != null) {
      this.down.wallUp = b;
    }
  }

  // EFFECT: sets the wallLeft field to b
  public void wallLeft(boolean b) {
    this.wallLeft = b;
    if (this.left != null) {
      this.left.wallRight = b;
    }
  }
}

// represents an attempted path to finish a maze
class Path {
  public boolean reachesEnd = false; // true if the path reaches the end point of the maze
  public final ArrayList<String> moves = new ArrayList<>(); // represents the directions of moves
  public final HashSet<Node> visited = new HashSet<>(); // represents visited nodes
  public final Stack<Node> pathDFS = new Stack<>(); // the path found by a DFS
  public final Deque<Node> pathBFS = new ArrayDeque<>(); // the path found by a BFS
}

// represents the functionality of a maze game;
class MazeGame {
  public static final int GRID_WIDTH = 50; // width of the cells
  public static final int GRID_HEIGHT = 50; // height of the cells
  public static final int DISPLAY_WIDTH = 1300; // width of the display
  public static final int DISPLAY_HEIGHT = 1300; // height of the display

  private Node playerPos; // player position node
  private ArrayList<ArrayList<Node>> maze; // the array list that sets the maze links
  private int r_val = 0; // the red color value (used for random color generation)
  private int g_val = 0; // the green color value (used for random color generation)
  private int b_val = 0; // the blue color value (used for random color generation)

  // Constructor
  MazeGame() {
    this.loadLevel("not random"); // loads the level to a starting state
  }


  // Gets the WorldImage from a given maze
  private WorldImage getImageFromMaze() {
    Node rowPointer = this.getBottomLeft();
    WorldImage image = null;

    for (int row = GRID_HEIGHT - 1; row >= 0; row--) {
      Node curr = rowPointer;
      WorldImage rowImage = rowPointer.getImage();
      for (int col = 0; col < GRID_WIDTH - 1; col++) {
        curr = curr.getRight();
        rowImage = new BesideImage(rowImage, curr.getImage());
      }
      if (image == null) {
        image = rowImage;
      } else {
        image = new AboveImage(rowImage, image);
      }
      rowPointer = rowPointer.getUp();
    }
    return image;
  }


  // Creates a maze, setting its links (neighbors, edges, walls), creating its boarders,
  // and setting up its walls
  private void initializeMaze(String genMethod) {
    createGrid();
    setInitialLinks();
    if (genMethod.equals("random")) {
      createMazeBruteForce();
    } else {
      createTrueMaze(algoForTrueMaze(getHashMapNodes(), makeEdgeGraph()));
    }
    setMazeBorders();
  }

  // creates a grid of cells in a maze with a given width and height
  private void createGrid() {
    this.maze = new ArrayList<>();
    // Start: a row index of 0
    // End: height - 1
    // What Changes: a new empty row is added to the maze
    // Why: so that a 2d ArrayList of nodes can be created to represent to
    // maze and links can be set from there

    for (int row = 0; row < MazeGame.GRID_HEIGHT; row++) {
      this.maze.add(new ArrayList<>());

      // Start: a column index of 0
      // End: width - 1
      // What Changes: a new node is made with the position (column, row)
      // and this is added to given row of the maze
      // Why: so that each row of the maze can be created in nodes


      for (int col = 0; col < MazeGame.GRID_WIDTH; col++) {
        Node n = new Node();
        this.maze.get(row).add(n);
      }
    }
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

    if (current.noWallLeft() && current.getLeft() != null && !path.visited.contains(current.getLeft())) {
      path.visited.add(current.getLeft());
      path.pathDFS.push(current.getLeft());
      path.moves.add("left");

      Path res = pathDFSHelper(path, current.getLeft(), goal);
      if (res != null) {
        return res;
      }
      path.moves.removeLast();
      path.pathDFS.pop();
    }

    if (current.noWallRight() && current.getRight() != null && !path.visited.contains(current.getRight())) {
      path.visited.add(current.getRight());
      path.pathDFS.push(current.getRight());
      path.moves.add("right");

      Path res = pathDFSHelper(path, current.getRight(), goal);
      if (res != null) {
        return res;
      }
      path.moves.removeLast();
      path.pathDFS.pop();
    }

    if (current.noWallUp() && current.getUp() != null && !path.visited.contains(current.getUp())) {
      path.visited.add(current.getUp());
      path.pathDFS.push(current.getUp());
      path.moves.add("up");

      Path res = pathDFSHelper(path, current.getUp(), goal);
      if (res != null) {
        return res;
      }
      path.moves.removeLast();
      path.pathDFS.pop();
    }

    if (current.noWallDown() && current.getDown() != null && !path.visited.contains(current.getDown())) {
      path.visited.add(current.getDown());
      path.pathDFS.push(current.getDown());
      path.moves.add("down");

      Path res = pathDFSHelper(path, current.getDown(), goal);
      if (res != null) {
        return res;
      }
      path.moves.removeLast();
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
      if (n2 == n1.getLeft()) {
        path.moves.add("left");
      } else if (n2 == n1.getRight()) {
        path.moves.add("right");
      } else if (n2 == n1.getUp()) {
        path.moves.add("up");
      } else if (n2 == n1.getDown()) {
        path.moves.add("down");
      }
    }

    path.visited.addAll(nodeOrder);

    path.reachesEnd = true;
    return path;
  }


  // calls the depth first search helper with the top left as the current,
  // and a path with only current in its visited as the inputed path

  private Path getValidPathDepthFirst() {
    Node start = this.getTopLeft();
    Node goal = this.getBottomRight();

    Path path = new Path();
    path.visited.add(start);

    return pathDFSHelper(path, start, goal);
  }


  // gets the valid breadth first path
  private Path getValidPathBreadthFirst() {
    Node start = this.getTopLeft();
    Node end = this.getBottomRight();

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

      if (current.noWallLeft() && current.getLeft() != null && !path.visited.contains(current.getLeft())) {
        path.visited.add(current.getLeft());
        cameFrom.put(current.getLeft(), current);
        path.pathBFS.add(current.getLeft());
      }
      if (current.noWallRight() && current.getRight() != null && !path.visited.contains(current.getRight())) {
        path.visited.add(current.getRight());
        cameFrom.put(current.getRight(), current);
        path.pathBFS.add(current.getRight());
      }
      if (current.noWallUp() && current.getUp() != null && !path.visited.contains(current.getUp())) {
        path.visited.add(current.getUp());
        cameFrom.put(current.getUp(), current);
        path.pathBFS.add(current.getUp());
      }
      if (current.noWallDown() && current.getDown() != null && !path.visited.contains(current.getDown())) {
        path.visited.add(current.getDown());
        cameFrom.put(current.getDown(), current);
        path.pathBFS.add(current.getDown());
      }
    }

    return null;
  }


  // displays a game over screen once the manual end of maze has been found
  private void gameOverScreen() {
    this.colorPath(null);
  }

  // tries 10000 times to get a valid path via brute force by using
  // randomized directions (not part of the assignment we just thought it was
  // cool so it will be a little less marked up)

  private Path getValidPathBruteForce() {
    Node cur = this.getTopLeft();
    int foundPaths = 0;
    Path path;

    while (foundPaths++ < 100000 && cur != this.getBottomRight()) {
      path = new Path();
      cur = this.getTopLeft();
      while (cur != this.getBottomRight()) {
        path.visited.add(cur);

        // Randomizes direction order
        ArrayList<String> dirs = new ArrayList<>();
        dirs.add("left");
        dirs.add("right");
        dirs.add("up");
        dirs.add("down");
        Collections.shuffle(dirs);

        boolean stuck = true;
        while (!dirs.isEmpty() && stuck) {
          String dir = dirs.removeLast();
          if (dir.equals("left")) {
            if (cur.noWallLeft() && !path.visited.contains(cur.getLeft())) {
              cur = cur.getLeft();
              path.moves.add(dir);
              stuck = false;
            }
          } else if (dir.equals("right")) {
            if (cur.noWallRight() && !path.visited.contains(cur.getRight())) {
              cur = cur.getRight();
              path.moves.add(dir);
              stuck = false;
            }
          } else if (dir.equals("up")) {
            if (cur.noWallUp() && !path.visited.contains(cur.getUp())) {
              cur = cur.getUp();
              path.moves.add(dir);
              stuck = false;
            }
          } else if (dir.equals("down")) {
            if (cur.noWallDown() && !path.visited.contains(cur.getDown())) {
              cur = cur.getDown();
              path.moves.add(dir);
              stuck = false;
            }
          }
        }
        if (stuck) {
          break;
        }
      }
      if (cur == this.getBottomRight()) {
        path.reachesEnd = true;
        return path;
      }
    }
    return null;
  }

  // sets the colors of the nodes and walls in a maze to be all red and light pink
  // if no valid path could be found and sets the nodes in the path to be red if one
  // is found

  private void colorPath(Path path) {
    if (path == null) {
      setNodeColors();
      setWallColors(120);
    } else {
      ArrayList<String> dirs = path.moves;
      Node cur = this.getTopLeft();
      cur.color = Color.YELLOW;
      while (!dirs.isEmpty()) {
        String dir = dirs.removeFirst();
        if (dir.equals("left")) {
          cur = cur.getLeft();
        } else if (dir.equals("right")) {
          cur = cur.getRight();
        } else if (dir.equals("up")) {
          cur = cur.getUp();
        } else if (dir.equals("down")) {
          cur = cur.getDown();
        }
        cur.color = Color.RED;
      }
      cur.color = Color.GREEN;
    }
  }

  // resets the path to its original colors and the wall colors to be black
  // not private, because path reset is a function of a key
  private void resetPath() {
    setRandomColors(this.r_val, this.g_val, this.b_val);
    setWallColors(0);
  }

  // Sets custom, randomized, light background colors to differentiate mazes

  private void randomizeMazeColors() {
    int r = new Random().nextInt(105) + 125;
    int g = new Random().nextInt(105) + 125;
    int b = new Random().nextInt(105) + 125;

    this.r_val = r;
    this.g_val = g;
    this.b_val = b;

    setRandomColors(r, g, b);
  }

  // Sets custom, randomized, light background colors to differentiate mazes

  private void setRandomColors(int r, int g, int b) {
    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = this.maze.get(row).get(col);
        if (row == 0 && col == 0) {
          cur.color = Color.YELLOW;
        } else if (row == GRID_HEIGHT - 1 && col == GRID_WIDTH - 1) {
          cur.color = Color.GREEN;
        } else {
          cur.color = new Color(new Random().nextInt(15) + r,
                  new Random().nextInt(15) + g, new Random().nextInt(15) + b);
        }
      }
    }
  }

  // sets the colors of all the nodes in the maze

  private void setNodeColors() {
    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = this.maze.get(row).get(col);
        cur.color = new Color(150, 100, 100);
      }
    }
  }

  // sets the colors of all the walls in the maze

  private void setWallColors(int r) {
    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = this.maze.get(row).get(col);
        cur.wallColor = new Color(r, 0, 0);
      }
    }
  }

  // sets the borders of the maze (the walls on the outside)

  private void setMazeBorders() {
    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = this.maze.get(row).get(col);
        if (row == 0) {
          cur.wallUp(true);
        }
        if (col == 0) {
          cur.wallLeft(true);
        }
        if (row == GRID_HEIGHT - 1) {
          cur.wallDown(true);
        }
        if (col == GRID_WIDTH - 1) {
          cur.wallRight(true);
        }
      }
    }
  }

  private void clearWalls() {
    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = this.maze.get(row).get(col);
        cur.wallUp(false);
        cur.wallDown(false);
        cur.wallLeft(false);
        cur.wallRight(false);
      }
    }
  }

  // creates a random maze (not well-formed but used to test our rendering and basic
  // functionality)

  private void createMazeBruteForce() {
    this.clearWalls();
    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = this.maze.get(row).get(col);
        if (new Random().nextInt(5) == 1) {
          cur.wallUp(true);
        }
        if (new Random().nextInt(5) == 1) {
          cur.wallLeft(true);
        }
        if (new Random().nextInt(5) == 1) {
          cur.wallDown(true);
        }
        if (new Random().nextInt(5) == 1) {
          cur.wallRight(true);
        }
      }
    }
  }

  private void createTrueMaze(HashSet<EdgeWeight> edges) {
    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = this.maze.get(row).get(col);
        if (edges.contains(cur.getUpWeight())) {
          cur.wallUp(false);
        }
        if (edges.contains(cur.getLeftWeight())) {
          cur.wallLeft(false);
        }
        if (edges.contains(cur.getDownWeight())) {
          cur.wallDown(false);
        }
        if (edges.contains(cur.getRightWeight())) {
          cur.wallRight(false);
        }
      }
    }
  }

  private HashSet<EdgeWeight> algoForTrueMaze(HashMap<Node, Node> nodes,
                                              ArrayList<EdgeWeight> edges) {
    HashSet<EdgeWeight> finalSet = new HashSet<>();
    int i = 1;
    while (i < MazeGame.GRID_WIDTH * MazeGame.GRID_HEIGHT) {
      EdgeWeight e = edges.getFirst();
      if (nodes.containsKey(e.a) && nodes.get(e.a).equals(e.a)) {
        nodes.replace(e.a, e.a, e.b);
        finalSet.add(e);
        i++;
      }
      edges.removeFirst();
    }

    return finalSet;
  }

  // EFFECT: sets the left, right, up, and down fields of the nodes in the maze
  // creates and sets the edge weights

  // sets the neighbors and edge weights of all of the nodes in the maze

  private void setInitialLinks() {
    // sets horizontal links
    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 1; col < GRID_WIDTH - 1; col++) {
        Node cur = this.maze.get(row).get(col);
        cur.setLeft(this.maze.get(row).get(col - 1));
        cur.setRight(this.maze.get(row).get(col + 1));
      }
    }

    // sets vertical links
    for (int row = 1; row < GRID_HEIGHT - 1; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = this.maze.get(row).get(col);
        cur.setUp(this.maze.get(row - 1).get(col));
        cur.setDown(this.maze.get(row + 1).get(col));
      }
    }

    // edge weights
    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = this.maze.get(row).get(col);
        if (row < GRID_HEIGHT - 1) {
          cur.setEdgeDown();
        }
        if (col < GRID_WIDTH - 1) {
          cur.setEdgeRight();
        }
      }
    }

  }

  // gets all the unique edge weights in the maze, sorts them, and returns them as a
  // HashMap with identical pairings

  private ArrayList<EdgeWeight> makeEdgeGraph() {

    // gets all the unique pairings

    ArrayList<EdgeWeight> edges = new ArrayList<>();

    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = this.maze.get(row).get(col);
        if (row < GRID_HEIGHT - 1) {
          edges.add(cur.getDownWeight());
        }
        if (col < GRID_WIDTH - 1) {
          edges.add(cur.getRightWeight());
        }
      }
    }

    // sorts them from lowest to highest using sort

    edges.sort(new CompareEdgeWeight());

    return edges;
  }

  private HashMap<Node, Node> getHashMapNodes() {
    HashMap<Node, Node> nodes = new HashMap<>();

    for (int row = 0; row < GRID_HEIGHT; row++) {
      for (int col = 0; col < GRID_WIDTH; col++) {
        Node cur = this.maze.get(row).get(col);
        nodes.put(cur, cur);
      }
    }
    return nodes;
  }

  // gets the bottom left node in the maze

  private Node getBottomLeft() {
    return this.maze.get(GRID_HEIGHT - 1).getFirst();
  }

  // gets the top left node in the maze

  private Node getTopLeft() {
    return this.maze.getFirst().getFirst();
  }

  // gets the bottom right node in the maze

  private Node getBottomRight() {
    return this.maze.get(GRID_HEIGHT - 1).get(GRID_WIDTH - 1);
  }


  // ..................PUBLIC METHODS...............................


  // gets the completed image of the maze for big bang display

  public WorldImage getImage() {
    return getImageFromMaze();
  }

  // EFFECT: initializes a random maze and sets the color of the nodes to be
  // generally similar pale color to get a cool look
  public void loadLevel(String genMethod) {
    initializeMaze(genMethod);
    this.randomizeMazeColors();
    this.playerPos = this.getTopLeft();
    this.playerPos.color = Color.CYAN;
  }

  // Finds a path and colors it for use in big bang key commands
  public void pathFindAndColor(String algo) {
    Path foundPath;
    this.resetPath();
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


  // moves a player by setting returning the node in the
  // given direction if it is valid for use in big bang key commands

  public void movePlayerPath(String dir) {
    this.resetPath();
    if (dir.equals("left")) {
      if (this.playerPos.noWallLeft()) {
        this.playerPos = this.playerPos.getLeft();
      }
    } else if (dir.equals("right")) {
      if (this.playerPos.noWallRight()) {
        this.playerPos = this.playerPos.getRight();
      }
    } else if (dir.equals("up")) {
      if (this.playerPos.noWallUp()) {
        this.playerPos = this.playerPos.getUp();
      }
    } else if (dir.equals("down")) {
      if (this.playerPos.noWallDown()) {
        this.playerPos = this.playerPos.getDown();
      }
    }

    if (this.playerPos == this.getBottomRight()) {
      this.gameOverScreen(); // shows a final scene
    } else {
      this.playerPos.color = Color.CYAN;
    }
  }
}

// handles the big bang aspects of the maze game

class MazeWorld extends World {
  private final MazeGame mazeGame; // the maze game that holds all the game info

  MazeWorld() {
    this.mazeGame = new MazeGame(); // makes a new mazeGame to play
  }

  // makes a world scene that displays the current image representation of the maze\

  public WorldScene makeScene() {
    WorldImage image = this.mazeGame.getImage();

    WorldScene ws = new WorldScene(MazeGame.DISPLAY_WIDTH, MazeGame.DISPLAY_HEIGHT);
    ws.placeImageXY(image, MazeGame.DISPLAY_WIDTH / 2, MazeGame.DISPLAY_HEIGHT / 2);
    return ws;
  }

  // key handler (I will comment more in the actual method as to what each thing does)

  public void onKeyEvent(String s) {
    if (s.equals(" ")) {
      this.mazeGame.loadLevel("not random"); // every time space is hit it loads a new level
    }
    if (s.equals("k")) {
      this.mazeGame.loadLevel("random");
    }
    if (s.equals("b")) {
      this.mazeGame.pathFindAndColor("breadthFirst");
    }
    if (s.equals("r")) {
      this.mazeGame.pathFindAndColor("bruteForce");
    }
    if (s.equals("d")) {
      this.mazeGame.pathFindAndColor("depthFirst");
    }
    if (s.equals("right") || s.equals("down") || s.equals("left") || s.equals("up")) {
      this.mazeGame.movePlayerPath(s);
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

@SuppressWarnings("unused")
class Examples {
  public void testGame(Tester t) {
    World mw = new MazeWorld();
    mw.bigBang(MazeGame.DISPLAY_WIDTH, MazeGame.DISPLAY_HEIGHT, 1);
  }
}

