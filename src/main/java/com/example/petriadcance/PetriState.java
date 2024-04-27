package com.example.petriadcance;

import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class PetriState {
    private String name;
    private List<PetriTransition> outgoingTransitions;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
