package com.example.petriadcance;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseUtil {
    private static String url;
    private static String username;
    private static String password;

    public static List<PetriNet> getPetriNets() {
        List<PetriNet> petriNets = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String query = "SELECT petri_net_id, name FROM PetriNet";
            try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int id = rs.getInt("petri_net_id");
                    String name = rs.getString("name");
                    PetriNet petriNet = new PetriNet(id, name);
                    petriNets.add(petriNet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return petriNets;
    }

    static {
        try {
            Properties properties = new Properties();
            properties.load(DatabaseUtil.class.getResourceAsStream("/com/example/petriadcance/db.properties"));
            url = properties.getProperty("db.url");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
