package com.example.petriadcance;

import java.util.ArrayList;
import java.util.List;

public class PetriTransition {
    private String name;
    private List<PetriStateView> sourceState;
    private List<PetriStateView> targetState;

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

}
