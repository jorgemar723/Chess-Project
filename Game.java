import board.Board;
import pieces.Player;

import java.util.Scanner;

/**
 * This class represents the main game logic for the chess game.
 */
public class Game {
    private Board board; // The chessboard
    private Player white; // The white player
    private Player black; // The black player
    private String playerTurn; // String to track the current player's turn

    /**
     * Constructor to initialize the game.
     */
    public Game() {
        this.board = new Board(); // Initialize the board
        this.white = new Player(true, board); // Initialize the white player
        this.black = new Player(false, board); // Initialize the black player
        this.playerTurn = "white"; // White starts the game
    }

    /**
     * Starts the game by initializing the board and starting the play loop.
     */
    public void start() {
        Scanner scnr = new Scanner(System.in);
        String p1Name;
        String p2Name;

        System.out.println("What's Player 1's name?");
        System.out.print("Player 1: ");
        p1Name = scnr.nextLine();
        this.white.setName(p1Name);

        System.out.println("What's Player 2's name?");
        System.out.print("Player 2: ");
        p2Name = scnr.nextLine();
        this.black.setName(p2Name);

        System.out.println("Game started.");
        play(scnr); // Pass the Scanner to the play method
    }

    /**
     * Ends the game with a result message.
     * 
     * @param result The result message to display.
     */
    public void end(String result) {
        System.out.println("Game over: " + result);
    }

    /**
     * Handles the main game play loop, prompting players for moves and updating the board.
     */
    public void play(Scanner scnr) {
        boolean gameOver = false;

        while (!gameOver) {
            board.display();

            // Display whose turn it is
            System.out.println((playerTurn.equals("white") ? white.getName() : black.getName()) + "'s turn");
            
            // Get the current player to make a move
            if (playerTurn.equals("white")) {
                white.makeMove(scnr);
            } else {
                black.makeMove(scnr);
            }

            // Check for check or checkmate
            if (board.isCheckmate(playerTurn.equals("white"))) {
                end(playerTurn.equals("white") ? "Black wins by checkmate" : "White wins by checkmate");
                gameOver = true;
            } else if (board.isInCheck(playerTurn.equals("white"))) {
                System.out.println((playerTurn.equals("white") ? "White" : "Black") + " is in check!");
            }

            // Switch turns
            playerTurn = playerTurn.equals("white") ? "black" : "white";
        }
        scnr.close(); // Close the Scanner here when the game ends
    }

    // Main method to test the class
    public static void main(String[] args) {
        Game game = new Game(); // Create a new game instance
        game.start(); // Start the game
    }
}
