import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.StdOut;

// Models a board in the 8-puzzle game or its generalization.
public class Board {
    private int[][] tiles;
    private int N;
    private int empty = 0;
    private int hamming;
    private int manhattan;

    // Construct a board from an N-by-N array of tiles, where 
    // tiles[i][j] = tile at row i and column j, and 0 represents the blank 
    // square.
    public Board(int[][] tiles) {
        this.N = tiles.length;
        this.hamming = 0;
        this.manhattan = 0;
        this.tiles = new int[tiles.length][tiles.length];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != empty && tileAt(i, j) != i * N + j + 1) {
                    hamming += 1;
                }
            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != empty && tileAt(i, j) != i * N + j + 1) {
                    int gBoardi = (tileAt(i, j) - 1) / N;
                    int gBoardj = tileAt(i, j) - 1 - gBoardi * N;
                    manhattan += Math.abs(i - gBoardi) + Math.abs(j - gBoardj);
                }
            }
        }
    }

    // Tile at row i and column j.
    public int tileAt(int i, int j) {
        return tiles[i][j];
    }

    // Size of this board.
    public int size() {
        return N * N;
    }

    // Number of tiles out of place.
    public int hamming() {
        return hamming;
    }

    // Sum of Manhattan distances between tiles and goal.
    public int manhattan() {
        return manhattan;
    }

    // Is this board the goal board?
    public boolean isGoal() {
        if (manhattan == 0 && hamming == 0) {
            return true;
        }
        return false;
    }

    // Is this board solvable?
    public boolean isSolvable() {
        if (N % 2 == 1) {
            return (inversions() % 2 == 0);
        } else {
            return ((inversions() + (blankPos() / N)) % 2 != 0);
        }
    }

    // Does this board equal that?
    public boolean equals(Board that) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.tileAt(i, j) != that.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    // All neighboring boards.
    public Iterable<Board> neighbors() {
        LinkedQueue<Board> neighbors = new LinkedQueue<Board>();
        int[][] position;
        int i = blankPos() / N;
        int j = blankPos() % N;
        int temp;
        if (i - 1 >= 0 && i - 1 < N) {
            position = cloneTiles();
            temp = position[i][j];
            position[i][j] = position[i - 1][j];
            position[i - 1][j] = temp;
            Board board = new Board(position);
            neighbors.enqueue(board);
        }
        if (j + 1 >= 0 && j + 1 < N) {
            position = cloneTiles();
            temp = position[i][j];
            position[i][j] = position[i][j + 1];
            position[i][j + 1] = temp;
            Board board = new Board(position);
            neighbors.enqueue(board);
        }
        if (i + 1 >= 0 && i + 1 < N) {
            position = cloneTiles();
            temp = position[i][j];
            position[i][j] = position[i + 1][j];
            position[i + 1][j] = temp;
            Board board = new Board(position);
            neighbors.enqueue(board);
        }
        if (j - 1 >= 0 && j - 1 < N) {
            position = cloneTiles();
            temp = position[i][j];
            position[i][j] = position[i][j - 1];
            position[i][j - 1] = temp;
            Board board = new Board(position);
            neighbors.enqueue(board);
        }
        return neighbors;
    }

    // String representation of this board.
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (j < N - 1) {
                    s.append(String.format("%2d ", tiles[i][j]));
                } else {
                    s.append(String.format("%2d", tiles[i][j]));
                }
            }
            if (i < N - 1) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    // Helper method that returns the position (in row-major order) of the 
    // blank (zero) tile.
    private int blankPos() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tileAt(i, j) == empty) {
                    return i * N + j;
                }
            }
        }
        return -1;
    }

    // Helper method that returns the number of inversions.
    private int inversions() {
        int[] arrays = new int[N * N + 1];
        int p = 0;
        int total = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tileAt(i, j) != empty) {
                    arrays[p] = tileAt(i, j);
                    p += 1;
                }
            }
        }

        for (int h = 0; h < N * N; h++) {
            for (int j = h + 1; j < N * N; j++) { 
                if (arrays[h] > arrays[j] && arrays[h] != 0 && arrays[j] != 0) {
                total += 1;
               }
            }
        }
        return total;
    }

    // Helper method that clones the tiles[][] array in this board and 
    // returns it.
    private int[][] cloneTiles() {
        int[][] temp = new int[this.tiles.length][this.tiles.length];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                temp[i][j] = this.tiles[i][j];
            }
        }
        return temp;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        StdOut.println(board.hamming());
        StdOut.println(board.manhattan());
        StdOut.println(board.isGoal());
        StdOut.println(board.isSolvable());
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
        }
    }
}
