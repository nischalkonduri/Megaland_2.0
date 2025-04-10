import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://192.168.1.14:3306/megaland2.0";
    private static final String USER = "root";
    private static final String PASSWORD = "password";
    private Connection connection;

    public DatabaseManager(){
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
        }
    }

    public void addToPlayers(String username, String password) {
        String query = "INSERT INTO Players (username, password) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            System.out.println("User added to players table.");
        } catch (SQLException e) {
            System.err.println("Failed to add user to players table.");
            e.printStackTrace();
        }
    }
    public boolean checkExistingUsername(String username) {
        String query = "SELECT Username FROM Players WHERE Username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking for existing username.");
            e.printStackTrace();
        }
        return false;
    }
    public boolean checkCorrectLogIn(String username, String password) {
        String query = "SELECT username, password FROM Players WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking login credentials.");
            e.printStackTrace();
        }
        return false;
    }

}
