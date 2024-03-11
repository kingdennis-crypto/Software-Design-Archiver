package nl.vu.cs.softwaredesign.app.Controllers;

import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class TestPageController extends BaseController {
    @FXML
    public void initialize() { }

    public void showHomeAlert() {
        showAlert("Home Alert", "Hello from the home screen");
    }

    public void showHomeDialog() {
        Runnable yes = () -> System.out.println("Yes");
        Runnable no = () -> System.out.println("No");

        showConfirmationDialog("Test dialog", "This is a simple yes no field", "Yes", "No", yes, no);
    }

    public void openSettingsPage() {
        openNewWindow("nl/vu/cs/softwaredesign/settings-view.fxml");
    }

    public void openFileSelector() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select folders");

        File selectedFolder = directoryChooser.showDialog(stage);

        // Print the selected folders
        if (selectedFolder != null) {
            System.out.println("Selected Folder: " + selectedFolder.getAbsolutePath());
        } else {
            System.out.println("No folders selected");
        }
    }
}
