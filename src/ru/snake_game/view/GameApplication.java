package ru.snake_game.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import ru.snake_game.controller.AIController;
import ru.snake_game.controller.Interfaces.IController;
import ru.snake_game.controller.KeyboardController;
import ru.snake_game.model.*;
import ru.snake_game.model.FieldObjects.*;
import ru.snake_game.model.Interfaces.*;
import ru.snake_game.model.util.*;

import java.io.FileInputStream;
import java.util.HashSet;
import java.util.HashMap;
import java.util.function.Supplier;

public class GameApplication extends Application {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private Stage primaryStage;
    private Scene mainMenuScene;
    private Scene gameScene;
    private Scene gameOverScene;
    private Scene signUp;
    private SubScene gameArea;

    private Duration tickDuration = new Duration(250);
    private double cellSize;
    private double strokeWidth;

    private IGame game;
    private KeyboardController controller;

    private Timeline tickLine;
    private Group gameObjectsToDraw;
    private HashMap<IFieldObject, Node> drawnObjects;
    private HashMap<Class, Supplier<Node>> howToPaint;

    @Override
    public void init() {
        initSignUp();
        initMainMenuScene();
        initGameScene();
        initPainter();
        initGameOverScene();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.resizableProperty().setValue(false);

        primaryStage.setScene(mainMenuScene);
        primaryStage.show();

        primaryStage.setWidth(WINDOW_WIDTH
                + mainMenuScene.getWindow().getWidth()
                - mainMenuScene.getWidth());
        primaryStage.setHeight(WINDOW_HEIGHT
                + mainMenuScene.getWindow().getHeight()
                - mainMenuScene.getHeight());
    }

    private void initPainter() {
        howToPaint = new HashMap<>();
        howToPaint.put(Wall.class, () -> {
            Rectangle res = new Rectangle(
                    cellSize,
                    cellSize);
            res.setFill(Color.GRAY);
            res.setStrokeType(StrokeType.INSIDE);
            res.setStroke(Color.BLACK);
            res.setStrokeWidth(strokeWidth);
            return res;
        });
        howToPaint.put(Apple.class, () -> {
            double r = cellSize / 2;
            Circle res = new Circle(r, r, r);
            res.setStrokeType(StrokeType.INSIDE);
            res.setStroke(Color.BLACK);
            res.setStrokeWidth(strokeWidth);
            res.setFill(Color.RED);
            return res;
        });
        howToPaint.put(SnakePart.class, () -> {
            double r = cellSize / 2;
            Circle res = new Circle(r, r, r);
            res.setStrokeType(StrokeType.INSIDE);
            res.setStroke(Color.BLACK);
            res.setStrokeWidth(strokeWidth);
            res.setFill(Color.LIGHTGREEN);
            return res;
        });
    }

    private void initMainMenuScene() {
        Button playButton = makeImageButton("image/Play.png");
        playButton.setOnAction(event -> primaryStage.setScene(signUp));

        EventHandler<ActionEvent> close =  event -> primaryStage.close();
        Parent root = makeGrid(playButton, close);
        mainMenuScene = new Scene(root);
    }

    private void initGameScene() {
        tickLine = new Timeline();
        tickLine.setOnFinished(event -> {
            game.tick();
            arrangeTickLineAndDrawnObjects();
            tickLine.play();
        });

        gameObjectsToDraw = new Group();
        gameArea = new SubScene(gameObjectsToDraw, WINDOW_HEIGHT, WINDOW_HEIGHT);
        gameArea.setFill(Color.LIGHTGRAY);

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(gameArea);
        gameScene = new Scene(root);

        gameScene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.ESCAPE)
                gameOver();
            else
                controller.pressKey(code);
        });
    }

    private void initSignUp() {
        TextField name = new TextField();
        TextField port = new TextField();
        TextField ip = new TextField();

        Button button = new Button("CONNECT");
        button.setCursor(Cursor.HAND);
        button.setOnAction(event -> connect(name.getText(), port.getText(), ip.getText()));
        HBox buttonHB = new HBox();
        buttonHB.getChildren().add(button);

        HBox[] hBoxes = new HBox[]{
                makeHBox("Name:", name),
                makeHBox("Port:", port),
                makeHBox("Ip:", ip),
                buttonHB
        };

        for (HBox hb : hBoxes) {
            hb.setAlignment(Pos.CENTER_RIGHT);
        }

        GridPane center = new GridPane();
        center.setVgap(10);
        center.setAlignment(Pos.CENTER);
        center.getChildren().addAll(hBoxes);
        for (int i = 0; i < center.getChildren().size(); i++) {
            GridPane.setConstraints(center.getChildren().get(i), 0, i);
        }

        EventHandler<ActionEvent> close =  event -> primaryStage.setScene(mainMenuScene);
        Parent root = makeGrid(center, close);
        signUp = new Scene(root);
    }

    private void startGame() {
        IField field = FieldMakers.makeBoardedField(15, 15);
        HashSet<IGenerator> generators = new HashSet<>();
        generators.add(new Generator<>(Apple.class, field));

        game = new Game(field, generators);

        HashMap<KeyCode, Vector> keys = new HashMap<>();
        keys.put(KeyCode.UP, Directions.UP);
        keys.put(KeyCode.DOWN, Directions.DOWN);
        keys.put(KeyCode.LEFT, Directions.LEFT);
        keys.put(KeyCode.RIGHT, Directions.RIGHT);

        controller = new KeyboardController(keys);

        ISnakeController snake = new SnakeController(field, new Vector(4, 4), Directions.RIGHT, controller);
        field.addSnake(snake);
        IController aiController = new AIController(field);
        ISnakeController aiSnake= new SnakeController(field, new Vector(8, 8), Directions.RIGHT, aiController);
        field.addSnake(aiSnake);

        drawnObjects = new HashMap<>();
        cellSize = ((double) Integer.min(WINDOW_HEIGHT, WINDOW_WIDTH)) / game.getField().getWidth();
        strokeWidth = cellSize / 20;
        arrangeTickLineAndDrawnObjects();
        tickLine.play();
        primaryStage.setScene(gameScene);
    }

    private void arrangeTickLineAndDrawnObjects() {
        tickLine.getKeyFrames().clear();
        gameObjectsToDraw.getChildren().clear();

        IField field = game.getField();
        for (int x = 0; x < field.getWidth(); x++) {
            for (int y = 0; y < field.getHeight(); y++) {
                Vector loc = new Vector(x, y);
                IFieldObject object = field.getObjectAt(loc);
                if (object == null)
                    continue;
                Node node;
                if (drawnObjects.containsKey(object)) {
                    node = drawnObjects.get(object);
                    tickLine.getKeyFrames().add(new KeyFrame(tickDuration,
                            new KeyValue(node.translateXProperty(), ((double) loc.getX()) / field.getWidth() * WINDOW_HEIGHT),
                            new KeyValue(node.translateYProperty(), ((double) loc.getY()) / field.getHeight() * WINDOW_HEIGHT)
                    ));
                } else {
                    node = howToPaint.get(object.getClass()).get();
                    drawnObjects.put(object, node);
                    node.translateXProperty().setValue(((double) loc.getX()) / field.getWidth() * WINDOW_HEIGHT);
                    node.translateYProperty().setValue(((double) loc.getY()) / field.getHeight() * WINDOW_HEIGHT);
                }
                gameObjectsToDraw.getChildren().add(node);
            }
        }
    }

    private void connect(String name, String port, String ip) {
        if (name.equals("") || port.equals("") || ip.equals("")) {
            // Please, do NOT remove ButtonType, it will cause an extremely big bug!!!!!
            new Alert(Alert.AlertType.NONE, "Wrong input.", ButtonType.CLOSE).show();
            return;
        }
        startGame();
    }

    private HBox makeHBox(String label, TextField textField) {
        HBox hb = new HBox();
        hb.getChildren().addAll(new Label(label), textField);
        return hb;
    }

    private Image getImage(String name){
        FileInputStream input = null;
        try {
            input = new FileInputStream(name);
        } catch (Exception ignored) {
        }
        return new Image(input);
    }

    private Button makeImageButton(String name) {
        Image image = getImage(name);
        ImageView imageView = new ImageView(image);
        Button button = new Button("", imageView);
        button.setCursor(Cursor.HAND);
        button.setBackground(Background.EMPTY);
        return button;
    }

    private Parent makeGrid(Node center, EventHandler<ActionEvent> exitAction) {
        Button exitButton = makeImageButton("image/Exit.png");
        exitButton.setOnAction(exitAction);
        BorderPane root = new BorderPane();
        root.setCenter(center);
        root.setBottom(exitButton);

        Image image = getImage("image/BackGr.png");
        BackgroundImage myBI= new BackgroundImage(image,
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        root.setBackground(new Background(myBI));

        return root;
    }

    public void gameOver() {
        tickLine.stop();
        primaryStage.setScene(gameOverScene);
    }

    private void initGameOverScene() {
        Text sceneTitle = new Text();
        sceneTitle.setText("GAME OVER");
        sceneTitle.setFont(Font.font("verdana", FontWeight.NORMAL, 50));

        EventHandler<ActionEvent> close =  event -> primaryStage.close();
        Parent root = makeGrid(sceneTitle, close);

        gameOverScene = new Scene(root);
    }
}