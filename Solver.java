import java.util.HashSet;

public class Solver {
    int minMoves = 0;
    Node dest = null;
    MinPQ<Node> pq = new MinPQ<Node>();
    MinPQ<Node> pqTwin = new MinPQ<Node>();
    HashSet<Board> visited = new HashSet<Board>();
    HashSet<Board> visitedTwin = new HashSet<Board>();
    Board initial;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        this.initial = initial;
    }
    
    // is the initial board solvable?
    public boolean isSolvable() {
        // twin 
        Node rootTwin = new Node(initial.twin());
        pqTwin.insert(rootTwin);
        visited.add(initial.twin());
        
        while (pqTwin.size() > 0) {
            Node curr = pq.delMin();
            
            // if find the solution
            if (curr.board.isGoal()) {
                return true;
            }
            // if not, get the neighbors and enqueue
            for (Board board : curr.board.neighbors()) {
                if (visited.contains(board)) continue; // can not be father's node
                Node node = new Node(board);
                visited.add(board);
                pq.insert(node);
            }
        }
        return false;
    }
    
    // min number of moves to solve initial board; -1 if no solution
    public int moves() {
        return minMoves;
    }
    
    // sequence of boards in a shortest solution; null if no solution
    public Iterable<Board> solution() {
        Node root = new Node(initial);
        pq.insert(root);
        visited.add(initial);
        
        while (pq.size() > 0) {
            Node curr = pq.delMin();
            
            // if find the solution
            if (curr.board.isGoal()) {
                minMoves = curr.moves;
                dest = curr;
                break;
            }
            // if not, get the neighbors and enqueue
            for (Board board : curr.board.neighbors()) {
                if (visited.contains(board)) continue; // can not be father's node
                Node node = new Node(board);
                node.moves = curr.moves + 1;
                node.prev = curr;
                visited.add(board);
                pq.insert(node);
            }
        }
        
        // reconstruct the solution
        Stack<Board> stk = new Stack<Board>();
        Node curr = dest;
        while (curr.prev != null) {
            stk.push(curr.board);
            curr = curr.prev;
        }
        stk.push(curr.board);
        return stk;
    }
    
    //solve a slider puzzle (given below)
    public static void main (String[]args) {
        // create initial board from file
        In in  = new In(args[0]); 
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard ouput
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        }else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }
}

class Node implements Comparable<Node>{
    public Board board; // point to the current board
    public int moves; // N moves to the current search node
    public Node prev; // point to the previous search node
 
    // constructor
    public Node (Board board) {
        this.board = board;
        prev = null;
        moves = 0;
    }

    @Override
    public int compareTo(Node that) {
        return (this.board.manhattan() + this.moves) - (that.board.manhattan() + that.moves);
    }   
}