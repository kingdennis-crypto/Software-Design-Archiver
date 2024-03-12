package nl.vu.cs.softwaredesign.app.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.DirectoryChooser;
import nl.vu.cs.softwaredesign.app.Utils.IconUtils;
import nl.vu.cs.softwaredesign.lib.Models.FileArchive;

import java.io.File;
import java.util.Objects;

public class HomePageController extends BaseController {

    @FXML
    private TreeView<String> treeViewTable;

    private File selectedFolder;

    @FXML
    public void initialize() {
        clearSelectedFolder();
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
        System.out.println("Choosing archive");
    }

    public void deArchiveSelection() {
        System.out.println("De archiving the selection");
    }

    public void archiveSelection() {
        FileArchive archive = new FileArchive(selectedFolder);
        System.out.println("Archiving the selection");
    }
}
