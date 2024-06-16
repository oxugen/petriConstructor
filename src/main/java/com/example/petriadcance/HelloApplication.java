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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HelloApplication extends Application {
    private Node selectedObject;
    private Timeline timeline;
    private PetriNet net = new PetriNet();
    private int secondsElapsed = 0;
    private BorderPane root = new BorderPane();
    private List<javafx.scene.Node> petriNetElements = new ArrayList<>(); // Список для хранения элементов сети Петри
    @Override
    public void start(Stage primaryStage) throws IOException {


        primaryStage.setTitle("Конструктор Сети Петри");
        //создание главной сцены
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();

        Button saveButton = new Button("Сохранить сеть");
        Button chooseNetworkButton = new Button("Выбрать сеть");
        Button checkReachabilityButton = new Button("Проверить связность");
        Button checkSafetyButton = new Button("Проверить безопасность");
        Button checkAllStatesReachableButton = new Button("Проверить достижимость");
        checkReachabilityButton.setOnAction(event -> checkReachability());
        checkSafetyButton.setOnAction(event -> checkSafety());
        checkAllStatesReachableButton.setOnAction(event -> checkAllStatesReachable());
        chooseNetworkButton.setOnAction(event -> {
            List<PetriNet> petriNets = DatabaseUtil.getPetriNets();
            ChoiceDialog<String> dialog = new ChoiceDialog<>(petriNets.get(0).getName(), petriNets.stream().map(p -> p.getName()).toList());
            dialog.setTitle("Загрузка сети Петри");
            dialog.setHeaderText("Выберите сеть для загрузки");
            dialog.setContentText("Доступные сети:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(petriNet -> {
                loadPetriNet(petriNet); // Загрузка выбранной сети
            });
        });
        root.getChildren().add(chooseNetworkButton);
        saveButton.setOnAction(event ->{
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Сохранить сеть Петри");
            dialog.setHeaderText("Введите название сети Петри:");
            dialog.setContentText("Название:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                net.saveToDatabase(name);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Успех");
                alert.setHeaderText(null);
                alert.setContentText("Сеть Петри успешно сохранена");
                alert.showAndWait();
            });
            //net.saveToDatabase();
        } );

        //создание кнопок
        ToggleButton addStateButton = new ToggleButton("Добавить состояние");
        ToggleButton addLineButton = new ToggleButton("Добавить линию");
        ToggleButton addTransitionButton = new ToggleButton("Добавить переход");

        ToggleButton deleteButton = new ToggleButton("Удалить");
        Button startProcessButton = new Button("Начало процесса");
        deleteButton.setOnAction(event -> {
            selectedObject = null;
        });
        //установка цветов и стиля для кнопок
        Label titleLabel = new Label("Визуализация Сети Петри");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setPadding(new Insets(10));
        saveButton.setStyle("-fx-background-color: rgba(246,225,2,0.8); -fx-text-fill: white;"); // фиолетовая кнопка
        saveButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        saveButton.setOnMousePressed(e ->  saveButton.setScaleX(0.9));
        saveButton.setOnMouseReleased(e -> saveButton.setScaleX(1.0));
        chooseNetworkButton.setStyle("-fx-background-color: rgba(26,29,217,0.8); -fx-text-fill: white;"); // фиолетовая кнопка
        chooseNetworkButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        chooseNetworkButton.setOnMousePressed(e ->  chooseNetworkButton.setScaleX(0.9));
        chooseNetworkButton.setOnMouseReleased(e -> chooseNetworkButton.setScaleX(1.0));
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
        checkReachabilityButton.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;");
        checkReachabilityButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        checkReachabilityButton.setOnMousePressed(e -> checkReachabilityButton.setScaleX(0.9));
        checkReachabilityButton.setOnMouseReleased(e -> checkReachabilityButton.setScaleX(1.0));

        checkSafetyButton.setStyle("-fx-background-color: #FF8C00; -fx-text-fill: white;");
        checkSafetyButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        checkSafetyButton.setOnMousePressed(e -> checkSafetyButton.setScaleX(0.9));
        checkSafetyButton.setOnMouseReleased(e -> checkSafetyButton.setScaleX(1.0));

        checkAllStatesReachableButton.setStyle("-fx-background-color: #006400; -fx-text-fill: white;");
        checkAllStatesReachableButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        checkAllStatesReachableButton.setOnMousePressed(e -> checkAllStatesReachableButton.setScaleX(0.9));
        checkAllStatesReachableButton.setOnMouseReleased(e -> checkAllStatesReachableButton.setScaleX(1.0));

        menuBox.getChildren().addAll(checkReachabilityButton, checkSafetyButton, checkAllStatesReachableButton);
// Добавление кнопок в меню
        menuBox.getChildren().addAll(addStateButton, addTransitionButton, addLineButton, deleteButton, startProcessButton);
        menuBox.getChildren().add(saveButton);
        menuBox.getChildren().add(chooseNetworkButton);
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
            List<PetriTransitionView> transitionsToProcess = new ArrayList<>();

            for (Node node : root.getChildren()) {
                if (node instanceof PetriTransitionView startTransition) {
                    List<PetriStateView> sourceOfStartStates = startTransition.getTransition().getSourceState();
                    List<PetriStateView> sourceOfTargetStates = startTransition.getTransition().getTargetState();
                    System.out.println("source" + sourceOfStartStates + "\n" + "target" + sourceOfTargetStates);

                    if (sumTokenValues(sourceOfStartStates) >= sourceOfTargetStates.size()) {
                        transitionsToProcess.add(startTransition);
                    }
                }
            }

            if (!transitionsToProcess.isEmpty()) {
                tokenMoved = true;
                processTransitions(transitionsToProcess, 0);
            }

            if(!tokenMoved){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Симуляция завершена!");
                alert.setHeaderText("Недостаточно токенов для продолжения симуляции.");
                alert.setContentText("Результаты работы: \n" +
                        "Время работы симуляции:" + secondsElapsed + " секунд \n" +
                        "Количество токенов:" + countOfTokens() + "\n" +
                        "Количество состояний:" + net.getStates().stream().count() + "\n" +
                        "Количество переходов:" + net.getTransitions().stream().count()
                );
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
                //deleteButton.setSelected(false);
            }

            if (addStateButton.isSelected()) {
                PetriState newState = new PetriState("New State");
                PetriStateView newStateView = new PetriStateView(x, y, 20, newState);
                newState.setX(x);
                newState.setY(y);
                root.getChildren().add(newStateView);
                root.getChildren().add(newStateView.getStateName());
                root.getChildren().add(newStateView.getToken());
                net.addState(newState);
                newStateView.setOnMouseClicked(stateMouseEvent -> {
                    // Проверяем, что клик был левой кнопкой мыши
                    if (stateMouseEvent.getButton() == MouseButton.PRIMARY) {
                        // Открываем диалоговое окно для изменения имени состояния
                        TextInputDialog dialog = new TextInputDialog(newState.getName());
                        dialog.setTitle("Изменить название состояния");
                        dialog.setHeaderText("Введите новое название:");
                        dialog.setContentText("Название:");

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
                newTransition.setX(x);
                newTransition.setY(y);
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
                        petriLine.setStartX(startX);
                        petriLine.setStartY(startY);
                        petriLine.setEndX(endX);
                        petriLine.setEndY(endY);
                        net.addLine(petriLine);
                        if(isTransitionActive){
                            stateHolder.transition.getTransition().setTargetState(stateHolder.state);
                        }
                        else{
                            stateHolder.transition.getTransition().setSourceState((stateHolder.state));
                        }
                        //stateHolder.state.getState().addOutgoingTransition(stateHolder.transition.getTransition());
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
    private void processTransitions(List<PetriTransitionView> transitions, int index) {
        if (index >= transitions.size()) {
            return;
        }

        PetriTransitionView startTransition = transitions.get(index);
        List<PetriStateView> sourceOfStartStates = startTransition.getTransition().getSourceState();
        List<PetriStateView> sourceOfTargetStates = startTransition.getTransition().getTargetState();

        // Check token availability again just before the update
        if (sumTokenValues(sourceOfStartStates) < sourceOfTargetStates.size()) {
            // If not enough tokens, stop further processing and do not proceed with this transition
            System.out.println("Not enough tokens for transition: " + startTransition);
            processTransitions(transitions, index + 1); // Move to next transition
            return;
        }

        // Atomic token transfer
        Timeline timeline = new Timeline();

        List<Integer> originalSourceTokens = new ArrayList<>();
        for (PetriStateView stateSource : sourceOfStartStates) {
            originalSourceTokens.add(Integer.parseInt(stateSource.getToken().getText()));
        }

        // Decrement source tokens
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2), e -> {
            for (int i = 0; i < sourceOfStartStates.size(); i++) {
                PetriStateView stateSource = sourceOfStartStates.get(i);
                int sourceTokens = originalSourceTokens.get(i) - sourceOfTargetStates.size();
                stateSource.setToken(String.valueOf(sourceTokens));
                System.out.println("Updated source tokens: " + sourceTokens);
            }
        }));

        // Prepare to increment target tokens
        List<Integer> originalTargetTokens = new ArrayList<>();
        for (PetriStateView petriTarget : sourceOfTargetStates) {
            originalTargetTokens.add(Integer.parseInt(petriTarget.getToken().getText()));
        }

        // Increment target tokens
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2.1), e -> {
            for (int i = 0; i < sourceOfTargetStates.size(); i++) {
                PetriStateView petriTarget = sourceOfTargetStates.get(i);
                int targetTokens = originalTargetTokens.get(i) + 1;
                petriTarget.setToken(String.valueOf(targetTokens));
                System.out.println("Updated target tokens: " + targetTokens);
            }
        }));

        timeline.setOnFinished(e -> processTransitions(transitions, index + 1));
        timeline.play();
    }
    private int sumTokenValues(List<PetriStateView> states) {
        return states.stream().mapToInt(state -> Integer.parseInt(state.getToken().getText())).sum();
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
//    public static int sumTokenValues(List<PetriStateView> petriStateViews) {
//        return petriStateViews.stream()
//                .mapToInt(view -> {
//                    String tokenValue = view.getToken().getText();
//                    try {
//                        return Integer.parseInt(tokenValue);
//                    } catch (NumberFormatException e) {
//                        // Обработка исключения, если значение token не удалось преобразовать в int
//                        System.err.println("Ошибка при преобразовании значения token в int: " + e.getMessage());
//                        return 0; // Возвращаем 0 в случае ошибки преобразования
//                    }
//                })
//                .sum();
//    }
    private void checkReachability() {
        boolean isReachable = false;
        List<PetriTransition> transitions = new ArrayList<>();
        for (Node node : root.getChildren()) {
            if (node instanceof PetriTransitionView transitionView){
                isReachable = true;
                transitions.add(transitionView.getTransition());
            }
        }
        for(Node node : root.getChildren()){
            if (node instanceof PetriStateView stateView){
                for(PetriTransition transition : transitions){
                    if(!transition.getSourceState().contains(stateView) && !transition.getTargetState().contains(stateView)){
                        isReachable = false;
                        break;
                    }
                }
            }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Проверка связности");
        alert.setHeaderText(null);
        alert.setContentText(isReachable ? "Все состояния связаны." : "Есть не связные состояния.");
        alert.showAndWait();
    }

    private void checkSafety() {
        boolean isSafe = false;
        for (Node node : root.getChildren()) {
            if (node instanceof PetriStateView stateView){
                if(!(stateView.getToken().getText().equals("0") || stateView.getToken().getText().equals("1"))){
                    isSafe = false;
                    break;
                }
                else{
                    isSafe = true;
                }
            }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Проверка безопасности");
        alert.setHeaderText(null);
        alert.setContentText(isSafe ? "Сеть находится в безопасном состоянии." : "Сеть не безопасна.");
        alert.showAndWait();
    }

    private void checkAllStatesReachable() {
        boolean allReachable = false;
        boolean tokenMoved = false;
        for (Node node : root.getChildren()) {
            if (node instanceof PetriTransitionView startTransition) {
                allReachable = true;
                List<PetriStateView> sourceOfStartStates = startTransition.getTransition().getSourceState();
                List<PetriStateView> sourceOfTargetStates = startTransition.getTransition().getTargetState();
                System.out.println("source" + sourceOfStartStates + "\n" + "target" + sourceOfTargetStates);
                if (sumTokenValues(sourceOfStartStates) >= sourceOfTargetStates.size()) {
                    tokenMoved = true;
                }
            }
        }
        //вывод ошибки
        if(!tokenMoved){
            allReachable = false;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Проверка достижимости");
        alert.setHeaderText(null);
        alert.setContentText(allReachable ? "Все состояния достижимы." : "Есть недостижимые состояния.");
        alert.showAndWait();
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
    public void loadPetriNet(String petriNetName) {
        clearCanvas();
        int petriNetId = 0;
        List<PetriStateView> stateViews = new ArrayList<>();
        List<PetriTransition> petriTransitions = new ArrayList<>();
        List<PetriState> petriStates = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection()) {
            // Загрузка сети Петри
            String queryNet = "SELECT name, petri_net_id FROM PetriNet WHERE name = ?";
            try (PreparedStatement stmtNet = connection.prepareStatement(queryNet)) {
                stmtNet.setString(1, petriNetName);
                try (ResultSet rsNet = stmtNet.executeQuery()) {
                    if (rsNet.next()) {
                        petriNetId = rsNet.getInt("petri_net_id");
                        net = new PetriNet(petriNetId, petriNetName);
                    }
                }
            }

            // Загрузка состояний
            String queryStates = "SELECT state_id, name, x, y FROM PetriState WHERE petri_net_id = ?";
            try (PreparedStatement stmtStates = connection.prepareStatement(queryStates)) {
                stmtStates.setInt(1, petriNetId);
                try (ResultSet rsStates = stmtStates.executeQuery()) {
                    while (rsStates.next()) {
                        int id = rsStates.getInt("state_id");
                        String name = rsStates.getString("name");
                        double x = rsStates.getDouble("x");
                        double y = rsStates.getDouble("y");
                        PetriState state = new PetriState(id, name, x, y);

                        PetriStateView newStateView = new PetriStateView(x, y, 20, state);
                        root.getChildren().add(newStateView);
                        root.getChildren().add(newStateView.getStateName());
                        root.getChildren().add(newStateView.getToken());
                        net.addState(state);
                        petriStates.add(state);
                        stateViews.add(newStateView);
                        petriNetElements.add(newStateView);
                        petriNetElements.add(newStateView.getStateName());
                        petriNetElements.add(newStateView.getToken());
                        newStateView.setOnMouseClicked(stateMouseEvent -> {
                            if (stateMouseEvent.getButton() == MouseButton.PRIMARY) {
                                // Открываем диалоговое окно для изменения имени состояния
                                TextInputDialog dialog = new TextInputDialog(state.getName());
                                dialog.setTitle("Изменить название состояния");
                                dialog.setHeaderText("Введите новое название:");
                                dialog.setContentText("Название:");

                                Optional<String> result = dialog.showAndWait();
                                result.ifPresent(nameOfState -> {
                                    state.setName(nameOfState);
                                    newStateView.getStateName().setText(nameOfState);
                                });

                                // Перехватываем событие, чтобы оно не передавалось родительским элементам
                                stateMouseEvent.consume();
                            }
                            // Проверяем, что клик был левой кнопкой мыши
                            if (stateMouseEvent.getButton() == MouseButton.MIDDLE) {
                                System.out.println("middle");
                                int value = Integer.parseInt(newStateView.getToken().getText());
                                newStateView
                                        .getToken().setText(
                                                String.valueOf(value + 1)
                                        );
                                stateMouseEvent.consume();
                            }
                        });
                    }
                }
            }

            // Загрузка переходов
            String queryTransitions = "SELECT transition_id, name, sourceState, targetState, x, y FROM PetriTransition WHERE petri_net_id = ?";
            try (PreparedStatement stmtTransitions = connection.prepareStatement(queryTransitions)) {
                stmtTransitions.setInt(1, petriNetId);
                try (ResultSet rsTransitions = stmtTransitions.executeQuery()) {
                    while (rsTransitions.next()) {
                        int id = rsTransitions.getInt("transition_id");
                        String name = rsTransitions.getString("name");
                        int sourceStateId = rsTransitions.getInt("sourceState");
                        int targetStateId = rsTransitions.getInt("targetState");
                        double x = rsTransitions.getDouble("x");
                        double y = rsTransitions.getDouble("y");
                        PetriStateView sourceState = getStateById(sourceStateId, stateViews);
                        PetriStateView targetState = getStateById(targetStateId, stateViews);
                        List<PetriStateView> sourceStates = new ArrayList<>();
                        List<PetriStateView> targetStates = new ArrayList<>();
                        sourceStates.add(sourceState);
                        targetStates.add(targetState);
                        PetriTransition transition = new PetriTransition(id, name, x, y, sourceStates, targetStates);
                        PetriTransitionView transitionView = new PetriTransitionView(x, y, 20, 30, transition);
                        petriTransitions.add(transition);
                        root.getChildren().add(transitionView);
                        net.addTransition(transition);
                        petriNetElements.add(transitionView);
                    }
                }
            }

            String queryLines = "SELECT line_id, name, startX, startY, endX, endY FROM PetriLine WHERE petri_net_id = ?";
            try (PreparedStatement stmtLines = connection.prepareStatement(queryLines)) {
                stmtLines.setInt(1, petriNetId);
                try (ResultSet rsLines = stmtLines.executeQuery()) {
                    while (rsLines.next()) {
                        int id = rsLines.getInt("line_id");
                        String name = rsLines.getString("name");
                        double startX= rsLines.getDouble("startX");
                        double startY = rsLines.getDouble("startY");
                        double endX = rsLines.getDouble("endX");
                        double endY = rsLines.getDouble("endY");
                        PetriLine line = new PetriLine(id, startX , startY, endX, endY, name);
                        PetriTransition petriTransition;
                        PetriState petriState;
                        petriTransition = getTransitionByX(startX, petriTransitions);
                        petriState = getStateByX(endX, petriStates);
                        if(petriTransition == null){
                            petriTransition = getTransitionByX(endX, petriTransitions);
                            petriState = getStateByX(endX, petriStates);
                        }
                        PetriLineView lineView = new PetriLineView(startX, startY, endX, endY, line, petriTransition, petriState);
                        net.addLine(line);
                        root.getChildren().add(lineView);
                        petriNetElements.add(lineView);
                    }
                }
            }

            // Обновление интерфейса для отображения загруженной сети
           // updateUIWithPetriNet(petriNet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private int countOfTokens(){
        int countOfTokens = 0;
        for (Node node : root.getChildren()) {
            if (node instanceof PetriStateView stateView){
                countOfTokens += Integer.parseInt(stateView.getToken().getText());
            }
        }
        return countOfTokens;
    }
    private void clearCanvas() {
        root.getChildren().removeAll(petriNetElements);
        petriNetElements.clear();
    }
    public static PetriStateView getStateById(int stateId, List<PetriStateView> stateViews) {
        for (PetriStateView view : stateViews) {
            PetriState state = view.getState();
            if (state.getStateId() == stateId) {
                return view;
            }
        }
        return null; // Если объект с таким stateId не найден
    }

    public static PetriState getStateByX(double x, List<PetriState> states) {
        for (PetriState state : states) {
            if (state.getX() == x) {
                return state;
            }
        }
        return null; // Если объект с таким stateId не найден
    }

    public static PetriTransition getTransitionByX(double x, List<PetriTransition> transitions) {
        for (PetriTransition transition : transitions) {
            if (transition.getX() == x) {
                return transition;
            }
        }
        return null; // Если объект с таким stateId не найден
    }
    public static void main(String[] args) {
        launch();
    }
}