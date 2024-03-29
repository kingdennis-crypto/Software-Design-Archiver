package nl.vu.cs.softwaredesign.app.controllers;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import nl.vu.cs.softwaredesign.app.utils.IconUtils;
import nl.vu.cs.softwaredesign.app.utils.MetadataUtils;
import nl.vu.cs.softwaredesign.lib.annotations.CompressionType;
import nl.vu.cs.softwaredesign.lib.enumerations.SettingsValue;
import nl.vu.cs.softwaredesign.lib.handlers.ArchiveHandler;
import nl.vu.cs.softwaredesign.lib.handlers.CompressionHandler;
import nl.vu.cs.softwaredesign.lib.handlers.ConfigurationHandler;
import nl.vu.cs.softwaredesign.lib.handlers.EncryptionHandler;
import nl.vu.cs.softwaredesign.lib.interfaces.ICompressionFormat;
import nl.vu.cs.softwaredesign.lib.models.FileArchive;

import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.InvalidObjectException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HomePageController extends BaseController {
    private static final String ARCHIVE_PICTURE = "archive.png";
    private static final String FOLDER_PICTURE = "folder.png";
    private static final String LABEL_KEY = "label";
    private static final String EXTENSION_KEY = "extension";
    private static final String CONTENT_KEY = "content";
    private static final String PASSWORD_KEY = "password";

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
    @FXML
    private Button addMetadataBtn;
    @FXML
    private MenuItem settingsMenuItem;
    @FXML
    private MenuItem logsMenuItem;
    @FXML
    private MenuItem selectFolderMenuItem;
    @FXML
    private MenuItem selectArchiveMenuItem;
    @FXML
    private ListView<String> metadataListView;

    private MetadataUtils metadataUtils;
    private File selectedFolder;
    private CompressionHandler compressionHandler;

    @FXML
    public void initialize() {
        this.compressionHandler = new CompressionHandler();
        this.metadataUtils = MetadataUtils.getInstance();

        pwdInput.visibleProperty().bind(includePwdCheckbox.selectedProperty());
        clearSelectedFolder();
        initializeMetadataListView();
        initializeIcons();
    }

    private void initializeIcons() {
        archiveBtn.setGraphic(IconUtils.createJavaFXIcon(ARCHIVE_PICTURE));
        deArchiveBtn.setGraphic(IconUtils.createJavaFXIcon("dearchive.png"));

        settingsMenuItem.setGraphic(IconUtils.createJavaFXIcon("settings.png"));
        logsMenuItem.setGraphic(IconUtils.createJavaFXIcon("logs.png"));
        selectArchiveMenuItem.setGraphic(IconUtils.createJavaFXIcon(ARCHIVE_PICTURE));
        selectFolderMenuItem.setGraphic(IconUtils.createJavaFXIcon(FOLDER_PICTURE));
    }

    private void initializeMetadataListView() {
        ObservableMap<String, String> metadata = metadataUtils.getMetadata();
        metadataListView.setItems(FXCollections.observableArrayList(metadata.keySet()));

        metadata.addListener((MapChangeListener<String, String>) change -> metadataListView.setItems(FXCollections.observableArrayList(metadata.keySet())));

        metadataListView.setOnMouseClicked(this::handleMetadataListDoubleClick);
    }

    private void handleMetadataListDoubleClick(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
            String selectedItem = metadataListView.getSelectionModel().getSelectedItem();

            if (selectedItem != null && !selectedItem.isEmpty()) {
                metadataUtils.setSelectedEntry(selectedItem);
                openMetadataPage();
            }
        }
    }

    public void openSettingsPage() {
        openNewWindow("settings-view.fxml");
    }

    public void openLogsPage() {
        openNewWindow("log-view.fxml");
    }

    public void openMetadataPage() {
        openNewWindow("metadata-view.fxml");
    }

    public void clearSelectedFolder() {
        selectedFolder = null;
        TreeItem<String> rootItem = new TreeItem<>("No folder chosen");
        rootItem.setExpanded(true);
        treeViewTable.setRoot(rootItem);

        includePwdCheckbox.setSelected(false);
        includePwdCheckbox.setDisable(true);

        pwdInput.clear();

        deArchiveBtn.setDisable(true);
        archiveBtn.setDisable(true);
        clearBtn.setDisable(true);

        metadataUtils.clear();
        addMetadataBtn.setDisable(true);
        metadataListView.setDisable(true);
    }

    private void makeTreeItem(TreeItem<String> treeItem, File rootFile) {
        Objects.requireNonNull(rootFile.listFiles());

        for (File file : Objects.requireNonNull(rootFile.listFiles())) {
            if (file.isFile()) {
                TreeItem<String> fileItem = new TreeItem<>(file.getName());
                fileItem.setGraphic(IconUtils.createJavaFXIcon("file.png"));
                treeItem.getChildren().add(fileItem);
            } else {
                TreeItem<String> nestedItem = new TreeItem<>(file.getName(), IconUtils.createJavaFXIcon(FOLDER_PICTURE));
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
            TreeItem<String> folderItem = new TreeItem<>(selectedFolder.getName(), IconUtils.createJavaFXIcon(FOLDER_PICTURE));
            makeTreeItem(folderItem, selectedFolder);
            treeViewTable.setRoot(folderItem);

            clearBtn.setDisable(false);
            archiveBtn.setDisable(false);
            deArchiveBtn.setDisable(true);

            includePwdCheckbox.setSelected(false);
            includePwdCheckbox.setDisable(false);
            addMetadataBtn.setDisable(false);
            metadataListView.setDisable(false);
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
            TreeItem<String> archiveItem = new TreeItem<>(selectedFolder.getName() + " (preview)", IconUtils.createJavaFXIcon(ARCHIVE_PICTURE));
            treeViewTable.setRoot(archiveItem);

            boolean hasPassword = EncryptionHandler.isPasswordProtected(selectedFolder.getAbsolutePath());

            includePwdCheckbox.setDisable(true);
            includePwdCheckbox.setSelected(hasPassword);

            clearBtn.setDisable(false);
            archiveBtn.setDisable(true);
            deArchiveBtn.setDisable(false);

            addMetadataBtn.setDisable(true);
            metadataListView.setDisable(false);

            Map<String, String> archiveMetadata = EncryptionHandler.readMetadataFromFile(selectedFolder.getAbsolutePath());

            if (archiveMetadata != null) {
                populateTreeViewFromMetadata(archiveMetadata.get(CONTENT_KEY), archiveItem);

                // Remove unnecessary metadata
                archiveMetadata.remove(CONTENT_KEY);
                archiveMetadata.remove(PASSWORD_KEY);
                archiveMetadata.remove(EXTENSION_KEY);
                archiveMetadata.remove(LABEL_KEY);
                metadataUtils.addKeyValue(archiveMetadata);
            }
        }
    }

    private void populateTreeViewFromMetadata(String metadataString, TreeItem<String> parentItem) {
        String[] lines = metadataString.split("\n");

        TreeItem<String> currentParent = parentItem;

        for (String line : lines) {
            String itemName = line.trim().replaceAll("[\\[\\]-]", "");
            if (line.matches("^\\s*\\[.*]$")) {
                TreeItem<String> directoryItem = new TreeItem<>(itemName, IconUtils.createJavaFXIcon(FOLDER_PICTURE));
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

        Map<String, String> archiveMetadata = EncryptionHandler.readMetadataFromFile(selectedFolder.getAbsolutePath());
        Class<ICompressionFormat> compressionFormat = compressionHandler.getCompressionFormatByLabel(archiveMetadata.get(LABEL_KEY));
        FileArchive archive = new FileArchive(selectedFolder);

        try {
            String destinationPath = selectedFolder.getAbsolutePath().replaceAll(archiveMetadata.get(EXTENSION_KEY), "");

            FileArchive deCompressed = ArchiveHandler.extractContents(compressionFormat.getDeclaredConstructor().newInstance(), archive, destinationPath, pwdInput.getText());

            clearSelectedFolder();
            showAlert(Alert.AlertType.INFORMATION, "Decompress", "Successfully decompressed your archive at: \n" + deCompressed.getROOT().getAbsolutePath());
        } catch (InvalidObjectException ex) {
            showAlert(Alert.AlertType.ERROR, "Decompression error", "The password you provided was incorrect!");
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Decompression error", "Something went wrong during the decryption!");
        }
    }

    public void archiveSelection() {
        ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();
        Class<ICompressionFormat> compressionFormat = compressionHandler.getCompressionFormatByLabel(configurationHandler.getProperty(SettingsValue.COMPRESSION_FORMAT));
        String extension = compressionHandler.getExtensionByLabel(configurationHandler.getProperty(SettingsValue.COMPRESSION_FORMAT));

        FileArchive archive = new FileArchive(selectedFolder);
        archive.addMetadata(metadataUtils.getMetadata());
        archive.addMetadata(EXTENSION_KEY, extension);
        archive.addMetadata(LABEL_KEY, compressionFormat.getAnnotation(CompressionType.class).label());

        if (includePwdCheckbox.isSelected()) {
            archive.addMetadata(PASSWORD_KEY, pwdInput.getText());
        }

        try {
            FileArchive compressed = ArchiveHandler.insertContents(compressionFormat.getDeclaredConstructor().newInstance(), archive, archive.getROOT().getAbsolutePath() + extension);

            clearSelectedFolder();
            showAlert(Alert.AlertType.INFORMATION, "Compressed", "Successfully compressed your archive at: \n" + compressed.getROOT().getAbsolutePath());
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Compression error", "Something went wrong during the encryption!");
        }
    }
}
