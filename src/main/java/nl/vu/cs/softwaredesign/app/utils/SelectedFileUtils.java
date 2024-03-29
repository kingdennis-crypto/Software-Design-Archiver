package nl.vu.cs.softwaredesign.app.utils;

import java.io.File;

public class SelectedFileUtils {
    private static SelectedFileUtils instance;
    private File selectedFile;

    private SelectedFileUtils() {
        this.selectedFile = null;
    }

    public static SelectedFileUtils getInstance() {
        if (SelectedFileUtils.instance == null) {
            SelectedFileUtils.instance = new SelectedFileUtils();
        }

        return SelectedFileUtils.instance;
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }
}
