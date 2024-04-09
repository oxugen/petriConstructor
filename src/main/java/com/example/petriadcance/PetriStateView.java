package com.example.petriadcance;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PetriStateView extends Circle {
    private PetriState state;

    public PetriStateView(double centerX, double centerY, double radius, PetriState state) {
        super(centerX, centerY, radius);
        this.setFill(Color.GREEN);
        this.setStyle("-fx-background-color: #FF7043; -fx-border-color: #E64A19; -fx-border-width: 2px; -fx-border-radius: 50%; -fx-effect: dropshadow(gaussian, rgba(92,189,31,0.3), 10, 0, 0, 2);");
        this.state = state;
    }

    public PetriState getState() {
        return state;
    }
}
