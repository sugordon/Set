package Client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Bridget on 4/23/2016.
 */

public class GUILobby extends JPanel{
    public JFrame lobby = new JFrame("Lobby");

    private JPanel games = new JPanel();
    private JPanel users = new JPanel();
    private JPanel scoreboard = new JPanel();

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

    String[] userColumns = {"Name",
            "Ranking"};
    String[] gameColumnLabels = {"Name",
            "Players"};

    /*
    Object[][] userData = {                                                     //swap for Player.getName, and Player.getScore
            {"TheM3owLord", new Integer(1)},
            {"GSu32", new Integer(3)},
            {"AlpacaFloof", new Integer(2)}
    };
    */

    Object[][] gameData = {                                                     //swap for Game.getGameName, and Game.getPlayers? and Game.getMaxPlayers?
            {"Meow Mix Cafe", "2/2"},
            {"Alpaca Picnic", "2/5"},
            {"The Set World Championship", "1/5"}
    };

    DefaultTableModel gameModel = new DefaultTableModel(gameData, gameColumnLabels);
    //DefaultTableModel userModel = new DefaultTableModel(userData, userColumns);

    public void createLobby(String uid){
        lobby.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        lobby.getContentPane().setBackground(new Color(51, 255, 255));
        lobby.setPreferredSize(new Dimension(1000, 700));
        lobby.setResizable(false);
        lobby.pack();
        this.myID = uid;

        games.setLayout(new BoxLayout(games, BoxLayout.Y_AXIS));
        //users.setLayout(new BoxLayout(users, BoxLayout.Y_AXIS));

        //Game Section
        JTable gameTable = new JTable(gameModel) {
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

        JPanel temp = new JPanel();
        temp.add(refreshGameButton);
        temp.add(createGameButton);
        temp.setAlignmentX(Component.CENTER_ALIGNMENT);
        temp.setMaximumSize(new Dimension(200, 10));
        games.add(temp);
        add(games, BorderLayout.CENTER);

        /*
        //Scoreboard Section
        userTable = new JTable(userModel) {
            @Override //Disable editing
            public boolean isCellEditable(int r, int c) {return false;}
        };
        formatTable(userTable);
        JScrollPane scrollPane2 = new JScrollPane(userTable);
        scrollPane2.setPreferredSize(new Dimension(200, 200));
        JLabel usersLabel = new JLabel("Users Online");
        usersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        refreshScoreboard.setAlignmentX(Component.CENTER_ALIGNMENT);
        users.add(usersLabel);
        users.add(scrollPane2);
        temp = new JPanel();
        temp.add(refreshScoreboard);
        temp.add(logoutButton);
        temp.setAlignmentX(Component.CENTER_ALIGNMENT);
        temp.setMaximumSize(new Dimension(200, 10));
        users.add(temp);
        add(users, BorderLayout.EAST);
        */

        addListeners();
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
                JOptionPane.showMessageDialog(null,getGameCreationPanel(),"Game Creation",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        createGameButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                create_game(myID, );
            }
        });
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
                    int selectedRow = gameTable.convertRowIndexToModel(lsm.getMinSelectionIndex());
                    int id = ((Integer) gameTable.getModel().getValueAt(selectedRow, 0)).intValue();
                    games.removeAll();
                    games.add(getGamePanel((String) gameTable.getModel().getValueAt(selectedRow, 1)));
                    games.revalidate();
                }
            }
        };

        gameTable.getSelectionModel().addListSelectionListener(lsl);

    }

    private void update_game_list(Object[][] gameData){
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

    private void formatTable(JTable t) {
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.removeColumn(t.getColumnModel().getColumn(0));
        t.getColumnModel().getColumn(0).setPreferredWidth(140);
        t.getColumnModel().getColumn(1).setPreferredWidth(60);
        t.setAutoCreateRowSorter(true);
    }

}
