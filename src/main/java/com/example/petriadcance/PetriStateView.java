package com.example.petriadcance;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PetriStateView extends Circle {
    private PetriState state;
    private Text stateName;
    private Text token;

    public PetriStateView(double centerX, double centerY, double radius, PetriState state) {
        super(centerX, centerY, radius);
        this.setFill(Color.GREEN);
        this.setStyle("-fx-background-color: #FF7043; -fx-border-color: #E64A19; -fx-border-width: 2px; -fx-border-radius: 50%; -fx-effect: dropshadow(gaussian, rgba(92,189,31,0.3), 10, 0, 0, 2);");
        this.state = state;
        this.stateName = new Text(centerX - radius / 2, centerY - radius - 5, state.getName()); // Установка текста над кругом
        this.stateName.setFont(Font.font("Arial", FontWeight.BOLD, 12)); // Установка шрифта и размера текста
        this.stateName.setFill(Color.BLACK); // Установка цвета текста
        this.stateName.setMouseTransparent(true);
        this.state.setToken("0");
        this.token = new Text( "0");
        this.token.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        double textWidth = token.getBoundsInLocal().getWidth();
        double textHeight = token.getBoundsInLocal().getHeight();
        this.token.setX(centerX - textWidth / 2);
        this.token.setY(centerY +  textHeight / 4);
        this.token.setFill(Color.BLACK);
        this.token.setMouseTransparent(true);
    }

    public PetriState getState() {
        return state;
    }

    public Text getStateName(){
        return stateName;
    }

    public Text getToken(){
        return token;
    }

    public void setToken(String token){
        this.token.setText(token);
        if(!this.token.getText().equals("0")){
            this.setFill(Color.RED);
        }
        else{
            this.setFill(Color.GREEN);
        }
    }
    public void setStateName(String text){
        this.stateName.setText(text);
    }
}
