package ru.snake_game.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import ru.snake_game.model.*;
import ru.snake_game.model.FieldObjects.*;
import ru.snake_game.model.Interfaces.*;
import ru.snake_game.model.util.*;

import java.util.Random;
import java.util.HashMap;
import java.util.function.Supplier;

public class GameApplication extends Application {
    private Stage primaryStage;

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    private Scene mainMenuScene;
    private Scene gameScene;
    private Scene pauseMenuScene;

    SubScene gameArea;

    private Duration tickDuration = new Duration(100);
    private double cellSize;
    private double strokeWidth;
    private Snake snake;
    private Game game;
    private Timeline tickLine;
    private Group gameObjectsToDraw;
    private HashMap<IFieldObject, Node> drawnObjects;
    private HashMap<Vector, Node> drawnSnakes;
    private HashMap<Class, Supplier<Node>> howToPaint;

    @Override
    public void init() {
        initMainMenuScene();
        initGameScene();
        initPauseMenuScene();

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
        howToPaint.put(Snake.class, () -> {
            double r = cellSize / 2;
            Circle res = new Circle(r, r, r);
            res.setStrokeType(StrokeType.INSIDE);
            res.setStroke(Color.BLACK);
            res.setStrokeWidth(strokeWidth);
            res.setFill(Color.LIGHTGREEN);
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
    }

    private void initGameScene() {
        tickLine = new Timeline();
        tickLine.setOnFinished(event -> {
            game.tick();
            handle(game.getField());
            arrangeTickLineAndDrawnObjects();
            if (snake.isAlive())
                tickLine.play();
//            else {
//                Text text = new Text("YOU DIED");
//                ((Group)gameScene.getRoot()).getChildren().add(text);
//            }
        });

        Group root = new Group();
        gameScene = new Scene(root);

        gameObjectsToDraw = new Group();
        //noinspection SuspiciousNameCombination
        gameArea = new SubScene(gameObjectsToDraw, WINDOW_HEIGHT, WINDOW_HEIGHT);
        gameArea.layoutXProperty().bind(gameArea.widthProperty().divide(2).subtract(WINDOW_WIDTH / 2).multiply(-1));
        gameArea.layoutYProperty().bind(gameArea.heightProperty().divide(2).subtract(WINDOW_HEIGHT / 2).multiply(-1));
        gameArea.setFill(Color.LIGHTGRAY);
        root.getChildren().add(gameArea);

        HashMap<KeyCode, Runnable> keyPressActions = new HashMap<>();
        keyPressActions.put(KeyCode.ESCAPE, this::pauseGame);
        keyPressActions.put(KeyCode.UP, () -> snake.setDirection(Directions.UP));
        keyPressActions.put(KeyCode.DOWN, () -> snake.setDirection(Directions.DOWN));
        keyPressActions.put(KeyCode.LEFT, () -> snake.setDirection(Directions.LEFT));
        keyPressActions.put(KeyCode.RIGHT, () -> snake.setDirection(Directions.RIGHT));

        gameScene.setOnKeyPressed(event -> {
            Runnable toDo = keyPressActions.get(event.getCode());
            if (toDo != null)
                toDo.run();
        });
    }

    private void startGame() {
        @SuppressWarnings("MagicNumber") IField field = FieldMakers.makeBoardedField(15, 15);
        snake = new Snake(new Vector(4, 4), Directions.RIGHT);
        int snakeNumber = field.addSnake(snake);
        game = new Game(field);
        drawnObjects = new HashMap<>();
        drawnSnakes = new HashMap<>();
        cellSize = ((double) Integer.min(WINDOW_HEIGHT, WINDOW_WIDTH)) / game.getField().getWidth();
        strokeWidth = cellSize / 20;
        arrangeTickLineAndDrawnObjects();
        tickLine.play();
        primaryStage.setScene(gameScene);
    }

    private void pauseGame() {
        primaryStage.setScene(pauseMenuScene);
        WritableImage image = new WritableImage(WINDOW_WIDTH, WINDOW_HEIGHT);
        gameScene.snapshot(image);
        ((GridPane) (pauseMenuScene.getRoot()))
                .setBackground(
                        new Background(
                                new BackgroundImage(
                                        image,
                                        BackgroundRepeat.NO_REPEAT,
                                        BackgroundRepeat.NO_REPEAT,
                                        BackgroundPosition.CENTER,
                                        BackgroundSize.DEFAULT)));
        tickLine.pause();
    }

    private void arrangeTickLineAndDrawnObjects() {
        tickLine.getKeyFrames().clear();
        gameObjectsToDraw.getChildren().clear();

        IField field = game.getField();
        for (int x = 0; x < field.getWidth(); x++) {
            for (int y = 0; y < field.getHeight(); y++) {
                Node node;
                Vector loc = new Vector(x, y);
                IFieldObject object = field.getObjectAt(loc);
                if (object == null)
                    continue;
                if (drawnObjects.containsKey(object)) {
                    node = drawnObjects.get(object);
                    tickLine.getKeyFrames().add(new KeyFrame(tickDuration,
                            new KeyValue(node.translateXProperty(),
                                    ((double) loc.getX()) / field.getWidth() * WINDOW_HEIGHT),
                            new KeyValue(node.translateYProperty(),
                                    ((double) loc.getY()) / field.getHeight() * WINDOW_HEIGHT)));
                } else {
                    node = howToPaint.get(object.getClass()).get();
                    drawnObjects.put(object, node);
                    node.translateXProperty().setValue(((double) loc.getX()) / field.getWidth() * WINDOW_HEIGHT);
                    node.translateYProperty().setValue(((double) loc.getY()) / field.getHeight() * WINDOW_HEIGHT);
                }
                gameObjectsToDraw.getChildren().add(node);
            }
        }
        for (Vector part : field.getSnake(0).getTrace())
        {
            Node node;
            if (drawnSnakes.containsKey(part)) {
                node = drawnSnakes.get(part);
                tickLine.getKeyFrames().add(new KeyFrame(tickDuration,
                        new KeyValue(node.translateXProperty(),
                                ((double) part.getX()) / field.getWidth() * WINDOW_HEIGHT),
                        new KeyValue(node.translateYProperty(),
                                ((double) part.getY()) / field.getHeight() * WINDOW_HEIGHT)));
            } else {
                double r = cellSize / 2;
                node = new Circle(r, r, r);
                ((Circle)node).setStrokeType(StrokeType.INSIDE);
                ((Circle)node).setStroke(Color.BLACK);
                ((Circle)node).setStrokeWidth(strokeWidth);
                ((Circle)node).setFill(Color.LIGHTGREEN);
                drawnSnakes.put(part, node);
                node.translateXProperty().setValue(((double) part.getX()) / field.getWidth() * WINDOW_HEIGHT);
                node.translateYProperty().setValue(((double) part.getY()) / field.getHeight() * WINDOW_HEIGHT);
            }
            gameObjectsToDraw.getChildren().add(node);
        }
    }

    private void handle(IField field) {
        boolean hasApple = false;
        for (int x = 0; x < field.getWidth(); x++) {
            for (int y = 0; y < field.getHeight(); y++) {
                if (field.getObjectAt(new Vector(x, y)) instanceof Apple) {
                    hasApple = true;
                    break;
                }
            }
        }
        if (!hasApple) {
            Vector location = field.findEmptyCell();

            //noinspection MagicNumber
            game.getField().addObject(new Apple(location, 1 + (new Random()).nextInt(2)));
        }
    }

    private void initPauseMenuScene() {
        Button[] buttons = new Button[]{
                new Button("Resume"),
                new Button("Restart"),
                new Button("Quit to Main Menu")
        };

        buttons[0].setOnAction(event -> {
            primaryStage.setScene(gameScene);
            tickLine.play();
        });

        buttons[1].setOnAction(event -> startGame());

        buttons[2].setOnAction(event -> {
            tickLine.stop();
            primaryStage.setScene(mainMenuScene);
        });

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setVgap(20);

        Text sceneTitle = new Text();
        sceneTitle.setText("PAUSE");
        sceneTitle.setFont(Font.font("verdana", FontWeight.BOLD, 50));
        root.add(sceneTitle, 0, 0);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setMinSize(180, 40);
            GridPane.setConstraints(buttons[i], 0, i + 1);
        }
        root.getChildren().addAll(buttons);
        pauseMenuScene = new Scene(root);
    }

    private void initMainMenuScene() {
        Group root = new Group();
        mainMenuScene = new Scene(root);
        mainMenuScene.fillProperty().setValue(Color.BLACK);

        GridPane buttonList = new GridPane();
        root.getChildren().add(buttonList);

        //noinspection MagicNumber
        buttonList.layoutXProperty().setValue(20);
        //noinspection MagicNumber
        buttonList.layoutYProperty()
                .bind(mainMenuScene.heightProperty().subtract(buttonList.heightProperty().add(20)));

        buttonList.setVgap(5);

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(event -> primaryStage.close());

        Button playButton = new Button("Play");
        playButton.setOnAction(event -> startGame());

        Button[] buttons = new Button[]{
                playButton,
                new Button("Options"),
                new Button("Credits"),
                exitButton
        };

        for (int i = 0; i < buttons.length; i++) {
            GridPane.setConstraints(buttons[i], 0, i);
        }
        buttonList.getChildren().addAll(buttons);
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
}