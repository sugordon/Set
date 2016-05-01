package Client;

import game.Card;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Bridget on 3/15/2016.
 */
public class GUIGame extends JPanel{

    // Toggle button so it grays out when pressed
    public game.Card [] cards = new game.Card[81];     //Array of cards, in a grid

    public JFrame gameboard = new JFrame("SET");

    private JTable userTable;

    public JPanel cardspace = new JPanel();
    private JPanel scoreboard = new JPanel();
    private JPanel users;

    public JButton setButton = new JButton("SET");
    private JButton refreshScoreboard = new JButton("Refresh");

    public String myUN;
    public int cardcount = 0;
    public int rows;
    private boolean selectEnabled = false;

    private ArrayList<Card> selected = new ArrayList<Card>(); //List of selected cards
    private HashMap<String, Location> cardMap = new HashMap<String, Location>();

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
        createAndShowBoard();
    }

    public MouseAdapter cardSelectionListener = new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {
            Card c = (Card) e.getSource();
            if(selectEnabled) {
                if(selected.contains(c)) {
                    c.setSelected(false);
                    selected.remove(c);
                } else if(selected.size() < 3) {
                    c.setEnabled(false);
                    selected.add(c);
                }
                String ids = "";
                for(Card i : selected)
                    ids += i.toString()+"`";
                //updateCards();
            }
        }
    };

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
                //submit the set
                submitSet(myUN);
                timer.removeActionListener(this);
            }
        }
    };

    //Creates JPanel and its characteristics for gameboard
    public void createAndShowBoard() {
        gameboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gameboard.getContentPane().setBackground(new Color(168, 168, 168));
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

        //Scoreboard
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
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setMaximumSize(new Dimension(200, 10));
        add(users, BorderLayout.EAST);

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
            //for(int i = 0; i < 81; i++) {
               // File f = new File("C:\\Users\\Bridget\\Documents\\Spring 2016\\Software\\Set\\Images\\0001.gif");
                //Image img = ImageIO.read(f);
                //Image img = ImageIO.read("../src/Client.Images/" + cards[i].toString() + ".gif");
                cards[0].setIcon(new ImageIcon(this.getClass().getResource("bin/Images/0000.gif")));
                //BufferedImage buttonicon = ImageIO.read(new("buttonIconPath"));
                //cards[0].setIcon(new ImageIcon(img));
                cards[0].setBorder(BorderFactory.createEmptyBorder());
                cards[0].setContentAreaFilled(false);

                File g = new File("C:/Users/Bridget/Documents/Spring 2016/Set/Images/0120.gif");
                Image img = ImageIO.read(g);
                cards[1].setIcon(new ImageIcon(img));
                cards[1].setBorder(BorderFactory.createEmptyBorder());
                cards[1].setContentAreaFilled(false);

                File h = new File("C:/Users/Bridget/Documents/Spring 2016/Set/Images/0001.gif");
                img = ImageIO.read(h);
                cards[2].setIcon(new ImageIcon(img));
                cards[2].setBorder(BorderFactory.createEmptyBorder());
                cards[2].setContentAreaFilled(false);

                File i = new File("C:/Users/Bridget/Documents/Spring 2016/Set/Images/0202.gif");
                img = ImageIO.read(i);
                cards[3].setIcon(new ImageIcon(img));
                cards[3].setBorder(BorderFactory.createEmptyBorder());
                cards[3].setContentAreaFilled(false);

                File j = new File("C:/Users/Bridget/Documents/Spring 2016/Set/Images/2121.gif");
                img = ImageIO.read(j);
                cards[4].setIcon(new ImageIcon(img));
                cards[4].setBorder(BorderFactory.createEmptyBorder());
                cards[4].setContentAreaFilled(false);

                File k = new File("C:/Users/Bridget/Documents/Spring 2016/Set/Images/1220.gif");
                img = ImageIO.read(k);
                cards[5].setIcon(new ImageIcon(img));
                cards[5].setBorder(BorderFactory.createEmptyBorder());
                cards[5].setContentAreaFilled(false);

                File l = new File("C:/Users/Bridget/Documents/Spring 2016/Set/Images/2222.gif");
                img = ImageIO.read(l);
                cards[6].setIcon(new ImageIcon(img));
                cards[6].setBorder(BorderFactory.createEmptyBorder());
                cards[6].setContentAreaFilled(false);

                File m = new File("C:/Users/Bridget/Documents/Spring 2016/Set/Images/0201.gif");
                img = ImageIO.read(m);
                cards[7].setIcon(new ImageIcon(img));
                cards[7].setBorder(BorderFactory.createEmptyBorder());
                cards[7].setContentAreaFilled(false);

                File n = new File("C:/Users/Bridget/Documents/Spring 2016/Set/Images/2122.gif");
                img = ImageIO.read(n);
                cards[8].setIcon(new ImageIcon(img));
                cards[8].setBorder(BorderFactory.createEmptyBorder());
                cards[8].setContentAreaFilled(false);

                File o = new File("C:/Users/Bridget/Documents/Spring 2016/Set/Images//1110.gif");
                img = ImageIO.read(o);
                cards[9].setIcon(new ImageIcon(img));
                cards[9].setBorder(BorderFactory.createEmptyBorder());
                cards[9].setContentAreaFilled(false);

                File p = new File("C:/Users/Bridget/Documents/Spring 2016/Set/Images/1000.gif");
                img = ImageIO.read(p);
                cards[10].setIcon(new ImageIcon(img));
                cards[10].setBorder(BorderFactory.createEmptyBorder());
                cards[10].setContentAreaFilled(false);

                File q = new File("C:/Users/Bridget/Documents/Spring 2016/Set/Images/2211.gif");
                img = ImageIO.read(q);
                cards[11].setIcon(new ImageIcon(img));
                cards[11].setBorder(BorderFactory.createEmptyBorder());
                cards[11].setContentAreaFilled(false);
           // }
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
                    selectEnable();
                    //Systems integration- Venkat
                } else {
                    //submitSet();
                    selectDisable();
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

    private void submitSet(String username) {
        //Gordon?
    }

    public void updateCards(Card cards[], boolean [] remove){
        //Gordon?
    }

    //If there isn't a valid set, the size of the board grid has to increase
    public void updateGrid(){

    }

    public void addCard(final Card c) {
        int columns = getComponentCount();
        final JPanel lastCol = (JPanel) getComponent(columns-1);
        c.addMouseListener(cardSelectionListener);
        cardcount++;
        int cardsInCol = lastCol.getComponentCount();
        if(cardsInCol < rows) {
            cardMap.put(c.toString(), new Location(columns-1,cardsInCol));
        } else { //column full - add a new column
            JPanel temp = new JPanel(new GridLayout(rows, 1, 3, 3));
            add(temp);
            cardMap.put(c.toString(), new Location(columns,0));
        }
        revalidate();
    }


    private void formatTable(JTable t) {
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.getColumnModel().getColumn(0).setPreferredWidth(140);
        t.getColumnModel().getColumn(1).setPreferredWidth(60);
        t.setAutoCreateRowSorter(true);
    }

    private JPanel getScoreboard(int id, String name) {
        //Get scoreboard data from Gordon?
        JPanel temp = new JPanel();
        temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));
        scoreboard.setLayout(new BoxLayout(scoreboard, BoxLayout.Y_AXIS));
        temp.add(Box.createRigidArea(new Dimension(20,20)));
        temp.add(scoreboard);
        temp.add(Box.createVerticalGlue());
        return temp;
    }

    public void updateScoreboard(String[] data) {
        for(int i=2; i<data.length; i+=4) {
            scoreboard.add(new JLabel(data[i+1]+" Score: "+data[i+2]));
        }
        scoreboard.revalidate();
    }

    private class Location {
        public int col;
        public int row;

        public Location(int c, int r) {
            this.col = c;
            this.row = r;
        }
    }

    public static void main(String [] args){
        GUIGame game = new GUIGame(3,"LOL");
        game.createAndShowBoard();
    }

}
