package pieces;

/**
 * This class represents a position on the chessboard.
 */
public class Position {
    private int row; // The row of the position
    private int column; // The column of the position

    /**
     * Constructor to initialize the position with row and column.
     * 
     * @param row The row of the position.
     * @param column The column of the position.
     */
    public Position(int row, int column) {
        this.row = row; // Set the row
        this.column = column; // Set the column
    }

    /**
     * Constructor to initialize the position from a string (e.g., "E2").
     * 
     * @param pos The string representation of the position.
     */
    public Position(String pos) {
        this.row = 8 - Character.getNumericValue(pos.charAt(1)); // Convert the numeric part of the string to a row
        this.column = pos.charAt(0) - 'A'; // Convert the character part of the string to a column
    }

    /**
     * Gets the row of the position.
     * 
     * @return The row of the position.
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column of the position.
     * 
     * @return The column of the position.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Checks if this position is equal to another object.
     * 
     * @param obj The object to compare to.
     * @return True if the positions are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Check if the objects are the same
        if (obj == null || getClass() != obj.getClass()) return false; // Check if the object is null or of a different class
        Position position = (Position) obj; // Cast the object to a Position
        return row == position.row && column == position.column; // Check if the rows and columns are equal
    }

    /**
     * Generates a hash code for the position.
     * 
     * @return The hash code of the position.
     */
    @Override
    public int hashCode() {
        return 31 * row + column; // Calculate the hash code
    }
}
