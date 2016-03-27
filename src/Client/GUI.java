package Client;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Created by Bridget on 3/15/2016.
 */
public class GUI {
    private static void createAndShowBoard() {
        JFrame gameboard = new JFrame("SET");
        gameboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gameboard.getContentPane().setBackground(new Color(51, 255, 255));
        gameboard.setPreferredSize(new Dimension(1000, 700));
        gameboard.setResizable(false);
        gameboard.pack();

        JPanel cardspace = new JPanel();
        Dimension cardspacesize = new Dimension(650,400);
        cardspace.setMinimumSize(cardspacesize);
        cardspace.setMaximumSize(cardspacesize);
        cardspace.setPreferredSize(cardspacesize);

        // BufferedImage buttonicon = ImageIO.read(new("buttonIconPath"));
        //card1.setBorder(BorderFactory.createEmptyBorder());
        //card1.setContentAreaFilled(false);

        // Toggle button so it grays out when pressed
        JToggleButton [] cards = new JToggleButton[12];     //Array of cards, in a grid
        for(int i = 0; i < 12; i++) {
            cards[i] = new JToggleButton();
            cards[i].setVisible(true);
            cards[i].setPreferredSize(new Dimension(20,20));
            cards[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("YOU PRESSED THE BUTTON.\n");
                }
            });
            cardspace.add(cards[i]);
        }


        cardspace.setLayout(new GridLayout(4,3));
        cardspace.setVisible(true);

        GridBagConstraints c;

        GridBagLayout layout = new GridBagLayout();

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 4;

        gameboard.setLayout(layout);
        gameboard.add(cardspace, c);

        gameboard.setVisible(true);
    }

    private static void createAndShowLogin() {
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

    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                createAndShowBoard();
            }
        });
    }
}
