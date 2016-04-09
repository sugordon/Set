package Client;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Bridget on 4/9/2016.
 */
public class GUILogin {

    private void createAndShowLogin() {
        JFrame loginscreen = new JFrame("Welcome to SET!");
        loginscreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginscreen.getContentPane().setBackground(new Color(51, 255, 255));
        loginscreen.setPreferredSize(new Dimension(1000, 700));
        loginscreen.setResizable(false);
        loginscreen.pack();

        JTextField unfield = new JTextField("Enter Username");
        JPasswordField passfield = new JPasswordField();

        loginscreen.setVisible(true);
    }
}
