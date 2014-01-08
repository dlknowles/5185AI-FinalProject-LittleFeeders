package danielknowlesprojectjava.components;

import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author lee.knowles
 */
public class PauseDialog extends JDialog
{
    public PauseDialog(EnvironmentFrame parentFrame)
    {
        super(parentFrame, "Paused", true);

        this.parentFrame = parentFrame;
        wasPaused = parentFrame.getEnvironment().isPaused();

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocation(400, 300);
        setSize(200, 75);
        setResizable(false);

        JButton unPauseButton = new JButton("Un Pause");

        unPauseButton.addActionListener(
            new ActionListener()
        {
            /**
             * Un pauses the application.
             */
            public void actionPerformed(ActionEvent e) { dispose(); }
        });

        add(unPauseButton);        
    }

    @Override
    public void dispose()
    {
        parentFrame.getEnvironment().unPause();

        super.dispose();
    }

    EnvironmentFrame parentFrame;
    boolean wasPaused;
}
