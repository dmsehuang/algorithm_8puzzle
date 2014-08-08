public class Board {
    public int[][] grid;

    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        int N = blocks.length;
        grid = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                grid[i][j] = blocks[i][j];
    }
    
    // board dimension N
    public int dimension() {
        return grid.length;
    }
    
    // number of blocks out of place
    public int hamming() {
        int N = dimension();
        int count = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] != (i * N + j + 1) % (N * N)) {
                    count++;
                }   
            }
        }
        return count;
    }
    
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int N = dimension();
        int count = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int m = grid[i][j] / N;
                int n = grid[i][j] % N - 1;
                count += Math.abs(m - i) + Math.abs(n - j);
            }
        }
        return count;
    }
    
    // is this board the goal board?
    public boolean isGoal() {
        int N = dimension();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] != (i * N + j + 1) % (N * N)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    // a board obtained by exchanging two adjacent blocks in the same row
    public Board twin() {
        int N = dimension();
        for (int i = 0; i < N; i++) {
            if (grid[i][0] != 0 && grid[i][1] != 0) {
                // swap two elements
                int tmp = grid[i][0];
                grid[i][0] = grid[i][1];
                grid[i][1] = tmp;
                Board board = new Board(grid);
                // swap back
                tmp = grid[i][0];
                grid[i][0] = grid[i][1];
                grid[i][1] = tmp;
                return board;
            }
        }
        return null;
    }
    
    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        
        Board that = (Board) y;
        int N = dimension();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.grid[i][j] != that.grid[i][j]) return false;
            }
        }
        return true;
    }
    
    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> stack = new Stack<Board>();
        int N = dimension();
        // mark the blank position
        boolean found = false;
        int i = 0;
        int j = 0;
        for (; !found && i < N; i++) {
            for (; !found && j < N; j++) {
                if (grid[i][j] == 0) {
                    found = true;
                }
            }
        }
        // move zero up
        if (i > 0) {
            grid[i][j] = grid[i-1][j];
            grid[i-1][j] = 0;
            Board board = new Board(grid);
            stack.push(board);
            grid[i-1][j] = grid[i][j];
            grid[i][j] = 0;
        }
        // move zero down
        if (i < N - 1) {
            grid[i][j] = grid[i+1][j];
            grid[i+1][j] = 0;
            Board board = new Board(grid);
            stack.push(board);
            grid[i+1][j] = grid[i][j];
            grid[i][j] = 0;
        }
        // move zero left
        if (j > 0) {
            grid[i][j] = grid[i][j-1];
            grid[i][j-1] = 0;
            Board board = new Board(grid);
            stack.push(board);
            grid[i][j-1] = grid[i][j];
            grid[i][j] = 0;
        }
        // move zero right
        if (j < N - 1) {
            grid[i][j] = grid[i][j+1];
            grid[i][j+1] = 0;
            Board board = new Board(grid);
            stack.push(board);
            grid[i][j+1] = grid[i][j];
            grid[i][j] = 0;
        }
        return stack;
    }
    
    // string representation of the board (in the output format specified below)
    public String toString() {
        int N = dimension();
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d", grid[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}