package com.example.petriadcance;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class PetriLineView extends Line {
    private PetriLine line;
    private PetriTransition source;
    private PetriState target;

    public PetriLineView(double startX, double startY, double endX, double endY, PetriLine line, PetriTransition source, PetriState target) {
        super(startX, startY, endX, endY);
        this.source = source;
        this.target = target;
        this.setStyle("-fx-stroke: #000000; -fx-stroke-width: 2px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 5, 0, 0, 2);");
        this.line = line;
    }

    public PetriTransition getSource() {
        return source;
    }

    public PetriState getTarget() {
        return target;
    }

    public PetriLine getLine() {
        return line;
    }
}
