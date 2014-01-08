package danielknowlesprojectjava.collections;

import danielknowlesprojectjava.components.Environment;
import danielknowlesprojectjava.components.Feeder;
import java.util.ArrayList;

/**
 * Stores a collection of feeder objects that live in the environment.
 */
public class FeederCollection {
    // collection of feeders in the environment
    private static ArrayList<Feeder> feeders;

    /**
     * Sets the feeder list.
     * @param f the arraylist of feeders
     */
    public static void setFeeders(ArrayList<Feeder> f) {
        feeders = f;
    }

    /**
     * Gets the feeders list.
     * @return an arraylist of feeder objects
     */
    public static ArrayList<Feeder> getFeeders() {
        return feeders;
    }

    /**
     * Adds a feeder to the collection.
     * @param f the feeder to add
     * @return true if successful, false if not
     */
    public static boolean addFeeder(Feeder f) {
        // first, make sure that we haven't reached the maximum feeder cap
        if (feeders.size() < Environment.MAX_FEEDERS) {
            feeders.add(f);
            return true;
        }

        return false;
    }

    /**
     * Adds a feeder to the collection.
     * @param speed the speed of the feeder
     * @param eyesight the eye strength of the feeder
     * @param intelligence the intelligence level of the feeder
     * @return true if successful, false if not
     */
    public static boolean addFeeder(int speed, int eyesight, int intelligence) {
        // first, make sure that we haven't reached the maximum feeder cap
        if (feeders.size() < Environment.MAX_FEEDERS) {
            feeders.add(new Feeder(speed, eyesight, intelligence));
            return true;
        }

        return false;
    }

    /**
     * Adds a random feeder to the collection.
     * @return true if successful, false if not
     */
    public static boolean addFeeder() {
        // first, make sure we haven't reached the maximum feeder cap
        if (feeders.size() < Environment.MAX_FEEDERS) {
            feeders.add(Feeder.getRandomFeeder());
            return true;
        }

        return false;
    }

    /**
     * Removes a feeder from the collection.
     * @param f the feeder to remove
     */
    public static void removeFeeder(Feeder f) {
        feeders.remove(f);
        f = null;
    }

    /**
     * Removes the feeder at the specified index.
     * @param index the index of the feeder to remove
     */
//    public static void removeFeederAt(int index) {
//        feeders.remove(index);
//
//    }

    /**
     * Clears the feeders collection.
     */
    public static void clearFeeders() {
        for (Feeder f : feeders)
            f = null;
        feeders.clear();
    }
}
