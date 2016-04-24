package Client;

import game.Card;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by Bridget on 3/15/2016.
 */
public class GUIGame extends JPanel{

    // Toggle button so it grays out when pressed
    public game.Card [] cards = new game.Card[81];     //Array of cards, in a grid

    public JFrame gameboard = new JFrame("SET");

    public JPanel cardspace = new JPanel();

    public JButton setButton = new JButton("SET");

    private Timer timer = new Timer(1000, null);
    private int t;
    private ActionListener setPress = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            setButton.setEnabled(false);
            t--;
            if(t < 0){
                timer.stop();
                setButton.setEnabled(true);
                timer.removeActionListener(this);
            }
        }
    };
    private ActionListener countdownSet = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            t--;
            if(t < 0){
                timer.stop();
                //submit the set (Venkat?)
                submitSet();
                timer.removeActionListener(this);
            }
        }
    };

    //Constructor
    public void GUIGame(){
        createAndShowBoard();
    }

    //Creates JPanel and its characteristics for gameboard
    public void createAndShowBoard() {
        gameboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gameboard.getContentPane().setBackground(new Color(51, 255, 255));
        gameboard.setPreferredSize(new Dimension(1000, 700));
        gameboard.setResizable(false);
        gameboard.pack();

        createCardSpace();

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

    public void createCardSpace(){
        Dimension cardspacesize = new Dimension(650,400);
        cardspace.setMinimumSize(cardspacesize);
        cardspace.setMaximumSize(cardspacesize);
        cardspace.setPreferredSize(cardspacesize);

        generateCards(cards);

        //Get which cards should be displayed from Server

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
    }

    public void generateCards(game.Card [] cards) {
        try {
            for(int i = 0; i < 81; i++) {
                File f = new File("src/Images/0000.gif");
                Image img = ImageIO.read(f);
                //Image img = ImageIO.read("src/Images/" + cards[i].toString() + ".gif");
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

    //Disables the SET button for a specified amount of time after it's been pressed
    public void disableSET(final int time) {
        if(time != 0) {
            t = time;
            setButton.setEnabled(false);
            timer.addActionListener(setPress);
            timer.setInitialDelay(0);
            timer.start();
        } else {
            setButton.setEnabled(false);
        }
    }

    //timer countdown for picking a SET
    public void setTimer(final int time) {
        t = time;
        //enable highlighting/selecting cards
        timer.addActionListener(countdownSet);
        timer.setInitialDelay(0);
        timer.start();
    }

    public void buttons(){
        setButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if(!timer.isRunning()) {
                    //Start accepting a possible set
                    //Systems integration- Venkat
                } else {
                    submitSet();
                }
            }
        });

    }

    private void submitSet(){

    }

    public void updateCards(Card cards[], boolean [] remove){

    }

    //If there isn't a valid set, the size of the board grid has to increase
    public void updateGrid(){

    }
}
