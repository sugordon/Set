package Client;

import javax.swing.*;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusListener;
import java.security.MessageDigest;

/**
 * Created by Bridget on 4/9/2016.
 */
public class GUILogin extends JPanel{

    private JButton loginButton = new JButton("Login");
    private JButton registerButton = new JButton("Register");
    private JTextField userText = new JTextField("Enter Username");
    private JPasswordField passwordText = new JPasswordField();

    private FocusListener UNFocus = new FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    userText.selectAll();
                }
            });
        }
    };

    private FocusListener passFocus = new FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    passwordText.selectAll();
                }
            });
        }
    };

    //Constructor
    public void createAndShowLogin(){
        buildLogin();
    }

    private void buildLogin() {
        JFrame loginscreen = new JFrame("Welcome to SET!");
        loginscreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginscreen.setLayout(null);
        loginscreen.getContentPane().setBackground(new Color(51, 255, 255));
        loginscreen.setPreferredSize(new Dimension(1000, 700));
        //loginscreen.setResizable(false);

        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(10, 10, 80, 25);
        loginscreen.add(userLabel);

        userText.setBounds(100, 10, 160, 25);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 40, 80, 25);
        loginscreen.add(passwordLabel);

        passwordText.setBounds(100, 40, 160, 25);
        loginButton.setBounds(10, 80, 80, 25);
        registerButton.setBounds(100, 80, 80, 25);

        addLoginAndRegisterListeners();

        loginscreen.add(loginButton);
        loginscreen.add(registerButton);

        userText.addFocusListener(UNFocus);
        passwordText.addFocusListener(passFocus);
        loginscreen.add(userText);
        loginscreen.add(passwordText);

        loginscreen.pack();

        loginscreen.setVisible(true);

    }

    public String getUN(){
        return userText.getText();
    }

    public String getPass(){
        /*try{
            MessageDigest md = MessageDigest.getInstance("SHA");
            return new String((new HexBinaryAdapter()).marshal(md.digest(new String(passwordText.getPassword()).getBytes())));
        } catch (Exception e) {
            return null;
        }
        */
        return passwordText.getText();
    }

    public void addLoginAndRegisterListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = getUN();
                String password = getPass();
                //ClientInit.inStream.println()
                JOptionPane.showConfirmDialog((Component) e.getSource(), username + "\n" + password);
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = getUN();
                String password = getPass();
                JOptionPane.showConfirmDialog((Component) e.getSource(), username + "\n" + password);
            }
        });
    }

    public void detachListeners() {
        for(ActionListener l : loginButton.getActionListeners())
            loginButton.removeActionListener(l);
        for(ActionListener l : registerButton.getActionListeners())
            registerButton.removeActionListener(l);
        userText.removeFocusListener(UNFocus);
        passwordText.removeFocusListener(passFocus);
    }
}
