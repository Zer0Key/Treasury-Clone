import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
/** 
 * @author Gunnar Hormann
*/

/**
 * Holds the state of one players board
 * as well as the methods to generate a board and process searches.
 */
public class Board {

    public static final char EMPTY = '.';
    public static final char TREASURE = 'O';
    public static final char HIT = 'X';
    public static final char NO_TREASURE_FOUND = '-';

    public static final int BOARD_SIZE = 5;

    private final char[][] fields = new char[BOARD_SIZE][BOARD_SIZE];
    private final int bigTreasureSize = 4;
    private final int treasureCount = 4;

    /**
     * Creates a new board
     * Calls placeTreasures function to place treasures randomly on the board
     */
    public Board() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                fields[i][j] = EMPTY;
            }
        }
        placeTreasures();
    }

    /**
     * Places treasures on the board
     * prevents treasures from being placed ontop one another
     * prevents treasures from being placed next to another except diagonally
     * Works with any size of board and any size of big treasure as long as big treasure is BOARD_SIZE-1
     * and as long as the aspect ratio of the board is 1:1
     */
    public void placeTreasures() {

        Random rand = new Random();
        Set<String> occupiedSpots = new HashSet<String>();

        /* Placing large treasure 
         * 
         * Avoiding array overflow and randomly setting starting index */
        int x = rand.nextInt(BOARD_SIZE-bigTreasureSize+1);
        int y = rand.nextInt(BOARD_SIZE-bigTreasureSize+1);
        int orientation = rand.nextInt(2); // randomly chosing orientation

        if(orientation == 0) { // placement if horizontal orientation
            for(int i = y; i < y+bigTreasureSize; i++) {
                fields[x][i] = TREASURE;
                occupiedSpots.add(x + "," + i); // keeping track of where treasure is placed
            }
        }else { // placement if vertical orientation
            for(int i = x; i < x+bigTreasureSize; i++) {
                fields[i][y] = TREASURE;
                occupiedSpots.add(i + "," + y); // keeping track of where treasure is placed
            }
        }

        /*Placing small treasures */
        int treasuresPlaced = 0;
        while (treasuresPlaced < treasureCount) {
            int smallX = rand.nextInt(BOARD_SIZE); // generating random position
            int smallY = rand.nextInt(BOARD_SIZE);
            String spot = smallX + "," + smallY; // keeping track of where treasure is placed

            /* Checking if spot is free as well as not next to another occupied spot
             * quite convoluted condition for if statement could use improvement
             */
            if(fields[smallX][smallY] == EMPTY &&
                !occupiedSpots.contains(spot) &&
                !occupiedSpots.contains((smallX+1) + "," + smallY) &&
                !occupiedSpots.contains((smallX-1) + "," + smallY) &&
                !occupiedSpots.contains(smallX + "," + (smallY+1)) &&
                !occupiedSpots.contains(smallX + "," + (smallY-1))) {
                fields[smallX][smallY] = TREASURE;
                occupiedSpots.add(spot);
                treasuresPlaced++;
            }
        }

    }

    /**
     * Create a Board from an exported string.
     */
    public Board(String savedBoard) {
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                int index = y * BOARD_SIZE + x;
                fields[x][y] = savedBoard.charAt(index);
            }
        }
    }

    /**
     * Prints the board to System.out
     *
     * @param hideTREASUREs if TRUE, replaces TREASUREs by empty fields in output
     */
    public void print(boolean hideTREASUREs) {
        /* print column headers A - J */
        System.out.print("# ");
        for (int x = 0; x < fields[0].length; x++) {
            char column = (char) (x + 65);
            System.out.print(" " + column);
        }
        System.out.println();

        for (int y = 0; y < fields.length; y++) {
            /* print row number */
            int rowNumber = y + 1;
            System.out.print(rowNumber + " ");
            if (rowNumber < 10) System.out.print(" ");

            /* print row */
            for (int x = 0; x < fields[y].length; x++) {
                char output = fields[x][y];
                if (output == TREASURE && hideTREASUREs)
                    output = EMPTY;
                System.out.print(output + " ");
            }
            System.out.println();
        }
    }
    /** 
     * Converts the board array to a string
     * @return String representation of the board
     */
    public String exportAsString() {
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                builder.append(fields[x][y]);
            }
        }
        builder.append("\n");
        return builder.toString();
    }

    /**
     * @return FALSE if at least one TREASURE is remaining. TRUE otherwise.
     */
    public boolean areAllTreasuresFound() {
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                if (fields[x][y] == TREASURE)
                    return false;
            }
        }
        return true;
    }
    /**
     * Returns content of specified array index
     * @param x Column index of fields array
     * @param y Row index of fields array
     * @return content of specified array index
     */
    public char getField(int x, int y) {
        return fields[x][y];
    }
}
