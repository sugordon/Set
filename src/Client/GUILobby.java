package Client;

import javax.swing.*;
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
    private JPanel scoreboard = new JPanel();

    private JTable gameList = new JTable();

    private JButton refreshGameButton = new JButton("Refresh");
    public JButton createGameButton = new JButton("Create Game");
    public JButton logoutButton = new JButton("Logout");
    public JButton joinGameButton = new JButton("Join Game");


    private JTextField nameField = new JTextField();    //game name
    private JTextField passField = new JTextField();    //game password
    private int myID;

    public void createLobby(int uid){
        lobby.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        lobby.getContentPane().setBackground(new Color(51, 255, 255));
        lobby.setPreferredSize(new Dimension(1000, 700));
        lobby.setResizable(false);
        lobby.pack();
        this.myID = uid;

        games.setLayout(new BoxLayout(games, BoxLayout.Y_AXIS));

        DefaultTableModel gameModel = new DefaultTableModel(gameData, gameColumns);
        JTable gameTable = new JTable(gameModel) {
            @Override
            public boolean isCellEditable(int r, int c) {return false;}
        };

        formatTable(gameTable);
        JScrollPane scrollPane = new JScrollPane(gameTable);
        scrollPane.setPreferredSize(new Dimension(COL_WIDTH, 200));
        JLabel gamesLabel = new JLabel("Games Listing");
        gamesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        games.add(gamesLabel);
        games.add(scrollPane);

        JPanel temp = new JPanel();
        temp.add(refreshGameButton);
        temp.add(createGameButton);
        temp.setAlignmentX(Component.CENTER_ALIGNMENT);
        temp.setMaximumSize(new Dimension(COL_WIDTH, 10));
        games.add(temp);
        add(games, BorderLayout.WEST);

        addListeners();
        refreshGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                update_game_list();
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                disconnect_from_server();
            }
        });
        createGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                create_game(myID, nameField.getText(), passField.getText(), 5);
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
    }

    private void update_game_list(){
        //GORDON FILL IN
    }

    private void disconnect_from_server(){
        //GORODON FILL IN
        //After you log out, takes you back to login screen
    }

    private void create_game(){

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
        temp.add(createGameButton);
        return temp;
    }

}
