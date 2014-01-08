package danielknowlesprojectjava.components;

import danielknowlesprojectjava.collections.FeederCollection;
import danielknowlesprojectjava.collections.FoodCollection;
import danielknowlesprojectjava.geneticAlgorithm.GeneticEngine;
import danielknowlesprojectjava.geneticAlgorithm.Genotype;
import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * The environment that the feeders and food live in.
 */
public class Environment extends JComponent {
    // the width and height of the environment
    private static int width;
    private static int height;
    private static int x;
    private static int y;
    // tells the application if the envirnoment is running (useful for pausing)
    private boolean running;
    // the maximum number of feeders in the environment
    public static final int MAX_FEEDERS = 500;
    // the maximum number of food in the environment
    public static final int MAX_FOOD = 500;
    public static Random rnd = new Random();
    private static int numFeeders = 20;
    private static int numFoods = 250;
    // The genetic engine that will evaluate and evolve the feeders
    public GeneticEngine genE;
    // the number of iterations through the game loop to run through before
    //  advancing to the next generation
    private int numGenLoops = 7300; // 365 * 20
    private int genLoopCount = 0;
    private EnvironmentFrame parentFrame;

    /**
     * Creates a new environment with default values.
     */
    public Environment(EnvironmentFrame parent) {
        super();

        parentFrame = parent;
    }

    /**
     * Renders the environment and its child components.
     * @param g the graphics context that handles rendering objects on screen
     */
    @Override
    public void paintComponent(Graphics g)
    {
        // We use the paintComponent method for the game loop.
        // After drawing the feeders and food, call updateEnvironment().
        // updateEnvironment() calls repaint() when it's finished, which in turn
        // calls paintComponent(), and we do it all again.
        
        updateEnvironment(g);

    }

    /**
     * Tells the feeders to render themselves to the environment.
     * @param g the graphics context used to draw the feeders
     */
    private void drawFeeders(Graphics g) {
        // call the paint method for each feeder
        for (Feeder f : FeederCollection.getFeeders()) {
            f.paintComponent(g);
        }
    }

    /**
     * Tells the food objects to render themselves tothe environment.
     * @param g the graphics context used to draw the food objects
     */
    private void drawFood(Graphics g) {
        // call the paint method for each food
        for (Food f : FoodCollection.getFoods()) {
            // make sure the food hasn't been eaten before rendering it.
            if (f.isActive())
                f.paintComponent(g);
        }
    }

    /**
     * Updates the environment.
     */
    private void updateEnvironment(Graphics g) {
        // see if application is paused
        if (running) {
            drawFood(g);
            drawFeeders(g);
            // loop through all food and feeders
            for (Food fd : FoodCollection.getFoods()) {
                // make sure the food is still active
                if (fd.isActive()) {
                    for (Feeder fr : FeederCollection.getFeeders()) {
                        // if the feeder "sees" the food, react to it.
                        if (fd.isPerceived(fr)) {// &&
                                //!fr.getObservedFood().contains(fd)) {
                            fr.reactTo(fd);
                        }
                    }
                }
            }
            // update all of the feeders
            for (Feeder fr : FeederCollection.getFeeders()) {
                fr.update();
            }
                  
            // if the generation loop count is the same as the number of generation
            //  loops until the next generation, advance the generation
            if (++genLoopCount == numGenLoops) {
                advanceTheGeneration();
                // make sure we reset the loop counter
                genLoopCount = 0;
            }
        }
        // repaint the component
        repaint();
    }

    /**
     * Gets the genetic engine used by the environment.
     * @return the genetic engine used by the environment
     */
    public GeneticEngine getGeneticEngine() { return genE; }

    /**
     * Sets the number of feeders in the environment.
     * @param n the number of feeders in the environment
     */
    public static void setNumFeeders(int n) {
        if (n < MAX_FEEDERS) numFeeders = n;
        else numFeeders = MAX_FEEDERS;
    }
    /**
     * Gets the number of feeders in the environment.
     * @return the number of feeders in the environment
     */
    public static int getNumFeeders() { return numFeeders;}
    /**
     * Sets the number of food objects in the environment.
     * @param n the number of food objects in the environment
     */
    public static void setNumFoods(int n) {
        if (n < MAX_FOOD) numFoods = n;
        else numFoods = MAX_FOOD;
    }
    /**
     * Gets the number of food objects in the environment.
     * @return the number of food objects in the environment
     */
    public static int getNumFoods() { return numFoods; }

    @Override
    public int getWidth() { return width; }
    /**
     * Sets the width of the environment.
     * @param value the width of the environment
     */
    public void setWidth(int value) { width = value; }

    @Override
    public int getHeight() { return height; }
    /**
     * Sets the height of the environment.
     * @param value the height of the environment
     */
    public void setHeight(int value) { height = value; }

    /**
     * Gets the lowest x coordinate objects are allowed to have in the
     * environment.
     * @return the minimum x coordinate in the environment
     */
    public static int getMinX() { return x; }
    /**
     * Gets the highest x coordinate objects are allowed to have in the
     * environment.
     * @return the maximum x coordinate in the environment
     */
    public static int getMaxX() {
        return x + width - 15;
    }

    /**
     * Gets the lowest y coordinate objects are allowed to have in the
     * environment.
     * @return the minimum y coordinate in the environment
     */
    public static int getMinY() { return y; }
    /**
     * Gets the highest y coordinate objects are allowed to have in the
     * environment.
     * @return the maximum y coordinate in the environment
     */
    public static int getMaxY() { return y + height - 15; }

    /**
     * Creates random food objects and place them into the food collection.
     */
    private void createFoods() {
        FoodCollection.clearFoods();

        for (int i = 0; i < numFoods; i++) {
            FoodCollection.addFood(Food.getRandomFood());
        }
    }

    /**
     * Creates feeders from the genetic engine's population and adds them to
     * the feeder collection.
     */
    private void createFeeders() {
        FeederCollection.clearFeeders();

        for (Genotype g : genE.getPopulation()) {
            FeederCollection.addFeeder(new Feeder(g.getChromosome()));
        }
    }

    /**
     * Initializes the genetic engine with default values.
     */
    private void initializeGenE() {
        // create a new genetic engine
        genE = new GeneticEngine(numFeeders, Integer.MAX_VALUE, 12);
    }

    private void advanceTheGeneration() {
        // advance the population
        genE.evaluatePopulation();
        // create new feeders from the new population
        createFeeders();
        FoodCollection.clearFoods();
        createFoods();

        int genNumber = genE.getAverageFitnesses().size();
        double avgFit = genE.getAverageFitnesses().get(genNumber - 1);
        double highFit = genE.getHighestFitnesses().get(genNumber - 1);

        parentFrame.gpDialog.addTableRow(genNumber, avgFit, highFit);
    }

    @Override
    public int getX() { return x; }

    @Override
    public int getY() { return y; }

    @Override
    public void setLocation(int x, int y)
    {
        Environment.x = x;
        Environment.y = y;
    }
 
    /**
     * Pauses the application.
     */
    public void pause() { running = false; }

    /**
     * Un pauses the application.
     */
    public void unPause() { running = true; }

    /**
     * Determines if the application is currently paused.
     * @return true if paused, false if not
     */
    boolean isPaused() {
        return running;
    }

    /**
     * Clears the environment.
     */
    private void clearEnvironment() {
        // clear out collections
        FeederCollection.clearFeeders();
        FoodCollection.clearFoods();
        // reset the feeder counts
        Feeder.resetFeederCount();
    }

    /**
     * Resets the environment to its initial state during this run.
     */
    public void resetEnvironment() {
        parentFrame.gpDialog.clearTable();
        clearEnvironment();
        initializeEnvironment();
    }

    /**
     * Initializes the environment to its initial state with the current
     * options.
     */
    public void initializeEnvironment() {
        // initialize feeders list
        FeederCollection.setFeeders(new ArrayList<Feeder>());
        // initialize the foods list
        FoodCollection.setFoods(new ArrayList<Food>());
        // set default render options for feeders
        Feeder.showFieldOfVision();
        // add a border so that the user will know where the environment is
        // located on the parent frame.
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        initializeGenE();
        createFoods();
        createFeeders();
        // set the environment's running flag
        running = true;
    }

}
