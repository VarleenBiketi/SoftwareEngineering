import java.util.*;

public class Knight extends Piece {
    public Knight(Color c) {
        super(c);
    }

    @Override
    public String toString() {
        return (color() == Color.WHITE ? "w" : "b") + "n";
    }

    @Override
    public List<String> moves(Board b, String loc) {
        List<String> possibleMoves = new ArrayList<>();
        // Define all possible L-shaped moves for the knight
        int[][] knightMoves = {
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };
        
        // Get the current row and column indices
        int row = loc.charAt(1) - '1';
        int col = loc.charAt(0) - 'a';
    
        // Iterate through all possible L-shaped moves
        for (int[] move : knightMoves) {
            int newRow = row + move[0];
            int newCol = col + move[1];
    
            // Check if the new position is within the bounds and is either empty or occupied by an opponent's piece
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                Piece target = b.getPiece("" + (char) ('a' + newCol) + (newRow + 1));
                if (target == null || target.color() != this.color()) {
                    possibleMoves.add("" + (char) ('a' + newCol) + (newRow + 1));
                }
            }
        }
        return possibleMoves;
    }
     
}
