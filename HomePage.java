import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class HomePage extends JPanel {
    private JButton hostButton = new JButton("Host Game");
    private JButton connectButton = new JButton("Connect to Game");
    private JButton rulesButton = new JButton("View Rules");
    private JButton logoutButton = new JButton("Logout");

    private BufferedImage loading;
    private JLabel welcome = new JLabel("Welcome to Megaland!");
    public JLabel nameWelcome = new JLabel("");
    private JFrame jFrame;
    public String username;

    public HomePage(JFrame frame, String username) {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        setSize(screenWidth, screenHeight);
        setLayout(null);
        this.jFrame = frame;
        this.username = username;
        nameWelcome.setText("<html>Hello <font color='red'>" + username + "</font>,</html>");

        double widthScale = screenWidth / 1920.0;
        double heightScale = screenHeight / 1040.0;

        try {
            loading = ImageIO.read(new File("Images" + File.separator + "MegalandWallpaper.jpg"));
        } catch (Exception ah) {
            ah.printStackTrace();
            System.out.println("Error Loading Images: " + ah.getMessage());
        }

        hostButton.setBounds((int)(400 * widthScale), (int)(550 * heightScale), (int)(500 * widthScale), (int)(75 * heightScale));
        connectButton.setBounds((int)(400 * widthScale), (int)(650 * heightScale), (int)(500 * widthScale), (int)(75 * heightScale));
        rulesButton.setBounds((int)(400 * widthScale), (int)(750 * heightScale), (int)(500 * widthScale), (int)(75 * heightScale));
        logoutButton.setBounds((int)(1650 * widthScale), (int)(30 * heightScale), (int)(200 * widthScale), (int)(50 * heightScale));
        nameWelcome.setBounds((int)(300 * widthScale), (int)(330 * heightScale), (int)(750 * widthScale), (int)(90 * heightScale));
        nameWelcome.setFont(new Font("Georgia", Font.BOLD, (int)(50 * Math.min(widthScale, heightScale))));
        nameWelcome.setForeground(Color.BLACK);
        welcome.setBounds((int)(300 * widthScale), (int)(385 * heightScale), (int)(750 * widthScale), (int)(90 * heightScale));
        welcome.setFont(new Font("Georgia", Font.BOLD, (int)(60 * Math.min(widthScale, heightScale))));
        welcome.setForeground(Color.BLACK);

        hostButton.setFocusPainted(false);
        connectButton.setFocusPainted(false);
        rulesButton.setFocusPainted(false);
        logoutButton.setFocusPainted(false);

        hostButton.setBackground(Color.black);
        hostButton.setForeground(Color.white);
        connectButton.setBackground(Color.black);
        connectButton.setForeground(Color.WHITE);
        rulesButton.setBackground(Color.black);
        rulesButton.setForeground(Color.WHITE);
        logoutButton.setBackground(Color.black);
        logoutButton.setForeground(Color.WHITE);
        hostButton.setBorder(BorderFactory.createEmptyBorder());
        connectButton.setBorder(BorderFactory.createEmptyBorder());
        rulesButton.setBorder(BorderFactory.createEmptyBorder());
        logoutButton.setBorder(BorderFactory.createEmptyBorder());
        logoutButton.setBorder(new LineBorder(Color.red, 1));

        hostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setContentPane(new HostPage(jFrame, username));
                jFrame.revalidate();
                jFrame.repaint();
            }
        });

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setContentPane(new ClientPage(jFrame, username));
                jFrame.revalidate();
                jFrame.repaint();
            }
        });

        hostButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hostButton.setBorder(new LineBorder(Color.white, 1));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                hostButton.setBorder(BorderFactory.createEmptyBorder());
            }
        });
        connectButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                connectButton.setBorder(new LineBorder(Color.white, 1));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                connectButton.setBorder(BorderFactory.createEmptyBorder());
            }
        });
        rulesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                rulesButton.setBorder(new LineBorder(Color.white, 1));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                rulesButton.setBorder(BorderFactory.createEmptyBorder());
            }
        });

        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutButton.setBorder(new LineBorder(Color.white, 1));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                logoutButton.setBorder(new LineBorder(Color.red, 1));
            }
        });
        hostButton.setFont(new Font("Georgia", Font.BOLD, (int)(30 * Math.min(widthScale, heightScale))));
        connectButton.setFont(new Font("Georgia", Font.BOLD, (int)(30 * Math.min(widthScale, heightScale))));
        rulesButton.setFont(new Font("Georgia", Font.BOLD, (int)(30 * Math.min(widthScale, heightScale))));
        logoutButton.setFont(new Font("Georgia", Font.BOLD, (int)(20 * Math.min(widthScale, heightScale))));

        hostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        rulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setContentPane(new LoginPage(jFrame));
                jFrame.revalidate();
                jFrame.repaint();
            }
        });

        add(hostButton);
        add(connectButton);
        add(rulesButton);
        add(welcome);
        add(nameWelcome);
        add(logoutButton);
        setVisible(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(loading, 0, 0, getWidth(), getHeight(), null);
    }
}
