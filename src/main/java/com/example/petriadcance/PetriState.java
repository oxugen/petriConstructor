package com.example.petriadcance;

import javafx.scene.text.Text;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetriState {
    private int stateId;
    private String name;
    private List<PetriTransition> outgoingTransitions;

    private int outgoingId;
    private double x;
    private double y;

    private String token = "0";

    public void addOutgoingTransition(PetriTransition transition) {
        outgoingTransitions.add(transition);
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }
    public List<PetriTransition> getOutgoingTransitions() {
        return outgoingTransitions;
    }
    public PetriState(String name) {
        this.name = name;
        this.outgoingTransitions = new ArrayList<>();
    }

    public int getStateId() {
        return stateId;
    }

    public String getName() {
        return name;
    }

    public void setOutgoingId(int outgoingId) {
        this.outgoingId = outgoingId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public PetriState(int stateId, String name, double x, double y) {
        this.stateId = stateId;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public void saveToDatabase(int petriNetId) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String insertState = "INSERT INTO PetriState (name, x, y, petri_net_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stateStmt = connection.prepareStatement(insertState, Statement.RETURN_GENERATED_KEYS)) {
                stateStmt.setString(1, name);
                stateStmt.setDouble(2, x);
                stateStmt.setDouble(3, y);
                stateStmt.setInt(4, petriNetId);
                stateStmt.executeUpdate();
                try (ResultSet generatedKeys = stateStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        stateId = generatedKeys.getInt(1);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
