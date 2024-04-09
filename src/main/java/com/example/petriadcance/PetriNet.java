package com.example.petriadcance;

import java.util.ArrayList;
import java.util.List;

public class PetriNet {
    private List<PetriState> states;
    private List<PetriTransition> transitions;

    public PetriNet() {
        states = new ArrayList<>();
        transitions = new ArrayList<>();
    }

    public void addState(PetriState state) {
        states.add(state);
    }

    public void removeTransition(PetriTransition transition) {
        transitions.remove(transition);
    }

    public void removeState(PetriState state) {
        states.remove(state);
    }

    public void addTransition(PetriTransition transition, PetriState sourceState, PetriState targetState) {
        // Устанавливаем исходное и целевое состояния для перехода
        transition.setSourceState(sourceState);
        transition.setTargetState(targetState);
        // Добавляем переход в список переходов сети Петри
        transitions.add(transition);
    }

    public boolean hasTransition(PetriState state1, PetriState state2) {
        for (PetriTransition transition : transitions) {
            if (transition.getSourceState().equals(state1) && transition.getTargetState().equals(state2)) {
                return true;
            }
        }
        return false;
    }

}
