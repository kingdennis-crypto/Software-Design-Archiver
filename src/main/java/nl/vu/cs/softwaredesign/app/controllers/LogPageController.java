package nl.vu.cs.softwaredesign.app.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import nl.vu.cs.softwaredesign.lib.singletons.ProgressLogger;

public class LogPageController extends BaseController {
    @FXML
    public ListView<String> logListView;

    @FXML
    public void initialize() {
        this.getLogData();
    }

    public void getLogData() {
        ProgressLogger progressLogger = ProgressLogger.getInstance();
        ObservableList<String> observableList = FXCollections.observableArrayList(progressLogger.readLogFile(20));
        logListView.setItems(observableList);

        // Scroll to bottom
        logListView.scrollTo(logListView.getItems().size() - 1);
    }

    public void closeLogWindow() {
        closeCurrentWindow();
    }
}
