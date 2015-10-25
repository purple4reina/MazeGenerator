public class MazePrinter {

    Maze maze;

    // unicode character for a full box
    String wallChar = "\u2588";

    MazePrinter(Maze maze) {
        this.maze = maze;
    }

    public void print() {
        this.maze = maze;

        // Top line
        for (int x = 0; x < this.maze.width; x++) {
            System.out.print(this.wallChar + this.wallChar + this.wallChar);
        }
        System.out.println(this.wallChar);

        // Alternate between horzontal and vertical lines to draw each from the
        // top down
        this.printVertical(maze.height - 1);
        for (int y = maze.height - 2; y >= 0; y--) {
            this.printHorizontal(y);
            this.printVertical(y);
        }

        // Bottom line
        for (int x = 0; x < maze.width; x++) {
            System.out.print(this.wallChar + this.wallChar + this.wallChar);
        }
        System.out.println(this.wallChar);

    }

    private void printHorizontal(int y) {
        // Given a y-coordinate, print the horizontal walls that correspond to
        // that row.

        // Print the left most wall
        System.out.print(this.wallChar);

        for (int x = 0; x < maze.width; x++) {
            if (maze.horizontalWalls[x][y].open) {
                System.out.print("  ");
            } else {
                System.out.print(this.wallChar + this.wallChar);
            }
            System.out.print(this.wallChar);
        }

        // Print new line to mark the end of this horizontal
        System.out.println();
    }

    private void printVertical(int y) {
        // Given a y-coordinate, print the vertical walls that correspond to
        // that row.

        // Print the left most wall (but not on the top row because this is the
        // exit)
        if (y < this.maze.height - 1) {
            System.out.print(this.wallChar + "  ");
        } else {
            System.out.print("   ");
        }

        for (int x = 0; x < maze.width - 1; x++) {
            if (maze.verticalWalls[x][y].open) {
                System.out.print(" ");
            } else {
                System.out.print(this.wallChar);
            }
            System.out.print("  ");
        }

        // Print the right most wall (but not on the bottom row because this is
        // the start)
        if (y != 0) {
            System.out.println(this.wallChar);
        } else {
            System.out.println(" start");
        }
    }
}
