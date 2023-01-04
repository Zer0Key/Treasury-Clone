import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

/** 
 * @author Gunnar Hormann
*/

/**
 * Requires Java 11 or higher.
 * Contains main method to initialize the game
 * Provides methods for loading, saving and resuming the game
 * as well as the main menu
 */
public class TreasuryHuntApp {

    private TreasuryHuntGame game;
    private final Path saveFilePath = Path.of("treasuryHunt.save");

    public static void main(String[] args) {
        TreasuryHuntApp treasuryHuntApp = new TreasuryHuntApp();
        treasuryHuntApp.mainMenu();
    }

    /**
     * Main menu of the game
     * not yet implemented
     */
    private void mainMenu() {

        // TODO print main menu to the console. Let user select an option. (s. Aufgabe 4)


        startNewGame();





    }

    /**
     * Restores a game from the file "treasuryHunt.save"
     */
    private void loadGame() {
        if (!hasSavedGame()) {
            System.out.println("Kein gespeicherter Spielstand vorhanden.");
            return;
        }

        try {
            String saveGame = Files.readString(saveFilePath, StandardCharsets.UTF_8);
            String[] boards = saveGame.split("\n");
            Board playerBoard = new Board(boards[0]);
            Board villainBoard = new Board(boards[1]);
            this.game = new TreasuryHuntGame(playerBoard, villainBoard);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Loading failed");
        }
    }

    /**
     * Saves a game into the file "treasuryHunt.save"
     */
    private void saveGame() {
        File file = saveFilePath.toFile();

        if (file.exists()) file.delete();
        try {
            file.createNewFile();

            String playerBoard = game.playerBoard.exportAsString();
            String villainBoard = game.villainBoard.exportAsString();
            Files.writeString(file.toPath(), playerBoard + villainBoard, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Save failed");
        }
    }

    /**
     * Checks if file "treasuryHunt.save" exists
     */
    private boolean hasSavedGame() {
        return saveFilePath.toFile().exists();
    }

    private boolean hasRunningGame() {
        return !(game == null || game.isFinished());
    }

    private void continueGame() {
        this.game.run();
    }

    private void startNewGame() {
        this.game = new TreasuryHuntGame();
        continueGame();
    }

}
