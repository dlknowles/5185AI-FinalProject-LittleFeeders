package danielknowlesprojectjava.actionListeners;

import danielknowlesprojectjava.components.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Lee
 */
public class OptionsActionListener implements ActionListener {

    EnvironmentFrame parentFrame;

    public OptionsActionListener(EnvironmentFrame parentFrame) {
    this.parentFrame = parentFrame;
    }

    public void actionPerformed(ActionEvent event)
    {
        parentFrame.getEnvironment().pause();
        OptionsDialog od = new OptionsDialog(parentFrame, true);
        od.setVisible(true);
    }
}
