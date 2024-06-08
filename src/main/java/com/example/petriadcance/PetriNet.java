package com.example.petriadcance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetriNet {
    private List<PetriState> states;
    private List<PetriTransition> transitions;
    private int petriNetId;
    private String name;
    private String description;

    private List<PetriLine> lines;
    public PetriNet() {
        states = new ArrayList<>();
        transitions = new ArrayList<>();
        lines = new ArrayList<>();
    }

    public int getPetriNetId() {
        return petriNetId;
    }

    public PetriNet(int petriNetId, String name) {
        states = new ArrayList<>();
        transitions = new ArrayList<>();
        lines = new ArrayList<>();
        this.petriNetId = petriNetId;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void addState(PetriState state) {
        states.add(state);
    }

    public void addLine(PetriLine line) {
        lines.add(line);
    }

    public void removeLine(PetriLine line) {
        lines.remove(line);
    }

    public void removeTransition(PetriTransition transition) {

        transitions.remove(transition);
        // Удаляем линии, связанные с этим переходом
        List<PetriLineView> linesToRemove = new ArrayList<>();
        // Удаление перехода из всех связанных линий

    }

    public void removeState(PetriState state) {
        states.remove(state);
    }

    public void addTransition(PetriTransition transition) {
        // Добавляем переход в список переходов сети Петри
        transitions.add(transition);
    }

    public void saveToDatabase(String netName) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String insertPetriNet = "INSERT INTO PetriNet (name, description) VALUES (?, ?)";
            try (PreparedStatement petriNetStmt = connection.prepareStatement(insertPetriNet, Statement.RETURN_GENERATED_KEYS)) {
                petriNetStmt.setString(1, netName);
                petriNetStmt.setString(2, description);
                petriNetStmt.executeUpdate();
                try (ResultSet generatedKeys = petriNetStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        petriNetId = generatedKeys.getInt(1);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            for (PetriState state : states) {
                state.saveToDatabase(petriNetId);
            }

            for (PetriTransition transition : transitions) {
                transition.saveToDatabase(petriNetId);
            }

            for (PetriLine line : lines) {
                line.saveToDatabase(petriNetId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//    public boolean hasTransition(PetriState state1, PetriTransition transition) {
//        for (PetriTransition transition : transitions) {
//            if (transition.getSourceState().equals(state1) && transition.getTargetState().equals(state2)) {
//                return true;
//            }
//        }
//        return false;
//    }

}
