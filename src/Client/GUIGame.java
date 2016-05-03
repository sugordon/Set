package Client;

import game.Card;
import game.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Bridget on 3/15/2016.
 */
public class GUIGame extends JPanel{

    // Toggle button so it grays out when pressed
    public ArrayList<game.Card> cards = new ArrayList<>();     //Array of cards, in a grid

    public game.Board board;

    public JFrame gameboard = new JFrame("SET");

    private JTable userTable;

    public JLabel thumb;

    public JPanel cardspace = new JPanel();
    private JPanel scoreboard = new JPanel();
    private JPanel users;

    public JButton setButton = new JButton("SET");
    //public JButton removeButton = new JButton("REMOVE");
    public String myUN;
    public int cardcount = 0;
    public int rows;
    public int cols = 3;
    private boolean selectEnabled = false;

    private ArrayList<Card> selected = new ArrayList<Card>(); //List of selected cards

    public Timer timer = new Timer();

    String[] userColumns = {"Name",
            "Ranking"};

    Object[][] userData = {                                                     //swap for Player.getName, and Player.getScore
            {"TheM3owLord", new Integer(1)},
            {"GSu32", new Integer(3)},
            {"AlpacaFloof", new Integer(2)}
    };

    DefaultTableModel userModel = new DefaultTableModel(userData, userColumns);

    //Constructor
    public GUIGame(int rows, String uid) {
        this.rows = rows;
        this.myUN = uid;
        board = new game.Board();
        cards = Game.createDeck(new ArrayList<>());
    }

    class submitSETTimer extends TimerTask {
        public void run() {
            System.out.println("Time's up! Submitted SET");
            setButton.setText("SET");
            selectDisable();
            int cardnum = rows * cols;
            for(int i = 0; i < cardnum; i++) {
                Card tmp = cards.get(i);
                tmp.setBorderPainted(false);
                tmp.setSelected(false);
                selected.remove(tmp);
            }
            submitSet(myUN, selected);
            //timer.cancel();
        }
    }

    class enableSETTimer extends TimerTask {
        public void run() {
            System.out.println("Time's up for opponent! Reenabling SET button.");
            setButton.setEnabled(true);
            //timer.cancel();
        }
    }

    //Creates JPanel and its characteristics for gameboard
    public void createAndShowBoard() {
        gameboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gameboard.getContentPane().setBackground(new Color(99, 111, 115));
        gameboard.setPreferredSize(new Dimension(1000, 700));
        gameboard.setResizable(false);
        gameboard.pack();
        buttons();

        /*
        //TEST CODE
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Card> cardset = new ArrayList<Card>();
                cardset.add(board.get(0,0));
                cardset.add(board.get(0,1));
                cardset.add(board.get(0,2));
                ArrayList<Card> newCards = new ArrayList<Card>(cardset);
                updateCards(cardset,newCards);
            }
        });
        */

        users = new JPanel();
        createCardSpace();

        createScoreboard();

        createBanner();

        layoutGame();

        gameboard.setVisible(true);

    }

    public void layoutGame(){
        GridBagConstraints c, s, b, q;

        GridBagLayout layout = new GridBagLayout();

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 4;
        c.gridheight = 3;

        gameboard.setLayout(layout);
        gameboard.add(cardspace, c);

        s = new GridBagConstraints();
        s.gridx = 6;
        s.gridy = 1;
        //s.gridwidth = 1;
        //s.gridheight = 1;
        s.fill = GridBagConstraints.BOTH;
        s.anchor = GridBagConstraints.CENTER;
        s.insets = new Insets(10, 10, 10, 10);

        gameboard.add(userTable, s);

        b = new GridBagConstraints();
        b.gridx = 6;
        b.gridy = 3;
        b.gridwidth = 1;
        b.gridheight = 1;

        gameboard.add(setButton, b);

        q = new GridBagConstraints();
        q.gridx = 0;
        q.gridy = 0;
        q.gridwidth = 15;
        q.gridheight = 1;
        q.fill = GridBagConstraints.BOTH;

        gameboard.add(thumb, q);


    }

    public void createScoreboard(){
        //Scoreboard
        userData = sortScoreboard(userData);
        userModel = new DefaultTableModel(userData, userColumns);
        userTable = new JTable(userModel) {
            @Override //Disable editing
            public boolean isCellEditable(int r, int c) {return false;}
        };
        formatTable(userTable);
        JScrollPane scrollPane1 = new JScrollPane(userTable);
        scrollPane1.setPreferredSize(new Dimension(200, 200));
        JLabel usersLabel = new JLabel("Users Online");
        usersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        users.add(usersLabel);
        users.add(scrollPane1);
        //setAlignmentX(Component.CENTER_ALIGNMENT);
        setMaximumSize(new Dimension(200, 10));
        //add(users, BorderLayout.EAST);
    }

    private Object [][] sortScoreboard(Object [][] Scoredata){
        userData = Scoredata;

        java.util.Arrays.sort(userData, new java.util.Comparator<Object[]>() {
            public int compare(Object[] a, Object[] b) {
                return -Integer.compare((Integer) a[1], (Integer) b[1]);
            }
        });

        return userData;
    }

    public void createBanner(){
        ImageIcon icon = new ImageIcon("./bin/GameBanner.jpg");
        thumb = new JLabel();
        thumb.setIcon(icon);
    }

    public void createCardSpace(){
        Dimension cardspacesize = new Dimension(650,400);
        cardspace.setMinimumSize(cardspacesize);
        cardspace.setMaximumSize(cardspacesize);
        cardspace.setPreferredSize(cardspacesize);

        generateCards(cards);

        //Get which cards should be displayed from Server
        int cardnum = rows * cols;
        for(int i = 0; i < cardnum; i++) {
            Card tmp = cards.get(i);
            tmp.setVisible(true);
            tmp.setPreferredSize(new Dimension(20,20));
            tmp.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Card c = (Card) e.getSource();
                    if(selectEnabled) {
                        if(selected.contains(c)) {
                            //c.setEnabled(true);
                            c.setSelected(false);
                            c.setBorderPainted(false);
                            selected.remove(c);
                        } else if(selected.size() < 3) {
                            //c.setEnabled(false);
                            c.setBorderPainted(true);
                            c.setSelected(true);
                            selected.add(c);
                            System.out.println("YOU PRESSED THE BUTTON.");
                        }
                    }
                }
            });
            cardspace.add(tmp);
            board.insert(tmp);
        }


        cardspace.setLayout(new GridLayout(4,3));
        cardspace.setVisible(true);
    }

    public void generateCards(ArrayList<game.Card> cards) {
        try {
            for (int i = 0; i < cards.size(); i++) {
                Card tmp = cards.get(i);
                String path = "./bin/" + tmp.toString() + ".gif";
                File f = new File(path);
                System.out.println(f.exists());
                Image img = ImageIO.read(f);
//                System.out.println(path);
//                ImageIcon img = new ImageIcon(getClass().getResource(path));
//                ImageIcon img = new ImageIcon(getClass().getResource("images_cards/0000.gif"));
//                tmp.setIcon(img);
                tmp.setIcon(new ImageIcon(img));
                tmp.setBorder(BorderFactory.createLineBorder(new Color(6, 138, 10), 10));
                tmp.setBorderPainted(false);
                tmp.setContentAreaFilled(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return cards;
    }
    //Disables the SET button for a specified amount of time after it's been pressed
    public void disableSET(final int time) {
        setButton.setEnabled(false);
        TimerTask enable = new enableSETTimer();
        timer.schedule(enable, 5000);  //3000 milliseconds = 3 seconds
    }

    public void buttons(){
        setButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //setButton.setEnabled(false);
                //if(!timer.isRunning()) {
                    //Start accepting a possible set
                if(selectEnabled == true)
                {
                    submitSet(myUN, selected);
                    int cardnum = rows * cols;
                    for(int i = 0; i < cardnum; i++) {
                        Card tmp = cards.get(i);
                        tmp.setBorderPainted(false);
                        tmp.setSelected(false);
                        selected.remove(tmp);
                    }
                    //System.out.print(selected.get(0).toString());
                }
                else {
                    selectEnable();
                    setButton.setText("SUBMIT");
                    TimerTask submit = new submitSETTimer();
                    timer.schedule(submit, 5000);     //3000 milliseconds = 3 seconds
                }
            }
        });
    }

    public void selectEnable(){
        selectEnabled = true;
    }

    public void selectDisable(){
        selectEnabled = false;
    }

    private void submitSet(String username, ArrayList<Card> selected){
        //Gordon fill in
    }

    //Replace cards selected with new cards
    //Gordon calls this
    public void updateCards(ArrayList<Card> discardCards,ArrayList<Card> newCards){
        for(int i = 0; i < discardCards.size(); i++ ) {
            cardspace.remove(discardCards.get(i));
        }
        int size = 0;
        if(newCards != null) {
            size = newCards.size();
            generateCards(newCards);

            for (int i = 0; i < size; i++){
                System.out.println("Addign new card");
                Card tmp = newCards.get(i);
                board.insert(tmp);
                tmp.setVisible(true);
                tmp.setPreferredSize(new Dimension(20, 20));
                tmp.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Card c = (Card) e.getSource();
                        if (selectEnabled) {
                            if (selected.contains(c)) {
                                //c.setEnabled(true);
                                c.setSelected(false);
                                c.setBorderPainted(false);
                                selected.remove(c);
                            } else if (selected.size() < 3) {
                                //c.setEnabled(false);
                                c.setBorderPainted(true);
                                c.setSelected(true);
                                selected.add(c);
                                System.out.println("YOU PRESSED THE BUTTON.");
                            }
                        }
                    }
                });
                cardspace.add(tmp);
            }
        }
        else
            System.out.printf("newCards is null");
        cardspace.revalidate();
        cardspace.repaint();
    }

    //If there isn't a valid set, the size of the board grid has to increase
    //Gordon calls this
    public void updateGrid(){
        cols += 1;
    }

    //Gordon calls this
    public void updateScoreboard(Object [][] data) {
        for(int i=2; i<data.length; i+=4) {
            scoreboard.add(new JLabel(data[i+1]+" Score: "+data[i+2]));
        }
        userData = data;
        createScoreboard();
        scoreboard.revalidate();
    }

    private void formatTable(JTable t) {
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.getColumnModel().getColumn(0).setPreferredWidth(140);
        t.getColumnModel().getColumn(1).setPreferredWidth(60);
        t.setAutoCreateRowSorter(true);
    }


    public static void main(String [] args){
        GUIGame game = new GUIGame(4,"LOL");
        game.createAndShowBoard();
    }

}
