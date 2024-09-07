package board;

import pieces.Piece;
import pieces.Position;
import pieces.Piece.King;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the chessboard for the game.
 */
public class Board {
    private Piece[][] board;
    private List<Piece> capturedPieces;

    /**
     * Initializes a new Board with pieces in their starting positions.
     */
    public Board() {
        this.board = new Piece[8][8];
        this.capturedPieces = new ArrayList<>();
        initialize();
    }

    /**
     * Initializes the chessboard with pieces in their starting positions.
     */
    public void initialize() {
        // Set up black pieces
        board[0][0] = new Piece.Rook(false, new Position(0, 0));
        board[0][1] = new Piece.Knight(false, new Position(0, 1));
        board[0][2] = new Piece.Bishop(false, new Position(0, 2));
        board[0][3] = new Piece.Queen(false, new Position(0, 3));
        board[0][4] = new Piece.King(false, new Position(0, 4));
        board[0][5] = new Piece.Bishop(false, new Position(0, 5));
        board[0][6] = new Piece.Knight(false, new Position(0, 6));
        board[0][7] = new Piece.Rook(false, new Position(0, 7));
        for (int col = 0; col < 8; col++) {
            board[1][col] = new Piece.Pawn(false, new Position(1, col));
        }

        // Set up white pieces
        board[7][0] = new Piece.Rook(true, new Position(7, 0));
        board[7][1] = new Piece.Knight(true, new Position(7, 1));
        board[7][2] = new Piece.Bishop(true, new Position(7, 2));
        board[7][3] = new Piece.Queen(true, new Position(7, 3));
        board[7][4] = new Piece.King(true, new Position(7, 4));
        board[7][5] = new Piece.Bishop(true, new Position(7, 5));
        board[7][6] = new Piece.Knight(true, new Position(7, 6));
        board[7][7] = new Piece.Rook(true, new Position(7, 7));
        for (int col = 0; col < 8; col++) {
            board[6][col] = new Piece.Pawn(true, new Position(6, col));
        }
    }

    /**
     * Moves a piece from one position to another.
     * @param from The starting position.
     * @param to The ending position.
     * @return true if the move is valid and executed, false otherwise.
     */
    public boolean movePiece(Position from, Position to) {
        Piece piece = board[from.getRow()][from.getColumn()];
        if (piece == null || !piece.validateMove(board, from.getRow(), from.getColumn(), to.getRow(), to.getColumn())) {
            return false;
        }

        Piece target = board[to.getRow()][to.getColumn()];
        if (target != null) {
            capturedPieces.add(target);
        }
        board[to.getRow()][to.getColumn()] = piece;
        board[from.getRow()][from.getColumn()] = null;
        piece.setPosition(to);
        return true;
    }

    /**
     * Retrieves a piece from the specified position.
     * @param position The position of the piece.
     * @return The piece at the specified position.
     */
    public Piece getPiece(Position position) {
        return board[position.getRow()][position.getColumn()];
    }

    /**
     * Displays the current state of the chessboard.
     */
    public void display() {
        System.out.println("   A  B  C  D  E  F  G  H");
        for (int row = 0; row < 8; row++) {
            System.out.print((8 - row) + " ");
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece == null) {
                    System.out.print("## ");
                } else {
                    System.out.print(piece + " ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Checks if a given color is in check.
     * @param isWhite The color to check.
     * @return true if the color is in check, false otherwise.
     */
    public boolean isInCheck(boolean isWhite) {
        Position kingPosition = findKingPosition(isWhite);
        return isUnderAttack(kingPosition, !isWhite);
    }

    /**
     * Checks if a given color is in checkmate.
     * @param isWhite The color to check.
     * @return true if the color is in checkmate, false otherwise.
     */
    public boolean isCheckmate(boolean isWhite) {
        if (!isInCheck(isWhite)) {
            return false;
        }
        // Try all possible moves to see if any of them can remove the check
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.isWhite() == isWhite) {
                    Position from = new Position(row, col);
                    for (int newRow = 0; newRow < 8; newRow++) {
                        for (int newCol = 0; newCol < 8; newCol++) {
                            Position to = new Position(newRow, newCol);
                            if (piece.validateMove(board, row, col, newRow, newCol)) {
                                Piece temp = board[newRow][newCol];
                                board[newRow][newCol] = piece;
                                board[row][col] = null;
                                piece.setPosition(to);
                                boolean stillInCheck = isInCheck(isWhite);
                                board[row][col] = piece;
                                board[newRow][newCol] = temp;
                                piece.setPosition(from);
                                if (!stillInCheck) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Finds the position of the king for a given color.
     * @param isWhite The color of the king.
     * @return The position of the king.
     */
    private Position findKingPosition(boolean isWhite) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece instanceof King && piece.isWhite() == isWhite) {
                    return new Position(row, col);
                }
            }
        }
        return null;
    }

    /**
     * Checks if a given position is under attack by the opposite color.
     * @param position The position to check.
     * @param isWhite The attacking color.
     * @return true if the position is under attack, false otherwise.
     */
    private boolean isUnderAttack(Position position, boolean isWhite) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.isWhite() == isWhite) {
                    if (piece.validateMove(board, row, col, position.getRow(), position.getColumn())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
