package com.example.petriadcance;

import java.util.ArrayList;
import java.util.List;

public class PetriNet {
    private List<PetriState> states;
    private List<PetriTransition> transitions;

    private List<PetriLine> lines;
    public PetriNet() {
        states = new ArrayList<>();
        transitions = new ArrayList<>();
        lines = new ArrayList<>();
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

//    public boolean hasTransition(PetriState state1, PetriTransition transition) {
//        for (PetriTransition transition : transitions) {
//            if (transition.getSourceState().equals(state1) && transition.getTargetState().equals(state2)) {
//                return true;
//            }
//        }
//        return false;
//    }

}
