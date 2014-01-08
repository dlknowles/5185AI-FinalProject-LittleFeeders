package danielknowlesprojectjava.actionListeners;

import danielknowlesprojectjava.components.EnvironmentFrame;
import danielknowlesprojectjava.components.PauseDialog;
import java.awt.event.*;

/**
 * Creates a dialog frame that tells the user that the application has been
 * paused.
 */
public class PauseActionListener implements ActionListener
{
    /**
     * Creates a new PauseActionListener.
     * @param aThis the environment frame that called the action
     */
    public PauseActionListener(EnvironmentFrame parentFrame)
    {
        this.parentFrame = parentFrame;
    }

    public void actionPerformed(ActionEvent event)
    {
        parentFrame.getEnvironment().pause();
        PauseDialog pd = new PauseDialog(parentFrame);
        pd.setVisible(true);
    }

    EnvironmentFrame parentFrame;
}
