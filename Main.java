import javax.swing.*;

public class Main extends JFrame{
    public Main(){
        super("MegaLand");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);

        System.out.println("Hello World!");

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}