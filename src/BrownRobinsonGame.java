import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BrownRobinsonGame extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent rootNode = FXMLLoader.load(getClass().getResource("BrownRobinsonGame.fxml"));
        Scene scene = new Scene(rootNode);

        primaryStage.setTitle("Brown - Robinson method");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
