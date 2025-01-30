import java.util.List;  // Import the List interface
import java.util.ArrayList;  // Import ArrayList for storing test results

public class Test {
    public static void test1() {
        Board b = Board.theBoard();
        b.clear();  // Clear the board to reset any state from previous tests

        Piece.registerPiece(new PawnFactory());
        Piece p = Piece.createPiece("bp");  // Create a black pawn
        b.addPiece(p, "a7");  // Add the pawn to position "a7"
        assert b.getPiece("a7") == p;
    }

    public static void testPawnMoves() {
        Board b = Board.theBoard();
        b.clear();  // Clear the board to reset any state from previous tests

        Piece.registerPiece(new PawnFactory());
        Piece wp = Piece.createPiece("wp");  // Create a white pawn
        Piece bp = Piece.createPiece("bp");  // Create a black pawn

        b.addPiece(wp, "a2");  // Add white pawn to position "a2"
        b.addPiece(bp, "b3");  // Add black pawn to position "b3"

        List<String> wpMoves = wp.moves(b, "a2");
        assert wpMoves.contains("a3");
        assert wpMoves.contains("a4");
        assert wpMoves.contains("b3");
        assert wpMoves.size() == 3;  // Size check for white pawn moves
    }

    public static void testKnightMoves() {
        Board b = Board.theBoard();
        b.clear();

        Piece.registerPiece(new KnightFactory());
        Piece wn = Piece.createPiece("wn");  // Create a white knight

        b.addPiece(wn, "b1");  // Add white knight to initial position
        List<String> knightMoves = wn.moves(b, "b1");
            
        assert knightMoves.contains("a3");
        assert knightMoves.contains("c3");
        assert knightMoves.contains("d2");
        assert knightMoves.size() == 3;  // Size check for knight moves
    }
    
    public static void testBishopMoves() {
        Board b = Board.theBoard();
        b.clear();

        Piece.registerPiece(new BishopFactory());
        Piece wb = Piece.createPiece("wb");  // Create a white bishop

        b.addPiece(wb, "c1");  // Add white bishop to initial position
        List<String> bishopMoves = wb.moves(b, "c1");
        assert bishopMoves.contains("d2");
        assert bishopMoves.contains("e3");
        assert bishopMoves.contains("f4");
        assert bishopMoves.contains("b2");
        
    }

    public static void testRookMoves() {
        Board b = Board.theBoard();
        b.clear();

        Piece.registerPiece(new RookFactory());
        Piece wr = Piece.createPiece("wr");  // Create a white rook

        b.addPiece(wr, "a1");  // Add white rook to initial position
        List<String> rookMoves = wr.moves(b, "a1");
        assert rookMoves.contains("a2");
        assert rookMoves.contains("a8");
        assert rookMoves.contains("h1");
        assert rookMoves.size() == 14;  // Size check for rook moves
    }

    public static void testQueenMoves() {
        Board b = Board.theBoard();
        b.clear();

        Piece.registerPiece(new QueenFactory());
        Piece wq = Piece.createPiece("wq");  // Create a white queen

        b.addPiece(wq, "d1");  // Add white queen to initial position
        List<String> queenMoves = wq.moves(b, "d1");
        assert queenMoves.contains("d8");
        assert queenMoves.contains("a4");
        assert queenMoves.contains("h5");
        assert queenMoves.size() == 21;  // Size check for queen moves
    }

    public static void testKingMoves() {
        Board b = Board.theBoard();
        b.clear();

        Piece.registerPiece(new KingFactory());
        Piece wk = Piece.createPiece("wk");  // Create a white king

        b.addPiece(wk, "e1");  // Add white king to initial position
        List<String> kingMoves = wk.moves(b, "e1");
        assert kingMoves.contains("e2");
        assert kingMoves.contains("d1");
        assert kingMoves.contains("f1");
        assert kingMoves.contains("d2");
        assert kingMoves.contains("f2");
        assert kingMoves.size() == 5;  // Size check for king moves
    }

    public static void testPieceCapture() {
        Board b = Board.theBoard();
        b.clear();

        Piece.registerPiece(new RookFactory());
        Piece wr = Piece.createPiece("wr");  // Create a white rook
        Piece br = Piece.createPiece("br");  // Create a black rook

        b.addPiece(wr, "a1");  // Add white rook to position a1
        b.addPiece(br, "a8");  // Add black rook to position a8

        List<String> rookMoves = wr.moves(b, "a1");
        assert rookMoves.contains("a8");
        assert rookMoves.size() == 15;  // Check for capture move
    }

    public static void testClearBoard() {
        Board b = Board.theBoard();
        b.clear();

        Piece.registerPiece(new KingFactory());
        Piece wk = Piece.createPiece("wk");  // Create a white king

        b.addPiece(wk, "e1");
        assert b.getPiece("e1") == wk;
        
        b.clear();
        assert b.getPiece("e1") == null;
    }

    public static void main(String[] args) {
        test1();  // Test adding a piece to the board
        testPawnMoves();  // Test pawn movement rules
        testKnightMoves();  // Test knight movement rules
        testBishopMoves();  // Test bishop movement rules
        testRookMoves();  // Test rook movement rules
        testQueenMoves();  // Test queen movement rules
        testKingMoves();  // Test king movement rules
        testPieceCapture();  // Test capturing opponent's pieces
        testClearBoard();  // Test clearing the board
    }
}
