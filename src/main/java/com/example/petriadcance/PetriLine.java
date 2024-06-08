package com.example.petriadcance;

import java.sql.*;

public class PetriLine {
    private int lineId;
    private int sourceId;
    private int targetId;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private String type;
    private String name;

    public PetriLine(int lineId, double startX, double startY, double endX, double endY, String name) {
        this.lineId = lineId;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.name = name;
    }

    public PetriLine(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void saveToDatabase(int petriNetId) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String insertLine = "INSERT INTO PetriLine (name, startX, startY, endX, endY, petri_net_id) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement lineStmt = connection.prepareStatement(insertLine, Statement.RETURN_GENERATED_KEYS)) {
                lineStmt.setString(1, name);
                lineStmt.setDouble(2, startX);
                lineStmt.setDouble(3, startY);
                lineStmt.setDouble(4, endX);
                lineStmt.setDouble(5, endY);
                lineStmt.setInt(6, petriNetId);
                lineStmt.executeUpdate();
                try (ResultSet generatedKeys = lineStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        lineId = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
