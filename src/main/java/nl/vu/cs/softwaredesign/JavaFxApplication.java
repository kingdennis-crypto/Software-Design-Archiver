package nl.vu.cs.softwaredesign;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.vu.cs.softwaredesign.app.Utils.MetadataUtils;
import nl.vu.cs.softwaredesign.lib.Handlers.ConfigurationHandler;

import java.io.IOException;

public class JavaFxApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Initialize singletons
        ConfigurationHandler.getInstance();
        MetadataUtils.getInstance();

        FXMLLoader loader = new FXMLLoader(JavaFxApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setTitle("Archiver");
        stage.setScene(scene);
        stage.show();
    }

    public static void main (String[] args){
        launch();
    }
}
