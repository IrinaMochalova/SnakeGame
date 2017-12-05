package client;

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

import model.*;
import model.FieldObjects.*;
import model.Interfaces.*;
import proto.Settings;
import proto.SocketClient;
import server.FieldMakers;

import java.io.FileInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Supplier;

public class GUI extends Application {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private Stage primaryStage;
    private Scene mainMenuScene;
    private Scene optionScene;
    private Scene signUpScene;
    private Scene offlineSignUpScene;
    private Scene gameScene;
    private Scene gameOverScene;
    private SubScene gameArea;

    private Duration tickDuration = new Duration(250);
    private double cellSize;
    private double strokeWidth;

    //private Client client;

    private Timeline tickLine;
    private Group gameObjectsToDraw;
    private HashMap<IFieldObject, Node> drawnObjects;
    private HashMap<Class, Supplier<Node>> howToPaint;

    @Override
    public void init() {
        initPainter();
        initMainMenuScene();
        initOptionScene();
        initSignUpScene();
        initOfflineSignUpScene();
        initGameScene();
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
            Rectangle res = new Rectangle(cellSize, cellSize);
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
        Button playButton = makeImageButton("images/Play.png");
        playButton.setOnAction(event -> primaryStage.setScene(optionScene));

        EventHandler<ActionEvent> close =  event -> primaryStage.close();
        Parent root = makeGrid(playButton, close);
        mainMenuScene = new Scene(root);
    }

    private void initOptionScene() {
        Button goOnline = new Button("GO ONLINE");
        goOnline.setOnAction(event -> primaryStage.setScene(signUpScene));

        Button offline = new Button("GO OFFLINE");
        offline.setOnAction(event -> primaryStage.setScene(offlineSignUpScene));

        GridPane center = new GridPane();
        center.setVgap(10);
        center.setAlignment(Pos.CENTER);
        center.getChildren().addAll(goOnline, offline);
        int  i = 0;
        for (Node button : center.getChildren()) {
            button.setCursor(Cursor.HAND);
            ((Button)button).setMinSize(150, 30);
            GridPane.setConstraints(button, 0, i);
            i += 1;
        }

        EventHandler<ActionEvent> close =  event -> primaryStage.setScene(mainMenuScene);
        Parent root = makeGrid(center, close);
        optionScene = new Scene(root);
    }

    private void initSignUpScene() {
        TextField ip = new TextField("localhost");
        TextField port = new TextField("15151");

        Button button = new Button("CONNECT");
        button.setCursor(Cursor.HAND);
        button.setOnAction(event -> connect(port.getText(), ip.getText()));

        HBox[] hBoxes = new HBox[]{
                makeHBox("Ip:", ip),
                makeHBox("Port:", port),
                new HBox(button)
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

        EventHandler<ActionEvent> close =  event -> primaryStage.setScene(optionScene);
        Parent root = makeGrid(center, close);
        signUpScene = new Scene(root);
    }

    private void initOfflineSignUpScene() {
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll(1, 2, 3);

        Button play = new Button("PLAY");
        play.setCursor(Cursor.HAND);
        play.setMinWidth(80);
        //play.setOnAction(event -> startOfflineGame((int)comboBox.getValue()));

        HBox[] hBoxes = new HBox[] {
                new HBox(new Label("Player count: "), comboBox),
                new HBox(play)
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

        EventHandler<ActionEvent> close =  event -> primaryStage.setScene(optionScene);
        Parent root = makeGrid(center, close);
        offlineSignUpScene = new Scene(root);
    }

    private void initGameScene() {
        tickLine = new Timeline();
        tickLine.setOnFinished(event -> {
            //arrangeTickLineAndDrawnObjects();
            tickLine.play();
        });

        gameObjectsToDraw = new Group();
        gameArea = new SubScene(gameObjectsToDraw, WINDOW_HEIGHT, WINDOW_HEIGHT);
        gameArea.setFill(Color.LIGHTGRAY);

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(gameArea);
        gameScene = new Scene(root);

        /*gameScene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.ESCAPE)
                gameOver();
            else if (pressKey(code) != null)
                client.setDirection(pressKey(code));
        });*/
    }

    private void initGameOverScene() {
        Text sceneTitle = new Text();
        sceneTitle.setText("GAME OVER");
        sceneTitle.setFont(Font.font("verdana", FontWeight.NORMAL, 50));

        EventHandler<ActionEvent> close =  event -> primaryStage.setScene(mainMenuScene);
        Parent root = makeGrid(sceneTitle, close);

        gameOverScene = new Scene(root);
    }

//    private void startGame() {
//        drawnObjects = new HashMap<>();
//        //cellSize = ((double) Integer.min(WINDOW_HEIGHT, WINDOW_WIDTH)) / client.getField().getWidth();
//        strokeWidth = cellSize / 20;
//        arrangeTickLineAndDrawnObjects();
//        tickLine.play();
//        primaryStage.setScene(gameScene);
//    }
//
//    private void arrangeTickLineAndDrawnObjects() {
//        tickLine.getKeyFrames().clear();
//        gameObjectsToDraw.getChildren().clear();
//
//        /*IField field = client.getField();
//        for (int x = 0; x < field.getWidth(); x++) {
//            for (int y = 0; y < field.getHeight(); y++) {
//                Vector loc = new Vector(x, y);
//                IFieldObject object = field.getObjectAt(loc);
//                if (object == null)
//                    continue;
//                Node node;
//                if (drawnObjects.containsKey(object)) {
//                    node = drawnObjects.get(object);
//                    tickLine.getKeyFrames().add(new KeyFrame(tickDuration,
//                            new KeyValue(node.translateXProperty(), ((double) loc.getX()) / field.getWidth() * WINDOW_HEIGHT),
//                            new KeyValue(node.translateYProperty(), ((double) loc.getY()) / field.getHeight() * WINDOW_HEIGHT)
//                    ));
//                } else {
//                    node = howToPaint.get(object.getClass()).get();
//                    drawnObjects.put(object, node);
//                    node.translateXProperty().setValue(((double) loc.getX()) / field.getWidth() * WINDOW_HEIGHT);
//                    node.translateYProperty().setValue(((double) loc.getY()) / field.getHeight() * WINDOW_HEIGHT);
//                }
//                gameObjectsToDraw.getChildren().add(node);
//            }
//        }*/
//    }

    private void connect(String port, String ip) {
        try {
            Socket socket = new Socket(ip, Integer.parseInt(port));
            /*client = new Client(new SocketClient(socket, Settings.MESSAGE_SIZE));
            while (client.getField() == null) {Thread.sleep(100);}*/
            //startGame();
        }
        catch (Exception ex) {
            new Alert(Alert.AlertType.NONE, "Wrong input.", ButtonType.CLOSE).show();
            ex.printStackTrace();
            return;
        }
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
        }
        catch (Exception ignored) {}
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
        Button exitButton = makeImageButton("images/Exit.png");
        exitButton.setOnAction(exitAction);
        BorderPane root = new BorderPane();
        root.setCenter(center);
        root.setBottom(exitButton);

        Image image = getImage("images/BackGr.png");
        BackgroundImage myBI= new BackgroundImage(image,
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        root.setBackground(new Background(myBI));

        return root;
    }

    /*private void startOfflineGame(int countOpponents) {
        IField field = FieldMakers.makeBoardedField(15, 15);
        HashSet<IGenerator> generators = new HashSet<>();
        generators.add(new Generator<>(Apple.class, field));

        Game game = new Game(field, generators);

        HashMap<KeyCode, Vector> keys = new HashMap<>();
        keys.put(KeyCode.UP, Direction.UP);
        keys.put(KeyCode.DOWN, Direction.DOWN);
        keys.put(KeyCode.LEFT, Direction.LEFT);
        keys.put(KeyCode.RIGHT, Direction.RIGHT);

        KeyboardPlayer controller = new KeyboardPlayer(keys);

        ISnakeController snake = new SnakeController(field, new Vector(1, 1), Direction.RIGHT, controller);
        field.addSnake(snake);


        IPlayer aiController = new AIPlayer(field);
        HashSet<ISnakeController> snakeControllers = new HashSet<>();
        for (int i = 0; i < countOpponents; i++)
            snakeControllers.add(new SnakeController(field, field.findEmptyCell(), Direction.RIGHT, aiController));
        for (ISnakeController snakeController : snakeControllers)
            field.addSnake(snakeController);

        drawnObjects = new HashMap<>();
        cellSize = ((double) Integer.min(WINDOW_HEIGHT, WINDOW_WIDTH)) / game.getField().getWidth();
        strokeWidth = cellSize / 20;
        arrangeTickLineAndDrawnObjects();
        tickLine.play();
        primaryStage.setScene(gameScene);
    }*/



    public void gameOver() {
        tickLine.stop();
        primaryStage.setScene(gameOverScene);
    }


    private Vector pressKey(KeyCode key) {
        switch (key) {
            case LEFT:
                return Direction.LEFT;
            case RIGHT:
                return Direction.RIGHT;
            case UP:
                return Direction.UP;
            case DOWN:
                return Direction.DOWN;
            default:
                return null;
        }
    }
}