import java.util.HashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class WordMasterServerGUI extends Application{
	TextField portNumberField;
	Button startServer;
	HashMap<String, Scene> sceneMap;
	VBox portInfoBox;
	Scene startScene;
	BorderPane startPane;
	WordMasterServer serverConnection;
	Label portNumberLabel;
	int portNumber;
	ListView<String> listItems;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Word Master Server");

		this.portNumberLabel = new Label("Port Number: ");
		this.portNumberField = new TextField("5555");
		this.startServer = new Button("Start Server");
		
		this.startServer.setOnAction(e->{

			try {
				this.portNumber = Integer.parseInt(portNumberField.getText());
			} catch (Exception err){
				System.out.println("Invalid Port Number, Try Again");
				Platform.exit();
			}

			primaryStage.setScene(sceneMap.get("server"));
			primaryStage.setTitle("Word Master Server");

			serverConnection = new WordMasterServer(data -> {
				Platform.runLater(()->{
					listItems.getItems().add(data.toString());
				});

			}, portNumber);
		});
		
		this.portInfoBox = new VBox(20, portNumberLabel, portNumberField, startServer);
		startPane = new BorderPane();
		startPane.setPadding(new Insets(70));
		startPane.setCenter(portInfoBox);
		
		startScene = new Scene(startPane, 400,400);
		
		listItems = new ListView<String>();
		
		sceneMap = new HashMap<String, Scene>();
		sceneMap.put("server",  createServerGui());
		
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
	
	public Scene createServerGui() {
		
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: #001524");
		pane.setCenter(listItems);
		return new Scene(pane, 500, 400);

	}

}
