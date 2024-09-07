import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import board.Board;
import pieces.Piece;
import pieces.Position;

/**
 * This class represents the graphical user interface for the chess game.
 * It handles the display of the chessboard, player names, and move history.
 */
public class ChessGUI {
    private JFrame frame; // Main frame of the GUI
    private JPanel boardPanel; // Panel to hold the chess board
    private JLabel[][] squares; // 2D array to represent the chess board squares
    private JLabel player1Label; // Label to display Player 1's name
    private JLabel player2Label; // Label to display Player 2's name
    private JList<String> movementList; // List to display the history of moves
    private DefaultListModel<String> movementListModel; // Model for the movement list
    private List<String> movements; // List to store movements
    private final int ROWS = 8; // Number of rows on the chess board
    private final int COLS = 8; // Number of columns on the chess board
    private Point firstClick = null; // To store the first click of the user
    private Board board; // Board object to manage the game logic
    private boolean whiteTurn = true; // To track which player's turn it is

    /**
     * Constructs the ChessGUI with player names.
     *
     * @param player1Name The name of player 1.
     * @param player2Name The name of player 2.
     */
    public ChessGUI(String player1Name, String player2Name) {
        board = new Board(); // Initialize the Board
        frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLayout(new BorderLayout());

        // Add labels for Player 1 and Player 2
        player1Label = new JLabel("Player 1: " + player1Name);
        player1Label.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(player1Label, BorderLayout.SOUTH);

        player2Label = new JLabel("Player 2: " + player2Name);
        player2Label.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(player2Label, BorderLayout.NORTH);

        initializeBoard(); // Initialize the board
        initializeMovementList(); // Initialize the movement list
        frame.setVisible(true); // Make the frame visible
    }

    /**
     * Initializes the chess board.
     */
    private void initializeBoard() {
        boardPanel = new JPanel(new GridLayout(ROWS, COLS)); // Set layout for board panel
        squares = new JLabel[ROWS][COLS]; // Initialize the squares array
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                JLabel label = new JLabel(); // Create a new label for each square
                label.setOpaque(true);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                if ((row + col) % 2 == 0) {
                    label.setBackground(Color.WHITE); // Set color for white squares
                } else {
                    label.setBackground(Color.GRAY); // Set color for gray squares
                }
                int currentRow = row; // Capture the current row
                int currentCol = col; // Capture the current col
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        handleMouseClick(e, currentRow, currentCol); // Handle mouse click event
                    }
                });
                boardPanel.add(label); // Add label to the board panel
                squares[row][col] = label; // Store the label in the squares array
            }
        }
        frame.add(boardPanel, BorderLayout.CENTER); // Add the board panel to the frame
        setupInitialPieces(); // Set up the initial pieces on the board
    }

    /**
     * Sets up the initial pieces on the board.
     */
    private void setupInitialPieces() {
        updateBoard(); // Update the board to display pieces
    }

    /**
     * Initializes the movement list.
     */
    private void initializeMovementList() {
        movementListModel = new DefaultListModel<>(); // Initialize the movement list model
        movementList = new JList<>(movementListModel); // Initialize the movement list
        JScrollPane scrollPane = new JScrollPane(movementList); // Add a scroll pane to the movement list
        scrollPane.setPreferredSize(new Dimension(200, 0)); // Set preferred width for the scroll pane
        frame.add(scrollPane, BorderLayout.EAST); // Add the scroll pane to the frame
        movements = new ArrayList<>(); // Initialize the movements list
    }

    /**
     * Handles mouse click events.
     *
     * @param e   The MouseEvent object.
     * @param row The row of the clicked square.
     * @param col The column of the clicked square.
     */
    private void handleMouseClick(MouseEvent e, int row, int col) {
        JLabel clickedLabel = (JLabel) e.getSource();
        if (firstClick == null) {
            // First click
            if (clickedLabel.getIcon() != null) {
                Piece piece = board.getPiece(new Position(row, col));
                if (piece.isWhite() == whiteTurn) {
                    firstClick = new Point(row, col); // Store the first click position
                    clickedLabel.setBorder(BorderFactory.createLineBorder(Color.RED)); // Highlight the selected piece
                }
            }
        } else {
            // Second click

            if (firstClick.x == row && firstClick.y == col) {
                // Clicked on the same spot as the first click
                JOptionPane.showMessageDialog(frame, "Please click on a different spot.");
                return; // Exit the method without further processing
            }

            Position from = new Position(firstClick.x, firstClick.y); // Get the from position
            Position to = new Position(row, col); // Get the to position
            if (board.movePiece(from, to)) { // Move the piece on the board
                updateBoard(); // Update the board to reflect the move
                recordMovement(from, to); // Record the move in the movement list

                // Check for check and checkmate
                if (board.isInCheck(!whiteTurn)) {
                    JOptionPane.showMessageDialog(frame, "Check!");
                }
                if (board.isCheckmate(!whiteTurn)) {
                    JOptionPane.showMessageDialog(frame, "Checkmate! " + (whiteTurn ? "White" : "Black") + " wins!");
                    // Disable further clicks after checkmate
                    disableBoard();
                }

                whiteTurn = !whiteTurn; // Switch turns
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid move"); // Show error message for invalid move
            }
            squares[firstClick.x][firstClick.y].setBorder(null); // Remove the border from the first click position
            firstClick = null; // Reset the first click position
        }
    }

    /**
     * Updates the board display.
     */
    private void updateBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Piece piece = board.getPiece(new Position(row, col)); // Get the piece at the current position
                if (piece != null) {
                    squares[row][col].setIcon(new ImageIcon("resources/images/" + piece.toString() + ".png")); // Set the icon for the piece
                } else {
                    squares[row][col].setIcon(null); // Clear the icon if no piece is present
                }
            }
        }
    }

    /**
     * Records a move in the movement list.
     *
     * @param from The starting position of the piece.
     * @param to   The ending position of the piece.
     */
    private void recordMovement(Position from, Position to) {
        String move = "Move from " + convertPosition(from) + " to " + convertPosition(to); // Create a move string
        movements.add(move); // Add the move to the movements list
        movementListModel.addElement(move); // Add the move to the movement list model
    }

    /**
     * Converts a Position object to a chess notation string.
     *
     * @param position The position to convert.
     * @return The chess notation string.
     */
    private String convertPosition(Position position) {
        char column = (char) ('A' + position.getColumn()); // Convert column index to a letter
        int rowNum = 8 - position.getRow(); // Convert row index to a number
        return "" + column + rowNum; // Return the chess notation string
    }

    /**
     * Gets player names through a dialog.
     *
     * @return An array containing the names of Player 1 and Player 2.
     */
    private static String[] getPlayerNames() {
        JTextField player1Field = new JTextField(); // Text field for Player 1 name
        JTextField player2Field = new JTextField(); // Text field for Player 2 name
        Object[] message = {
            "Player 1 name:", player1Field,
            "Player 2 name:", player2Field
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Enter Player Names", JOptionPane.OK_CANCEL_OPTION); // Show dialog to get player names
        if (option == JOptionPane.OK_OPTION) {
            return new String[]{player1Field.getText(), player2Field.getText()}; // Return the player names
        } else {
            return null; // Return null if the dialog was canceled
        }
    }

    /**
     * Disables the board.
     */
    private void disableBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                for (MouseListener ml : squares[row][col].getMouseListeners()) {
                    squares[row][col].removeMouseListener(ml); // Remove all mouse listeners from each square
                }
            }
        }
    }

    /**
     * The main method to start the chess game.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] playerNames = getPlayerNames(); // Get player names
            if (playerNames != null && playerNames.length == 2) {
                new ChessGUI(playerNames[0], playerNames[1]); // Create a new ChessGUI with the player names
            } else {
                System.exit(0);
            }
        });
    }
}
