package nl.vu.cs.softwaredesign.app.Controllers;

import javafx.fxml.FXML;
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
import nl.vu.cs.softwaredesign.lib.Models.FileArchive;

import java.io.File;
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

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            TreeItem<String> archiveItem = new TreeItem<>(selectedFile.getName(), IconUtils.createJavaFXIcon("archive.png"));
            treeViewTable.setRoot(archiveItem);
        }
    }

    public void deArchiveSelection() {
        System.out.println("De archiving the selection");
    }

    public void archiveSelection() {
        // Check which compression format is chosen in the configuration.
        //  Then open a file chooser with a filter for that compression format
        ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();

        Class<ICompressionFormat> compressionFormat = compressionHandler.getCompressionFormatByLabel(configurationHandler.getProperty(SettingsValue.COMPRESSION_FORMAT));

        System.out.println(compressionFormat.getClass());
        System.out.println(compressionFormat.getClass().getName());

        FileArchive archive = new FileArchive(selectedFolder);
        System.out.println("Archiving the selection");
    }
}
