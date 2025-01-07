import java.util.*;

public class Pawn extends Piece {
    public Pawn(Color c) {
        super(c);
    }

    @Override
    public String toString() {
        return (color() == Color.WHITE ? "w" : "b") + "p";
    }

    @Override
    public List<String> moves(Board b, String loc) {
        List<String> possibleMoves = new ArrayList<>();
        int row = loc.charAt(1) - '1';
        int col = loc.charAt(0) - 'a';
        int direction = (color() == Color.WHITE) ? 1 : -1;

        // Normal move (one square ahead)
        addMoveIfValid(b, possibleMoves, row + direction, col, false);
        
        // Double move from starting row
        if ((color() == Color.WHITE && row == 1) || (color() == Color.BLACK && row == 6)) {
            addMoveIfValid(b, possibleMoves, row + 2 * direction, col, false);
        }
        
        // Capturing moves (diagonals)
        addMoveIfValid(b, possibleMoves, row + direction, col - 1, true);
        addMoveIfValid(b, possibleMoves, row + direction, col + 1, true);
        
        return possibleMoves;
    }

    private void addMoveIfValid(Board b, List<String> possibleMoves, int row, int col, boolean mustCapture) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) return;

        Piece target = b.getPiece("" + (char) ('a' + col) + (row + 1));
        if (mustCapture) {
            if (target != null && target.color() != this.color()) possibleMoves.add("" + (char) ('a' + col) + (row + 1));
        } else {
            if (target == null) possibleMoves.add("" + (char) ('a' + col) + (row + 1));
        }
    }
}
