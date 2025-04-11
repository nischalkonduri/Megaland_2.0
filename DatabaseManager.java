import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public void addToHostTable(String username, int selectedNumberOfPlayer, String gameCode) {
        String query = "INSERT INTO hostinfo (HostUsername, MaxClients, GameCode, Client1, Client2, Client3, Client4, GameStarted) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setInt(2, selectedNumberOfPlayer);
            stmt.setString(3, gameCode);
            stmt.setString(4, null);
            stmt.setString(5, null);
            stmt.setString(6, null);
            stmt.setString(7, null);
            stmt.setBoolean(8, false);
            stmt.executeUpdate();
            System.out.println("Game host information added to hostinfo table.");
        } catch (SQLException e) {
            System.err.println("Failed to add game host information to hostinfo table.");
            e.printStackTrace();
        }
    }

    public void removeHost(String username) {
        String query = "DELETE FROM hostinfo WHERE HostUsername = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Host with username '" + username + "' removed from hostinfo table.");
            } else {
                System.out.println("No host found with username '" + username + "' in hostinfo table.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to remove host with username '" + username + "' from hostinfo table.");
            e.printStackTrace();
        }
    }

    public boolean connectToHostGame(String username, String gameCode) {
        String selectQuery = "SELECT MaxClients, Client1, Client2, Client3, Client4, GameStarted FROM hostinfo WHERE GameCode = ?";
        String updateQuery = "";

        try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery)) {
            selectStmt.setString(1, gameCode);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int maxClients = rs.getInt("MaxClients");
                boolean gameStarted = rs.getBoolean("GameStarted");
                String client1 = rs.getString("Client1");
                String client2 = rs.getString("Client2");
                String client3 = rs.getString("Client3");
                String client4 = rs.getString("Client4");

                if (!gameStarted) {
                    if (maxClients > 1) {
                        if (client1 == null) {
                            updateQuery = "UPDATE hostinfo SET client1 = ? WHERE GameCode = ?";
                        } else if (maxClients > 2 && client2 == null) {
                            updateQuery = "UPDATE hostinfo SET client2 = ? WHERE GameCode = ?";
                        } else if (maxClients > 3 && client3 == null) {
                            updateQuery = "UPDATE hostinfo SET client3 = ? WHERE GameCode = ?";
                        } else if (maxClients > 4 && client4 == null) {
                            updateQuery = "UPDATE hostinfo SET client4 = ? WHERE GameCode = ?";
                        }

                        if (!updateQuery.isEmpty()) {
                            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                                updateStmt.setString(1, username);
                                updateStmt.setString(2, gameCode);
                                int rowsUpdated = updateStmt.executeUpdate();
                                return rowsUpdated > 0;
                            }
                        } else {
                            System.out.println("Game with code '" + gameCode + "' is full.");
                            return false;
                        }
                    } else {
                        System.out.println("Game with code '" + gameCode + "' has invalid max clients (<= 0).");
                        return false;
                    }
                } else {
                    System.out.println("Game with code '" + gameCode + "' has already started.");
                    return false;
                }
            } else {
                System.out.println("No active game found with code '" + gameCode + "'.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error connecting user '" + username + "' to game '" + gameCode + "'.");
            e.printStackTrace();
            return false;
        }
    }

    public void addClient(String username, String gameCode) {
        String query = "INSERT INTO clientinfo (ClientUsername, GameCode) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, gameCode);
            stmt.executeUpdate();
            System.out.println("Client '" + username + "' added to clientinfo for game '" + gameCode + "'.");
        } catch (SQLException e) {
            System.err.println("Failed to add client '" + username + "' to clientinfo for game '" + gameCode + "'.");
            e.printStackTrace();
        }
    }

    public void removeClientFromGame(String username) {
        String deleteClientInfoQuery = "DELETE FROM clientinfo WHERE ClientUsername = ?";
        try (PreparedStatement deleteClientInfoStmt = connection.prepareStatement(deleteClientInfoQuery)) {
            deleteClientInfoStmt.setString(1, username);
            int rowsAffectedClientInfo = deleteClientInfoStmt.executeUpdate();
            if (rowsAffectedClientInfo > 0) {
                System.out.println("Client '" + username + "' removed from clientinfo.");
            } else {
                System.out.println("Client '" + username + "' not found in clientinfo.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to remove client '" + username + "' from clientinfo.");
            e.printStackTrace();
            return;
        }
        String selectHostInfoQuery = "SELECT GameCode, Client1, Client2, Client3, Client4 FROM hostinfo WHERE Client1 = ? OR Client2 = ? OR Client3 = ? OR Client4 = ?";
        try (PreparedStatement selectHostInfoStmt = connection.prepareStatement(selectHostInfoQuery)) {
            selectHostInfoStmt.setString(1, username);
            selectHostInfoStmt.setString(2, username);
            selectHostInfoStmt.setString(3, username);
            selectHostInfoStmt.setString(4, username);

            ResultSet rs = selectHostInfoStmt.executeQuery();

            while (rs.next()) {
                String gameCode = rs.getString("GameCode");
                String c1 = rs.getString("Client1");
                String c2 = rs.getString("Client2");
                String c3 = rs.getString("Client3");
                String c4 = rs.getString("Client4");

                System.out.println("Updating game '" + gameCode + "' - Clients before: [" + c1 + ", " + c2 + ", " + c3 + ", " + c4 + "]");

                List<String> updatedClients = new ArrayList<>();
                if (c1 != null && !c1.equals(username)) updatedClients.add(c1);
                if (c2 != null && !c2.equals(username)) updatedClients.add(c2);
                if (c3 != null && !c3.equals(username)) updatedClients.add(c3);
                if (c4 != null && !c4.equals(username)) updatedClients.add(c4);

                while (updatedClients.size() < 4) updatedClients.add(null);

                String updateHostInfoQuery = "UPDATE hostinfo SET Client1 = ?, Client2 = ?, Client3 = ?, Client4 = ? WHERE GameCode = ?";
                try (PreparedStatement updateHostInfoStmt = connection.prepareStatement(updateHostInfoQuery)) {
                    updateHostInfoStmt.setString(1, updatedClients.get(0));
                    updateHostInfoStmt.setString(2, updatedClients.get(1));
                    updateHostInfoStmt.setString(3, updatedClients.get(2));
                    updateHostInfoStmt.setString(4, updatedClients.get(3));
                    updateHostInfoStmt.setString(5, gameCode);

                    int rowsAffectedHostInfo = updateHostInfoStmt.executeUpdate();
                    if (rowsAffectedHostInfo > 0) {
                        System.out.println("Client '" + username + "' removed from game '" + gameCode + "' in hostinfo. Clients now: " + updatedClients);
                    } else {
                        System.out.println("No rows updated for game '" + gameCode + "'. Possible issue.");
                    }
                } catch (SQLException e) {
                    System.err.println("Failed to update client slots in hostinfo for game '" + gameCode + "'.");
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            System.err.println("Error finding or processing hostinfo records for client '" + username + "'.");
            e.printStackTrace();
        }
    }


    public ArrayList<String> getPlayersInGame(String gameCode) {
        ArrayList<String> players = new ArrayList<>();
        String query = "SELECT HostUsername, client1, client2, client3, client4 FROM hostinfo WHERE GameCode = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, gameCode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hostUsername = rs.getString("HostUsername");
                String client1 = rs.getString("Client1");
                String client2 = rs.getString("Client2");
                String client3 = rs.getString("Client3");
                String client4 = rs.getString("Client4");

                if (hostUsername != null && !hostUsername.isEmpty()) {
                    players.add(hostUsername);
                }
                if (client1 != null && !client1.isEmpty()) {
                    players.add(client1);
                }
                if (client2 != null && !client2.isEmpty()) {
                    players.add(client2);
                }
                if (client3 != null && !client3.isEmpty()) {
                    players.add(client3);
                }
                if (client4 != null && !client4.isEmpty()) {
                    players.add(client4);
                }
            } else {
                System.out.println("No game found with code: " + gameCode);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving players for game code: " + gameCode);
            e.printStackTrace();
        }
        return players;
    }

    public boolean checkIfHostActive(String gameCode) {
        String query = "SELECT GameCode FROM hostinfo WHERE GameCode = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, gameCode);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking if host is active.");
            e.printStackTrace();
        }
        return false;
    }

    public void setGameStartedStatus(String gameCode) {
        String query = "UPDATE hostinfo SET GameStarted = 1 WHERE GameCode = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, gameCode);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error setting GameStarted status.");
            e.printStackTrace();
        }
    }

    public boolean checkGameStarted(String gameCode) {
        String query = "SELECT GameStarted FROM hostinfo WHERE GameCode = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, gameCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("GameStarted") == 1;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if game has started.");
            e.printStackTrace();
        }
        return false;
    }

}
