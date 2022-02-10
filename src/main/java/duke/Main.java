package duke;

import java.io.IOException;
import java.net.URL;

import duke.components.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Duke using FXML.
 */
public class Main extends Application {

    private Duke duke = new Duke("data/duke.txt");

    @Override
    public void start(Stage stage) {
        try {
            // Checks if /view/MainWindow.fxml file exists
            URL mainWindowFile = Main.class.getResource("/view/MainWindow.fxml");
            assert mainWindowFile != null : "Main Window File not found";
            // Loads the Main Window.
            FXMLLoader fxmlLoader = new FXMLLoader(mainWindowFile);
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setDuke(duke);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
