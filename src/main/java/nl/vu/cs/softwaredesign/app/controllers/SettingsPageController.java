package nl.vu.cs.softwaredesign.app.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import nl.vu.cs.softwaredesign.lib.annotations.CompressionType;
import nl.vu.cs.softwaredesign.lib.enumerations.CompressionLevel;
import nl.vu.cs.softwaredesign.lib.enumerations.SettingsValue;
import nl.vu.cs.softwaredesign.lib.handlers.CompressionHandler;
import nl.vu.cs.softwaredesign.lib.handlers.ConfigurationHandler;
import nl.vu.cs.softwaredesign.lib.handlers.KeyHandler;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
        return FXCollections.observableArrayList(CompressionHandler.getAvailableCompressionsType()
                .stream()
                .map(CompressionType::label)
                .collect(Collectors.toList()));
    }

    /**
     * Retrieves a list of compression levels for the combo box.
     * @return List of compression levels.
     */
    private ObservableList<String> getCompressionLevels() {
        return FXCollections.observableArrayList(Arrays.stream(CompressionLevel.values())
                .map(level -> level.label)
                .collect(Collectors.toList()));
    }

    /**
     * Sets the values of UI components based on saved configuration settings.
     */
    private void setSettingsValues() {
        CompressionHandler.getAvailableCompressionsType();

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

        try {
            configurationHandler.saveProperties();
            showAlert("Saved properties", "Successfully saved the properties");
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Saving properties", "Something went wrong trying to save the properties");
        }

    }

    /**
     * Resets the settings to the last saved state.
     */
    public void resetProperties() {
        try {
            configurationHandler.resetSavedProperties();
            setSettingsValues();
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Resetted properties", "Something went wrong trying to reset the properties");
        }
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

    public void resetEncryptionKey() {
        KeyHandler keyHandler = new KeyHandler();

        Runnable yes = () -> {
            try {
                keyHandler.createAndSetMainKey();
            } catch (IOException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        };

        showConfirmationDialog(Alert.AlertType.WARNING, "Reset encryption key",
                "Are you sure you want to reset your encryption key? You cannot decrypt your old archives",
                "Generate new key", "No, keep old key",
                yes, () -> {});
    }

    /**
     * Closes and resets the settings to the last saved state
     */
    public void cancelSettingsView() {
        resetProperties();
        closeCurrentWindow();
    }
}
