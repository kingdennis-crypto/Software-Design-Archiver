package nl.vu.cs.softwaredesign.app.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.*;


public class MetadataController extends BaseController {
    @FXML
    private TextField dataKeyInput;

    @FXML
    private TextField dataValueInput;

    @FXML
    private ListView<String> metadataList;

    private Map<String, String> metadataMap;

    @FXML
    public void initialize() {
        metadataMap = new HashMap<>();
        metadataMap.put("Dennis", "Value 1");
        metadataMap.put("Simon", "Value 2");
        metadataMap.put("Joli-Coeur", "Value 3");
        metadataMap.put("Jaïr", "Value 4");

        setMetadataList();

        metadataList.setOnMouseClicked(this::handleOnClickList);
    }

    /**
     * Populates the contents of the metadata list view sorted alphabetically.
     */
    private void setMetadataList() {
        List<String> sortedKeys = new ArrayList<>(metadataMap.keySet());
        Collections.sort(sortedKeys);
        ObservableList<String> names = FXCollections.observableArrayList(sortedKeys);
        metadataList.setItems(names);
        clearInputs();
    }

    /**
     * Clears the key value input fields
     */
    private void clearInputs() {
        dataKeyInput.clear();
        dataValueInput.clear();
    }

    /**
     * Handles double-click event on the metadata list items.
     * Retrieves the selected key from the metadata list and updates the key value.
     * @param mouseEvent The MouseEvent representing the mouse click event.
     */
    private void handleOnClickList(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
            String metadataKey = metadataList.getSelectionModel().getSelectedItem();

            if (metadataKey != null) {
                String metadataValue = metadataMap.get(metadataKey);

                dataKeyInput.setText(metadataKey);
                dataValueInput.setText(metadataValue);
            }
        }
    }

    /**
     * Adds or updates the metadata based in the key value entered the input fields
     */
    public void setMetadata() {
        String key = dataKeyInput.getText().trim();
        String value = dataValueInput.getText().trim();

        if (key.isEmpty() || value.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "The key or value cannot be empty.");
            return;
        }

        metadataMap.put(dataKeyInput.getText(), dataValueInput.getText());
        setMetadataList();
        clearInputs();
        showAlert("Set metadata", "Successfully set metadata");
    }

    /**
     * Deletes the selected metadata entry and removes the key-value pair from the metadata map
     */
    public void deleteMetadata() {
        String selectedKey = metadataList.getSelectionModel().getSelectedItem();

        if (selectedKey == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select an metadata entry to delete.");
            return;
        }

        Runnable deleteEntry = () -> {
            metadataMap.remove(selectedKey);
            setMetadataList();
            showAlert("Delete metadata", "Metadata entry deleted successfully.");
            clearInputs();
        };

        showConfirmationDialog(Alert.AlertType.CONFIRMATION,
                "Confirmation",
                "Are you sure you want to delete the selected metadata entry?",
                "Delete",
                "Cancel",
                deleteEntry,
                () -> {});
    }

    /**
     * Closes the metadata window.
     */
    public void closeMetadataWindow() {
        closeCurrentWindow();
    }
}
