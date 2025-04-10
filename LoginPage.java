import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginPage extends JPanel {
    public JFrame jFrame;
    public DatabaseManager databaseManager = new DatabaseManager();

    public LoginPage(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setLayout(null);
        this.jFrame = frame;

        setBackground(new Color(0x1B1B1B));

        JLabel loginLabel = new JLabel("LOGIN", JLabel.CENTER);
        loginLabel.setBounds((screenSize.width - 600) / 2, 100, 600, 100);
        loginLabel.setFont(new Font("Georgia", Font.BOLD, 60));
        loginLabel.setForeground(new Color(0x00FF00));
        add(loginLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds((screenSize.width - 600) / 2 + 50, 250 - 20, 200, 30);
        usernameLabel.setFont(new Font("Courier New", Font.PLAIN, 20));
        usernameLabel.setForeground(new Color(0x00FF00));
        add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds((screenSize.width - 600) / 2 + 50, 290 - 20, 500, 40);
        usernameField.setBackground(new Color(0x333333));
        usernameField.setForeground(new Color(0x00FF00));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(0x00FF00), 2));
        usernameField.setCaretColor(Color.GREEN);
        usernameField.setFont(new Font("Courier New", Font.PLAIN, 20));
        add(usernameField);

        JLabel usernameErrorLabel = new JLabel("");
        usernameErrorLabel.setBounds((screenSize.width - 600) / 2 + 50, 340-30, 500, 30);
        usernameErrorLabel.setForeground(Color.RED);
        usernameErrorLabel.setFont(new Font("Courier New", Font.PLAIN, 14));
        usernameErrorLabel.setVisible(false);
        add(usernameErrorLabel);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds((screenSize.width - 600) / 2 + 50, 350 - 10, 200, 30);
        passwordLabel.setFont(new Font("Courier New", Font.PLAIN, 20));
        passwordLabel.setForeground(new Color(0x00FF00));
        add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds((screenSize.width - 600) / 2 + 50, 390 - 10, 500, 40);
        passwordField.setBackground(new Color(0x333333));
        passwordField.setForeground(new Color(0x00FF00));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(0x00FF00), 2));
        passwordField.setCaretColor(Color.GREEN);
        passwordField.setFont(new Font("Courier New", Font.PLAIN, 20));
        add(passwordField);

        JLabel passwordErrorLabel = new JLabel("");
        passwordErrorLabel.setBounds((screenSize.width - 600) / 2 + 50,440-20, 500, 30);
        passwordErrorLabel.setForeground(Color.RED);
        passwordErrorLabel.setFont(new Font("Courier New", Font.PLAIN, 14));
        passwordErrorLabel.setVisible(false);
        add(passwordErrorLabel);

        JButton loginButton = new JButton("Enter");
        loginButton.setBounds((screenSize.width - 600) / 2 + 50, 470 - 10, 200, 40);
        loginButton.setBackground(new Color(0x00FF00));
        loginButton.setForeground(new Color(0x1B1B1B));
        loginButton.setFont(new Font("Courier New", Font.PLAIN, 20));
        loginButton.setBorder(new LineBorder(new Color(0x00FF00), 2));
        loginButton.setFocusPainted(false);
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                boolean valid = true;

                if (username.isEmpty()) {
                    usernameErrorLabel.setText("Username cannot be empty.");
                    usernameErrorLabel.setVisible(true);
                    valid = false;
                } else {
                    usernameErrorLabel.setVisible(false);
                }

                if (password.isEmpty()) {
                    passwordErrorLabel.setText("Password cannot be empty.");
                    passwordErrorLabel.setVisible(true);
                    valid = false;
                } else {
                    passwordErrorLabel.setVisible(false);
                }

                if (valid) {
                    if(databaseManager.checkCorrectLogIn(usernameField.getText(), passwordField.getText())){
                        jFrame.setContentPane(new HomePage(jFrame, usernameField.getText()));
                        jFrame.revalidate();
                        jFrame.repaint();
                    } else{
                        passwordErrorLabel.setText("Username or Password Incorrect");
                        passwordErrorLabel.setVisible(true);
                    }
                }
            }
        });

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setBounds((screenSize.width - 600) / 2 + 220 + 50, 470 - 10, 280, 40);
        createAccountButton.setBackground(new Color(0xFF0000));
        createAccountButton.setForeground(new Color(0x1B1B1B));
        createAccountButton.setFont(new Font("Courier New", Font.BOLD, 20));
        createAccountButton.setBorder(new LineBorder(new Color(0xFF0000), 2));
        createAccountButton.setFocusPainted(false);
        add(createAccountButton);

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setContentPane(new CreateAccount(jFrame));
                jFrame.revalidate();
                jFrame.repaint();
            }
        });

        JLabel guestLabel = new JLabel("Continue as Guest");
        guestLabel.setBounds((screenSize.width - 600) / 2 + 50, 520 - 10, 500, 30);
        guestLabel.setForeground(new Color(0x00FF00));
        guestLabel.setFont(new Font("Courier New", Font.PLAIN, 18));
        guestLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        guestLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String randomUsername = "guest" + String.format("%06d", (int)(Math.random() * 1000000));
                databaseManager.addToPlayers(randomUsername, "");
                jFrame.setContentPane(new HomePage(jFrame, randomUsername));
                jFrame.revalidate();
                jFrame.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                guestLabel.setForeground(new Color(0x00CC00));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                guestLabel.setForeground(new Color(0x00FF00));
            }
        });
        add(guestLabel);

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1040);
        frame.setLocationRelativeTo(null);
        frame.add(new LoginPage(frame));
        frame.setVisible(true);
    }
}
