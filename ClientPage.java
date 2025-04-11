import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.*;
import java.net.*;
import java.nio.Buffer;
import java.util.ArrayList;

public class ClientPage extends JPanel{
    private BufferedImage loading;


    private JLabel titleLabel = new JLabel("Establish Connection");
    private JLabel nameLabel = new JLabel("Name: ");
    public JTextField nameTextField = new JTextField(20);
    private JLabel ipLabel = new JLabel("Game Code: ");
    private JTextField ipTextField = new JTextField(15);
    public JButton confirmButton = new JButton("Connect to Live Game!");
    private JButton homeButton = new JButton("Home");
    private JTextArea playersJoined = new JTextArea();
    private JScrollPane playersScrollPane = new JScrollPane(playersJoined);
    private JLabel peopleInGameLabel = new JLabel("People in Game Server: ");
    private JFrame jFrame1;

    public String username;
    public double widthScale;
    public double heightScale;
    public DatabaseManager databaseManager = new DatabaseManager();
    public Boolean connectedToGame = false;
    private JPanel peoplePanel = new JPanel();

    public ArrayList<String> peopleInGame = new ArrayList<>();
    public String gameCode;
    public Thread lobbyUpdater;
    private volatile boolean isLobbyUpdaterRunning = false;


    public ClientPage(JFrame frame, String username){

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        setSize(screenWidth, screenHeight);

        widthScale = screenWidth / 1920.0;
        heightScale = screenHeight / 1040.0;
        setLayout(null);
        this.jFrame1 = frame;
        this.username = username;

        try {
            loading = ImageIO.read(new File("Images" + File.separator + "MegalandWallpaper.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Loading Images: " + e.getMessage());
        }

        titleLabel.setBounds((int)(280 * widthScale), (int)(340 * heightScale-10), (int)(750 * widthScale), (int)(90 * heightScale));
        titleLabel.setFont(new Font("Georgia", Font.BOLD, (int)(50 * Math.min(widthScale, heightScale))));
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel);

        nameLabel.setBounds((int)((394-(394-280)) * widthScale), (int)(450 * heightScale-15), (int)(140 * widthScale), (int)(40 * heightScale));
        nameLabel.setFont(new Font("Georgia", Font.PLAIN,(int)(25 * Math.min(widthScale, heightScale))));
        nameLabel.setOpaque(true);
        nameLabel.setBackground(Color.black);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setVerticalAlignment(SwingConstants.CENTER);
        nameLabel.setBorder(new LineBorder(Color.white, 1));
        add(nameLabel);

        nameTextField.setBounds((int)((532-(394-280)) * widthScale), (int)(450 * heightScale-15), (int)(292 * widthScale), (int)(40 * heightScale));
        nameTextField.setEnabled(false);
        nameTextField.setFocusable(true);
        nameTextField.setBackground(Color.BLACK);
        nameTextField.setForeground(Color.WHITE);
        nameTextField.setHorizontalAlignment(SwingConstants.CENTER);
        nameTextField.setFont(new Font("Georgia", Font.PLAIN,(int)(25 * Math.min(widthScale, heightScale))));
        nameTextField.setCaretColor(Color.white);
        nameTextField.setBorder(new LineBorder(Color.white, 1));
        nameTextField.setEditable(false);
        nameTextField.setText(username);
        add(nameTextField);

        ipLabel.setBounds((int)((394-(394-280)) * widthScale), (int)(510 * heightScale-20), (int)(140 * widthScale), (int)(40 * heightScale));
        ipLabel.setFont(new Font("Georgia", Font.PLAIN,(int)(20 * Math.min(widthScale, heightScale))));
        ipLabel.setVisible(true);
        ipLabel.setOpaque(true);
        ipLabel.setBackground(Color.black);
        ipLabel.setForeground(Color.WHITE);
        ipLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ipLabel.setVerticalAlignment(SwingConstants.CENTER);
        ipLabel.setBorder(new LineBorder(Color.white, 1));
        add(ipLabel);

        ipTextField.setBounds((int)((532-(394-280)) * widthScale), (int)(510 * heightScale-20), (int)(292 * widthScale), (int)(40 * heightScale));
        ipTextField.setVisible(true);
        ipTextField.setFocusable(true);
        ipTextField.setBackground(Color.BLACK);
        ipTextField.setForeground(Color.WHITE);
        ipTextField.setHorizontalAlignment(SwingConstants.CENTER);
        ipTextField.setFont(new Font("Georgia", Font.PLAIN,(int)(25 * Math.min(widthScale, heightScale))));
        ipTextField.setCaretColor(Color.white);
        ipTextField.setBorder(new LineBorder(Color.white, 1));
        ipTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateConfirmButtonState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateConfirmButtonState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateConfirmButtonState();
            }
        });
        add(ipTextField);

        confirmButton.setBounds((int)((394-(394-280)) * widthScale), (int)(570 * heightScale-20), (int)(430 * widthScale), (int)(40 * heightScale));
        confirmButton.setFont(new Font("Georgia",Font.BOLD,(int)(18 * Math.min(widthScale, heightScale))));
        confirmButton.setEnabled(false);
        confirmButton.setBackground(Color.black);
        confirmButton.setForeground(Color.white);
        confirmButton.setBorder(BorderFactory.createEmptyBorder());
        confirmButton.setFocusPainted(false);
        confirmButton.addActionListener(e -> {
            gameCode = ipTextField.getText();
            connectedToGame = databaseManager.connectToHostGame(username, gameCode);
            if(connectedToGame){
                databaseManager.addClient(username, gameCode);
                confirmButton.setText("Connected");
                confirmButton.setEnabled(false);
                ipTextField.setEnabled(false);
                homeButton.setText("Leave");
                peopleInGame = databaseManager.getPlayersInGame(gameCode);
                playersJoined.setText("");
                StringBuilder sb = new StringBuilder("");
                for(String playerName : peopleInGame){
                    sb.append(playerName).append("\n");
                }
                gameCode = ipTextField.getText();
                playersJoined.setText(sb.toString());
                peopleInGame = databaseManager.getPlayersInGame(gameCode);
                startLobbyUpdateThread();
            }
        });
        add(confirmButton);


        homeButton.setBounds((int)(10 * widthScale+10), (int)(10 * heightScale+10), (int)(100 * widthScale+20), (int)(40 * heightScale));
        homeButton.setFont(new Font("Georgia", Font.BOLD, (int)(20 * Math.min(widthScale, heightScale))));
        homeButton.setFocusPainted(false);
        homeButton.setBackground(Color.black);
        homeButton.setForeground(Color.white);
        homeButton.addActionListener(e -> {
            if(connectedToGame){
                stopLobbyUpdateThread();
                databaseManager.removeClientFromGame(username);
                jFrame1.setContentPane(new HomePage(jFrame1, username));
                jFrame1.revalidate();
                jFrame1.repaint();
            }
            else{
                jFrame1.setContentPane(new HomePage(jFrame1, username));
                jFrame1.revalidate();
                jFrame1.repaint();
            }
        });

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

        peopleInGameLabel.setBounds(394-(394-280), 630, 300, 40);
        peopleInGameLabel.setBounds((int)((394-(394-280)) * widthScale), (int)(630 * heightScale-20), (int)(300 * widthScale+20), (int)(40 * heightScale));
        peopleInGameLabel.setFont(new Font("Georgia", Font.PLAIN, (int)(20 * Math.min(widthScale, heightScale))));
        peopleInGameLabel.setVisible(true);
        peopleInGameLabel.setOpaque(true);
        peopleInGameLabel.setBackground(Color.black);
        peopleInGameLabel.setForeground(Color.WHITE);
        peopleInGameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        peopleInGameLabel.setVerticalAlignment(SwingConstants.CENTER);
        peopleInGameLabel.setBorder(new LineBorder(Color.white, 1));
        add(peopleInGameLabel);

        playersJoined.setFont(new Font("Georgia", Font.PLAIN, (int)(20 * Math.min(widthScale, heightScale))));
        playersJoined.setEditable(false);
        playersJoined.setBounds((int)((394-(394-280)) * widthScale), (int)((690-15) * heightScale-20), (int)(430 * widthScale+20), (int)(240 * heightScale-20));
        playersJoined.setBackground(Color.BLACK);
        playersJoined.setForeground(Color.WHITE);
        playersJoined.setBorder(new LineBorder(Color.white, 1));

        playersScrollPane.setBounds((int)((394-(394-280)) * widthScale), (int)((690-15) * heightScale-20), (int)(430 * widthScale+20), (int)(240 * heightScale-20));
        add(playersJoined);

        peoplePanel.setBounds((int)((394-(394-280)) * widthScale), (int)((690-15) * heightScale-20), (int)(430 * widthScale+20), (int)(240 * heightScale-20));
        peoplePanel.setLayout(null);
        peoplePanel.add(playersScrollPane);
        add(peoplePanel);

        setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
        if(databaseManager.checkIfHostActive(gameCode)){
            peopleInGame = databaseManager.getPlayersInGame(gameCode/*ipTextField.getText()*/); //use commented code if players disappearing
            System.out.println(peopleInGame);
            playersJoined.setText("");
            StringBuilder sb = new StringBuilder("");
            for(String playerName : peopleInGame){
                sb.append(playerName).append("\n");
            }
            playersJoined.setText(sb.toString());
            if(databaseManager.checkGameStarted(gameCode)){
              System.out.println("Game Has Started!");
//            jFrame.setContentPane(new ____(jFrame, username, gameCode)); //Class Name, frame, username, and gamecode
//            jFrame.revalidate();
//            jFrame.repaint();
              stopLobbyUpdateThread();
            }
        } else{
            stopLobbyUpdateThread();
            jFrame1.setContentPane(new HomePage(jFrame1, username));
            jFrame1.revalidate();
            jFrame1.repaint();
        }

    }
    private void updateConfirmButtonState(){
        boolean isIPValid = !ipTextField.getText().trim().isEmpty();
        confirmButton.setEnabled(isIPValid);
        if(isIPValid) {
            confirmButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    confirmButton.setBorder(new LineBorder(Color.white, 1));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    confirmButton.setBorder(BorderFactory.createEmptyBorder());
                }
            });
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Graphics bg = buffer.getGraphics();
        g.drawImage(loading, 0, 0, getWidth(), getHeight(), null);
        //g.drawImage(buffer, 0, 0, null);
    }
}
