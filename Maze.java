import java.util.ArrayList;
import java.util.Stack;


public class Maze {

    int width;
    int height;
    Point[][] points;
    Wall[][] horizontalWalls;
    Wall[][] verticalWalls;

    Maze(int width, int height) {
        this.width = width;
        this.height = height;

        // Create points for each coordinate
        // Uses an array of arrays so that a point can be accessed using its
        // coordinates. For example a point with coordinates (2, 4) can be
        // accessed using this.points[2][4].
        this.points = new Point[this.width][this.height];
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                this.points[x][y] = new Point(x, y);
            }
        }

        // Create walls between each point
        // Vertical walls
        // Uses an array of arrays so that each wall can be accessed using
        // coordinates.
        this.verticalWalls = new Wall[this.width - 1][this.height];
        for (int x = 0; x < this.width - 1; x++) {
            for (int y = 0; y < this.height; y++) {
                this.verticalWalls[x][y]
                    = new Wall(points[x][y], points[x + 1][y]);
            }
        }

        // Horizontal walls
        // Uses an array of arrays so that each wall can be accessed using
        // coordinates.
        this.horizontalWalls = new Wall[this.width][this.height - 1];
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height - 1; y++) {
                this.horizontalWalls[x][y]
                    = new Wall(points[x][y], points[x][y + 1]);
            }
        }
    }

    public void generate() {
        // Generates a new maze path
        // Uses recursive backtracking
        // https://en.wikipedia.org/wiki/Maze_generation_algorithm

        // When this number reaches width * height, it is known that all points
        // have been visited by the generating algorithm
        int totalVisitedPoints = 1;

        // Randomly choose a direction to favor when creating the path
        String[] directions = {"horizontal", "vertical", "shiftchange", "none"};
        String randomDirection =
            directions[(int)(Math.random() * 10) % directions.length];

        Point thisPoint = this.points[0][this.height - 1];
        Stack<Point> path = new Stack<Point>();
        while (totalVisitedPoints < this.width * this.height) {
            thisPoint.visited = true;

            ArrayList<Point> thisPointNeighbors = this.getNeighbors(thisPoint);

            if (this.hasUnvisitedNeighbors(thisPointNeighbors)) {
                // choose random unvisited neighbor
                Point nextPoint
                    = this.getRandomUnvisitedNeighbor(thisPoint, randomDirection);

                // push the current point to the stack
                path.push(thisPoint);

                // remove the wall between the current point and the chosen
                // point
                this.removeWall(thisPoint, nextPoint);

                // make the chosen point the current point
                thisPoint = nextPoint;
                totalVisitedPoints++;
            } else if (path.size() > 0) {
                // Pop a cell from the stack
                thisPoint = path.pop();
            }
        }
    }

    private void removeWall(Point point1, Point point2) {
        /* Given two points, remove the wall between them */
        int point1X = point1.x;
        int point1Y = point1.y;
        int point2X = point2.x;
        int point2Y = point2.y;

        if (point1Y == point2Y) {
            // this is a vertical wall
            this.verticalWalls[Math.min(point1X, point2X)][point1Y].open = true;
        } else {
            // this is a horizontal wall
            this.horizontalWalls[point1X][Math.min(point1Y, point2Y)].open = true;
        }
    }

    private Point getRandomUnvisitedNeighbor(ArrayList<Point> neighbors) {
        double numNeighbors = neighbors.size();

        double choice = (Math.random() * 10) % numNeighbors;
        int index = (int)choice;
        Point randomPoint = neighbors.get(index);

        while (randomPoint.visited) {
            choice = (Math.random() * 10) % numNeighbors;
            index = (int)choice;
            randomPoint = neighbors.get(index);
        }

        return randomPoint;
    }

    private Point getRandomUnvisitedNeighbor(Point point, String direction) {
        // Given the point and the direction to favor, return a random
        // neighbor.
        ArrayList<Point> neighbors = getNeighbors(point);

        if (direction == "horizontal") {
            // Favor horizontal neighbors by adding all the horizontal
            // neighbors to the given list of neighbors a couple of times. This
            // means they will be more likely to be chosen.
            neighbors.addAll(getHorizontalNeighbors(point));
            neighbors.addAll(getHorizontalNeighbors(point));
            return getRandomUnvisitedNeighbor(neighbors);
        } else if (direction == "vertical") {
            // Favor vertical neighbors by adding all the vertical neighbors to
            // the given list of neighbors a couple of times. This means they
            // will be more likely to be chosen.
            neighbors.addAll(getVerticalNeighbors(point));
            neighbors.addAll(getVerticalNeighbors(point));
            return getRandomUnvisitedNeighbor(neighbors);
        } else if (direction == "shiftchange") {
            // Half way through the puzzle switch from favoring horizontal to
            // favoring vertical. This is most effective in larger puzzles.
            int halfway = this.width / 2;
            if (point.x > halfway) {
                return getRandomUnvisitedNeighbor(point, "horizontal");
            } else {
                return getRandomUnvisitedNeighbor(point, "vertical");
            }
        } else {
            return getRandomUnvisitedNeighbor(neighbors);
        }
    }

    private boolean hasUnvisitedNeighbors(ArrayList<Point> neighbors) {
        for (Point thisPoint : neighbors) {
            if (!thisPoint.visited) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Point> getNeighbors(Point point) {
        // Given a Point, return an array containing all its neighbors
        // horizontally and vertically
        ArrayList<Point> neighbors = getHorizontalNeighbors(point);
        neighbors.addAll(getVerticalNeighbors(point));
        return neighbors;
    }

    private ArrayList<Point> getVerticalNeighbors(Point point) {
        ArrayList<Point> neighbors = new ArrayList<Point>();
        int thisPointX = point.x;
        int thisPointY = point.y;

        // neighbor below
        if (thisPointY > 0) {
            neighbors.add(this.points[thisPointX][thisPointY - 1]);
        }

        // neighbor above
        if (thisPointY < this.height - 1) {
            neighbors.add(this.points[thisPointX][thisPointY + 1]);
        }

        return neighbors;
    }

    private ArrayList<Point> getHorizontalNeighbors(Point point) {
        ArrayList<Point> neighbors = new ArrayList<Point>();
        int thisPointX = point.x;
        int thisPointY = point.y;

        // neighbor to the right
        if (thisPointX < this.width - 1) {
            neighbors.add(this.points[thisPointX + 1][thisPointY]);
        }

        // neighbor to the left
        if (thisPointX > 0) {
            neighbors.add(this.points[thisPointX - 1][thisPointY]);
        }

        return neighbors;
    }

    public void print() {
        // Prints this maze to stdout
        MazePrinter printer = new MazePrinter(this);
        printer.print();
    }
}



class Wall {
    // Represents the wall that separates two Points

    Point[] points;
    boolean open = false;

    Wall(Point point1, Point point2) {
        this.points = new Point[2];
        this.points[0] = point1;
        this.points[1] = point2;
    }
}


class Point {

    int x;  // x-coordinate
    int y;  // y-coordinate
    boolean visited = false;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
