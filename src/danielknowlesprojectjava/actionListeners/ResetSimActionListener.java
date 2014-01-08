/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package danielknowlesprojectjava.actionListeners;

import danielknowlesprojectjava.components.EnvironmentFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Lee
 */
public class ResetSimActionListener implements ActionListener {
    private EnvironmentFrame parent;

    public ResetSimActionListener(EnvironmentFrame parentFrame) {
        parent = parentFrame;
    }

    public void actionPerformed(ActionEvent e) {
        parent.getEnvironment().resetEnvironment();
    }

}
