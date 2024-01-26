import java.util.ArrayList;
import java.util.HashMap;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class WordMasterClientGUI extends Application {

    Font h1 = Font.loadFont("file:src/main/resources/Fonts/Inter-Bold.otf", 80);
    Font h2 = Font.loadFont("file:src/main/resources/Fonts/Inter-Bold.otf", 60);
    Font h3 = Font.loadFont("file:src/main/resources/Fonts/Inter-Bold.otf", 30);
    Font quit = Font.loadFont("file:src/main/resources/Fonts/Lato-Bold.ttf", 20);

    Button joinServer;
    HashMap<String, Scene> sceneMap;
    VBox portInfoBox;
    Scene startScene;
    BorderPane startPane;

    Button playButton;
    WordMasterInfo info;

    Text chooseCategoryText;
    Button category1Button;
    Button category2Button;
    Button category3Button;
    Text category1AttemptsText;
    Text category2AttemptsText;
    Text category3AttemptsText;
    Integer category1Attempts;
    Integer category2Attempts;
    Integer category3Attempts;

    BorderPane gamePageRoot;
    HBox letterContainer;
    HBox guessingContainer;
    VBox gameContent;
    HBox gameInfoContent;
    Text wordInfoText;
    TextField enterGuessField;
    Text category;
    Text attemptInfo;
    Button guessLetterButton;

    Button playAgainButtonL;
    Button playAgainButtonW;

    Integer wordLength;

    WordMasterClient clientConnection;
    Label portNumberLabel;
    TextField portNumberField;
    ListView<String> gameInfoItems;


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Word Master Client");

        this.portNumberLabel = new Label("Port Number: ");
        this.portNumberField = new TextField("5555");
        this.joinServer = new Button("Join Game");

        this.joinServer.setOnAction(e -> {
            try {
                clientConnection = new WordMasterClient(data -> {
                    Platform.runLater(() -> {

                        info = (WordMasterInfo) data;

                        System.out.println("Client chose " + info.chosenCategory);

                        if (info.startAgain){

                            System.out.println("Player has started new game");

                        } else {

                            category.setText(info.chosenCategory);
                            wordLength = info.currentLetters.size();
                            wordInfoText.setText("there are " + wordLength + "  letters");
                            attemptInfo.setText(info.attemptCount +" attempts left");

                            updateWordDisplay(info.currentLetters);

                            if (info.attemptCount <= 0){

                                category1AttemptsText.setText(info.categoryData.get("animals").toString());
                                category2AttemptsText.setText(info.categoryData.get("countries").toString());
                                category3AttemptsText.setText(info.categoryData.get("food").toString());

                                try {
                                    primaryStage.setScene(sceneMap.get("category"));
                                    primaryStage.show();
                                } catch (Exception err){
                                    System.out.println("Can't switch to category scene");
                                }

                                if(info.categoryData.get(info.chosenCategory) == 0){
                                    System.out.println("Game Ended");
                                   switch(info.chosenCategory){
                                       case "animals":
                                           category1Button.setDisable(true);
                                           break;
                                       case "countries":
                                           category2Button.setDisable(true);
                                           break;
                                       case "food":
                                           category3Button.setDisable(true);
                                           break;
                                   }

                                    try {
                                        PauseTransition pause = new PauseTransition(Duration.seconds(2));
                                        pause.setOnFinished(event -> primaryStage.setScene(sceneMap.get("lose")));
                                        pause.play();
                                        primaryStage.show();
                                    } catch (Exception err){
                                        System.out.println("Can't switch to lose scene");
                                    }
                                } else {
                                    try {
                                        PauseTransition pause = new PauseTransition(Duration.seconds(2));
                                        pause.setOnFinished(event -> primaryStage.setScene(sceneMap.get("category")));
                                        pause.play();
                                        primaryStage.show();
                                    } catch (Exception err){
                                        System.out.println("Can't switch to category scene");
                                    }
                                }

                            } else if (info.ca1 && info.chosenCategory.equals("animals")) {

                                System.out.println("Word was guessed correctly ");
                                category1Button.setDisable(true);

                                try {
                                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                                    pause.setOnFinished(event -> primaryStage.setScene(sceneMap.get("category")));
                                    pause.play();
                                    primaryStage.show();
                                } catch (Exception err){
                                    System.out.println("Can't switch to category scene");
                                }
                            }
                            else if (info.ca2 && info.chosenCategory.equals("countries")) {

                                System.out.println("Word was guessed correctly ");
                                category2Button.setDisable(true);
                                try {
                                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                                    pause.setOnFinished(event -> primaryStage.setScene(sceneMap.get("category")));
                                    pause.play();
                                    primaryStage.show();
                                } catch (Exception err){
                                    System.out.println("Can't switch to category scene");
                                }
                            }
                            else if (info.ca3 && info.chosenCategory.equals("food")) {
                                System.out.println("Word was guessed correctly ");
                                category3Button.setDisable(true);
                                try {
                                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                                    pause.setOnFinished(event -> primaryStage.setScene(sceneMap.get("category")));
                                    pause.play();
                                    primaryStage.show();
                                } catch (Exception err){
                                    System.out.println("Can't switch to category scene");
                                }
                            }
                            if (info.ca1 && info.ca2 && info.ca3) {
                                System.out.println("User Won");
                                try {
                                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                                    pause.setOnFinished(event -> primaryStage.setScene(sceneMap.get("win")));
                                    pause.play();

                                } catch (Exception err){
                                    System.out.println("Can't switch to win scene");
                                }
                            }
                        }

                    });

                }, Integer.parseInt(this.portNumberField.getText()));
                clientConnection.start();
            } catch (Exception err) {
                System.out.println("Failed to connect!");
                System.exit(0);
                Platform.exit();
            }

            primaryStage.setScene(sceneMap.get("start"));
            primaryStage.setTitle("Word Master");
        });

        this.portInfoBox = new VBox(20, portNumberLabel, portNumberField, joinServer);
        startPane = new BorderPane();
        startPane.setPadding(new Insets(70));
        startPane.setCenter(portInfoBox);

        startScene = new Scene(startPane, 500, 400);

        playButton = new Button("PLAY");
        playButton.setOnAction(e -> {
            primaryStage.setScene(sceneMap.get("category"));

            clientConnection.sendMessage(new WordMasterInfo(' ', -1, null, new HashMap<>(), new ArrayList<>(), false, true, false,
                    false, false));
        });

        wordLength = 0;

        chooseCategoryText = new Text("choose a category");
        chooseCategoryText.setFont(h2);

        category1Button = createCategoryButton("animals");
        category1Button.setOnAction(e -> {
            System.out.println("User selected animals");
            clientConnection.sendMessage(new WordMasterInfo(' ', 6, "animals", info.categoryData, new ArrayList<>(),
                    false,
                    false,  false,
                    false, false));
            System.out.println("Message Sent");
            primaryStage.setScene(sceneMap.get("game"));

        });

        category2Button = createCategoryButton("countries");
        category2Button.setOnAction(e -> {
            System.out.println("User selected countries");
            clientConnection.sendMessage(new WordMasterInfo(' ', 6, "countries", new HashMap<>(), new ArrayList<>(),
                    false,
                    false, false,
                    false, false));
            primaryStage.setScene(sceneMap.get("game"));
        });

        category3Button = createCategoryButton("food");
        category3Button.setOnAction(e -> {
            System.out.println("User selected food");
            clientConnection.sendMessage(new WordMasterInfo(' ', 6, "food", new HashMap<>(), new ArrayList<>(), false, false,
                    false,
                    false, false));
            primaryStage.setScene(sceneMap.get("game"));
        });

        guessLetterButton = new Button("GUESS");
        guessLetterButton.setOnAction(e -> {
            try {
                String guessedLetter = enterGuessField.getText(0, 1);
                char guess = guessedLetter.charAt(0);

                // send the character guessed over to the server to check
                System.out.println("User guessed: " + guess);
                clientConnection.sendMessage(new WordMasterInfo(guess, 6, category.getText(), new HashMap<>(), new ArrayList<>(),
                        false,
                        false, false, false, false ));
                enterGuessField.clear();

            } catch (Exception err){
                System.out.println("User didn't enter anything");
            }

        });

        playAgainButtonL = new Button("PLAY AGAIN");
        playAgainButtonL.setFont(quit);
        playAgainButtonL.setStyle("-fx-background-radius: 999px; -fx-background-color: #15616DB2; -fx-text-fill: white");
        playAgainButtonL.setPrefWidth(230);
        playAgainButtonL.setPrefHeight(70);
        playAgainButtonL.setOnAction(e -> {

            try {
                primaryStage.setScene(sceneMap.get("category"));

                clientConnection.sendMessage(new WordMasterInfo(' ', -1, null, new HashMap<>(), new ArrayList<>(), true, false,
                        false,
                        false, false));
                info.categoryData.put("animals", 3);
                info.categoryData.put("countries", 3);
                info.categoryData.put("food", 3);

                category1AttemptsText.setText(info.categoryData.get("animals").toString());
                category2AttemptsText.setText(info.categoryData.get("countries").toString());
                category3AttemptsText.setText(info.categoryData.get("food").toString());
                category1Button.setDisable(false);
                category2Button.setDisable(false);
                category3Button.setDisable(false);
            } catch (Exception err){
                System.out.println("Cant create button");
            }

        });

        playAgainButtonW = new Button("PLAY AGAIN");
        playAgainButtonW.setFont(quit);
        playAgainButtonW.setStyle("-fx-background-radius: 999px; -fx-background-color: #15616DB2; -fx-text-fill: white");
        playAgainButtonW.setPrefWidth(230);
        playAgainButtonW.setPrefHeight(70);
        playAgainButtonW.setOnAction(e -> {

            try {
                primaryStage.setScene(sceneMap.get("category"));

                clientConnection.sendMessage(new WordMasterInfo(' ', -1, null, new HashMap<>(), new ArrayList<>(), true, false,
                        false,
                        false, false));
                info.categoryData.put("animals", 3);
                info.categoryData.put("countries", 3);
                info.categoryData.put("food", 3);

                category1AttemptsText.setText(info.categoryData.get("animals").toString());
                category2AttemptsText.setText(info.categoryData.get("countries").toString());
                category3AttemptsText.setText(info.categoryData.get("food").toString());
                category1Button.setDisable(false);
                category2Button.setDisable(false);
                category3Button.setDisable(false);
            } catch (Exception err){
                System.out.println("Cant create button");
            }

        });

        gameInfoItems = new ListView<String>();
        sceneMap = new HashMap<String, Scene>();
        sceneMap.put("start", createOpeningGameScene());
        sceneMap.put("category", createCategoryScene());
        sceneMap.put("game", createGameScene());
        sceneMap.put("win", createWinGameScene());
        sceneMap.put("lose", createLoseGameScene());

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        primaryStage.setScene(startScene);
        primaryStage.show();
    }

    public Scene createOpeningGameScene() {
        VBox titlePage = new VBox(100);

        playButton.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #15616DB2; -fx-background-radius: 30;");
        playButton.setFont(h3);
        playButton.setPrefSize(150, 50);

        Text gameTitle = new Text("WORD MASTER\nCHALLENGE");
        gameTitle.setTextAlignment(TextAlignment.CENTER);

        Font gameTitleFont = h1;
        gameTitle.setFont(gameTitleFont);

        titlePage.getChildren().addAll(gameTitle, playButton);
        titlePage.setAlignment(Pos.CENTER);

        return new Scene(titlePage, 1300, 800);
    }

    public Scene createLoseGameScene() {

        Text loseText = new Text("you lost.");
        loseText.setFont(h1);

        Button quitButton = createQuitButton();

        VBox loseMenuButtonContainer = new VBox(20, playAgainButtonL, quitButton);
        loseMenuButtonContainer.setAlignment(Pos.CENTER);

        VBox loseScene = new VBox(200, loseText, loseMenuButtonContainer);
        loseScene.setAlignment(Pos.CENTER);

        return new Scene(loseScene, 1300, 800);
    }

    public Scene createWinGameScene() {

        Text winText = new Text("YOU WIN!");
        winText.setFont(h1);

        Button quitButton = createQuitButton();

        VBox winMenuButtonContainer = new VBox(20, playAgainButtonW, quitButton);
        winMenuButtonContainer.setAlignment(Pos.CENTER);

        VBox winScene = new VBox(200, winText, winMenuButtonContainer);
        winScene.setAlignment(Pos.CENTER);

        return new Scene(winScene, 1300, 800);
    }

    public Scene createCategoryScene() {

        category1Button.setFont(h3);
        category2Button.setFont(h3);
        category3Button.setFont(h3);

        category1Attempts = 3;
        category2Attempts = 3;
        category3Attempts = 3;


        category1AttemptsText = new Text(category1Attempts.toString());
        category2AttemptsText = new Text(category2Attempts.toString());
        category3AttemptsText = new Text(category3Attempts.toString());

        category1AttemptsText.setFont(h3);
        category2AttemptsText.setFont(h3);
        category3AttemptsText.setFont(h3);

        HBox category1Container = new HBox(50, category1AttemptsText, category1Button);
        HBox category2Container = new HBox(50, category2AttemptsText, category2Button);
        HBox category3Container = new HBox(50, category3AttemptsText, category3Button);
        category1Container.setAlignment(Pos.CENTER);
        category2Container.setAlignment(Pos.CENTER);
        category3Container.setAlignment(Pos.CENTER);

        VBox categoriesContainer = new VBox(28, category1Container, category2Container, category3Container);
        categoriesContainer.setAlignment(Pos.CENTER);
        VBox categoryPage = new VBox(100, chooseCategoryText, categoriesContainer);
        categoryPage.setAlignment(Pos.CENTER);

        return new Scene(categoryPage, 1300, 800);
    }

    public Scene createGameScene() {
        gamePageRoot = new BorderPane();
        gamePageRoot.setStyle("-fx-background-color: white");
        gamePageRoot.setPadding(new Insets(40, 100, 10, 100));

        letterContainer = new HBox (30);
        letterContainer.setPrefHeight(150);
        letterContainer.setMaxWidth(900);
        letterContainer.setStyle("-fx-background-color: #FFECD1; -fx-background-radius: 30px ");
        letterContainer.setAlignment(Pos.CENTER);


        wordInfoText = new Text("there are " + wordLength + "  letters");
        wordInfoText.setFont(h2);

        Text guessText = new Text("guess a letter:");
        guessText.setFont(h3);

        enterGuessField = new TextField("");
        enterGuessField.setStyle("-fx-background-radius: 30px; -fx-background-color: #FFECD1");
        enterGuessField.setPrefWidth(150);
        enterGuessField.setPrefHeight(100);
        enterGuessField.setMaxWidth(150);
        enterGuessField.setMaxHeight(100);
        enterGuessField.setFont(h2);

        guessLetterButton.setFont(h3);
        guessLetterButton.setStyle("-fx-background-color: #78290F; -fx-background-radius: 33px; -fx-text-fill: white;");

        guessingContainer = new HBox(30, guessText, enterGuessField, guessLetterButton);
        guessingContainer.setAlignment(Pos.CENTER);

        gameContent = new VBox(50, letterContainer, guessingContainer);
        gameContent.setAlignment(Pos.CENTER);

        category = new Text("");
        category.setFont(h3);

        attemptInfo = new Text("");
        attemptInfo.setFont(h3);

        gameInfoContent = new HBox(300, category, new Region(), attemptInfo);
        gameInfoContent.setAlignment(Pos.CENTER);

        gamePageRoot.setCenter(gameContent);
        gamePageRoot.setTop(wordInfoText);
        gamePageRoot.setBottom(gameInfoContent);

        BorderPane.setAlignment(wordInfoText, Pos.CENTER);
        BorderPane.setMargin(gameContent, new Insets(20, 50, 200, 50));

        return new Scene(gamePageRoot, 1300, 800);
    }

    private void updateWordDisplay(ArrayList<Character> currentLetters) {
        letterContainer.getChildren().clear();
        for (char letter : currentLetters){
            Text letterText;
            if (String.valueOf(letter).equals("_")){
                letterText = new Text("  ");
            } else {
                letterText = new Text(String.valueOf(letter).toUpperCase() + " ");
            }
            letterText.setFont(h1);
            letterContainer.getChildren().add(letterText);
        }
    }

    private Button createCategoryButton(String category) {
            Button button = new Button(category);

            String baseStyle = "-fx-background-radius: 20px; -fx-pref-width: 381px; -fx-pref-height: 110px; -fx-text-fill: white;";
            String activeStyle = "-fx-background-color: #FF7D00; " + baseStyle;
            String disabledStyle = "-fx-background-color: #15616D; " + baseStyle; // Use the same color with reduced opacity

            button.setStyle(activeStyle);

            button.setOnAction(e -> {
                System.out.println("User selected: " + category);
                clientConnection.sendMessage(new WordMasterInfo(' ', 6, category, new HashMap<>(), new ArrayList<>(), false, false,
                        false,
                        false, false));
            });

            button.disableProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    button.setStyle(disabledStyle);
                } else {
                    button.setStyle(activeStyle);
                }
            });

            return button;
    }

    private Button createQuitButton(){
        Button quitButton = new Button("QUIT");
        quitButton.setFont(quit);
        quitButton.setPrefWidth(230);
        quitButton.setPrefHeight(70);
        quitButton.setStyle("-fx-background-radius: 999px; -fx-background-color: #78290F; -fx-text-fill: white");
        quitButton.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });
        return quitButton;
    }

}