package nl.vu.cs.softwaredesign.app.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import nl.vu.cs.softwaredesign.app.Utils.IconUtils;
import nl.vu.cs.softwaredesign.lib.Annotations.CompressionType;
import nl.vu.cs.softwaredesign.lib.Enumerations.SettingsValue;
import nl.vu.cs.softwaredesign.lib.Handlers.CompressionHandler;
import nl.vu.cs.softwaredesign.lib.Handlers.ConfigurationHandler;
import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;
import nl.vu.cs.softwaredesign.lib.Models.ContentExtractor;
import nl.vu.cs.softwaredesign.lib.Models.ContentInserter;
import nl.vu.cs.softwaredesign.lib.Models.FileArchive;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HomePageController extends BaseController {

    @FXML
    private TreeView<String> treeViewTable;

    private File selectedFolder;
    private CompressionHandler compressionHandler;

    @FXML
    public void initialize() {
        clearSelectedFolder();
        this.compressionHandler = new CompressionHandler();
    }

    public void openSettingsPage() {
        openNewWindow("settings-view.fxml");
    }

    public void openReportPage() {
        openNewWindow("report-view.fxml");
    }

    public void openTestPage() {
        openNewWindow("test-view.fxml");
    }

    public void openMetadataPage() {
        openNewWindow("metadata-view.fxml");
    }

    public void clearSelectedFolder() {
        this.selectedFolder = null;
        TreeItem<String> rootItem = new TreeItem<>("No folder chosen");
        rootItem.setExpanded(true);
        treeViewTable.setRoot(rootItem);
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
            TreeItem<String> archiveItem = new TreeItem<>(selectedFolder.getName(), IconUtils.createJavaFXIcon("archive.png"));
            treeViewTable.setRoot(archiveItem);
        }
    }

    public void deArchiveSelection() {
        ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();

        Class<ICompressionFormat> compressionFormat = compressionHandler.getCompressionFormatByLabel(configurationHandler.getProperty(SettingsValue.COMPRESSION_FORMAT));

        FileArchive archive = new FileArchive(selectedFolder);

        try {
            String filepath = archive.getROOT().getAbsolutePath().replaceAll(".zip", "");
            ContentExtractor.extractContents(compressionFormat.getDeclaredConstructor().newInstance(), archive, filepath, "pwd");

            clearSelectedFolder();
            showAlert(Alert.AlertType.INFORMATION, "Decryption", "Successfully decrypted your archive");
        } catch (InvalidObjectException ex) {
            showAlert(Alert.AlertType.ERROR, "Decryption error", "The password you provided was incorrect!");
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Decryption error", "Something went wrong during the decryption!");
        }
    }

    public void archiveSelection() {
        // Check which compression format is chosen in the configuration.
        //  Then open a file chooser with a filter for that compression format
        ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();

        Class<ICompressionFormat> compressionFormat = compressionHandler.getCompressionFormatByLabel(configurationHandler.getProperty(SettingsValue.COMPRESSION_FORMAT));

        System.out.println(compressionFormat.getClass());
        System.out.println(compressionFormat.getClass().getName());

        FileArchive archive = new FileArchive(selectedFolder);
        archive.addMetadata("password", "pwd");
        System.out.println("Archiving the selection");

        try {
            ContentInserter.insertContents(compressionFormat.getDeclaredConstructor().newInstance(), archive, archive.getROOT().getAbsolutePath() + ".zip");

            clearSelectedFolder();
            showAlert(Alert.AlertType.INFORMATION, "Encryption", "Successfully encrypted your archive");
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Encryption error", "Something went wrong during the encryption!");
        }

    }
}
