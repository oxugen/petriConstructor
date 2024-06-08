package com.example.petriadcance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetriTransition {
    private int transitionId;
    private String name;
    private double x;
    private double y;

    private List<PetriStateView> sourceState;
    private List<PetriStateView> targetState;

    public PetriTransition(int transitionId, String name, double x, double y, List<PetriStateView> sourceState, List<PetriStateView> targetState) {
        this.transitionId = transitionId;
        this.name = name;
        this.x = x;
        this.y = y;
        this.sourceState = sourceState;
        this.targetState = targetState;
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
    public void setTargetState(PetriStateView targetState) {
        this.targetState.add(targetState);
    }
    public List<PetriStateView> getTargetState() {
        return targetState;
    }

    public void setSourceState(PetriStateView sourceState) {
        this.sourceState.add(sourceState);
    }
    public List<PetriStateView> getSourceState() {
        return sourceState;
    }

    public PetriTransition(String name) {
        this.name = name;
        this.sourceState = new ArrayList<>();
        this.targetState = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTransitionId() {
        return transitionId;
    }

    public void saveToDatabase(int petriNetId) {
        int sourceStateId = sourceState.get(0).getState().getStateId();
        int targetStateId = targetState.get(0).getState().getStateId();
        try (Connection connection = DatabaseUtil.getConnection()) {
            String insertTransition = "INSERT INTO PetriTransition (name, x, y, sourceState, targetState, petri_net_id) VALUES (?, ?, ?, ?, ? , ?)";
            try (PreparedStatement transitionStmt = connection.prepareStatement(insertTransition, Statement.RETURN_GENERATED_KEYS)) {
                transitionStmt.setString(1, name);
                transitionStmt.setDouble(2, x);
                transitionStmt.setDouble(3, y);
                transitionStmt.setInt(4, sourceStateId);
                transitionStmt.setInt(5, targetStateId);
                transitionStmt.setInt(6, petriNetId);
                transitionStmt.executeUpdate();
                try (ResultSet generatedKeys = transitionStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        transitionId = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
