import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateAccount extends JPanel {
    public JFrame jFrame;
    public DatabaseManager databaseManager = new DatabaseManager();
    public CreateAccount(JFrame frame) {
        this.jFrame = frame;
        setLayout(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setBackground(new Color(0x1B1B1B));

        JLabel createAccountLabel = new JLabel("CREATE ACCOUNT", JLabel.CENTER);
        createAccountLabel.setBounds((screenSize.width - 600) / 2, 100-15, 600, 100);
        createAccountLabel.setFont(new Font("Georgia", Font.BOLD, 50));
        createAccountLabel.setForeground(new Color(0x00FF00));
        add(createAccountLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds((screenSize.width - 600) / 2 + 50, 220-30, 200, 30);
        usernameLabel.setFont(new Font("Courier New", Font.PLAIN, 20));
        usernameLabel.setForeground(new Color(0x00FF00));
        add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds((screenSize.width - 600) / 2 + 50, 260-30, 500, 40);
        usernameField.setBackground(new Color(0x333333));
        usernameField.setForeground(new Color(0x00FF00));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(0x00FF00), 2));
        usernameField.setCaretColor(Color.GREEN);
        usernameField.setFont(new Font("Courier New", Font.PLAIN, 20));
        add(usernameField);

        JLabel usernameError = new JLabel("Username cannot be empty.");
        usernameError.setBounds((screenSize.width - 600) / 2 + 50, 305-30, 400, 20);
        usernameError.setForeground(Color.RED);
        usernameError.setFont(new Font("Courier New", Font.PLAIN, 14));
        usernameError.setVisible(false);
        add(usernameError);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds((screenSize.width - 600) / 2 + 50, 330-30, 200, 30);
        passwordLabel.setFont(new Font("Courier New", Font.PLAIN, 20));
        passwordLabel.setForeground(new Color(0x00FF00));
        add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds((screenSize.width - 600) / 2 + 50, 370-30, 500, 40);
        passwordField.setBackground(new Color(0x333333));
        passwordField.setForeground(new Color(0x00FF00));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(0x00FF00), 2));
        passwordField.setCaretColor(Color.GREEN);
        passwordField.setFont(new Font("Courier New", Font.PLAIN, 20));
        add(passwordField);

        JLabel passwordError = new JLabel("Password cannot be empty.");
        passwordError.setBounds((screenSize.width - 600) / 2 + 50, 415-30, 400, 20);
        passwordError.setForeground(Color.RED);
        passwordError.setFont(new Font("Courier New", Font.PLAIN, 14));
        passwordError.setVisible(false);
        add(passwordError);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds((screenSize.width - 600) / 2 + 50, 440-30, 300, 30);
        confirmPasswordLabel.setFont(new Font("Courier New", Font.PLAIN, 20));
        confirmPasswordLabel.setForeground(new Color(0x00FF00));
        add(confirmPasswordLabel);

        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds((screenSize.width - 600) / 2 + 50, 480-30, 500, 40);
        confirmPasswordField.setBackground(new Color(0x333333));
        confirmPasswordField.setForeground(new Color(0x00FF00));
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(new Color(0x00FF00), 2));
        confirmPasswordField.setCaretColor(Color.GREEN);
        confirmPasswordField.setFont(new Font("Courier New", Font.PLAIN, 20));
        add(confirmPasswordField);

        JLabel confirmPasswordError = new JLabel("Passwords do not match.");
        confirmPasswordError.setBounds((screenSize.width - 600) / 2 + 50, 525-30, 400, 20);
        confirmPasswordError.setForeground(Color.RED);
        confirmPasswordError.setFont(new Font("Courier New", Font.PLAIN, 14));
        confirmPasswordError.setVisible(false);
        add(confirmPasswordError);

        JButton signupButton = new JButton("Sign Up");
        signupButton.setBounds((screenSize.width - 600) / 2 + 50, 570-30, 200, 40);
        signupButton.setBackground(new Color(0x00FF00));
        signupButton.setForeground(new Color(0x1B1B1B));
        signupButton.setFont(new Font("Courier New", Font.PLAIN, 20));
        signupButton.setBorder(new LineBorder(new Color(0x00FF00), 2));
        signupButton.setFocusPainted(false);
        add(signupButton);

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean valid = true;

                usernameError.setVisible(false);
                passwordError.setVisible(false);
                confirmPasswordError.setVisible(false);

                if (usernameField.getText().trim().isEmpty()) {
                    usernameError.setVisible(true);
                    valid = false;
                }

                if (String.valueOf(passwordField.getPassword()).trim().isEmpty()) {
                    passwordError.setVisible(true);
                    valid = false;
                }

                if (String.valueOf(confirmPasswordField.getPassword()).trim().isEmpty()) {
                    confirmPasswordError.setText("Please confirm your password.");
                    confirmPasswordError.setVisible(true);
                    valid = false;
                } else if (!String.valueOf(passwordField.getPassword()).equals(String.valueOf(confirmPasswordField.getPassword()))) {
                    confirmPasswordError.setText("Passwords do not match.");
                    confirmPasswordError.setVisible(true);
                    valid = false;
                }

                if (valid) {
                    if(databaseManager.checkExistingUsername(usernameField.getText())){
                        valid = false;
                        usernameError.setText("Username Already Exists.");
                        usernameError.setVisible(true);
                    }else{
                        databaseManager.addToPlayers(usernameField.getText(), passwordField.getText());
                        jFrame.setContentPane(new HomePage(jFrame, usernameField.getText()));
                        jFrame.revalidate();
                        jFrame.repaint();
                    }
                }
            }
        });

        JButton loginRedirectButton = new JButton("Back to Login");
        loginRedirectButton.setBounds((screenSize.width - 600) / 2 + 320, 570-30, 280, 40);
        loginRedirectButton.setBackground(new Color(0xFF0000));
        loginRedirectButton.setForeground(new Color(0x1B1B1B));
        loginRedirectButton.setFont(new Font("Courier New", Font.BOLD, 20));
        loginRedirectButton.setBorder(new LineBorder(new Color(0xFF0000), 2));
        loginRedirectButton.setFocusPainted(false);
        add(loginRedirectButton);

        loginRedirectButton.addActionListener(e -> {
            jFrame.setContentPane(new LoginPage(jFrame));
            jFrame.revalidate();
            jFrame.repaint();
        });
    }
}
