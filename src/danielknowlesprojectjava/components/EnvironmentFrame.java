package danielknowlesprojectjava.components;

import java.awt.Color;
import javax.swing.JFrame;

/**
 * The frame that hosts the environment.
 */
public class EnvironmentFrame extends JFrame {
    private static final int WIDTHOFFSET = 15;
    private static final int HEIGHTOFFSET = 60;
    private static final int XOFFSET = 5;
    private static final int YOFFSET = 5;
    private Environment e;
    public GAPerformanceDialog gpDialog;

    /**
     * Creates a new environment frame
     * @param width the width of the frame
     * @param height the height of the frame
     */
    public EnvironmentFrame(int width, int height)
    {
        super();

        // Set the frame settings
        getContentPane().setBackground(Color.white);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width, height);
        this.setTitle("CPSC 5185 Final Project - Little Feeders GA");
        this.setJMenuBar(new EnvironmentMenuBar(this));
        this.setLocationByPlatform(true);

        // Set up the environment
        e = new Environment(this);
        e.setHeight(height - HEIGHTOFFSET);
        e.setWidth(width - WIDTHOFFSET);
        e.setLocation(XOFFSET, YOFFSET);
        this.add(e);

        e.initializeEnvironment();

        gpDialog = new GAPerformanceDialog(this,
                e.getGeneticEngine().getAverageFitnesses(),
                e.getGeneticEngine().getHighestFitnesses());
        gpDialog.setLocationRelativeTo(this);
        gpDialog.setVisible(true);
    }

    /**
     * Gets the environment associated with this environment frame.
     * @return the environment
     */
    public Environment getEnvironment() { return e; }

}
