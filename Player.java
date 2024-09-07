package pieces;

import java.util.Scanner;
import board.Board;

/**
 * This class represents a player in the chess game.
 */
public class Player {
    private boolean isWhite; // Indicates if the player is using white pieces
    private Piece[] availablePieces = new Piece[16];
    private Board board;
    private String name;

    /**
     * Constructs a Player with a specified color and board.
     *
     * @param isWhite Indicates if the player is using white pieces.
     * @param board   The chess board.
     */
    public Player(boolean isWhite, Board board) {
        this.isWhite = isWhite;
        this.board = board;
        String color = isWhite ? "white" : "black";

        if (color.equals("black")) {
            int tempIndex = 0;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 8; j++) {
                    availablePieces[tempIndex] = board.getPiece(new Position(i, j));
                    tempIndex++;
                }
            }
        } else if (color.equals("white")) {
            int tempIndex = 0;
            for (int i = 7; i > 5; i--) {
                for (int j = 0; j < 8; j++) {
                    availablePieces[tempIndex] = board.getPiece(new Position(i, j));
                    tempIndex++;
                }
            }
        }
    }

    /**
     * Makes a move based on user input.
     *
     * @param scnr Scanner to read user input.
     */
    public void makeMove(Scanner scnr) {
        System.out.print("Enter your next move: ");
        String moveFrom = scnr.next();
        String moveTo = scnr.next();

        Position from = new Position(moveFrom);
        Position to = new Position(moveTo);
        board.movePiece(from, to);
    }

    /**
     * Sets the player's name.
     *
     * @param name The name of the player.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the player's name.
     *
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if the player is controlling the white pieces.
     *
     * @return true if the player is controlling the white pieces, false otherwise.
     */
    public boolean isWhite() {
        return isWhite;
    }
}
