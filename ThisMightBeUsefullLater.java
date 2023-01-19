public class ThisMightBeUsefullLater {
    

public void placeTreasures() {
    Random rand = new Random();
    Set<String> smallTreasureOccupiedSpots = new HashSet<>();
    Set<String> bigTreasureOccupiedSpots = new HashSet<>();
    Set<String> triedArrangements = new HashSet<>();
    boolean placedBigTreasure = false;

    /*loop until big treasure is placed */
    while (!placedBigTreasure) {

        smallTreasureOccupiedSpots.clear();
        int treasuresPlaced = 0;
        String curArrangement = "";

        /*Add small treasures */
        while (treasuresPlaced < treasureCount) {

            /*generate random coordinate */
            int x = rand.nextInt(BOARD_SIZE);
            int y = rand.nextInt(BOARD_SIZE);
            String spot = x + "," + y; //store coordinates as string
            curArrangement += spot + "-"; //store arrangement of all generated small treasures as string

            /*Add treasures on empty spots */
            if (!smallTreasureOccupiedSpots.contains(spot)) {
                fields [x][y] = TREASURE;
                smallTreasureOccupiedSpots.add(spot);
                treasuresPlaced++;
            }
        }
        /*check if generated arrangement of all treasures was generated previously and re-generates if true */
        if (triedArrangements.contains(curArrangement)) {
            continue;
        }
        triedArrangements.add(curArrangement);

        /*Add big treasure */

        int x = rand.nextInt(BOARD_SIZE - bigTreasureSize + 1);
        int y = rand.nextInt(BOARD_SIZE - bigTreasureSize + 1);
        int orientation = rand.nextInt(2);
        boolean isValid = true;

        /*Check if big treasure fits horizontally or vertically */
        if(orientation == 0) { //horizontal check
            for(int i = y; i < y + bigTreasureSize; i++) {
                String spot = x + "," + i;
                if (bigTreasureOccupiedSpots.contains(spot) || smallTreasureOccupiedSpots.contains(spot)) {
                    isValid = false;
                    break;
                }
            }
        } else { //vertical check
            for(int i = x; i < x + bigTreasureSize; i++) {
                String spot = i + "," + y;
                if (bigTreasureOccupiedSpots.contains(spot) || smallTreasureOccupiedSpots.contains(spot)) {
                    isValid = false;
                    break;
                }
            }
        }

        /*Add big treasure */
        if (isValid) {
            if(orientation == 0) {
                for(int i = y; i < y + bigTreasureSize; i++) {
                    fields [x][i] = TREASURE;
                    bigTreasureOccupiedSpots.add(x + "," + i);
                }
            } else {
                for (int i = y; i < x + bigTreasureSize; i++) {
                    fields [x][i] = TREASURE;
                    bigTreasureOccupiedSpots.add(i + "," + y);
                }
            }
            placedBigTreasure = true;
        }
    }
}
}