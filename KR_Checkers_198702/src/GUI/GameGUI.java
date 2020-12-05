package GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GameGUI extends Application {

    public static final int squareSize = 100;
    public static int width;
    public static int height;

    private Group squareGroup = new Group();
    private Group checkerGroup = new Group();
    private Group buttonGroup = new Group();

    private Game game = null;

    @Override
    public void start(Stage primaryStage) {

        Label boardSizeMsg = new Label("Board size: ");
        ChoiceBox<String> boardSize = new ChoiceBox<>();
        boardSize.getItems().addAll("7", "8", "9", "10");
        boardSize.setValue("8");

        Label difficultyMsg = new Label("Difficulty: ");
        ChoiceBox<String> difficulty = new ChoiceBox<>();
        difficulty.getItems().addAll("Easy", "Regular", "Hard");
        difficulty.setValue("Regular");

        Button load = new Button("Load");
        load.setOnAction(e -> {
            try {
                Game data = (Game) ResourceManager.load("a.save");
                this.game = data;
//                displayGame();
//                interact();

            } catch (Exception error) {
                System.out.println("Couldn't load: " + error.getMessage());
            }
        });

        Button generateNewGame = new Button("Generate Game");
        generateNewGame.setOnAction(e -> {
            Scene scene = new Scene(createGame(Integer.valueOf(boardSize.getValue()))) ;
            primaryStage.setTitle("Checkers");
            primaryStage.setScene(scene);
            primaryStage.show();
        });

        HBox h = new HBox();
        h.setPadding(new Insets(0, 25, 25, 25));
        h.setAlignment(Pos.CENTER);
        h.setSpacing(5);
        h.getChildren().addAll(difficultyMsg, difficulty, boardSizeMsg, boardSize);

        HBox h2 = new HBox();
        h2.setAlignment(Pos.CENTER);
        h2.setSpacing(5);
        h2.getChildren().addAll(generateNewGame, load);

        VBox v = new VBox();
        v.setPadding(new Insets(25, 25, 25, 25));
        v.setAlignment(Pos.CENTER);
        v.getChildren().addAll(h, h2);

        primaryStage.setTitle("Checkers Main Menu");
        primaryStage.setScene(new Scene(v, 400, 300));
        primaryStage.show();
    }

    private Parent createGame(int boardSize) {
        this.width = boardSize;
        this.height = boardSize;

        Button save = new Button("Save");
        save.setOnAction(e -> {
            try {
                ResourceManager.save(this.game, "a.save");
            } catch (Exception error) {
                System.out.println("Couldn't save: " + error.getMessage());
            }
        });

        Pane board = new Pane();
        board.setPrefSize(width * squareSize, height * squareSize);
        board.getChildren().addAll(squareGroup, checkerGroup);
        ArrayList<Checker> rCheckers = new ArrayList();
        ArrayList<Checker> bCheckers = new ArrayList();
        ArrayList<Square> wSquares = new ArrayList();
        ArrayList<Square> bSquares = new ArrayList();
        for(int i=0; i<height; i++) {
            for(int j=0; j<width; j++) {
                if((i+j)%2 == 0) {
                    Square white = new WhiteSquare(new int[]{i, j});
                    wSquares.add(white);
                    squareGroup.getChildren().add(white);
                }
                else {
                    Square black = new BlackSquare(new int[]{i, j});
                    bSquares.add(black);
                    squareGroup.getChildren().add(black);
                }
                if(j <= 2 && (i+j)%2 != 0) {
                    Checker red = new RedChecker(new int[]{i, j}, 0.4);
                    rCheckers.add(red);
                    checkerGroup.getChildren().add(red);
                }
                else if(j >= 5 && (i+j)%2 != 0) {
                    Checker black = new BlackChecker(new int[]{i, j}, 0.4);
                    bCheckers.add(black);
                    checkerGroup.getChildren().add(black);
                }
            }
        }

        game = new Game(boardSize, rCheckers, bCheckers, wSquares, bSquares);

        HBox h = new HBox();
        h.setPadding(new Insets(0, 25, 25, 25));
        h.setAlignment(Pos.CENTER);
        h.setSpacing(5);
        h.getChildren().addAll(save);

        VBox v = new VBox();
        v.setPadding(new Insets(25, 25, 25, 25));
        v.setAlignment(Pos.CENTER);
        v.getChildren().addAll(h, board);

        return v;
    }

    public static void main(String[] args) {
        launch(args);
    }
}