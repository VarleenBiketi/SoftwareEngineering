import java.util.*;

public class Rook extends Piece {
    public Rook(Color c) {
        super(c);
    }

    @Override
    public String toString() {
        return (color() == Color.WHITE ? "w" : "b") + "r";
    }

    @Override
    public List<String> moves(Board b, String loc) {
        List<String> possibleMoves = new ArrayList<>();
        addMovesInDirection(b, loc, 1, 0, possibleMoves);  // Right
        addMovesInDirection(b, loc, -1, 0, possibleMoves); // Left
        addMovesInDirection(b, loc, 0, 1, possibleMoves);  // Up
        addMovesInDirection(b, loc, 0, -1, possibleMoves); // Down
        return possibleMoves;
    }

    private void addMovesInDirection(Board b, String loc, int rowStep, int colStep, List<String> possibleMoves) {
        int row = loc.charAt(1) - '1';
        int col = loc.charAt(0) - 'a';
        while (true) {
            row += rowStep;
            col += colStep;
            if (row < 0 || row >= 8 || col < 0 || col >= 8) break;

            String newLoc = "" + (char) ('a' + col) + (row + 1);
            if (b.getPiece(newLoc) == null) {
                possibleMoves.add(newLoc);
            } else {
                if (b.getPiece(newLoc).color() != this.color()) possibleMoves.add(newLoc);
                break;
            }
        }
    }
}
