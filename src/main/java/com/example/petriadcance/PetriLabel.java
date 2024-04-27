package com.example.petriadcance;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class PetriLabel extends Label {
    public PetriLabel(String text) {
        super(text);
        setTextFill(Color.BLACK); // Установка цвета текста
    }
}
