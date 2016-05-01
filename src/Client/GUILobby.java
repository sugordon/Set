package Client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Bridget on 4/23/2016.
 */

public class GUILobby extends JPanel{
    public JFrame lobby = new JFrame("Lobby");
    public JButton createGameButton = new JButton("Create Game");
    public JButton createGameButton2 = new JButton("Submit New Game");
    public JButton cancelGameCreate = new JButton("Cancel");
    public JButton logoutButton = new JButton("Logout");
    public JButton joinGameButton = new JButton("Join Game");
    public int selectedRow;
    String[] gameColumnLabels = {"Game Name", "Owner", "Players", "Max Players"};
    ArrayList<Object[]> gameData = new ArrayList<>();
    DefaultTableModel gameModel = new DefaultTableModel();
    private JPanel games = new JPanel();
    private JPanel theOneTruePanel = new JPanel(new GridLayout(1,2));
    private JTable gameTable;
    private JButton refreshGameButton = new JButton("Refresh");
    private JTextField nameField = new JTextField();    //game name
    private JPasswordField passField = new JPasswordField();    //game password
    private String myID;
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

    public void createLobby(String uid){
        lobby.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagConstraints c = new GridBagConstraints();

        lobby.getContentPane().setBackground(new Color(168, 168, 168));
        lobby.setPreferredSize(new Dimension(1000, 700));
        lobby.setResizable(false);
        lobby.pack();
        this.myID = uid;

        games.setLayout(new BoxLayout(games, BoxLayout.Y_AXIS));
        games.setSize(new Dimension(800,800));

        //Game Section

        Object[][] gd = {

                {"", "", "", ""}
        };
        DefaultTableModel gameModel = new DefaultTableModel(gd, gameColumnLabels);
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

        setAlignmentX(Component.CENTER_ALIGNMENT);
        setMaximumSize(new Dimension(200, 10));


        games.setVisible(true);
        addListeners();

        passField.setPreferredSize(new Dimension(160, 25));
        nameField.setPreferredSize(new Dimension(160, 25));

        //The one actual panel where everything appears
        theOneTruePanel.setBorder(BorderFactory.createLineBorder(new Color(51, 255, 255)));
        theOneTruePanel.setPreferredSize(new Dimension(1000,800));
        this.update_game_list(gameData);


        String display;
        try {
            display = (String) gameTable.getModel().getValueAt(selectedRow, 1);
        }
        catch(ArrayIndexOutOfBoundsException e){
            display = "No games";
        }

        theOneTruePanel.add(getGamePanel(display));
        theOneTruePanel.add(games);



        //theOneTruePanel.add(refreshGameButton);
        //theOneTruePanel.add(createGameButton);

        add(theOneTruePanel, BorderLayout.CENTER);

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
                    resetTheOneTruePanel();
                }
            }
        };

        gameTable.getSelectionModel().addListSelectionListener(lsl);

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
                theOneTruePanel.repaint();
            }
        });
        createGameButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String gameName = nameField.getText();
                String password = getPass();
                create_game(myID, gameName, password, 5);
            }
        });

        cancelGameCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTheOneTruePanel();;
            }
        });

        joinGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO : GORDON
            }
        });

        passField.addFocusListener(passFocus);
        nameField.addFocusListener(nameFocus);

    }

    private void update_game_list(ArrayList<Object[]> gameData){
        System.out.println("HIHI");
        ClientInit.inStream.println("GAMES");
        System.out.println("SENT");
        String s = null;
        try {
            while ((s = ClientInit.outStream.readLine()) == null);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading Games");
        }
        System.out.println(s);

        gameData.clear();
        String[] sents = s.split(":");
        for (String sent : Arrays.copyOfRange(sents, 1, sents.length-1)) {
            gameData.add(sent.split(","));
        }
        Object tmp[][] = new Object[gameData.size()][4];
        tmp = gameData.toArray(tmp);
        gameModel = new DefaultTableModel(tmp, gameColumnLabels);
        gameTable.setModel(gameModel);
        formatTable(gameTable);
    }

    private void disconnect_from_server(){
        try {
            ClientInit.sck.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //After you log out, takes you back to login screen
    }

    private void create_game(String uid, String game_name, String game_password, int max_users){
        System.out.println(game_name);
        if (game_name == null) {
            JOptionPane.showMessageDialog(null, "Null game name");
            return;
        }
        ClientInit.inStream.println("CREATE,"+game_name+","+max_users+","+game_password);
        String s = null;
        try {
            while ((s = ClientInit.outStream.readLine()) == null);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error creating game");
            resetTheOneTruePanel();
        }
        System.out.println(s);
        resetTheOneTruePanel();
    }

    public void resetTheOneTruePanel() {
        detachListeners();
        theOneTruePanel.removeAll();

        String display;
        try {
            display = (String) gameTable.getModel().getValueAt(selectedRow, 1);
        }
        catch(ArrayIndexOutOfBoundsException e){
            display = "No games";
        }
        theOneTruePanel.add(getGamePanel(display));
        theOneTruePanel.add(games);
        theOneTruePanel.revalidate();
        theOneTruePanel.repaint();
        addListeners();

    }

    //Game panel
    private JPanel getGamePanel(String name) {
        JPanel temp = new JPanel();
        temp.setMaximumSize(new Dimension(200,800));
        temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));
        temp.add(Box.createRigidArea(new Dimension(20,20)));
        temp.add(new JLabel("<html><h1>"+name+"</h1></html>"));
        temp.add(new JLabel("<html><p>Players:</p></html>"));
        temp.add(Box.createVerticalGlue());
        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(joinGameButton);
        buttons.add(refreshGameButton);
        buttons.add(createGameButton);
        temp.add(buttons);
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
        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(createGameButton2);
        buttons.add(cancelGameCreate);
        temp.add(buttons);
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
