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
    /**
     * Main method for running the game
     */
    public static void main(String[] args) {
        TreasuryHuntApp treasuryHuntApp = new TreasuryHuntApp();
        treasuryHuntApp.splashScreen();
        treasuryHuntApp.mainMenu();
    }
    /**
     * Splash screen during startup
     */
    private void splashScreen() {
        clearScreen();
        System.out.println("               Schatzsuche");
        System.out.println();
        System.out.println("                    |");
        System.out.println("                    |");
        System.out.println("             |    __-__        ");
        System.out.println("           __-__ /  | (            ");
        System.out.println("          /  | ((   | |        ");
        System.out.println("        /(   | ||___|_.  .|        ");
        System.out.println("      .' |___|_|`---|-'.' (            ");
        System.out.println(" '-._/_| (   |\\     |.'    \\        ");
        System.out.println("     '-._|.-.|-.    |'-.____'.        ");
        System.out.println("         |------------------'        ");
        System.out.println("          `----------------'");
        System.out.println();
        System.out.println("Dieses Spiel wird durch Zahleneingaben navigiert");
        System.out.println();
        System.out.println("Drücke ENTER um ins Hauptmenü zu gelangen");
        System.out.println();
        new Scanner(System.in).nextLine();
        
    }

    /**
     * Main menu of the game
     * Allows for the user to select one of five options
     * Not all optians are allways available depending on the game state
     */
    public void mainMenu() {
        while(true){
            clearScreen();
            /*Create array with menu options */
            String[] menuOptions = new String[] {"Neues Spiel starten", "Spiel fortsetzen", "Spiel laden", "Spiel speichern", "Spiel beenden"};
            int count = 0;
            /*Iterate through array and only display available options */
            for(String element : menuOptions) {
                count++;
                if((count == 2 || count == 4) && !hasRunningGame()) {
                    continue;
                }
                if(count == 3 && !hasSavedGame()) {
                    continue;
                }
                System.out.println("(" + count + ")" + element);
            }
            count = 0;

            /**
             * Gets user input to navigate the menu
             * Handles input from user that tries to access hidden fields
             * Handles invalid user input
             */
            Scanner navInput = new Scanner(System.in);
            if(navInput.hasNextInt()) {
            int input = navInput.nextInt();
            switch (input) {
                case 1:
                clearScreen();
                startNewGame();
                break;

                case 2:
                if(!hasRunningGame()) {
                    /*Print error message, wait 3 seconds and then return to main menu */
                    clearScreen();
                    System.out.println("Momentan läuft kein Spiel! \n \nStarte ein neues Spiel!");
                    System.out.println();
                    System.out.println("Kehre zum Hauptmenü zurück...");
                    waitFor(3000);
                } else {
                    clearScreen();
                    continueGame();
                }
                break;

                case 3:
                if(!hasSavedGame()) {
                    /*Print error message, wait 3 seconds and then return to main menu */
                    clearScreen();
                    System.out.println("Kein gespeichertes Spiel gefunden! \n \nStarte ein neues Spiel!");
                    System.out.println();
                    System.out.println("Kehre zum Hauptmenü zurück...");
                    waitFor(3000);
                } else {
                    System.out.println("Lade Spiel...");
                    waitFor(1000);
                    loadGame();
                    clearScreen();
                    continueGame();
                }
                break;

                case 4:
                if(!hasRunningGame()) {
                    /*Print error message, wait 3 seconds and then return to main menu */
                    clearScreen();
                    System.out.println("Kein laufendes Spiel zum speichern gefunden! \n \nStarte ein neues Spiel!");
                    System.out.println();
                    System.out.println("Kehre zum Hauptmenü zurück...");
                    waitFor(3000);
                } else {                
                    saveGame();
                    System.out.println("Spiel gespeichert!");
                    System.out.println("Kehre zum Hauptmenü zurück...");
                    waitFor(3000);
                }
                break;

                case 5:
                clearScreen();
                System.out.println("Bist du dir sicher, dass du das Spiel beenden willst? \n \nY/N");
                /*Wait for user input and either exit app or return to main menu */
                while (true) {
                    String inputExit = new Scanner(System.in).nextLine();
                    if(inputExit.equalsIgnoreCase("Y")) {
                        System.exit(0);
                    } else if (inputExit.equalsIgnoreCase("N")) { 
                        break;}
                    else {
                        System.out.println("Falsche Eingabe. Bitte Y oder N eingeben!");
                    }
                }
                break;

                default:
                /*In case any integr other than 1-5 is entered
                * Displays error message and reloads main menu with delay
                */
                clearScreen();
                System.out.println("Falsche Eingabe!");
                System.out.println();
                System.out.println("Kehre zum Hauptmenü zurück...");
                waitFor(3000);
                }
            } else {
                /*In case of wrong input returns error message and reloads main menu */
                clearScreen();
                System.out.println("Falsche Eingabe!\nBitte Zahl eingeben!");
                System.out.println();
                System.out.println("Kehre zum Hauptmenü zurück...");
                navInput.next();
                waitFor(3000);
            }
        }
    }
    /**
     * Method for waiting for a certain amount of time
     * @param millis Integer of milliseconds to wait
     */
    public static void waitFor(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    /**
     * Method for clearing the screen
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
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
