package pieces;

/**
 * Abstract class representing a chess piece.
 */
public abstract class Piece {
    private boolean isWhite;
    private Position position;

    /**
     * Constructs a Piece.
     *
     * @param isWhite   Indicates if the piece is white.
     * @param position The initial position of the piece.
     */
    public Piece(boolean isWhite, Position position) {
        this.isWhite = isWhite;
        this.position = position;
    }

    /**
     * Checks if the piece is white.
     *
     * @return true if the piece is white, false otherwise.
     */
    public boolean isWhite() {
        return isWhite;
    }

    /**
     * Gets the current position of the piece.
     *
     * @return The current position.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position of the piece.
     *
     * @param position The new position.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Abstract method to determine if a move is valid.
     *
     * @param board       The current state of the board.
     * @param currentRow  The current row of the piece.
     * @param currentCol  The current column of the piece.
     * @param newRow      The new row of the piece.
     * @param newCol      The new column of the piece.
     * @return true if the move is valid, false otherwise.
     */
    public abstract boolean validateMove(Piece[][] board, int currentRow, int currentCol, int newRow, int newCol);

    @Override
    public abstract String toString();

    /**
     * Subclass representing a Bishop piece.
     */
    public static class Bishop extends Piece {
        /**
         * Constructs a Bishop.
         *
         * @param isWhite   Indicates if the piece is white.
         * @param position The initial position of the piece.
         */
        public Bishop(boolean isWhite, Position position) {
            super(isWhite, position);
        }

        @Override
        public boolean validateMove(Piece[][] board, int currentRow, int currentCol, int newRow, int newCol) {
            if ((newRow >= 8 || newRow < 0) || (newCol >= 8 || newCol < 0)) {
                return false;
            }
            return Math.abs(newRow - currentRow) == Math.abs(newCol - currentCol);
        }

        @Override
        public String toString() {
            return this.isWhite() ? "wB" : "bB";
        }
    }

    /**
     * Subclass representing a Knight piece.
     */
    public static class Knight extends Piece {
        /**
         * Constructs a Knight.
         *
         * @param isWhite   Indicates if the piece is white.
         * @param position The initial position of the piece.
         */
        public Knight(boolean isWhite, Position position) {
            super(isWhite, position);
        }

        @Override
        public boolean validateMove(Piece[][] board, int currentRow, int currentCol, int newRow, int newCol) {
            if ((newRow >= 8 || newRow < 0) || (newCol >= 8 || newCol < 0)) {
                return false;
            }
            int rowDiff = Math.abs(newRow - currentRow);
            int colDiff = Math.abs(newCol - currentCol);
            return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
        }

        @Override
        public String toString() {
            return this.isWhite() ? "wN" : "bN";
        }
    }

    /**
     * Subclass representing a Rook piece.
     */
    public static class Rook extends Piece {
        /**
         * Constructs a Rook.
         *
         * @param isWhite   Indicates if the piece is white.
         * @param position The initial position of the piece.
         */
        public Rook(boolean isWhite, Position position) {
            super(isWhite, position);
        }

        @Override
        public boolean validateMove(Piece[][] board, int currentRow, int currentCol, int newRow, int newCol) {
            if ((newRow >= 8 || newRow < 0) || (newCol >= 8 || newCol < 0)) {
                return false;
            }
            return newRow == currentRow || newCol == currentCol;
        }

        @Override
        public String toString() {
            return this.isWhite() ? "wR" : "bR";
        }
    }

    /**
     * Subclass representing a Queen piece.
     */
    public static class Queen extends Piece {
        /**
         * Constructs a Queen.
         *
         * @param isWhite   Indicates if the piece is white.
         * @param position The initial position of the piece.
         */
        public Queen(boolean isWhite, Position position) {
            super(isWhite, position);
        }

        @Override
        public boolean validateMove(Piece[][] board, int currentRow, int currentCol, int newRow, int newCol) {
            if ((newRow >= 8 || newRow < 0) || (newCol >= 8 || newCol < 0)) {
                return false;
            }
            int rowDiff = Math.abs(newRow - currentRow);
            int colDiff = Math.abs(newCol - currentCol);
            return rowDiff == colDiff || newRow == currentRow || newCol == currentCol;
        }

        @Override
        public String toString() {
            return this.isWhite() ? "wQ" : "bQ";
        }
    }

    /**
     * Subclass representing a King piece.
     */
    public static class King extends Piece {
        /**
         * Constructs a King.
         *
         * @param isWhite   Indicates if the piece is white.
         * @param position The initial position of the piece.
         */
        public King(boolean isWhite, Position position) {
            super(isWhite, position);
        }

        @Override
        public boolean validateMove(Piece[][] board, int currentRow, int currentCol, int newRow, int newCol) {
            if ((newRow >= 8 || newRow < 0) || (newCol >= 8 || newCol < 0)) {
                return false;
            }
            int rowDiff = Math.abs(newRow - currentRow);
            int colDiff = Math.abs(newCol - currentCol);
            return rowDiff <= 1 && colDiff <= 1;
        }

        @Override
        public String toString() {
            return this.isWhite() ? "wK" : "bK";
        }
    }

/**
 * Subclass representing a Pawn piece.
 */
public static class Pawn extends Piece {
    /**
     * Constructs a Pawn.
     *
     * @param isWhite   Indicates if the piece is white.
     * @param position The initial position of the piece.
     */
    public Pawn(boolean isWhite, Position position) {
        super(isWhite, position);
    }

    @Override
    public boolean validateMove(Piece[][] board, int currentRow, int currentCol, int newRow, int newCol) {
        if ((newRow >= 8 || newRow < 0) || (newCol >= 8 || newCol < 0)) {
            return false;
        }
        int rowDiff = newRow - currentRow;
        int colDiff = newCol - currentCol;
        Piece target = board[newRow][newCol];

        if (this.isWhite()) {
            // Standard move
            if (colDiff == 0 && (rowDiff == -1 || (currentRow == 6 && rowDiff == -2))) {
                return target == null;
            }
            // Capture move
            if (Math.abs(colDiff) == 1 && rowDiff == -1) {
                return target != null && !target.isWhite();
            }
        } else {
            // Standard move
            if (colDiff == 0 && (rowDiff == 1 || (currentRow == 1 && rowDiff == 2))) {
                return target == null;
            }
            // Capture move
            if (Math.abs(colDiff) == 1 && rowDiff == 1) {
                return target != null && target.isWhite();
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return this.isWhite() ? "wp" : "bp";
    }
}

}
