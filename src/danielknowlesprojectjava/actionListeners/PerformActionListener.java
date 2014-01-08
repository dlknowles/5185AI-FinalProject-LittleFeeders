package danielknowlesprojectjava.actionListeners;

import danielknowlesprojectjava.components.EnvironmentFrame;
import danielknowlesprojectjava.components.GAPerformanceDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An action listener that fires when the view GA Performance Table option
 * is selected.
 */
public class PerformActionListener implements ActionListener {
    EnvironmentFrame parentFrame;

    /**
     * Creates a new perform action listener.
     * @param parent the parent environment frame
     */
    public PerformActionListener(EnvironmentFrame parent) {
        parentFrame = parent;
    }

    /**
     * Performs the action of the listener. Displays the performance dialog
     * frame.
     * @param e the event that caused the action to fire
     */
    public void actionPerformed(ActionEvent e) {
        // if the performance table dialog frame is not already visible
        if (!parentFrame.gpDialog.isVisible()) {
//            // set the environment frame's performance table dialog frame
//            parentFrame.gpDialog = new GAPerformanceDialog(parentFrame,
//                    parentFrame.getEnvironment().getGeneticEngine().getAverageFitnesses(),
//                    parentFrame.getEnvironment().getGeneticEngine().getHighestFitnesses());
//
//            // set the performance table dialog frame visible
//            parentFrame.gpDialog.setVisible(true);
            parentFrame.gpDialog.setVisible(true);
        } else {
            parentFrame.gpDialog.setVisible(false);
        }
    }

}
