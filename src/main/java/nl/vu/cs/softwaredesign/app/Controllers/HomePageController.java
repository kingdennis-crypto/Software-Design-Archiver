package nl.vu.cs.softwaredesign.app.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomePageController extends BaseController {
    @FXML
    public void initialize() { }

    public void showHomeAlert() {
        showAlert("Home Alert", "Hello from the home screen");
    }

    public void showHomeDialog() {
        Runnable yes = () -> System.out.println("Yes");
        Runnable no = () -> System.out.println("No");

        showConfirmationDialog("Home dialog", "This is a simple yes no field", yes, no);
    }

    public void openSettingsPage() {
        openNewWindow("nl/vu/cs/softwaredesign/settings-view.fxml");
    }
}
