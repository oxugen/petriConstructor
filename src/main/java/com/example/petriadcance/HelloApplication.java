package com.example.petriadcance;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private Node selectedObject;
    @Override
    public void start(Stage primaryStage) throws IOException {


        primaryStage.setTitle("Petri Net Constructor");

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();

        PetriNet net = new PetriNet();

        ToggleButton addStateButton = new ToggleButton("Add State");
        ToggleButton addTransitionButton = new ToggleButton("Add Transition");

        ToggleButton deleteButton = new ToggleButton("Delete");
        deleteButton.setOnAction(event -> {
            // Установка выбранного объекта в null для сброса предыдущего выбора
            selectedObject = null;
            // Включение режима удаления
            deleteButton.setSelected(true);
        });

// Добавляем кнопку удаления на экран
// Вам нужно определить расположение кнопки на экране
        root.setRight(deleteButton);

        Label titleLabel = new Label("Визуализация Сети Петри");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setPadding(new Insets(10));

        root.setStyle("-fx-background-color: #f0f0f0;"); // Установка светлого фона
        addStateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Зеленая кнопка
        addTransitionButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;"); // Синяя кнопка
        addStateButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        addTransitionButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        addStateButton.setOnMousePressed(e -> addStateButton.setScaleX(0.9));
        addStateButton.setOnMouseReleased(e -> addStateButton.setScaleX(1.0));
        VBox buttonsBox = new VBox(10, addStateButton, addTransitionButton);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setPadding(new Insets(10));
        buttonsBox.setSpacing(20); // Установка пространства между кнопками
        root.setTop(buttonsBox);

        VBox menuBox = new VBox(10);
        menuBox.setPadding(new Insets(10));
        menuBox.setAlignment(Pos.TOP_LEFT);

// Добавление кнопок в меню
        menuBox.getChildren().addAll(addStateButton, addTransitionButton);

// Размещение надписи и меню в главном контейнере
        root.setTop(titleLabel);
        root.setLeft(menuBox);

        class StateHolder {
            PetriStateView state;
        }
        StateHolder stateHolder = new StateHolder();

        addStateButton.setOnAction(e -> {
            addTransitionButton.setSelected(false); // Отключаем другую кнопку при включении текущей
        });

        addTransitionButton.setOnAction(e -> {
            if (addTransitionButton.isSelected()) {
                // Если кнопка выбрана, сохраняем первое состояние и ждем следующего клика
                stateHolder.state = null; // Сбрасываем состояние
                addStateButton.setSelected(false); // отключаем кнопку добавления состояния
            } else {
                // Если кнопка не выбрана, сбрасываем переменную состояния
                stateHolder.state = null; // Сбрасываем состояние
            }
        });

//        buttonsBox.setPadding(new Insets(10));
//        root.setTop(buttonsBox);

        root.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();

            if (deleteButton.isSelected()) {
                // Поиск объекта на экране по координатам щелчка
                for (Node node : root.getChildren()) {
                    if (node.contains(x, y)) {
                        // Установка выбранного объекта
                        selectedObject = node;
                        break;
                    }
                }
                // Если объект найден и это состояние или переход, удаляем его
                if (selectedObject != null && (selectedObject instanceof PetriStateView || selectedObject instanceof PetriTransitionView)) {
                    root.getChildren().remove(selectedObject);
                    // Удаление состояния или перехода из сети Петри
                    if (selectedObject instanceof PetriStateView) {
                        net.removeState(((PetriStateView) selectedObject).getState());
                    } else if (selectedObject instanceof PetriTransitionView) {
                        net.removeTransition(((PetriTransitionView) selectedObject).getTransition());
                    }
                }
                // Сброс выбранного объекта
                selectedObject = null;
                // Выключение режима удаления
                deleteButton.setSelected(false);
            }

            if (addStateButton.isSelected()) {
                PetriState newState = new PetriState("New State");
                PetriStateView newStateView = new PetriStateView(x, y, 20, newState);
                root.getChildren().add(newStateView);
                net.addState(newState);
            } else if (addTransitionButton.isSelected()) {
                if (stateHolder.state == null) {
                    // Если первое состояние не установлено, сохраняем его
                    for (Node node : root.getChildren()) {
                        if (node instanceof PetriStateView && node.contains(x, y)) {
                            stateHolder.state = (PetriStateView) node;
                            break;
                        }
                    }
                } else {
                    // Создаем переход между первым состоянием и новым состоянием
                    PetriStateView secondState = null;
                    for (Node node : root.getChildren()) {
                        if (node instanceof PetriStateView && node.contains(x, y)) {
                            secondState = (PetriStateView) node;
                            break;
                        }
                    }

                    if (secondState != null && !net.hasTransition(stateHolder.state.getState(), secondState.getState())) {
                        double startX = stateHolder.state.getCenterX();
                        double startY = stateHolder.state.getCenterY();
                        double endX = secondState.getCenterX();
                        double endY = secondState.getCenterY();
                        double transitionCenterX = (startX + endX) / 2;
                        double transitionCenterY = (startY + endY) / 2;

                        PetriTransition newTransition = new PetriTransition("New Transition");
                        PetriTransitionView transitionView = new PetriTransitionView(transitionCenterX, transitionCenterY, 20, 20, newTransition);
                        root.getChildren().add(transitionView);
                        net.addTransition(newTransition, stateHolder.state.getState(), secondState.getState());

                        // Создаем линию между состояниями
                        Line line = new Line(startX, startY, endX, endY);
                        line.setStyle("-fx-stroke: #000000; -fx-stroke-width: 2px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 5, 0, 0, 2);");
                        root.getChildren().add(line);
                    }

                    // Сбрасываем состояние и отключаем кнопку добавления перехода
                    stateHolder.state = null;
                    addTransitionButton.setSelected(false);

                }

            }
        });

    }

    public static void main(String[] args) {
        launch();
    }
}