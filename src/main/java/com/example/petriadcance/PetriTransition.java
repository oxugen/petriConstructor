package com.example.petriadcance;

public class PetriTransition {
    private String name;
    private PetriState sourceState;
    private PetriState targetState; // Добавляем поле для хранения целевого состояния

    // Конструктор и другие поля и методы

    // Метод для установки целевого состояния
    public void setTargetState(PetriState targetState) {
        this.targetState = targetState;
    }

    public PetriState getTargetState() {
        return targetState;
    }
    public PetriTransition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSourceState(PetriState sourceState) {
        this.sourceState = sourceState;
    }

    // Метод для получения исходного состояния
    public PetriState getSourceState() {
        return sourceState;
    }
}
