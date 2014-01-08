package danielknowlesprojectjava.collections;

import danielknowlesprojectjava.collections.FeederCollection;
import danielknowlesprojectjava.components.Environment;
import danielknowlesprojectjava.components.Feeder;
import danielknowlesprojectjava.components.Food;
import java.awt.geom.Point2D;
import java.util.*;

/**
 * The static collection of Food objects in the environment.
 */
public class FoodCollection {
    // The food list
    private static ArrayList<Food> foods = new ArrayList<Food>();


    /**
     * Adds a random food object to the collection.
     * @return true if successful, false if not
     */
    public static boolean addFood() {
        // first, make sure we haven't reached the maximum food cap
        if (foods.size() < Environment.MAX_FOOD) {
            foods.add(Food.getRandomFood());
            return true;
        }

        return false;
    }

    /**
     * Adds a new food object to the collection.
     * @param f the food object to add
     * @return true if successful, false if not
     */
    public static boolean addFood(Food f) {
        // first, make sure we haven't reached the maximum food cap
        if (foods.size() < Environment.MAX_FOOD) {
            foods.add(f);
            return true;
        }

        return false;
    }

    /**
     * Adds a new food object to the collection.
     * @param width the width of the food
     * @param height the height of the food
     * @param location the location of the food in the environment
     * @return true if successful, false if not
     */
    public static boolean addFood(int width, int height, Point2D.Double location) {
        // first, make sure we haven't reached the maximum food cap
        if (foods.size() < Environment.MAX_FOOD) {
            foods.add(new Food(width, height, location));
            return true;
        }

        return false;
    }

    /**
     * Removes a food object from the foods list and from the feeders'
     * observed lists. Marks the food as eaten.
     * @param f the food object to remove
     */
    public static void removeFood(Food f) {
        f.markAsEaten();
        f = null;
        for (Feeder fr : FeederCollection.getFeeders()) {
            fr.removeFromObserved(f);
        }

        //foods.remove(f);
    }

    /**
     * Removes the food object at the specified index. Also removes food from
     * the feeders' observed lists. Marks the food as eaten.
     * @param index the index of the food object to remove
     */
//    public static void removeFoodAt(int index) {
//        foods.get(index).markAsEaten();
//        for (Feeder fr : FeederCollection.getFeeders()) {
//            fr.removeFromObserved(foods.get(index));
//        }
//       // Food f = foods.get(index);
//       // foods.remove(f);
//    }

    /**
     * Gets the foods list.
     * @return a list of food object
     */
    public static ArrayList<Food> getFoods() {
        return foods;
    }

    /**
     * Sets the foods list.
     * @param f the array list of food objects
     */
    public static void setFoods(ArrayList<Food> f) {
        foods = f;
    }

    /**
     * Clears the foods list, removing all food objects.
     */
    public static void clearFoods() {
        for (Food f : foods)
            f = null;
        foods.clear();
    }
}
