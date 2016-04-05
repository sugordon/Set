package Client;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

import game.Card;

/**
 * Created by Bridget on 3/15/2016.
 */
public class GUI {

    // Toggle button so it grays out when pressed
    public static game.Card [] cards = new game.Card[64];     //Array of cards, in a grid

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

        generateCards(cards);

        //I need to be given which cards should be displayed
        for(int i = 0; i < 12; i++) {
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

    public static void generateCards(game.Card [] cards) {
        try {
            for(int i = 0; i < 64; i++) {
                File f = new File("src/Images/0000.png");
                Image img = ImageIO.read(f);
                //Image img = ImageIO.read("src/Images/" + cards[i].toString() + ".png");
                cards[i].setIcon(new ImageIcon(img));
                //BufferedImage buttonicon = ImageIO.read(new("buttonIconPath"));
                cards[i].setBorder(BorderFactory.createEmptyBorder());
                cards[i].setContentAreaFilled(false);
            }
        }catch (IOException ex) {
                ex.printStackTrace();
            }

        //return cards;
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
        //generateCards();
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                createAndShowBoard();
            }
        });
    }
}
