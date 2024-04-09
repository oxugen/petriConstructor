package com.example.petriadcance;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PetriTransitionView extends Rectangle {
    private PetriTransition transition;

    public PetriTransitionView(double x, double y, double width, double height, PetriTransition transition) {
        super(x, y, width, height);
        this.setFill(Color.RED);
        //this.setStyle("-fx-stroke: #FFFF00; -fx-stroke-width: 2px; -fx-effect: dropshadow(gaussian, rgba(32,85,196,0.41), 10, 0, 0, 2);");
        this.transition = transition;
    }

    public PetriTransition getTransition() {
        return transition;
    }
}
