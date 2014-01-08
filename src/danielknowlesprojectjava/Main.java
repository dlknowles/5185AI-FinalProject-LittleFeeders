/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package danielknowlesprojectjava;

import danielknowlesprojectjava.components.EnvironmentFrame;

/**
 * Entry point of the application.
 */
public class Main {

    /**
     * The entry point of the application.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EnvironmentFrame ef = new EnvironmentFrame(600, 600);
        ef.setVisible(true); 
    }

}
