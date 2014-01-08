package danielknowlesprojectjava.components;

import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Point2D.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * The feeder object that will be searching the environment and eating food.
 */
public class Feeder extends JComponent {
    // render flags for attributes
    private static boolean displayFov = true;
    private static boolean displayID = true;
    private static boolean displayFoodEaten = true;// direction in radians
    private double direction = 0.0;
    // raw trait values (used for evaluation)
    private int eyesight = 0;
    private int intelligence = 0;
    private int speed = 0;
    // effective trait values (used for environment interactions)
    private double effectiveEyesight = 0.0;
    private double effectiveIntelligence = 0.0;
    private double effectiveSpeed = 0.0;
    // max values
    public static final int MAXEYESIGHT = 15;
    public static final int MAXINTELLIGENCE = 15;
    public static final int MAXSPEED = 15;
    // modifiers (multiply by raw values to get effective values
    private static double eyesightModifier = 6.67;
    private static double intelligenceModifier = 6.67;
    private static double speedModifier = 0.013; // TODO: set speed modifier
    // component values
    private double width = 5;
    private double height = 5;
    // feeder's movement vector (initilize to (0,0))
    private Point2D.Double movementVector = new Point2D.Double();
    // feeder count and ID are used to identify feeders in the UI
    private static int feederCount = 0;
    private int feederID;
    // the feeder's current location
    private Point2D.Double currentLocation = new Point2D.Double();
    // perception points that define the field of vision
    private Point2D perPoint1;
    private Point2D perPoint2;
    // the number of food objects eaten
    private int foodEaten = 0;
    // list of food that has been observed by the feeder
    private ArrayList<Food> observedFood = new ArrayList<Food>();

    /**
     * Creates a new feeder.
     */
    public Feeder() {
        // set the feeder's uid
        setFeederUid();
        // set a random location for the feeder
        setRandomLocation();
    }

    /**
     * Creates a new feeder.
     * @param chromosome the feeder's chromosome array
     */
    public Feeder(boolean[] chromosome) {
        // split the chromosome into thirds (one third for each trait).
        int traitLength = chromosome.length;
        traitLength /= 3;
        // set the feeder traits value by "decoding" the chromosome array.
        // this will be done by creating three binary strings corresponding to
        // the three parts of the chromosome array. then, the base 10
        // representation will be the actual trait value.
        int indexCount = 0;

        // set speed
        String binSpeed = "";
        for (int i = 0; i < traitLength; i++) {
            if (chromosome[indexCount]) binSpeed += "1";
            else binSpeed += "0";
            indexCount++;
        }
        speed = Integer.parseInt(binSpeed, 2);
        effectiveSpeed = speed * speedModifier;

        // set eyesight
        String binEyesight = "";
        for (int i = 0; i < traitLength; i++) {
            if (chromosome[indexCount]) binEyesight += "1";
            else binEyesight += "0";
            indexCount++;
        }
        eyesight = Integer.parseInt(binEyesight, 2);
        effectiveEyesight = eyesight * eyesightModifier;
        
        // set intelligence
        String binIntelligence = "";
        for (int i = 0; i < traitLength; i++) {
            if (chromosome[indexCount]) binIntelligence += "1";
            else binIntelligence += "0";
            indexCount++;
        }
        intelligence = Integer.parseInt(binIntelligence, 2);
        effectiveIntelligence = intelligence * intelligenceModifier;

        setFeederUid();
        setRandomLocation();
        setRandomMovementVector();
    }

    /**
     * Creates a new feeder.
     * @param speed the feeder's speed
     * @param eyesight the feeder's eyesight strength
     * @param intelligence the feeder's intelligence level
     */
    public Feeder(int speed, int eyesight, int intelligence) {
       // set the feeder's uid
        setFeederUid();
        this.speed = speed;
        effectiveSpeed = speed * speedModifier;
        this.eyesight = eyesight;
        effectiveEyesight = eyesight * eyesightModifier;
        this.intelligence = intelligence;
        effectiveIntelligence = intelligence * intelligenceModifier;
        setRandomMovementVector();
    }

    /**
     * Sets the current location of the feeder to a random point in the 
     * environment.
     */
    private void setRandomLocation() {
        currentLocation = new Point2D.Double(
                (double) (Math.random() * Environment.getMaxX()),
                (double) (Math.random() * Environment.getMaxY()));
    }

    /**
     * Sets the feeder's unique ID
     */
    private void setFeederUid() {
        // increment feeder count and set feeder ID
        feederID = ++feederCount;
    }

    /**
     * Creates a feeder with random genetic trait values.
     * @return a random feeder
     */
    public static Feeder getRandomFeeder() {
        // create random trait values
        int s = Environment.rnd.nextInt(MAXSPEED + 1);
        int e = Environment.rnd.nextInt(MAXEYESIGHT + 1);
        int i = Environment.rnd.nextInt(MAXINTELLIGENCE + 1);

        // return a new feeder using the previous traits
        return new Feeder(s, e, i);
    }

    /**
     * Sets the feeder's movement vector to a random vector based on its speed.
     */
    private void setRandomMovementVector() {
        // set direction to random number
        direction = (Math.random() * 360) * (Math.PI / 180);
        // convert to radians
        //direction *= (Math.PI / 180);
        // set movement vector to the direction * speed
        setMovementVector();
    }

    /**
     * Sets the movement vector based on the direction and speed of the feeder.
     */
    private void setMovementVector() {
        // set movement vector to the direction * speed
        movementVector = new Point2D.Double(Math.cos(direction) * effectiveSpeed,
                                            Math.sin(direction) * effectiveSpeed); 
    }

    /**
     * Gets the feeder's effective speed.
     * @return the feeder's effective speed
     */
    public double getEffectiveSpeed() {
        return effectiveSpeed;
    }

    /**
     * Defines the available quadrants to use when creating a random movement
     * vector.
     */
    private enum QUADRANT {
        ONE,            // vector can be created in quad one
        TWO,            // quad two
        THREE,          // quad three
        FOUR,           // quad four
        ONE_OR_TWO,     // either quad one or quad two
        ONE_OR_FOUR,    // either quad one or quad four
        TWO_OR_THREE,   // either quad two or quad three
        THREE_OR_FOUR   // either quad three or quad four
    };

    private void setRandomMovementVector(QUADRANT whatQuad) {
        // set the direction based on the available quadrant
        // the comments specify degrees, but the direction is being converted
        // to radians.
        switch (whatQuad) {
            case ONE:           // angle between 0 and 90 degrees
                setRandomDirection(0.1, 89.9);
                break;
            case TWO:           // between 90 and 180 degrees
                setRandomDirection(90.1, 179.9);
                break;
            case THREE:         // between 180 and 270 degrees
                setRandomDirection(180.1, 269.9);
                break;
            case FOUR:          // between 270 and 360 degrees
                setRandomDirection(27.1, 359.9);
                break;
            case ONE_OR_TWO:    // between 0 and 180 degrees
                setRandomDirection(0.1, 179.9);
                break;
            case ONE_OR_FOUR:   // in quad one or four
                // determine which quad to use
                double decision = Math.random() * 2;
                if (decision <= 1)  // quad one
                    setRandomDirection(0.1, 89.9);
                else                // quad four
                    setRandomDirection(270.1, 359.9);
                break;
            case TWO_OR_THREE:  // between 90 and 270 degrees
                setRandomDirection(90.1, 269.9);
                break;
            case THREE_OR_FOUR: // between 180 and 360 degrees
                setRandomDirection(180.1, 359.9);
                break;
        }

        // set movement vector
        setMovementVector();
    }

    /**
     * Sets a random direction (in radians).
     * @param minTheta the minimum value for the angle (in degrees)
     * @param maxTheta the maximum valud for the angle (in dgrees)
     */
    private void setRandomDirection(double minTheta, double maxTheta) {
        double degrees = Math.random() * maxTheta + minTheta;
        direction = degrees * (Math.PI / 180);
    }

    /**
     * Determines if the feeder's field of vision is displayed.
     * @return true if vision is displayed, false if not
     */
    public static boolean isVisionDisplayed() { return displayFov; }

    /**
     * Shows all feeder fields of vision.
     */
    public static void hideFieldOfVision() { displayFov = false; }
    
    /**
     * Hides all feeder fields of vision.
     */
    public static void showFieldOfVision() { displayFov = true; }

    /**
     * Renders the feeder in the environment.
     * @param g the graphics context that draws the feeder
     */
    @Override
    public void paintComponent(Graphics g) {
        String additionalInfo = "";
        if (displayID)
            additionalInfo += String.valueOf(feederID);

        if (displayFoodEaten) {
            if (displayID)
                additionalInfo += " : ";
            additionalInfo += String.valueOf(foodEaten);
        }

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.WHITE);
        Ellipse2D e = new Ellipse2D.Double(
            currentLocation.getX(), currentLocation.getY(), width, height);
        g2.fill(e);

        g2.setColor(Color.BLACK);
        g2.drawString(additionalInfo, (float) getCenter().getX(),
                     (float) getCenter().getY());

        g2.draw(e);

        calcPerceptionPoints();

        if (displayFov)
            drawLineOfSight(g2);
    }

    /**
     * Gets the center of the feeder's graphical display.
     * @return the center of the feeder's graphical display
     */
    public Point2D getCenter()
    {
        double centerX = currentLocation.getX() + (double) (width / 2);
        double centerY = currentLocation.getY() + (double) (height / 2);

        return new Point2D.Double(centerX, centerY);
    }

    /**
     * Gets the feeder's first line of sight perception point.
     * @return the feeder's first line of sight perception point
     */
    public Point2D getPerPoint1() { return perPoint1; }

    /**
     * Gets the feeder's second line of sight perception point.
     * @return the feeder's second line of sight perception point
     */
    public Point2D getPerPoint2() { return perPoint2; }

    /**
     * Sets the feeder count to zero
     */
    public static void resetFeederCount() { feederCount = 0; }

    /**
     * Decrements the feeder count by one.
     */
    public static void decrementPersonNumber() { feederCount--; }

    /**
     * Set the values for the points that define the feeder's line of sight.
     */
    private void calcPerceptionPoints() {
        double centerX = getCenter().getX();
        double centerY = getCenter().getY();

        // Get the mid-point of the farthest line of the perception triangle
        Point2D.Double endPoint = getEndPoint(
            new Point2D.Double(centerX, centerY),
            movementVector, effectiveEyesight);

        // Get the top point of the perception triangle
        // First, we need to modify the slope based on the quadrant the person is looking at
        Point2D[] newSlopes = getNewSlopes();
        double distance2 = (effectiveEyesight * Math.sin(Math.toDegrees(60))) /
                Math.sin(Math.toDegrees(30));
        perPoint1 = getEndPoint(endPoint, newSlopes[0], distance2);
        perPoint2 = getEndPoint(endPoint, newSlopes[1], distance2);
    }

    /**
     * Displays the person's line of sight as a triangle. It should be noted
     * that the person will not be able to see things at the outer angles of
     * the triangle. The line of sight is limited by the person's sight distance.
     * You should view the outer angles and edge more like an arc.
     * @param g2 the graphics context
     */
    private void drawLineOfSight(Graphics2D g2)
    {
        double centerX = getCenter().getX();
        double centerY = getCenter().getY();
        g2.setColor(Color.LIGHT_GRAY);

        g2.drawLine((int) Math.round(centerX),
                    (int) Math.round(centerY),
                    (int) Math.round(perPoint1.getX()),
                    (int) Math.round(perPoint1.getY()));

        // Get the bottom point of the perception triangle
        g2.drawLine((int) Math.round(centerX),
                    (int) Math.round(centerY),
                    (int) Math.round(perPoint2.getX()),
                    (int) Math.round(perPoint2.getY()));

        g2.drawLine((int) Math.round(perPoint1.getX()),
                    (int) Math.round(perPoint1.getY()),
                    (int) Math.round(perPoint2.getX()),
                    (int) Math.round(perPoint2.getY()));

        g2.setColor(Color.BLACK);
    }

    /**
     * Helper method that gets an array of two slopes used to build the line
     * of sight triangle.
     * @return the slopes of the side edges of the line of sight triangle
     */
    private Point2D[] getNewSlopes()
    {
        double mX = movementVector.getX();
        double mY = movementVector.getY();
        Point2D[] newSlopes = new Point2D.Double[2];

        newSlopes[0] = movementVector;
        newSlopes[1] = movementVector;

        if (mX < 0 && mY != 0)
        {
            // Looking to the top left or bottom left
            newSlopes[0] = new Point2D.Double(mY, mX * -1);
            newSlopes[1] = new Point2D.Double(mY * -1, mX);
        }
        else if (mX > 0 && mY != 0)
        {
            // Looking to the top right or bottom right
            newSlopes[0] = new Point2D.Double(mY, mX * -1);
            newSlopes[1] = new Point2D.Double(mY * -1, mX);
        }
        else if (mX == 0 && mY != 0)
        {
            // Looking directly up or down
            newSlopes[0] = new Point2D.Double(mY, mX);
            newSlopes[1] = new Point2D.Double(mY * -1, mX);
        }
        else if (mY == 0 && mX != 0)
        {
            // Looking left or right
            newSlopes[0] = new Point2D.Double(mY, mX);
            newSlopes[1] = new Point2D.Double(mY, mX * -1);
        }

        return newSlopes;
    }

    /**
     * Finds the point on a line that is a specified distance away from another
     * point.
     * @param startPoint the starting point
     * @param slope the slope of the line (in other words the direction the
     * other point is in)
     * @param distance the distance the end point is from the start point
     * @return the end point (the point that is the specified distance away
     * from the starting point and in the direction of the slope)
     */
    private Point2D.Double getEndPoint(Point2D startPoint,
                                        Point2D slope,
                                        double distance)
    {

        double slopeLength = Math.sqrt((slope.getX() * slope.getX()) +
                                (slope.getY() * slope.getY()));
        if (slopeLength == 0)
            slopeLength = 1;
        Point2D.Double newSlope =
            new Point2D.Double((slope.getX() / slopeLength) * distance,
                                (slope.getY() / slopeLength) * distance);
        Point2D.Double endPoint =
            new Point2D.Double(newSlope.getX() + startPoint.getX(),
                                newSlope.getY() + startPoint.getY());

        return endPoint;
    }


    /**
     * Gets the raw fitness score for the feeder.
     * @param chromosome the chromosome array for the genotype
     * @return the raw fitness score for the feeder
     */
    public double GetFitness() {
        return foodEaten; // * 1.25;// + 1;
    }

    /**
     * Makes the feeder start moving towards an object in the environment.
     * @param target the object to move towards
     */
    public void moveTo(Food target) {
        // make sure the target is active
        if (target.isActive())
        {
            // first get the distance to the target
            double distance = getCenter().distance(target.getCenter());
            // also get the length of the feeder's movement vector
            double mvLength = Math.sqrt(
                    movementVector.x * movementVector.x +
                    movementVector.y * movementVector.y);
            // if the distance is zero
            if (distance <= mvLength || distance <= target.getWidth() / 2) {
                // check to see if the feeder thinks the target is food
                if (isFood(target)) {
                    // if it thinks its food, eat it
                    eat(target);
                } else {
                    // otherwise, just remove it from observed
                    removeFromObserved(target);
                }
            } else { // if distance is not zero, set movement vector
                // first, get the slope of the line between the feeder and the target
                Point2D.Double m = new Point2D.Double(
                        target.getCenter().x - getCenter().getX(),
                        target.getCenter().y - getCenter().getY());
                // then, normalize the slope
                // 1. get length
                double mLength = Math.sqrt(m.x * m.x + m.y * m.y);
                // 2. get new points by dividing by length
                m.x = m.x / mLength;
                m.y = m.y / mLength;

                // now, set new direction
                movementVector.x = m.x * effectiveSpeed;
                movementVector.y = m.y * effectiveSpeed;
                //setMovementVector();
            }
        }
    }

    /**
     * Checks to see if the feeder thinks the object is food.
     * @param f the target object
     * @return true if the feeder determines the object is food, false if it
     * does not think it is food.
     */
    public boolean isFood(Food f) {
        // if intelligence is maxxed out, return true
        if (intelligence == MAXINTELLIGENCE) return true;
        // otherwise, check against intelligence

        // get a random number to check intelligence against
        double intelRoll =  intelligenceRoll();// * distance;

        // see if intelligence is greater than the roll
        if (effectiveIntelligence > intelRoll) return true;
        else return false;
    }

    /**
     * Makes the feeder eat a food object.
     * @param f the food object to eat
     */
    private void eat(Food f) {
        // increment food eaten value
        foodEaten++;
        // remove the food object from the environment
        f.markAsEaten();
    }

    /**
     * Makes a feeder react to a perceived food object.
     * @param fd the food object to react to
     */
    public void reactTo(Food fd) {
        // if intelligent enough, investigate the food
        if (isFood(fd)) {
            // add the food to the observed list
            observedFood.add(fd);
            // moveTo(fd);
        }
    }

    /**
     * Gets the closest food in the observed food list.
     */
    private Food getClosestObserved() {
        int closest = 0;    // keep track of closest food's index
        // and the distance to the closest food object
        double closestDistance = getCenter().distance(
                observedFood.get(closest).getCenter());

        // check the rest of the list
        for (int i = 1; i < observedFood.size(); i++) {
            // get the distance to this food object
            double distance = getCenter().distance(
                    observedFood.get(i).getCenter());
            // if it's closer than the closest, make it the closest
            if (distance < closestDistance) {
                closest = i;
                closestDistance = distance;
            }
        }

        return observedFood.get(closest);
    }

    /**
     * Updates the feeder.
     */
    public void update() {
        // check the observed foods list first. if it isn't empty, make sure
        // the feeder is moving towards the closest food object.
        if (observedFood.size() > 0) {
            for (int i = 0; i < observedFood.size(); i++) {
                if (observedFood.get(i).isActive()) {
                    moveTo(getClosestObserved());
                    break;
                }
            }
        }

        // move the feeder along its movement vector
        currentLocation.x += movementVector.x;
        currentLocation.y += movementVector.y;
        // adjust location to make sure the feeder is still in bounds
        adjustLocation();
    }

    /**
     * Adjusts the current location to make sure the feeder stays in bounds.
     */
    private void adjustLocation() {
        double newX = currentLocation.x;
        double newY = currentLocation.y;

        if (newX < Environment.getMinX() || newX > Environment.getMaxX() ||
            newY < Environment.getMinY() || newY > Environment.getMaxY())
        {
            // flags that determine whether or not a coordinate was adjusted
            // setRandomMovementVector();
            
            if (newX < Environment.getMinX()) {
                newX = Environment.getMinX();
            }
            else if (newX > Environment.getMaxX()) {
                newX = Environment.getMaxX();
            }

            if (newY < Environment.getMinY()) {
                newY = Environment.getMinY();
            }
            else if (newY > Environment.getMaxY()) {
                newY = Environment.getMaxY(); 
            }

            // adjust the direction and movment vector
            adjustDirection();
            
            // set the new location and we're done.
            setNewLocation(newX, newY);
        }
    }

    /**
     * Sets the direction to a random direction based on the feeder's
     * location.
     * @param newX
     * @param newY
     */
    private void adjustDirection() {
         // set a random movement vector based on where the feeder is.
        // will determine the quadrant to point vector to (relative to the
        // feeder's current location).
        if (currentLocation.x < Environment.getMinX()) { // on left edge
            if (currentLocation.y < Environment.getMinY()) { // on top left corner
                // set to random vector pointed at fourth quadrant
                setRandomMovementVector(QUADRANT.FOUR);
            }
            else if (currentLocation.y > Environment.getMaxY()) { // on bottom left corner
                // set to first quadrant
                setRandomMovementVector(QUADRANT.ONE);
            }
            else {  // just on left edge somewhere
                // set to either the first or fourth quadrant
                setRandomMovementVector(QUADRANT.ONE_OR_FOUR);
            }
        }
        else if (currentLocation.x > Environment.getMaxX()) {   // on right edge
            if (currentLocation.y < Environment.getMinY()) { // on top right corner
                // set to random vector pointed at third quadrant
                setRandomMovementVector(QUADRANT.THREE);
            }
            else if (currentLocation.y > Environment.getMaxY()) { // on bottom right corner
                // set to second quadrant
                setRandomMovementVector(QUADRANT.TWO);
            }
            else {  // just on right edge somewhere
                // set to either the second or third quadrant
                setRandomMovementVector(QUADRANT.TWO_OR_THREE);
            }
        }
        else if (currentLocation.y < Environment.getMinY()) {    // on top edge
            // set to point to the third or fourth quad
            setRandomMovementVector(QUADRANT.THREE_OR_FOUR);
            // in checking y, we don't need to check x since it was checked
            // above.
        }
        else if (currentLocation.y > Environment.getMaxY()) {    // on bottom edge
            // set to point to the first or second quad
            setRandomMovementVector(QUADRANT.THREE_OR_FOUR);
        }
    }

    /**
     * Sets a new location for the feeder.
     * @param newX the new x coordinate
     * @param newY the new y coordinate
     */
    private void setNewLocation(double newX, double newY) {
        currentLocation.x = newX;
        currentLocation.y = newY;
    }

    /**
     * Gets the feeder's effective eyesight strength.
     * @return the feeder's eyesight
     */
    public double getEffectiveEyesight() { return effectiveEyesight; }

    /**
     * Gest the feeder's current location.
     * @return the feeder's current location
     */
    public Point2D.Double getCurrentLocation() { return currentLocation; }

    /**
     * Gets a random number between zero and the maximum effective intelligence
     * value.
     * @return a random number between zero and the maximum effective
     * intelligence level
     */
    public static double intelligenceRoll() {
        return Math.random() * MAXINTELLIGENCE * intelligenceModifier;
    }

    /**
     * Gets the feeder's effective intelligence level.
     * @return the feeder's effective intelligence level
     */
    double getEffectiveIntelligence() {
        return effectiveIntelligence;
    }

    /**
     * Gets the feeder's observed food list.
     * @return
     */
    public ArrayList<Food> getObservedFood() { return observedFood; }

    /**
     * Removes a food object from the observed list.
     * @param f the food object to remove
     */
    public void removeFromObserved(Food f) {
        if (observedFood.contains(f)) observedFood.remove(f);
    }

    static void displayFeederID() {
        displayID = true;
    }

    /**
     * Displays the feeder's unique ID.
     */
    static void hideFeederID() { displayID = false; }

    /**
     * Displays the food eaten amount.
     */
    static void displayFoodEaten() { displayFoodEaten = true; }

    /**
     * Hides the food eaten amount.
     */
    static void hideFoodEaten() { displayFoodEaten = false; }

    /**
     * Determines if the feeder ID field is displayed.
     * @return true if it is displayed, false if it is not
     */
    static boolean isIDDisplayed() { return displayID; }

    /**
     * Determines if the food eaten amount is displayed.
     * @return true if it is displayed, false if it is not.
     */
    static boolean isEatenDisplayed() { return displayFoodEaten; }

    public void dispose() {
        
    }
}