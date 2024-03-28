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
import nl.vu.cs.softwaredesign.lib.enumerations.SettingsValue;
import nl.vu.cs.softwaredesign.lib.handlers.CompressionHandler;
import nl.vu.cs.softwaredesign.lib.handlers.ConfigurationHandler;
import nl.vu.cs.softwaredesign.lib.handlers.KeyHandler;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.stream.Collectors;

public class SettingsPageController extends BaseController {
    ConfigurationHandler configurationHandler;

    @FXML
    private ComboBox<String> compressionFormatCombo;

    @FXML
    private Text compressOutputTxt, decompressOutputTxt;

    private static final String EMPTY_COMPRESS_OUTPUT = "No default compress output selected!";
    private static final String EMPTY_DECOMPRESS_OUTPUT = "No default decompress output selected!";

    /**
     * Sets upt the configuration handler and populates all fields with settings if they exists
     */
    @FXML
    public void initialize() {
        this.configurationHandler = ConfigurationHandler.getInstance();

        setDefaultSettings();
        setSettingsValues();
    }

    private void setDefaultSettings() {
        this.compressionFormatCombo.setItems(getCompressionFormats());
        this.compressionFormatCombo.getSelectionModel().select(0);

        compressOutputTxt.setText(EMPTY_COMPRESS_OUTPUT);
        decompressOutputTxt.setText(EMPTY_DECOMPRESS_OUTPUT);
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
     * Sets the values of UI components based on saved configuration settings.
     */
    private void setSettingsValues() {
        String format = configurationHandler.getProperty(SettingsValue.COMPRESSION_FORMAT);
        String compressOut = configurationHandler.getProperty(SettingsValue.DEFAULT_COMPRESS_OUTPUT);
        String decompressOut = configurationHandler.getProperty(SettingsValue.DEFAULT_DECOMPRESS_OUTPUT);

        if (format != null)
            this.compressionFormatCombo.setValue(format);

        if (compressOut != null)
            this.compressOutputTxt.setText(compressOut);

        if (decompressOut != null)
            this.decompressOutputTxt.setText(decompressOut);
    }

    public void clearCompressOutput() {
        configurationHandler.clearProperty(SettingsValue.DEFAULT_COMPRESS_OUTPUT);
        compressOutputTxt.setText(EMPTY_COMPRESS_OUTPUT);
    }

    public void clearDecompressOutput() {
        configurationHandler.clearProperty(SettingsValue.DEFAULT_DECOMPRESS_OUTPUT);
        decompressOutputTxt.setText(EMPTY_DECOMPRESS_OUTPUT);
    }

    public void chooseCompressOutput() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select default compress output");

        File outputFolder = directoryChooser.showDialog(stage);

        if (outputFolder != null) {
            configurationHandler.setProperty(SettingsValue.DEFAULT_COMPRESS_OUTPUT, outputFolder.getAbsolutePath());
            compressOutputTxt.setText(outputFolder.getAbsolutePath());
        }
    }

    public void chooseDecompressOutput() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select default decompress output");

        File outputFolder = directoryChooser.showDialog(stage);

        if (outputFolder != null) {
            configurationHandler.setProperty(SettingsValue.DEFAULT_DECOMPRESS_OUTPUT, outputFolder.getAbsolutePath());
            decompressOutputTxt.setText(outputFolder.getAbsolutePath());
        }
    }

    /**
     * Saves the current settings to the configuration file.
     */
    public void saveProperties() {
        configurationHandler.setProperty(SettingsValue.COMPRESSION_FORMAT, compressionFormatCombo.getValue());

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
