
import java.util.ArrayList;
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

    String userName;
    int attempts = 0;



    /**
     * Set to TRUE to keep the game loop running. Set to FALSE to exit.
     */
    boolean running;


    /**
     * When playing, enemy treasures should be hidden from the player.
     * Change below to FALSE for testing purposes during development of this program.
     */
    private final boolean hideVillainShips = true;

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
     * Gets user name for leaderboard
     */
    public void run() {
        running = true;
        TreasuryHuntApp.clearScreen();
        System.out.println("Spiel gestartet. Drücke ENTER während der Zieleingabe, um zum Hauptmenü zurückzukehren.");
        System.out.println("Gib deinen Namen ein: ");
        Scanner newUser = new Scanner(System.in);
        userName = newUser.next();
        TreasuryHuntApp.clearScreen();

        while (running) {
            playersTurn();
            if (running) villainsTurn();
        }
    }

    /**
     * Players turn during game loop
     * Allows player to pause or resume
     * as well as exit the game loop
     */
    private void playersTurn() {

        boolean miss = false;

        while (!miss && running) {
            while(!isFinished()) {
                while (true) {

                    /*Get user input */
                    System.out.println("Spieler ist am Zug.");
                    villainBoard.print(hideVillainShips);
                    System.out.println("Gebe die Koordinaten ein, an denen du suchen willst! Z.B. A1");
                    System.out.println("Drücke ENTER nach deiner Eingabe um sie zu bestätigen");
                    Scanner playerAim = new Scanner(System.in);
                    String input = playerAim.nextLine();
                    
                    /*Let user return to main menu */
                    if (input.isEmpty()) {
                        System.out.println("Spiel pausiert.");
                        System.out.println("Willst du zum Hauptmenü zurückkehren?");
                        Scanner returnMain = new Scanner(System.in);
                        String inputReturn = returnMain.nextLine();
                        if(inputReturn.equalsIgnoreCase("Y")) {
                            running = false;
                            break;
                        } else if (inputReturn.equalsIgnoreCase("N")) {
                            continue;
                        } else {
                            System.out.println("Falsche Eingabe! Bitte Y oder N eingeben!");
                        }
                        break;
                    }
                
                    /*Validate user input for correct format */
                    if (!input.matches("[A-Ea-e][1-5]")) {
                        System.out.println("Ungültige Eingabe. Bitte versuche es erneut.");
                        continue;
                    }
                
                    /*Compare user input with vilain board and output response
                     * add attempt for each input for leaderboard
                     */
                    int[] playerSearch = convertCoordinatesToInt(input);
                    int x = playerSearch[0];
                    int y = playerSearch[1];
                    if (villainBoard.getField(x, y) == Board.TREASURE) {
                        attempts++;
                        System.out.println();
                        System.out.println("Treffer!");
                        System.out.println();
                        villainBoard.setField(x, y, Board.HIT);
                        isFinished();
                    } else {
                        attempts++;
                        System.out.println();
                        System.out.println("Daneben!");
                        System.out.println();
                        villainBoard.setField(x, y, Board.NO_TREASURE_FOUND);
                        miss = true;
                    }
                    break;
                }
                /*Pause after every player turn
                 * Don't pause when exiting the game
                 */
                if(!isFinished() && running) {
                    pause();
                }
                break;
            }
            /*Check for win condition and end game if true */
            if (isFinished()) {
                System.out.println("Du hast gewonnen!");
                System.out.println();
                System.out.println("Drücke ENTER um zum Hauptmenü zurückzukehren!");
                new Scanner(System.in).nextLine();
                TreasuryHuntApp.addScore(userName, attempts);
                running = false;
                break;
            }

        }
            
    }
    

    /**
     * Opponents turn during game loop
     */
    private void villainsTurn() {

        boolean miss = false;

        while(!miss && true){
            while(!isFinished()) {
                while(true){

                    /*Report to user what the vilain is doing */
                    System.out.println("Gegner ist am Zug.");
                    playerBoard.print(false);
                    int[] villainSearch = getVillainSearch();
                    int x = villainSearch[0];
                    int y = villainSearch[1];
                    if (playerBoard.getField(x, y) == Board.TREASURE) {
                        System.out.println();
                        System.out.println("Treffer!");
                        System.out.println();
                        playerBoard.setField(x, y, Board.HIT);
                        isFinished();
                    } else {
                        System.out.println();
                        System.out.println("Daneben!");
                        System.out.println();
                        playerBoard.setField(x, y, Board.NO_TREASURE_FOUND);
                        miss = true;
                    }
                    break;
                }
                if(!isFinished()){
                    pause();
                }
                break;
            }
            if (isFinished()) {
                System.out.println("Der Gegner hat gewonnen :(");
                System.out.println();
                System.out.println("Drücke ENTER um zum Hauptmenü zurück zu kehren!");
                new Scanner(System.in).nextLine();
                running = false;
                break;
            }
        }
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
     * Does not shoot the same coordinates twice.
     * Gives feedback to the player about opponents actions
     * @return Targeted coordinates of the AI
     */
    private int[] getVillainSearch() {
        
        ArrayList<int[]> attemptedShots = new ArrayList<int[]>(); //Stores coordinates of previous shots
        int[] shot = new int[2];
        boolean validShot = false;
        Random rand = new Random();
    
        while(!validShot){
            shot[0] = rand.nextInt(Board.BOARD_SIZE);
            shot[1] = rand.nextInt(Board.BOARD_SIZE);
    
            if(!attemptedShots.contains(shot)) { // Check if target is already in the list
                validShot = true;
                attemptedShots.add(shot);
            }
        }
        System.out.println("Gegner sucht auf " + convertCoordinatesToString(shot));
        TreasuryHuntApp.waitFor(1000);
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
        String y = Integer.toString(input[1] + 1);
        return x + y;
    }
}
