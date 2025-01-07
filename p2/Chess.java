import java.io.*;
import java.util.*;

public class Chess {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: java Chess layout moves");
            return;
        }

        // Register all pieces
        Piece.registerPiece(new KingFactory());
        Piece.registerPiece(new QueenFactory());
        Piece.registerPiece(new RookFactory());
        Piece.registerPiece(new BishopFactory());
        Piece.registerPiece(new KnightFactory());
        Piece.registerPiece(new PawnFactory());

        Board board = Board.theBoard();
        board.registerListener(new Logger());

        // Read layout file
        BufferedReader layoutReader = new BufferedReader(new FileReader(args[0]));
        String line;
        while ((line = layoutReader.readLine()) != null) {
            if (line.startsWith("#")) continue;  // Ignore comments
            if (line.trim().isEmpty()) continue; // Ignore empty lines
            String[] parts = line.split("=");
            board.addPiece(Piece.createPiece(parts[1]), parts[0]);
        }
        layoutReader.close();

        // Read and execute moves file
        BufferedReader movesReader = new BufferedReader(new FileReader(args[1]));
        while ((line = movesReader.readLine()) != null) {
            if (line.startsWith("#")) continue;  // Ignore comments
            if (line.trim().isEmpty()) continue; // Ignore empty lines
            String[] parts = line.split("-");
            board.movePiece(parts[0], parts[1]);
        }
        movesReader.close();

        // Print the final board state
        System.out.println("Final board:");
        board.iterate(new BoardPrinter());
    }
}
