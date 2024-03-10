package nl.vu.cs.softwaredesign.app.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import nl.vu.cs.softwaredesign.lib.Enumerations.SettingsValue;
import nl.vu.cs.softwaredesign.lib.Handlers.ConfigurationHandler;

import java.io.File;

public class SettingsPageController extends BaseController {
    ConfigurationHandler configurationHandler;

    @FXML
    private ComboBox<String> compressionFormatCombo;

    @FXML
    private ComboBox<String> compressionLevelCombo;

    @FXML
    private TextField excludeFilesInput;

    @FXML
    private Text outputDirTxt;

    @FXML
    public void initialize() {
        this.configurationHandler = ConfigurationHandler.getInstance();

        this.compressionFormatCombo.setItems(getCompressionFormats());
        this.compressionLevelCombo.setItems(getCompressionLevels());

        setSettingsValues();
    }

    private ObservableList<String> getCompressionFormats() {
        ObservableList<String> items = FXCollections.observableArrayList();

        items.add("ZIP");
        items.add("RAR");

        return items;
    }

    private ObservableList<String> getCompressionLevels() {
        ObservableList<String> items = FXCollections.observableArrayList();

        items.add("Low");
        items.add("Medium");
        items.add("High");

        return items;
    }

    private void setSettingsValues() {
        String format = configurationHandler.getProperty(SettingsValue.COMPRESSION_FORMAT);
        String level = configurationHandler.getProperty(SettingsValue.COMPRESSION_LEVEL);
        String exclude = configurationHandler.getProperty(SettingsValue.EXCLUDE_FILES);
        String output = configurationHandler.getProperty(SettingsValue.DEFAULT_OUTPUT);

        this.compressionFormatCombo.setValue(format);
        this.compressionLevelCombo.setValue(level);
        this.excludeFilesInput.setText(exclude);
        this.outputDirTxt.setText(output);
    }

    public void saveProperties() {
        configurationHandler.setProperty(SettingsValue.COMPRESSION_FORMAT, compressionFormatCombo.getValue());
        configurationHandler.setProperty(SettingsValue.COMPRESSION_LEVEL, compressionLevelCombo.getValue());
        configurationHandler.setProperty(SettingsValue.EXCLUDE_FILES, excludeFilesInput.getText());
        configurationHandler.setProperty(SettingsValue.DEFAULT_OUTPUT, outputDirTxt.getText());

        configurationHandler.saveProperties();

        showAlert("Saved properties", "Successfully saved the properties");
    }

    public void resetProperties() {
        configurationHandler.resetSavedProperties();
        setSettingsValues();
    }

    public void selectOutputDir() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select folder");

        File outputFolder = directoryChooser.showDialog(stage);

        if (outputFolder != null) {
            outputDirTxt.setText(outputFolder.getAbsolutePath());
        }
    }
}
