package danielknowlesprojectjava.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Random;
import javax.swing.JComponent;

/**
 * Food for the feeders to eat.
 */
public class Food extends JComponent {
    private Point2D.Double currentLocation = new Point2D.Double();
    private int width = 5;
    private int height = 5;
    private static int defaultSize = 5;
    private boolean active = true;

    /**
     * Creates a new food object.
     */
    public Food() {
        super();
    }

    /**
     * Creates a new food object.
     * @param x the x coordinate of the food's current location
     * @param y the y coordinate of the food's current location
     * @param w the width of the food object
     * @param h the height of the food object
     */
    public Food(double x, double y, int w, int h) {
        currentLocation.x = x;
        currentLocation.y = y;
        width = w;
        height = h;
    }

    /**
     * Gets the food's current location
     * @return the food's current location
     */
    public Point2D.Double getCurrentLocation() { return currentLocation; }

    private static Random rnd = new Random();
    /**
     * Creates a random food object.
     * @return a random food object
     */
    public static Food getRandomFood() {
        // set random position that is in environment bounds
        int maxX = Environment.getMaxX() - (Environment.getMinX() + defaultSize);
        int maxY = Environment.getMaxY() - (Environment.getMinY() + defaultSize);
        int minX = Environment.getMinX() + defaultSize;
        int minY = Environment.getMinY() + defaultSize;

        Point2D.Double pos = new Point2D.Double(
                (double) (rnd.nextDouble() * maxX + minX),
                (double) (rnd.nextDouble() * maxY + minY));

        // create new food
        return new Food(defaultSize, defaultSize, pos);
    }

    /**
     * Creates a new food object.
     * @param w the width of the food
     * @param h the height of the food
     * @param location the location of the food in the environment
     */
    public Food(int w, int h, Point2D.Double location) {
        width = w;
        height = h;
        currentLocation = location;
    }

    /**
     * Determines if the food is perceived by a feeder.
     * @param fr the feeder to check
     * @return true if the feeder sees the food, false if it does not
     */
    public boolean isPerceived(Feeder fr) {
        // if food has been eaten, the feeder can't eat it again
        if (!active) return false;
        // if the food is already in the feeder's observed list, it has already
        // responded to it.
        if (fr.getObservedFood().contains(this)) return false;
        double distance = currentLocation.distance(fr.getCurrentLocation());
        // if the feeder is blind, it won't see anything, it will have to be at
        //  the food's location so that it can touch and/or smell it.
        if (fr.getEffectiveEyesight() == 0) {
            if (distance <= fr.getEffectiveSpeed()) {
                // we'll use the feeder's intelligence to see if it notices
                //  the object.
                //if (fr.isFood(this))
                    return true;
            }
        } else {
            if (distance <= 0 || distance <= fr.getEffectiveEyesight()) {
                 // check if food is located within cone of perception
                if (isInConeOfPerception(fr, this.getCenter()))
                {
                    // need to check intelligence to see if the feeder thinks
                    // this object is a food.
//                    if (fr.isFood(this))
                        return true;
                }
                else
                {
                    //check if food surrounds cone of perception in case the
                    // feeder isn't facing the food but is inside the food's circle
                    if (isBleedOver(fr))
                    {
                        // need to check intelligence to see if the feeder thinks
                        // this object is a food.
//                        if (fr.isFood(this))
                            return true;
                    }
                }
            }
        }

        // if all else fails, return false
        return false;
    }

    private boolean isBleedOver(Feeder f)
    {
        //get center coordinates for food
        double eCenterX = this.getCenter().x;
        double eCenterY = this.getCenter().y;

        //get corners of bounding box for food
        Point2D topLeft = new Point2D.Double((eCenterX - (this.getWidth() / 2)), (eCenterY - (this.getHeight() / 2)));

        Point2D topRight = new Point2D.Double((eCenterX + (this.getWidth() / 2)), (eCenterY - (this.getHeight() / 2)));

        Point2D bottomLeft = new Point2D.Double((eCenterX - (this.getWidth() / 2)), (eCenterY + (this.getHeight() / 2)));

        Point2D bottomRight = new Point2D.Double((eCenterX + (this.getWidth() / 2)), (eCenterY + (this.getHeight() / 2)));

        if (isInConeOfPerception(f, topLeft))
        {
            return true;
        }

        if (isInConeOfPerception(f, topRight))
        {
            return true;
        }

        if (isInConeOfPerception(f, bottomLeft))
        {
            return true;
        }

        if (isInConeOfPerception(f, bottomRight))
        {
            return true;
        }

        return false;
    }

    /**
     * Checks to see if the food is in the feeder's cone of perception.
     * @param f the food object
     * @param tPoint the point to check
     * @return true if the food is in the feeder's FOV, false if it is not
     */
    private boolean isInConeOfPerception(Feeder f, Point2D tPoint)
    {
        double px = f.getCenter().getX();
        double py = f.getCenter().getY();
        double ex = tPoint.getX();
        double ey = tPoint.getY();
        double ax = f.getPerPoint1().getX();
        double ay = f.getPerPoint1().getY();
        double bx = f.getPerPoint2().getX();
        double by = f.getPerPoint2().getY();

        // find x and y properties for the vectors a-p, e-p, b-p
        double apx = ax - px;
        double apy = ay - py;
        double epy = ey - py;
        double epx = ex - px;
        double bpy = by - py;
        double bpx = bx - px;

        //find z for cross products ap x ep, ap x bp and bp x ep
        double apepz = apx * epy - apy * epx;
        double apbpz = apx * bpy - apy * bpx;
        double bpepz = bpx * epy - bpy * epx;
        double bpapz = bpx * apy - bpy * apx;

        //if the product of apepz and apbpz is >=0 then point is on correct side of line ap
        boolean testAP = (apepz * apbpz >= 0);
        boolean testBP = (bpepz * bpapz >= 0);

        //if it not on the correct side then return false
        return testAP && testBP;
    }

    /**
     * Gets the center of the food object's circle.
     * @return the center of the food object
     */
    public Point2D.Double getCenter()
    {
        double centerX = currentLocation.x + (width / 2);
        double centerY = currentLocation.y + (height / 2);

        return new Point2D.Double(centerX, centerY);
    }

    @Override
    public void paintComponent(Graphics g)
    {

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.green);
        Ellipse2D r = new Ellipse2D.Double(
            currentLocation.x, currentLocation.y, width, height);

        g2.draw(r);
        g2.fill(r);
        g2.setColor(Color.black);
    }

    /**
     * Marks the food as eaten by setting its active flag to false.
     */
    public void markAsEaten() { active = false; }

    /**
     * Determines if the food is still active.
     * @return true if it is active, false if it is not
     */
    public boolean isActive() { return active; }
}
