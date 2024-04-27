package com.example.petriadcance;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HelloApplication extends Application {
    private Node selectedObject;
    @Override
    public void start(Stage primaryStage) throws IOException {


        primaryStage.setTitle("Petri Net Constructor");
        //создание главной сцены
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();

        PetriNet net = new PetriNet();
        //создание кнопок
        ToggleButton addStateButton = new ToggleButton("Add State");
        ToggleButton addLineButton = new ToggleButton("Add Line");
        ToggleButton addTransitionButton = new ToggleButton("Add Transition");

        ToggleButton deleteButton = new ToggleButton("Delete");
        Button startProcessButton = new Button("Start Process");
        deleteButton.setOnAction(event -> {
            selectedObject = null;
        });
        //установка цветов и стиля для кнопок
        Label titleLabel = new Label("Визуализация Сети Петри");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setPadding(new Insets(10));
        addLineButton.setStyle("-fx-background-color: #090809; -fx-text-fill: white;"); //черная кнопка
        addLineButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        addLineButton.setOnMousePressed(e -> deleteButton.setScaleX(0.9));
        addLineButton.setOnMouseReleased(e -> deleteButton.setScaleX(1.0));
        startProcessButton.setStyle("-fx-background-color: rgba(227,0,176,0.3); -fx-text-fill: white;"); // фиолетовая кнопка
        startProcessButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        startProcessButton.setOnMousePressed(e -> startProcessButton.setScaleX(0.9));
        startProcessButton.setOnMouseReleased(e -> startProcessButton.setScaleX(1.0));
        deleteButton.setStyle("-fx-background-color: #af4c5e; -fx-text-fill: white;"); // бежевая кнопка
        deleteButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        deleteButton.setOnMousePressed(e -> deleteButton.setScaleX(0.9));
        deleteButton.setOnMouseReleased(e -> deleteButton.setScaleX(1.0));
        root.setStyle("-fx-background-color: #f0f0f0;"); // Установка светлого фона
        addStateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Зеленая кнопка
        addTransitionButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;"); // Синяя кнопка
        addStateButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        addTransitionButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        addStateButton.setOnMousePressed(e -> addStateButton.setScaleX(0.9));
        addStateButton.setOnMouseReleased(e -> addStateButton.setScaleX(1.0));
        VBox buttonsBox = new VBox(10, addStateButton, addTransitionButton, deleteButton); //добавление кнопок в общий контейнер
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setPadding(new Insets(10));
        buttonsBox.setSpacing(20); // Установка пространства между кнопками
        root.setTop(buttonsBox);

        VBox menuBox = new VBox(10);
        menuBox.setPadding(new Insets(10));
        menuBox.setAlignment(Pos.TOP_LEFT);

// Добавление кнопок в меню
        menuBox.getChildren().addAll(addStateButton, addTransitionButton, addLineButton, deleteButton, startProcessButton);

// Размещение надписи и меню в главном контейнере
        root.setTop(titleLabel);
        root.setLeft(menuBox);

        class StateHolder {
            PetriStateView state;
            PetriTransitionView transition;
        }
        StateHolder stateHolder = new StateHolder();
        addLineButton.setOnAction(e -> {
            addStateButton.setSelected(false);
            addTransitionButton.setSelected(false);
            deleteButton.setSelected(false);// Отключаем другую кнопку при включении текущей
        });
        addStateButton.setOnAction(e -> {
            addTransitionButton.setSelected(false);
            addLineButton.setSelected(false);
            deleteButton.setSelected(false);// Отключаем другую кнопку при включении текущей
        });
        deleteButton.setOnAction(e -> {
            addStateButton.setSelected(false);
            addLineButton.setSelected(false);
            addTransitionButton.setSelected(false);// Отключаем другую кнопку при включении текущей
        });
        addTransitionButton.setOnAction(e -> {
            if (addTransitionButton.isSelected()) {
                // Если кнопка выбрана, сохраняем первое состояние и ждем следующего клика
                stateHolder.state = null; // Сбрасываем состояние
                stateHolder.transition = null;
                addLineButton.setSelected(false);
                addStateButton.setSelected(false); // отключаем кнопку добавления состояния
                deleteButton.setSelected(false); // отключаем кнопку добавления состояния

            } else {
                // Если кнопка не выбрана, сбрасываем переменную состояния
                stateHolder.state = null; // Сбрасываем состояние
                stateHolder.transition = null;
            }
        });
        addLineButton.setOnAction(e -> {
            if (addLineButton.isSelected()) {
                // Если кнопка выбрана, сохраняем первое состояние и ждем следующего клика
                stateHolder.state = null; // Сбрасываем состояние
                stateHolder.transition = null;
                addStateButton.setSelected(false); // отключаем кнопку добавления состояния
                deleteButton.setSelected(false);
                addTransitionButton.setSelected(false);
                 // отключаем кнопку добавления состояния
            } else {
                // Если кнопка не выбрана, сбрасываем переменную состояния
                stateHolder.state = null; // Сбрасываем состояние
                stateHolder.transition = null;
            }
        });
        //данная кнопка отвечает за старт процесса переходов токенов, если токенов не хватает - выводит ошибку
        startProcessButton.setOnAction(event -> {
            System.out.println("hello");
            boolean tokenMoved = false;
            for (Node node : root.getChildren()) {
                if (node instanceof PetriTransitionView startTransition) {
                    List<PetriStateView> sourceOfStartStates = startTransition.getTransition().getSourceState();
                    List<PetriStateView> sourceOfTargetStates = startTransition.getTransition().getTargetState();
                    System.out.println("source" + sourceOfStartStates + "\n" + "target" + sourceOfTargetStates);
                    if (sumTokenValues(sourceOfStartStates) >= sourceOfTargetStates.size()) {
                        tokenMoved = true;
                        Timeline timeline = new Timeline();
                        // Добавляем события в Timeline для обновления токенов
                        for (PetriStateView stateSource : sourceOfStartStates) {
                            int sourceTokens = Integer.parseInt(stateSource.getToken().getText());
                            System.out.println("source Tokens " + sourceTokens);
                            // Обновляем значение токена через 2 секунды
                            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2), e -> {
                                stateSource.setToken(String.valueOf(sourceTokens - 1)); // Обновляем значение токена
                            }));
                        }
                        for (PetriStateView petriTarget : sourceOfTargetStates) {
                            int targetTokens = Integer.parseInt(petriTarget.getToken().getText());
                            System.out.println("target token" + targetTokens);
                            // Обновляем значение токена через 2 секунды
                            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2), e -> {
                                petriTarget.setToken(String.valueOf(targetTokens + 1)); // Обновляем значение токена
                            }));
                        }
                        // Обновляем значения токенов
                        timeline.play();
                    }
                }
            }
            //вывод ошибки
            if(!tokenMoved){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Insufficient tokens");
                alert.setContentText("There are not enough tokens in the source state(s) to transition.");
                alert.showAndWait();
            }
            System.out.println("simulation done!");
        });

        //отслеживаем любой клик по сцене для отслеживания клика по кнопке
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
                // Если объект найден и это состояние, переход или линия, удаляем его
                if (selectedObject != null && (selectedObject instanceof PetriStateView ||
                        selectedObject instanceof PetriTransitionView ||
                        selectedObject instanceof PetriLineView)) {
                    root.getChildren().remove(selectedObject);
                    // Удаление состояния, перехода или линии из сети Петри
                    if (selectedObject instanceof PetriStateView) {
                        PetriStateView stateView = (PetriStateView) selectedObject;
                        // Удаление текста из контейнера
                        root.getChildren().remove(stateView.getStateName());
                        root.getChildren().remove(stateView.getToken());
                        net.removeState(stateView.getState());
                        //net.removeState(((PetriStateView) selectedObject).getState());
                    } else if (selectedObject instanceof PetriTransitionView) {
                        net.removeTransition(((PetriTransitionView) selectedObject).getTransition());
                    } else if (selectedObject instanceof PetriLineView) {
                        root.getChildren().remove((PetriLineView) selectedObject);
                        net.removeLine(((PetriLineView) selectedObject).getLine());
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
                root.getChildren().add(newStateView.getStateName());
                root.getChildren().add(newStateView.getToken());
                net.addState(newState);
                newStateView.setOnMouseClicked(stateMouseEvent -> {
                    // Проверяем, что клик был левой кнопкой мыши
                    if (stateMouseEvent.getButton() == MouseButton.PRIMARY) {
                        // Открываем диалоговое окно для изменения имени состояния
                        TextInputDialog dialog = new TextInputDialog(newState.getName());
                        dialog.setTitle("Edit State Name");
                        dialog.setHeaderText("Enter new state name:");
                        dialog.setContentText("Name:");

                        Optional<String> result = dialog.showAndWait();
                        result.ifPresent(name -> {
                            newState.setName(name);
                            newStateView.getStateName().setText(name);
                        });

                        // Перехватываем событие, чтобы оно не передавалось родительским элементам
                        stateMouseEvent.consume();
                    }
                    else if (stateMouseEvent.getButton() == MouseButton.MIDDLE) {
                        System.out.println("middle");
                        int value = Integer.parseInt(newStateView.getToken().getText());
                        newStateView
                                .getToken().setText(
                                        String.valueOf(value + 1)
                                );
                        stateMouseEvent.consume();
                    }
                });
                //если нажата кнопка по добавлению перехода
            } else if(addTransitionButton.isSelected()){
                PetriTransition newTransition = new PetriTransition("New Transition");
                PetriTransitionView transitionView = new PetriTransitionView(x, y, 20, 30, newTransition);
                root.getChildren().add(transitionView);
                net.addTransition(newTransition);
            }
            // если нажата кнопка по добавлению линии
            else if (addLineButton.isSelected()) {
                boolean isTransitionActive = false;
                if (stateHolder.state == null && stateHolder.transition == null) {
                    // Если первое состояние не установлено, сохраняем его
                    for (Node node : root.getChildren()) {
                        if (node instanceof PetriStateView && node.contains(x, y)) {
                            stateHolder.state = (PetriStateView) node;
                            break;
                        }
                        else if(node instanceof PetriTransitionView && node.contains(x,y)){
                            stateHolder.transition = (PetriTransitionView) node;
                            isTransitionActive = true;
                            break;
                        }
                    }
                } else {
                    // Создаем переход между первым состоянием и новым состоянием
                    for (Node node : root.getChildren()) {
                        if (node instanceof PetriStateView && node.contains(x, y)) {
                            stateHolder.state = (PetriStateView) node;
                            isTransitionActive = true;
                            break;
                        }
                        else if(node instanceof PetriTransitionView && node.contains(x,y)){
                            stateHolder.transition = (PetriTransitionView) node;
                            break;
                        }
                    }

                    if (stateHolder.transition != null && stateHolder.state != null) {
                        double startX = stateHolder.state.getCenterX();
                        double startY = stateHolder.state.getCenterY();
                        double endX = stateHolder.transition.getX() + 5;
                        double endY = stateHolder.transition.getY() + 15;

                        PetriLine petriLine = new PetriLine("New line");
                        PetriLineView lineView = new PetriLineView(startX, startY, endX, endY, petriLine, stateHolder.transition.getTransition(), stateHolder.state.getState());
                        //newTransition.addConnectedLine(lineView);
                        net.addLine(petriLine);
                        if(isTransitionActive){
                            stateHolder.transition.getTransition().setTargetState(stateHolder.state);
                        }
                        else{
                            stateHolder.transition.getTransition().setSourceState((stateHolder.state));
                        }
                        stateHolder.state.getState().addOutgoingTransition(stateHolder.transition.getTransition());
                        root.getChildren().add(lineView);


                        // Создаем линию между состояниями
                    }

                    // Сбрасываем состояние и отключаем кнопку добавления перехода
                    stateHolder.state = null;
                    stateHolder.transition = null;
                    addTransitionButton.setSelected(false);
                    isTransitionActive = false;

                }

            }
        });

    }

    //отдельный метод по созданию текстового поля
    private TextField createTextField(double x, double y, String promptText) {
        TextField textField = new TextField();
        textField.setLayoutX(x);
        textField.setLayoutY(y);
        textField.setPromptText(promptText);
        return textField;
    }
// подсчет суммы токенов
    public static int sumTokenValues(List<PetriStateView> petriStateViews) {
        return petriStateViews.stream()
                .mapToInt(view -> {
                    String tokenValue = view.getToken().getText();
                    try {
                        return Integer.parseInt(tokenValue);
                    } catch (NumberFormatException e) {
                        // Обработка исключения, если значение token не удалось преобразовать в int
                        System.err.println("Ошибка при преобразовании значения token в int: " + e.getMessage());
                        return 0; // Возвращаем 0 в случае ошибки преобразования
                    }
                })
                .sum();
    }
    class TransitionHandler implements EventHandler<ActionEvent> {
        private PetriStateView state;
        private int newTokens;

        public TransitionHandler(PetriStateView state, int newTokens) {
            this.state = state;
            this.newTokens = newTokens;
        }

        @Override
        public void handle(ActionEvent event) {
            state.setToken(String.valueOf(newTokens)); // Обновляем значение токена
        }
    }
    public static void main(String[] args) {
        launch();
    }
}