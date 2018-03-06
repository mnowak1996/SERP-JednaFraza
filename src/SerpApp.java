import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Created  by Michal Nowak
 **/

public class SerpApp extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SerpApp.class.getResource("Serp.fxml"));
            Parent layout = fxmlLoader.load();
            Scene scene = new Scene(layout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Serp - jedna fraza");
            scene.getStylesheets().add("style.css");
            primaryStage.show();
        }catch(Exception ex){
            System.out.print(ex.getMessage());
        }
    }
}
