import java.util.*;

public class King extends Piece {
    public King(Color c) {
        super(c);
    }

    @Override
    public String toString() {
        return (color() == Color.WHITE ? "w" : "b") + "k";
    }

    @Override
    public List<String> moves(Board b, String loc) {
        List<String> possibleMoves = new ArrayList<>();
        int[] rowMoves = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] colMoves = {-1, 0, 1, -1, 1, -1, 0, 1};
        int row = loc.charAt(1) - '1';
        int col = loc.charAt(0) - 'a';

        for (int i = 0; i < 8; i++) {
            int newRow = row + rowMoves[i];
            int newCol = col + colMoves[i];
            if (isValidMove(newRow, newCol, b)) {
                possibleMoves.add("" + (char) ('a' + newCol) + (newRow + 1));
            }
        }
        return possibleMoves;
    }

    private boolean isValidMove(int row, int col, Board b) {
        return row >= 0 && row < 8 && col >= 0 && col < 8 && 
               (b.getPiece("" + (char) ('a' + col) + (row + 1)) == null || 
                b.getPiece("" + (char) ('a' + col) + (row + 1)).color() != this.color());
    }
}
