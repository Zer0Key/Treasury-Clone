import java.util.Comparator;
import java.util.Map;

public class ValueComparator implements Comparator<String> {

    /**
     * Used to sort Maps by key-value pairs
     * Implements the comparator interface to allow value sorting
     */
    
    Map<String, Integer> base;

    /**
     * @param base Map to be sorted
     */
    public ValueComparator (Map<String, Integer> base) {
        this.base = base;
    }

    /**
     * Used to compare two values in the Map.
     * @return 1 if a >= than b
     * @return -1 if a < b
     */
    public int compare (String a, String b) {
        if(base.get(a) >= base.get(b)) {
            return 1;
        } else {
            return -1;
        }
    }
}