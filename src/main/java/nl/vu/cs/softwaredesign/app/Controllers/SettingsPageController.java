package nl.vu.cs.softwaredesign.app.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import nl.vu.cs.softwaredesign.lib.Annotations.CompressionType;
import nl.vu.cs.softwaredesign.lib.Enumerations.SettingsValue;
import nl.vu.cs.softwaredesign.lib.Handlers.CompressionHandler;
import nl.vu.cs.softwaredesign.lib.Handlers.ConfigurationHandler;

import java.io.File;
import java.util.stream.Collectors;

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

    /**
     * Sets upt the configuration handler and populates all fields with settings if they exists
     */
    @FXML
    public void initialize() {
        this.configurationHandler = ConfigurationHandler.getInstance();

        this.compressionFormatCombo.setItems(getCompressionFormats());
        this.compressionLevelCombo.setItems(getCompressionLevels());

        setSettingsValues();
    }

    /**
     * Retrieves a list of compression formats for the combo box.
     * @return List of compression formats.
     */
    private ObservableList<String> getCompressionFormats() {
        ObservableList<String> items = FXCollections.observableArrayList();

        items.addAll(CompressionHandler.getAvailableCompressions()
                .stream()
                .map(CompressionType::label)
                .collect(Collectors.toList()));

        return items;
    }

    /**
     * Retrieves a list of compression levels for the combo box.
     * @return List of compression levels.
     */
    private ObservableList<String> getCompressionLevels() {
        ObservableList<String> items = FXCollections.observableArrayList();

        items.add("Low");
        items.add("Medium");
        items.add("High");

        return items;
    }

    /**
     * Sets the values of UI components based on saved configuration settings.
     */
    private void setSettingsValues() {
        CompressionHandler.getAvailableCompressions();

        String format = configurationHandler.getProperty(SettingsValue.COMPRESSION_FORMAT);
        String level = configurationHandler.getProperty(SettingsValue.COMPRESSION_LEVEL);
        String exclude = configurationHandler.getProperty(SettingsValue.EXCLUDE_FILES);
        String output = configurationHandler.getProperty(SettingsValue.DEFAULT_OUTPUT);

        this.compressionFormatCombo.setValue(format);
        this.compressionLevelCombo.setValue(level);
        this.excludeFilesInput.setText(exclude);
        this.outputDirTxt.setText(output);
    }

    /**
     * Saves the current settings to the configuration file.
     */
    public void saveProperties() {
        configurationHandler.setProperty(SettingsValue.COMPRESSION_FORMAT, compressionFormatCombo.getValue());
        configurationHandler.setProperty(SettingsValue.COMPRESSION_LEVEL, compressionLevelCombo.getValue());
        configurationHandler.setProperty(SettingsValue.EXCLUDE_FILES, excludeFilesInput.getText());
        configurationHandler.setProperty(SettingsValue.DEFAULT_OUTPUT, outputDirTxt.getText());

        configurationHandler.saveProperties();

        showAlert("Saved properties", "Successfully saved the properties");
    }

    /**
     * Resets the settings to the last saved state.
     */
    public void resetProperties() {
        configurationHandler.resetSavedProperties();
        setSettingsValues();
    }

    /**
     * Opens a directory chooser to allow the user to select an output directory for the archive.
     */
    public void selectOutputDir() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select folder");

        File outputFolder = directoryChooser.showDialog(stage);

        if (outputFolder != null) {
            outputDirTxt.setText(outputFolder.getAbsolutePath());
        }
    }

    /**
     * Closes and resets the settings to the last saved state
     */
    public void cancelSettingsView() {
        resetProperties();
        closeCurrentWindow();
    }
}
