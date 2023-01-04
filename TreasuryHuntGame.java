
import java.util.Random;
import java.util.Scanner;

/** 
 * @author Gunnar Hormann
*/

/**
 * An instance of this class holds the state of a running match and the game logic.
 */
public class TreasuryHuntGame {

    final Board playerBoard;
    final Board villainBoard;

    /**
     * Set to TRUE to keep the game loop running. Set to FALSE to exit.
     */
    boolean running;

    /**
     * When playing, enemy treasures should be hidden from the player.
     * Change below to FALSE for testing purposes during development of this program.
     */
    private final boolean hideVillainShips = false;

    /**
     * Creates a new game with new boards.
     */
    public TreasuryHuntGame() {
        playerBoard = new Board();
        villainBoard = new Board();
    }

    /**
     * Creates a game based on saved boards from a previous game.
     */
    public TreasuryHuntGame(Board playerBoard, Board villainBoard) {
        this.playerBoard = playerBoard;
        this.villainBoard = villainBoard;
    }


    /**
     * Main game loop. Keep running to play.
     * Interrupt the loop to get back to main menu.
     */
    public void run() {
        running = true;
        System.out.println("Spiel gestartet. Drücke ENTER während der Zieleingabe, um zum Hauptmenü zurückzukehren.");

        while (running) {
            playersTurn();
            if (running) villainsTurn();
        }
    }

    /**
     * Players turn during game loop
     * Allows player to pause or resume
     * as well as exit the game loop
     * not yet fully implemented
     */
    private void playersTurn() {

        System.out.println("Spieler ist am Zug.");
        villainBoard.print(hideVillainShips);

        int[] playerSearch = null;

        // TODO (s. Aufgabe 5)

        // player wants to exit game
        if (playerSearch == null) {
            System.out.println("Spiel pausiert.");
            running = false;
        }

        pause();
    }


    /**
     * Opponents turn during game loop
     * not yet fully implemented
     */
    private void villainsTurn() {

        System.out.println("Gegner ist am Zug.");
        playerBoard.print(false);
        int[] villainSearch = getVillainSearch();

        // TODO (s. Aufgabe 6)

        pause();
    }

    /**
     * Asks the user to press ENTER to continue.
     * Can be called anywhere in the game to avoid too much output at once.
     */
    private void pause() {
        System.out.println();
        System.out.println("Drücke ENTER um fortzufahren...");
        System.out.println();
        new Scanner(System.in).nextLine();
    }

    /**
     * Gets an array with the two coordinates (x,y) the villain shoots at.
     * Gives feedback to the player about opponents actions
     */
    private int[] getVillainSearch() {
        int x;
        int y;

        // Strategy to aim a shot: Pick a random field that is empty
        do {
            x = new Random().nextInt(Board.BOARD_SIZE);
            y = new Random().nextInt(Board.BOARD_SIZE);
        } while (playerBoard.getField(x, y) != Board.EMPTY);

        int[] shot = new int[]{x, y};
        System.out.println("Gegner sucht auf " + convertCoordinatesToString(shot));
        return shot;
    }

    /**
     * Checks if the game is finished
     * @return true if the game is finished
     */
    public boolean isFinished() {
        return playerBoard.areAllTreasuresFound() || villainBoard.areAllTreasuresFound();
    }


    /**
     * Converts alphanumeric board coordinates to array indexes, e.g. A1 to [0,0]
     * @return array index of the board
     */
    public static int[] convertCoordinatesToInt(String input) {
        int x = input.toUpperCase().charAt(0) - 65;
        int y = Integer.parseInt(input.substring(1)) - 1;
        return new int[]{x, y};
    }

    /**
     * Converts array indexes to ahlphanumeric board coordinates, e.g. [0,0] to A1
     * @return alphanumeric string of array index
     */
    public static String convertCoordinatesToString(int[] input) {
        char x = (char) (input[0] + 65);
        String y = Integer.toString(input[1]);
        return x + y;
    }
}
