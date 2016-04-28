package Client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * Created by Bridget on 4/23/2016.
 */

public class GUILobby extends JPanel{
    public JFrame lobby = new JFrame("Lobby");

    private JPanel games = new JPanel();
    private JPanel theOneTruePanel = new JPanel(new GridLayout(1,1,0,0));

    //private JTable userTable;
    private JTable gameTable;

    //private JButton refreshScoreboard = new JButton("Refresh");
    private JButton refreshGameButton = new JButton("Refresh");
    public JButton createGameButton = new JButton("Create Game");
    public JButton createGameButton2 = new JButton("Submit New Game");
    public JButton logoutButton = new JButton("Logout");
    public JButton joinGameButton = new JButton("Join Game");


    private JTextField nameField = new JTextField();    //game name
    private JTextField passField = new JTextField();    //game password
    private String myID;


    public int selectedRow;

    String[] gameColumnLabels = {"Name",
            "Players"};

    Object[][] gameData = {                                                     //swap for Game.getGameName, and Game.getPlayers? and Game.getMaxPlayers?
            {"Meow Mix Cafe", "2/2"},
            {"Alpaca Picnic", "2/5"},
            {"The Set World Championship", "1/5"}
    };

    DefaultTableModel gameModel = new DefaultTableModel(gameData, gameColumnLabels);

    public void createLobby(String uid){
        lobby.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        lobby.getContentPane().setBackground(new Color(168, 168, 168));
        lobby.setPreferredSize(new Dimension(1000, 700));
        lobby.setResizable(false);
        lobby.pack();
        this.myID = uid;

        games.setLayout(new BoxLayout(games, BoxLayout.Y_AXIS));

        //Game Section
        gameTable = new JTable(gameModel) {
            @Override
            public boolean isCellEditable(int r, int c) {return false;}
        };

        formatTable(gameTable);
        JScrollPane scrollPane = new JScrollPane(gameTable);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        JLabel gamesLabel = new JLabel("Games Listing");
        gamesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        games.add(gamesLabel);
        games.add(scrollPane);

        add(refreshGameButton);
        add(createGameButton);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setMaximumSize(new Dimension(200, 10));

        add(games, BorderLayout.SOUTH);
        games.setVisible(true);
        addListeners();
        passField.addFocusListener(passFocus);
        nameField.addFocusListener(nameFocus);

        //The one actual panel where everything appears
        theOneTruePanel.setBorder(BorderFactory.createLineBorder(new Color(51, 255, 255)));
        theOneTruePanel.add(getGamePanel((String) gameTable.getModel().getValueAt(selectedRow, 1)));
        add(theOneTruePanel, BorderLayout.CENTER);


        refreshGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                update_game_list(gameData);
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                disconnect_from_server();
            }
        });
        createGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                theOneTruePanel.removeAll();
                theOneTruePanel.add(getGameCreationPanel());
                theOneTruePanel.revalidate();
            }
        });
        createGameButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String gameName = getName();
                String password = getPass();
                create_game(myID, gameName, password, 5);
            }
        });
        lobby.add(this);
        setVisible(true);
        theOneTruePanel.setVisible(true);
    }

    private void addListeners(){
        lobby.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //TODO: Add proper exit behavior (take back to login)
                super.windowClosing(e);
            }
        });

        ListSelectionListener lsl = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()) return;
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if(!lsm.isSelectionEmpty()) {
                    selectedRow = gameTable.convertRowIndexToModel(lsm.getMinSelectionIndex());
                    theOneTruePanel.removeAll();
                    theOneTruePanel.add(getGamePanel((String) gameTable.getModel().getValueAt(selectedRow, 1)));
                    theOneTruePanel.revalidate();
                }
            }
        };

        gameTable.getSelectionModel().addListSelectionListener(lsl);

    }

    private FocusListener passFocus = new FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    passField.selectAll();
                }
            });
        }
    };

    private FocusListener nameFocus = new FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    nameField.selectAll();
                }
            });
        }
    };

    private void update_game_list(Object[][] gameData){
        System.out.println("HIHI");
        ClientInit.inStream.println("GAMES");
        System.out.println("SENT");
        String s = null;
        try {
            while ((s = ClientInit.outStream.readLine()) == null);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Loading Games");
        }
        System.out.println(s);

        //GORDON FILL IN
        gameModel = new DefaultTableModel(gameData, gameColumnLabels);
        gameTable.setModel(gameModel);
        formatTable(gameTable);
    }

    private void disconnect_from_server(){
        //GORODON FILL IN
        //After you log out, takes you back to login screen
    }

    private void create_game(String uid, String game_name, String game_password, int max_users){

    }

    public void resetTheOneTruePanel() {
        theOneTruePanel.removeAll();
        theOneTruePanel.add(getGamePanel((String) gameTable.getModel().getValueAt(selectedRow, 1)));
    }

    //Game panel
    private JPanel getGamePanel(String name) {
        JPanel temp = new JPanel();
        temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));
        temp.add(Box.createRigidArea(new Dimension(20,20)));
        temp.add(new JLabel("<html><h1>"+name+"</h1></html>"));
        temp.add(new JLabel("<html><p>Players:</p></html>"));
        temp.add(Box.createVerticalGlue());
        temp.add(joinGameButton);
        return temp;
    }

    private JPanel getGameCreationPanel() {
        JPanel temp = new JPanel();
        temp.add(Box.createRigidArea(new Dimension(0, 20)));
        temp.add(Box.createRigidArea(new Dimension(20, 0)));
        temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));
        temp.add(new JLabel("<html><h1>Create Game</h1></html>"));
        JPanel names = new JPanel();
        names.add(new JLabel("Enter a name: "));
        names.add(nameField);
        names.add(new JLabel("Enter a password: "));
        names.add(passField);
        temp.add(names);
        temp.add(Box.createVerticalGlue());
        temp.add(createGameButton2);
        return temp;
    }


    public String getGameName(){
        return nameField.getText();
    }

    public String getPass(){
        return passField.getText();
    }

    private void formatTable(JTable t) {
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.getColumnModel().getColumn(0).setPreferredWidth(140);
        t.getColumnModel().getColumn(1).setPreferredWidth(60);
        t.setAutoCreateRowSorter(true);
    }

    public void detachListeners() {
        for(ActionListener l : refreshGameButton.getActionListeners())
            refreshGameButton.removeActionListener(l);
        for(ActionListener l : createGameButton.getActionListeners())
            createGameButton.removeActionListener(l);
        for(ActionListener l : logoutButton.getActionListeners())
            logoutButton.removeActionListener(l);
        for(ActionListener l : joinGameButton.getActionListeners())
            joinGameButton.removeActionListener(l);
        passField.removeFocusListener(passFocus);
    }

}
