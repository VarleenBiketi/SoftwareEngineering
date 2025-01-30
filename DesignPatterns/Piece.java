import java.util.*;

abstract public class Piece {
    private Color color;  // Add a field to store the piece color
    private static Map<Character, PieceFactory> pieceFactories = new HashMap<>();

    // Constructor that accepts a Color parameter
    public Piece(Color c) {
        this.color = c;
    }

    // Method to get the color of the piece
    public Color color() {
        return this.color;
    }

    // Register a new piece type with its corresponding factory
    public static void registerPiece(PieceFactory pf) {
        pieceFactories.put(pf.symbol(), pf);
    }

    // Factory method to create a piece based on a string like "wp" (white pawn)
    public static Piece createPiece(String name) {
        // Validate the input name format
        if (name == null || name.length() != 2) {
            throw new IllegalArgumentException("Invalid piece name format.");
        }
    
        Color color;
        if (name.charAt(0) == 'b') {
            color = Color.BLACK;
        } else if (name.charAt(0) == 'w') {
            color = Color.WHITE;
        } else {
            throw new IllegalArgumentException("Invalid color: " + name.charAt(0));
        }
    
        // Get the piece type (the second character)
        char pieceType = name.charAt(1);
        PieceFactory factory = pieceFactories.get(pieceType);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid piece type: " + pieceType);
        }
    
        return factory.create(color);
    }
    

    // Abstract methods to be implemented by subclasses
    abstract public String toString();
    abstract public List<String> moves(Board b, String loc);
}
