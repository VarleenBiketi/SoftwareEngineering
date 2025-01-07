import java.util.*;

public class Board {
    private Piece[][] pieces = new Piece[8][8];  // 2D array to store the pieces on the board
    private static Board boardInstance = new Board();  // Singleton instance of Board
    private List<BoardListener> listeners = new ArrayList<>();  // List to store listeners

    // Private constructor to prevent instantiation
    private Board() {}

    // Returns the single instance of the Board class
    public static Board theBoard() {
        return boardInstance;
    }

    // Returns the piece at a given location (e.g., "a3"), or null if the location is empty
    public Piece getPiece(String loc) {
        int row = loc.charAt(1) - '1';
        int col = loc.charAt(0) - 'a';
        return pieces[row][col];
    }

    // Adds a piece to a given location on the board
    public void addPiece(Piece p, String loc) {
        int row = loc.charAt(1) - '1';
        int col = loc.charAt(0) - 'a';
        if (pieces[row][col] != null) {
            throw new IllegalArgumentException("Location already occupied");
        }
        pieces[row][col] = p;
    }

    // Moves a piece from one location to another, handling captures if necessary
    public void movePiece(String from, String to) {
        Piece p = getPiece(from);
        if (p == null) {
            throw new IllegalArgumentException("No piece at the source location");
        }

        int fromRow = from.charAt(1) - '1';
        int fromCol = from.charAt(0) - 'a';
        int toRow = to.charAt(1) - '1';
        int toCol = to.charAt(0) - 'a';

        Piece captured = pieces[toRow][toCol];
        pieces[toRow][toCol] = p;
        pieces[fromRow][fromCol] = null;

        for (BoardListener bl : listeners) {
            bl.onMove(from, to, p);
            if (captured != null) {
                bl.onCapture(p, captured);
            }
        }
    }

    // Clears the board by setting all positions to null
    public void clear() {
        for (int i = 0; i < 8; i++) {
            Arrays.fill(pieces[i], null);
        }
    }

    // Registers a new listener to observe board events
    public void registerListener(BoardListener bl) {
        listeners.add(bl);
    }

    // Removes a specific listener
    public void removeListener(BoardListener bl) {
        listeners.remove(bl);
    }

    // Removes all registered listeners
    public void removeAllListeners() {
        listeners.clear();
    }

    // Iterates over all board squares, calling visit on each square
    public void iterate(BoardInternalIterator bi) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                String loc = "" + (char) ('a' + col) + (row + 1);
                bi.visit(loc, pieces[row][col]);
            }
        }
    }
}
