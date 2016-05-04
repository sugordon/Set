package Client;

import game.Card;
import game.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
    private String userLocked;

    private ArrayList<Card> selected = new ArrayList<Card>(); //List of selected cards

    public Timer timer;

    String[] userColumns = {"Name",
            "Score"};

    Object[][] userData = {                                                     //swap for Player.getName, and Player.getScore
            {"", new Integer(0)},
    };

    DefaultTableModel userModel = new DefaultTableModel(userData, userColumns);

    //Constructor
    public GUIGame(int rows, String uid) {
        this.rows = rows;
        this.myUN = uid;
        board = new game.Board();
        cards = Game.createDeck(new ArrayList<>());
    }


    //Creates JPanel and its characteristics for gameboard
    public void createAndShowBoard() {
        gameboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gameboard.getContentPane().setBackground(new Color(99, 111, 115));
        gameboard.setPreferredSize(new Dimension(1000, 700));
        gameboard.setResizable(false);
        gameboard.pack();
        buttons();

        ClientInit.inStream.println("GAME_START");

        users = new JPanel();
        createCardSpace();

        createScoreboard();

        createBanner();

        layoutGame();

        gameboard.setVisible(true);
        gameboard.setTitle(this.myUN);

        this.gameboard.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.gameboard.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //TODO: Add proper exit behavior
                super.windowClosing(e);

                try {
                    ClientInit.sck.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
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
                Image img = ImageIO.read(f);
//                System.out.println(path);
//                ImageIcon img = new ImageIcon(getClass().getResource(path));
//                ImageIcon img = new ImageIcon(getClass().getResource("images_cards/0000.gif"));
//                tmp.setIcon(img);
                tmp.setText(tmp.toString());
                tmp.setIcon(new ImageIcon(img));
                tmp.setBorder(BorderFactory.createLineBorder(new Color(6, 138, 10), 10));
                tmp.setBorderPainted(false);
                tmp.setContentAreaFilled(false);
                tmp.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Card c = (Card) e.getSource();
                        //Toggle the button
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
                            System.out.println("YOU PRESSED THE BUTTON. " + c.toString());
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return cards;
    }
    //Disables the SET button for a specified amount of time after it's been pressed

    public void buttons(){
        setButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.out.println("BUTTON PRESSED");
                //Start accepting a possible set
                if (userLocked == null || userLocked.equals(myUN))
                {
                    //Can submit right now
                    if (setButton.getText() == "SUBMIT") {
                        if (myUN.equals(userLocked)) {
                            submitSet(myUN, selected);
                            timer.stop();
                        }
//                        reset();
//                        clearButtons();
//                        setButton.setText("SET");
                    } else {//Clicked the Set button, send a Lock request
                        if (setButton.getText() == "SET" && (userLocked == null || myUN.equals(userLocked))) {
                            ClientInit.inStream.println("LOCK");
                        }
                    }
                }
            }
        });
    }

    //Clears it and deletes it from the selected set
    private void clearButtons() {
        for (int i = 0; i < selected.size(); i++) {
            Card tmp = selected.get(i);
            tmp.setBorderPainted(false);
            tmp.setSelected(false);
        }
        selected.clear();
    }

    private void submitSet(String username, ArrayList<Card> selected){
        if (selected.size() != 3) {
            this.disabled();
            return;
        }
        String s = "REPLACE";
        for (Card c : selected) {
            s += ","+c.toString();
        }
        System.out.println("SND="+s);
        clearButtons();
        ClientInit.inStream.println(s);
    }

    //Replace cards selected with new cards
    //Gordon calls this
    public void updateCards(ArrayList<Card> newCards) {
        cardspace.removeAll();
        this.board.removeAll();
        int size = 0;
        if(newCards != null) {
            size = newCards.size();
            generateCards(newCards);

            for (int i = 0; i < size; i++){
//                System.out.println("Addign new card");
                Card tmp = newCards.get(i);
                board.insert(tmp);
                tmp.setVisible(true);
                tmp.setPreferredSize(new Dimension(20, 20));
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

    public void processResponse(String msg){
        String [] tokens = msg.split(",");
        System.out.println("MSG="+msg);
        System.out.println("Game processing the response");


        if(tokens[0].equals("ACK_REPLACE")){
            clearButtons();
            selected.clear();
            userLocked = null;
            System.out.println("TOK=" + tokens[2] + " NAME="+this.myUN);
            if (tokens[1].equals("SUCCESS")) {
                ArrayList<game.Card> newBoard = new ArrayList<>(12);
                for (int i = 3; i < tokens.length - 1; i++) {
                    newBoard.add(new Card(tokens[i]));
                }
                this.updateCards(newBoard);
                this.updateUserModel(tokens[2], 1);
                if(tokens[2].equals(this.myUN))
                    enableButton();
                else
                    disabled();

            } else {
                if (myUN.equals(tokens[2])) {
                    JOptionPane.showMessageDialog(this.gameboard, "Not a Set");
                    disabled();
                } else {
                    System.out.println("The other client is disabled");
                    enableButton();
                }
                this.updateUserModel(tokens[2], -1);
            }
            userLocked = null;
        } else if (tokens[0].equals("ACK_START")) {
            clearButtons();
            selected.clear();
            boolean seenCards = false;
            ArrayList<Object[]> newScoreboard = new ArrayList<>();
            ArrayList<Card> cards = new ArrayList<>(12);

            for (int i = 3; i < tokens.length; i++) {
                if (seenCards == false) {
                    if (tokens[i].equals("CARDS")) {
                        seenCards = true;
                        continue;
                    }
                    Object[] row = {tokens[i], new Integer(0)};
                    newScoreboard.add(row);

                } else {
                    if (tokens[i].equals("GAME"))
                        break;
                    cards.add(new Card(tokens[i]));
                }
            }
            Object tmp[][] = new Object[newScoreboard.size()][4];
            tmp = newScoreboard.toArray(tmp);
            updateScoreboard(tmp);
//            System.out.println(cards);
            this.updateCards(cards);
        } else if (tokens[0].equals("LOCK")) {
            if (userLocked == null) {
                System.err.println("userLocked not null " + userLocked);
            }
            userLocked = tokens[1];
            if (userLocked.equals(this.myUN) == false) {
                clearButtons();
                selected.clear();
                System.out.println("LOCK client " + this.myUN);
                this.locked(tokens[1]);
            } else {
                //Submit mode
                this.setButton.setText("SUBMIT");
                this.locked(tokens[1]);
            }
        }
        else if (tokens[0].equals("ENABLE")){
            clearButtons();
            selected.clear();
            enableButton();
        }
    }

    private void updateUserModel(String name, int dir) {
        System.out.println("BEGIN UPDATE");
        ArrayList<Object[]> newScoreboard = new ArrayList<>();

        System.out.println("ROW="+userTable.getModel().getRowCount());
        for (int i = 0; i < userTable.getModel().getRowCount(); i++) {
            Object[] row = {userTable.getModel().getValueAt(i,0), new Integer(((Integer) userTable.getModel().getValueAt(i, 1)).intValue()+dir)};
            System.out.println(row[0] + " " + row[1]);
            newScoreboard.add(row);
        }

    }

    private void disabled() {
        setButton.setEnabled(false);
        setButton.setText("Disabled");
        Timer t = new Timer(Game.LOCKTIME, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userLocked == null) {
                    enableButton();
                }
            }
        });
        t.setRepeats(false);
        t.start();
    }

    private void enableButton() {
        if(userLocked!=null){
            locked(userLocked);
        }
        else {
            setButton.setEnabled(true);
            setButton.setText("SET");
        }
    }

    private void locked(String user) {
        System.out.println(user + " is locked");
        userLocked = user;
        if (user.equals(this.myUN)) {
            setButton.setText("SUBMIT");
            timer = new Timer(Game.LOCKTIME, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    submitSet(myUN, selected);
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            this.setButton.setText("Locked by " + user);
        }

    }

    //Gordon calls this
    public void updateScoreboard(Object [][] data) {
//        for(int i=0; i<data.length; i+=4) {
//            scoreboard.add(new JLabel(data[i+1]+" Score: "+data[i+2]));
//        }

        DefaultTableModel gameModel = new DefaultTableModel(data, this.userColumns);
//        setModel(gameModel);
        userTable.setModel(gameModel);
        formatTable(userTable);

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


    private void disconnect_from_server(){
        try {
            ClientInit.sck.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //After you log out, takes you back to login screen
    }

}
