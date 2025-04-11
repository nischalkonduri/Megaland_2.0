import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HostPage extends JPanel{

    private JLabel hostSettingsLabel = new JLabel("Set Up Hosting Settings");
    private JLabel nameLabel = new JLabel("Name: ");
    public JTextField nameTextField = new JTextField(20);
    private JButton confirmSettings = new JButton("Confirm Settings");
    private JLabel numPlayersLabel = new JLabel("Number of Players: ");
    private ButtonGroup playerGroup = new ButtonGroup();
    private JRadioButton[] playerButtons = new JRadioButton[4];
    private JLabel ipLabel = new JLabel("Game Code: ");
    private JLabel ipPlaceHolderLabel = new JLabel("Waiting for game settings...");
    private JButton homeButton = new JButton("Home");
    private JButton startHostingButton = new JButton("Start Hosting");
    private JButton startButton = new JButton("Start Game");
    private boolean settingsConfirmed = false;
    private BufferedImage loading;
    private String ipAddress;
    public String hostName;
    public final int[] selectedNumberOfPlayers = {1};
    private JLabel peopleInGameLabel = new JLabel("People in Game Server: ");
    private JTextArea peopleListArea = new JTextArea();
    private JScrollPane peopleScrollPane = new JScrollPane(peopleListArea);
    private JPanel peoplePanel = new JPanel();
    public DatabaseManager databaseManager = new DatabaseManager();

    public String gameCode;
    public ArrayList<String> peopleInGame = new ArrayList<>();

    public String username;
    public JFrame jFrame;
    public double widthScale;
    public double heightScale;

    public Thread lobbyUpdater;
    private volatile boolean isLobbyUpdaterRunning = false;


    public HostPage(JFrame frame, String username) {
        this.jFrame = frame;
        this.username = username;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        hostName="";
        setSize(screenWidth, screenHeight);

        widthScale = screenWidth / 1920.0;
        heightScale = screenHeight / 1040.0;

        setLayout(null);

        try {
            loading = ImageIO.read(new File("Images" + File.separator + "MegalandWallpaper.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Loading Images: " + e.getMessage());
        }

        hostSettingsLabel.setBounds((int)(270 * widthScale), (int)(315 * heightScale), (int)(750 * widthScale), (int)(90 * heightScale));
        hostSettingsLabel.setFont(new Font("Georgia", Font.BOLD, (int)(50 * Math.min(widthScale, heightScale))));
        hostSettingsLabel.setForeground(Color.BLACK);
        add(hostSettingsLabel);

        nameLabel.setBounds((int)(260 * widthScale), (int)(420 * heightScale-10), (int)(140 * widthScale), (int)(40 * heightScale));
        nameLabel.setFont(new Font("Georgia", Font.PLAIN,(int)(25 * Math.min(widthScale, heightScale))));
        nameLabel.setOpaque(true);
        nameLabel.setBackground(Color.black);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setVerticalAlignment(SwingConstants.CENTER);
        nameLabel.setBorder(new LineBorder(Color.white, 1));
        add(nameLabel);

        nameTextField.setBounds((int)(398 * widthScale), (int)(420 * heightScale-10), (int)(292 * widthScale), (int)(40 * heightScale));
        nameTextField.setFocusable(true);
        nameTextField.setBackground(Color.BLACK);
        nameTextField.setForeground(Color.WHITE);
        nameTextField.setHorizontalAlignment(SwingConstants.CENTER);
        nameTextField.setFont(new Font("Georgia", Font.PLAIN,(int)(25 * Math.min(widthScale, heightScale))));
        nameTextField.setCaretColor(Color.white);
        nameTextField.setBorder(new LineBorder(Color.white, 1));
        nameTextField.setText(username);
        add(nameTextField);
        nameTextField.setEditable(false);

        confirmSettings.setBounds((int)(725 * widthScale), (int)(420 * heightScale-10), (int)(270 * widthScale), (int)(40 * heightScale));
        confirmSettings.setBorder(new LineBorder(Color.white, 1));
        confirmSettings.setFont(new Font("Georgia", Font.BOLD, (int)(20 * Math.min(widthScale, heightScale))));
        confirmSettings.setEnabled(false);
        confirmSettings.setBackground(Color.black);
        confirmSettings.setForeground(Color.white);
        confirmSettings.setBorder(BorderFactory.createEmptyBorder());
        confirmSettings.setFocusPainted(false);
        confirmSettings.addActionListener(e -> {
            if (settingsConfirmed) {
                nameTextField.setEnabled(true);
                startButton.setVisible(true);
                startHostingButton.setEnabled(false);
                confirmSettings.setText("Confirm Settings");
                ipPlaceHolderLabel.setText("Waiting for game settings...");
                setPlayerButtonsEnabled(true);
                peoplePanel.setVisible(true);
            } else {
                hostName = nameTextField.getText();
                nameTextField.setEnabled(false);
                confirmSettings.setText("Change Settings");
                ipPlaceHolderLabel.setText("Start Hosting Game...");
                setPlayerButtonsEnabled(false);
                peoplePanel.setVisible(true);
                startButton.setVisible(true);
                updateStartButtonState();
                startHostingButton.setEnabled(true);
            }
            settingsConfirmed = !settingsConfirmed;
            revalidate();
            repaint();
        });
        add(confirmSettings);

        numPlayersLabel.setBounds((int)(260 * widthScale), (int)(480 * heightScale-10), (int)(200 * widthScale), (int)(40 * heightScale));
        numPlayersLabel.setFont(new Font("Georgia", Font.PLAIN, (int)(20 * Math.min(widthScale, heightScale))));
        numPlayersLabel.setOpaque(true);
        numPlayersLabel.setBackground(Color.black);
        numPlayersLabel.setForeground(Color.WHITE);
        numPlayersLabel.setHorizontalAlignment(SwingConstants.CENTER);
        numPlayersLabel.setVerticalAlignment(SwingConstants.CENTER);
        numPlayersLabel.setBorder(new LineBorder(Color.white, 1));
        add(numPlayersLabel);

        String[] numberPlayersOption = {"2", "3", "4", "5"};
        for (int i = 0; i < numberPlayersOption.length; i++) {
            final int index = i;
            playerButtons[i] = new JRadioButton(numberPlayersOption[i]);
            playerButtons[i].setBounds((int)((480+i*55) * widthScale), (int)(550 * heightScale - 60-10), (int)(55 * widthScale), (int)(80 * heightScale));
            playerButtons[i].setFont(new Font("Georgia", Font.BOLD, (int)(20 * Math.min(widthScale, heightScale))));
            playerButtons[i].setOpaque(false);
            playerButtons[i].setBackground(Color.black);
            playerButtons[i].setFocusPainted(false);
            playerButtons[i].setBorderPainted(false);
            playerButtons[i].setContentAreaFilled(false);
            playerButtons[i].addActionListener(e -> {
                selectedNumberOfPlayers[0] = Integer.parseInt(numberPlayersOption[index]);
                updateConfirmSettingsButtonState();
                updateStartButtonState();
            });
            playerGroup.add(playerButtons[i]);
            add(playerButtons[i]);
        }

        ipLabel.setBounds((int)(260 * widthScale), (int)(540 * heightScale-15), (int)(250 * widthScale), (int)(40 * heightScale));
        ipLabel.setFont(new Font("Georgia", Font.PLAIN, (int)(20 * Math.min(widthScale, heightScale))));
        ipLabel.setVisible(true);
        ipLabel.setOpaque(true);
        ipLabel.setBackground(Color.black);
        ipLabel.setForeground(Color.WHITE);
        ipLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ipLabel.setVerticalAlignment(SwingConstants.CENTER);
        ipLabel.setBorder(new LineBorder(Color.white, 1));
        add(ipLabel);

        ipPlaceHolderLabel.setBounds((int)(508 * widthScale), (int)(540 * heightScale-15), (int)(400 * widthScale), (int)(40 * heightScale));
        ipPlaceHolderLabel.setFont(new Font("Georgia", Font.ITALIC, (int)(20 * Math.min(widthScale, heightScale))));
        ipPlaceHolderLabel.setVisible(true);
        ipPlaceHolderLabel.setOpaque(true);
        ipPlaceHolderLabel.setBackground(Color.black);
        ipPlaceHolderLabel.setForeground(Color.WHITE);
        ipPlaceHolderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ipPlaceHolderLabel.setVerticalAlignment(SwingConstants.CENTER);
        ipPlaceHolderLabel.setBorder(new LineBorder(Color.white, 1));
        add(ipPlaceHolderLabel);

        peopleInGameLabel.setBounds((int)(260 * widthScale), (int)(600 * heightScale-20), (int)(300 * widthScale), (int)(40 * heightScale));
        peopleInGameLabel.setFont(new Font("Georgia", Font.PLAIN, (int)(20 * Math.min(widthScale, heightScale))));
        peopleInGameLabel.setVisible(true);
        peopleInGameLabel.setOpaque(true);
        peopleInGameLabel.setBackground(Color.black);
        peopleInGameLabel.setForeground(Color.WHITE);
        peopleInGameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        peopleInGameLabel.setVerticalAlignment(SwingConstants.CENTER);
        peopleInGameLabel.setBorder(new LineBorder(Color.white, 1));
        add(peopleInGameLabel);

        peopleListArea.setFont(new Font("Georgia", Font.PLAIN, (int)(20 * Math.min(widthScale, heightScale))));
        peopleListArea.setEditable(false);
        peopleListArea.setBounds((int)(260 * widthScale), (int)(645 * heightScale-15), (int)(430 * widthScale), (int)(240 * heightScale - 20));
        peopleListArea.setBackground(Color.BLACK);
        peopleListArea.setForeground(Color.WHITE);
        peopleListArea.setBorder(new LineBorder(Color.white, 1));
        add(peopleListArea);

        peopleScrollPane.setBounds((int)(260 * widthScale), (int)(645 * heightScale-15), (int)(430 * widthScale), (int)(240 * heightScale - 20));
        add(peopleScrollPane);

        peoplePanel.setBounds((int)(260 * widthScale), (int)(645 * heightScale-15), (int)(430 * widthScale), (int)(240 * heightScale - 20));
        peoplePanel.setLayout(null);
        peoplePanel.add(peopleScrollPane);
        add(peoplePanel);

        startHostingButton.setBounds((int)(260 * widthScale), (int)(905 * heightScale-20-20), (int)(200 * widthScale), (int)(40 * heightScale));
        startHostingButton.setFont(new Font("Georgia", Font.BOLD,(int)(20 * Math.min(widthScale, heightScale))));
        startHostingButton.setEnabled(false);
        startHostingButton.addActionListener(e -> {
            startHostingButton.setEnabled(false);
            startHostingButton.setBackground(Color.BLACK);
            startHostingButton.setForeground(Color.WHITE);
            startHostingButton.setText("Hosting...");
            confirmSettings.setEnabled(false);
            startHostingButton.setBorder(new LineBorder(Color.white, 1));
            StringBuilder code = new StringBuilder();
            Random r = new Random();
            for (int i = 0; i < 6; i++) {
                code.append((char) ('a' + r.nextInt(26)));
            }
            gameCode = code.toString();
            ipPlaceHolderLabel.setText(gameCode);
            databaseManager.addToHostTable(username, selectedNumberOfPlayers[0], gameCode);
            homeButton.setText("Leave");
            peopleInGame = databaseManager.getPlayersInGame(gameCode);
            startLobbyUpdateThread();
        });
        add(startHostingButton);
        startHostingButton.setFocusPainted(false);
        startHostingButton.setBackground(Color.black);
        startHostingButton.setForeground(Color.white);
        startHostingButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startHostingButton.setBorder(new LineBorder(Color.white, 1));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                startHostingButton.setBorder(null);
            }
        });


        startButton.setBounds((int)(490 * widthScale), (int)(905 * heightScale-20-20), (int)(200 * widthScale), (int)(40 * heightScale));
        startButton.setFont(new Font("Georgia",Font.BOLD,(int)(20 * Math.min(widthScale, heightScale))));
        startButton.setEnabled(false);
        startButton.addActionListener(e -> {
            databaseManager.setGameStartedStatus(gameCode);
//            jFrame.setContentPane(new ____(jFrame, username, gameCode)); //Class Name, frame, username, and gamecode
//            jFrame.revalidate();
//            jFrame.repaint();
            stopLobbyUpdateThread();
            startButton.setEnabled(false);
        });
        startButton.setFocusPainted(false);
        startButton.setBackground(Color.black);
        startButton.setForeground(Color.white);
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setBorder(new LineBorder(Color.white, 1));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setBorder(null);
            }
        });
        add(startButton);

        homeButton.setBounds((int)(10 * widthScale+10), (int)(10 * heightScale+10), (int)(100 * widthScale+20), (int)(40 * heightScale));
        homeButton.setFont(new Font("Georgia", Font.BOLD, (int)(20 * Math.min(widthScale, heightScale))));
        homeButton.addActionListener(e -> {

            databaseManager.removeHost(username);
            stopLobbyUpdateThread();
            jFrame.setContentPane(new HomePage(jFrame, username));
            jFrame.revalidate();
            jFrame.repaint();
        });
        homeButton.setFocusPainted(false);
        homeButton.setBackground(Color.black);
        homeButton.setForeground(Color.white);
        homeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                homeButton.setBorder(new LineBorder(Color.white, 1));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                homeButton.setBorder(null);
            }
        });
        add(homeButton);

        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    public void stopLobbyUpdateThread() {
        isLobbyUpdaterRunning = false;
        if (lobbyUpdater != null) {
            lobbyUpdater.interrupt();
        }
    }


    private void startLobbyUpdateThread() {
        lobbyUpdater = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                SwingUtilities.invokeLater(() -> updateLobbyView());
            }
        });
        lobbyUpdater.setDaemon(true);
        lobbyUpdater.start();
        isLobbyUpdaterRunning = true;
    }
    private void updateLobbyView() {
        peopleInGame = databaseManager.getPlayersInGame(gameCode);
        peopleListArea.setText("");
        StringBuilder sb = new StringBuilder("");
        for(String playerName : peopleInGame){
            sb.append(playerName).append("\n");
        }
        peopleListArea.setText(sb.toString());
        updateStartButtonState();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(loading, 0, 0, getWidth(), getHeight(), null);
    }

    private void setPlayerButtonsEnabled(boolean enabled) {
        for (JRadioButton button : playerButtons) {
            button.setEnabled(enabled);
        }
    }


    public void updateConfirmSettingsButtonState(){
        boolean isNameValid = !nameTextField.getText().trim().isEmpty();
        boolean isPlayerSelected = playerGroup.getSelection() != null;
        confirmSettings.setEnabled(isPlayerSelected);
        if(isPlayerSelected) {
            confirmSettings.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    confirmSettings.setBorder(new LineBorder(Color.white, 1));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    confirmSettings.setBorder(BorderFactory.createEmptyBorder());
                }
            });
        }
    }

    public void updateStartButtonState(){
        int numberOfPeople = databaseManager.getPlayersInGame(gameCode).size();
        startButton.setEnabled(numberOfPeople == selectedNumberOfPlayers[0]);
        System.out.println(selectedNumberOfPlayers[0]  + "Selected Number of People");
        System.out.println(numberOfPeople  + "Number of People");

    }
}