package nl.vu.cs.softwaredesign.app.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Abstract base class for JavaFX controllers providing common functionalities.
 */
public abstract class BaseController {
    protected Stage stage;

    /**
     * Sets the main stage for the controller.
     * @param stage The main stage to set.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Switches to a new scene specified by the FXML file.
     * @param fxmlPath The path to the FXML file for the new scene.
     */
    protected void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            BaseController newController = loader.getController();
            newController.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Opens a new window with the specified FXML file.
     * @param fxmlFileName The filename of the FXML file for the new window.
     */
    protected void openNewWindow(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/nl/vu/cs/softwaredesign/" + fxmlFileName));
            Parent root = loader.load();

            Stage newStage = new Stage();
            BaseController newController = loader.getController();
            newController.setStage(newStage);

            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(stage);
            newStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Closes the current window
     */
    protected void closeCurrentWindow() {
        stage.close();
    }

    /**
     * Shows an informational alert dialog.
     * @param title     The title of the alert.
     * @param message   The message to display in the alert.
     */
    protected void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows a confirmation dialog with title and message and actions for "Yes" and "No".
     * @param title     The title of the confirmation dialog.
     * @param message   The message to display in the confirmation dialog.
     * @param yesAction The action to perform when the user clicks "Yes"
     * @param noAction  The action to perform when the user clicks "No"
     */
    protected void showConfirmationDialog(String title, String message, Runnable yesAction, Runnable noAction) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                if (yesAction != null) {
                    yesAction.run();
                }
            } else if (response == noButton) {
                if (noAction != null) {
                    noAction.run();
                }
            }
        });
    }
}