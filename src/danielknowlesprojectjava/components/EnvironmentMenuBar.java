package danielknowlesprojectjava.components;

import danielknowlesprojectjava.actionListeners.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

/**
 * Holds the menu bar for user interactions and application options.
 */
class EnvironmentMenuBar extends JMenuBar {
    private final EnvironmentFrame parentFrame;

    public EnvironmentMenuBar(EnvironmentFrame parentFrame)
    {
        super();
        this.parentFrame = parentFrame;

        add(getFileMenu());
        add(getOptionsMenu());
        add(getViewMenu());
    }

     /**
     * Creates the file menu for the menu bar.
     * @return the file menu
     */
    private JMenu getFileMenu()
    {
        JMenu fileMenu = new JMenu("File");
        JMenuItem resetItem = new JMenuItem("Start Over");
        JMenuItem pauseItem = new JMenuItem("Pause");

        pauseItem.addActionListener(new PauseActionListener(parentFrame));
        resetItem.addActionListener(new ResetSimActionListener(parentFrame));

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener((ActionListener) new
            ActionListener()
            {
                public void actionPerformed(ActionEvent event)
                {
                    System.exit(0);
                }
            });

        fileMenu.add(resetItem);
        fileMenu.add(pauseItem);
        fileMenu.add(new javax.swing.JSeparator(SwingConstants.HORIZONTAL));
        fileMenu.add(exitItem);

        return fileMenu;
    }

    /**
     * Creates the options menu.
     * @return the options menu
     */
    private JMenu getOptionsMenu()
    {
        JMenu optionsMenu = new JMenu("Options");
        JCheckBoxMenuItem displayFOVItem = 
                new JCheckBoxMenuItem("Show Feeders' Fields of Vision");
        displayFOVItem.setState(Feeder.isVisionDisplayed());
        JCheckBoxMenuItem displayEatenItem = 
                new JCheckBoxMenuItem("Show Food Eaten");
        displayEatenItem.setState(Feeder.isEatenDisplayed());
        JCheckBoxMenuItem displayIDItem = 
                new JCheckBoxMenuItem("Show Feeder ID");
        displayIDItem.setState(Feeder.isIDDisplayed());
        JMenuItem addOptionsItem = new JMenuItem("Additional Options...");


        displayFOVItem.addItemListener(
            new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                AbstractButton button = (AbstractButton) e.getItem();
                if (button.isSelected())
                {
                    Feeder.showFieldOfVision();
                }
                else
                {
                    Feeder.hideFieldOfVision();
                }
            }
        });

        displayIDItem.addItemListener(
            new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                AbstractButton button = (AbstractButton) e.getItem();
                if (button.isSelected())
                {
                    Feeder.displayFeederID();
                }
                else
                {
                    Feeder.hideFeederID();
                }
            }
        });

        displayEatenItem.addItemListener(
            new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                AbstractButton button = (AbstractButton) e.getItem();
                if (button.isSelected())
                {
                    Feeder.displayFoodEaten();
                }
                else
                {
                    Feeder.hideFoodEaten();
                }
            }
        });

        addOptionsItem.addActionListener(new OptionsActionListener(parentFrame));


        optionsMenu.add(displayFOVItem);
        optionsMenu.add(displayIDItem);
        optionsMenu.add(displayEatenItem);
        optionsMenu.add(new javax.swing.JSeparator(SwingConstants.HORIZONTAL));
        optionsMenu.add(addOptionsItem);

        return optionsMenu;
    }

    /**
     * Creates the view menu.
     * @return the view menu
     */
    private JMenu getViewMenu() {
        JMenu viewMenu = new JMenu("View");
        JMenuItem performItem = new JMenuItem("GA Performance Table");

        performItem.addActionListener(new PerformActionListener(parentFrame));

        viewMenu.add(performItem);
        return viewMenu;
    }
}
