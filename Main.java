import javax.swing.*;
import java.awt.*;

public class Main extends JFrame{
    DatabaseManager databaseManager = new DatabaseManager();
    public Main(){
        super("MegaLand");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        //System.out.println("Hello World!");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setLocationRelativeTo(null);

        LoginPage loginPage = new LoginPage(this);
        loginPage.setSize(screenSize.width, screenSize.height);
        add(loginPage);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}