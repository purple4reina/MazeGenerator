public class MazeGenerator {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java MazeGenerator width height");
            System.exit(1);
        }

        int width = 0;
        int height = 0;
        try {
            width = Integer.parseInt(args[0]);
            height = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("width and height arguments must be integers!");
            System.err.println("Usage: java MazeGenerator width height");
            System.exit(1);
        }

        // Create the Maze
        Maze maze = new Maze(width, height);

        // Generate the path
        maze.generate();

        // Print the Maze to stdout
        maze.print();
    }
}
