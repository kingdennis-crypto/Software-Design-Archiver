package nl.vu.cs.softwaredesign.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import nl.vu.cs.softwaredesign.app.utils.MetadataUtils;

import java.util.Map;

public class MetadataController extends BaseController {
    @FXML
    private TextField keyInput;

    @FXML
    private TextField valueInput;

    private MetadataUtils metadataUtils;

    @FXML
    public void initialize() {
        metadataUtils = MetadataUtils.getInstance();

        Map.Entry<String, String> selectedMetadata = metadataUtils.getSelectedEntry();

        if (selectedMetadata != null) {
            keyInput.setText(selectedMetadata.getKey());
            valueInput.setText(selectedMetadata.getValue());
        }
    }

    public void addMetadata() {
        metadataUtils.addKeyValue(keyInput.getText(), valueInput.getText());

        keyInput.clear();
        valueInput.clear();
    }

    public void closeMetadataWindow() {
        closeCurrentWindow();
    }
}
