package nl.vu.cs.softwaredesign.app.Controllers;

import javafx.animation.KeyValue;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableMapValue;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import nl.vu.cs.softwaredesign.app.Utils.IconUtils;
import nl.vu.cs.softwaredesign.lib.Enumerations.SettingsValue;
import nl.vu.cs.softwaredesign.lib.Handlers.CompressionHandler;
import nl.vu.cs.softwaredesign.lib.Handlers.ConfigurationHandler;
import nl.vu.cs.softwaredesign.lib.Handlers.EncryptionHandler;
import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;
import nl.vu.cs.softwaredesign.lib.Models.ContentExtractor;
import nl.vu.cs.softwaredesign.lib.Models.ContentInserter;
import nl.vu.cs.softwaredesign.lib.Models.FileArchive;

import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HomePageController extends BaseController {

    @FXML
    private TreeView<String> treeViewTable;

    @FXML
    private TextField pwdInput;

    @FXML
    private CheckBox includePwdCheckbox;

    @FXML
    private Button clearBtn;

    @FXML
    private Button deArchiveBtn;

    @FXML
    private Button archiveBtn;

    private File selectedFolder;
    private CompressionHandler compressionHandler;

    @FXML
    public void initialize() {
        pwdInput.visibleProperty().bind(includePwdCheckbox.selectedProperty());

        clearSelectedFolder();
        this.compressionHandler = new CompressionHandler();
    }

    public void openSettingsPage() {
        openNewWindow("settings-view.fxml");
    }

    public void clearSelectedFolder() {
        this.selectedFolder = null;
        TreeItem<String> rootItem = new TreeItem<>("No folder chosen");
        rootItem.setExpanded(true);
        treeViewTable.setRoot(rootItem);

        includePwdCheckbox.setSelected(false);
        includePwdCheckbox.setDisable(true);

        pwdInput.clear();

        deArchiveBtn.setDisable(true);
        archiveBtn.setDisable(true);
        clearBtn.setDisable(true);
    }

    private void makeTreeItem(TreeItem<String> treeItem, File rootFile) {
        Objects.requireNonNull(rootFile.listFiles());

        for (File file : Objects.requireNonNull(rootFile.listFiles())) {
            if (file.isFile()) {
                TreeItem<String> fileItem = new TreeItem<>(file.getName());
                fileItem.setGraphic(IconUtils.createJavaFXIcon("file.png"));
                treeItem.getChildren().add(fileItem);
            } else {
                TreeItem<String> nestedItem = new TreeItem<>(file.getName(), IconUtils.createJavaFXIcon("folder.png"));
                makeTreeItem(nestedItem, file);
                treeItem.getChildren().add(nestedItem);
            }
        }

    }

    public void chooseFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select folder");

        selectedFolder = directoryChooser.showDialog(stage);

        if (selectedFolder != null) {
            TreeItem<String> folderItem = new TreeItem<>(selectedFolder.getName(), IconUtils.createJavaFXIcon("folder.png"));
            makeTreeItem(folderItem, selectedFolder);
            treeViewTable.setRoot(folderItem);

            clearBtn.setDisable(false);
            archiveBtn.setDisable(false);
            deArchiveBtn.setDisable(true);

            includePwdCheckbox.setSelected(false);
            includePwdCheckbox.setDisable(false);
        }
    }

    public void chooseArchive() {
        List<String> extensions = compressionHandler.getCompressionExtensions().stream().map(s -> "*" + s).collect(Collectors.toList());
        String extensionFormat = String.join(", ", extensions);
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(String.format("Archives (%s)", extensionFormat), extensions);
        fileChooser.getExtensionFilters().add(filter);

        selectedFolder = fileChooser.showOpenDialog(stage);

        if (selectedFolder != null) {
            TreeItem<String> archiveItem = new TreeItem<>(selectedFolder.getName() + " (preview)", IconUtils.createJavaFXIcon("archive.png"));
            treeViewTable.setRoot(archiveItem);

            boolean hasPassword = EncryptionHandler.isPasswordProtected(selectedFolder.getAbsolutePath());

            includePwdCheckbox.setDisable(true);
            includePwdCheckbox.setSelected(hasPassword);

            clearBtn.setDisable(false);
            archiveBtn.setDisable(true);
            deArchiveBtn.setDisable(false);

            Map<String, String> archiveMetadata = EncryptionHandler.readMetadataFromFile(selectedFolder.getAbsolutePath());

            if (archiveMetadata != null) {
                populateTreeViewFromMetadata(archiveMetadata.get("content"), archiveItem);
            }
        }
    }

    private void populateTreeViewFromMetadata(String metadataString, TreeItem<String> parentItem) {
        String[] lines = metadataString.split("\n");

        TreeItem<String> currentParent = parentItem;

        for (String line : lines) {
            String itemName = line.trim().replaceAll("[\\[\\]-]", "");
            if (line.matches("^\\s*\\[.*]$")) {
                TreeItem<String> directoryItem = new TreeItem<>(itemName, IconUtils.createJavaFXIcon("folder.png"));
                currentParent.getChildren().add(directoryItem);
                currentParent = directoryItem;
            } else if (line.matches("^\\s*- .*")) {
                currentParent.getChildren().add(new TreeItem<>(itemName, IconUtils.createJavaFXIcon("file.png")));
            }
        }
    }

    public void deArchiveSelection() {
        boolean hasPassword = EncryptionHandler.isPasswordProtected(selectedFolder.getAbsolutePath());

        if (hasPassword && pwdInput.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Password", "No password was provided!");
            return;
        }

        ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();

        Class<ICompressionFormat> compressionFormat = compressionHandler.getCompressionFormatByLabel(configurationHandler.getProperty(SettingsValue.COMPRESSION_FORMAT));

        FileArchive archive = new FileArchive(selectedFolder);

        try {
            // TODO: Add way to dynamically change the extension
            String filepath = archive.getROOT().getAbsolutePath().replaceAll(".zip", "");
            FileArchive deCompressed = ContentExtractor.extractContents(compressionFormat.getDeclaredConstructor().newInstance(), archive, filepath, pwdInput.getText());

            clearSelectedFolder();
            showAlert(Alert.AlertType.INFORMATION, "Decompress", String.format("Successfully decompressed your archive at: \n%s", deCompressed.getROOT().getAbsolutePath()));
        } catch (InvalidObjectException ex) {
            showAlert(Alert.AlertType.ERROR, "Decompression error", "The password you provided was incorrect!");
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Decompression error", "Something went wrong during the decryption!");
        }
    }

    public void archiveSelection() {
        ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();

        Class<ICompressionFormat> compressionFormat = compressionHandler.getCompressionFormatByLabel(configurationHandler.getProperty(SettingsValue.COMPRESSION_FORMAT));

        FileArchive archive = new FileArchive(selectedFolder);

        if (includePwdCheckbox.isSelected()) {
            archive.addMetadata("password", pwdInput.getText());
        }

        try {
            FileArchive compressed = ContentInserter.insertContents(compressionFormat.getDeclaredConstructor().newInstance(), archive, archive.getROOT().getAbsolutePath() + ".zip");

            clearSelectedFolder();
            showAlert(Alert.AlertType.INFORMATION, "Compressed", String.format("Successfully compressed your archive at: \n%s", compressed.getROOT().getAbsolutePath()));
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Compression error", "Something went wrong during the encryption!");
        }
    }
}
