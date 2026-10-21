package kz.studentgrades;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Main extends Application {
    @Override public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Scene scene = new Scene(loader.load(), 1050, 650);
        scene.getStylesheets().add(Main.class.getResource("styles.css").toExternalForm());
        stage.setTitle("Student Grade Calculator"); stage.setMinWidth(850); stage.setMinHeight(560);
        stage.setScene(scene); stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
