package nl.vu.cs.softwaredesign.lib.Models;

import java.io.File;
import java.util.List;

public class Folder {
    public List<File> files;
    public List<Folder> folders;

    public Folder() { }

    public void addFile(File file) {
        this.files.add(file);
    }

    public void addFiles(List<File> files) {
        this.files.addAll(files);
    }

    public void addFolder(Folder folder) {
        this.folders.add(folder);
    }

    public void addFolders(List<Folder> folders) {
        this.folders.addAll(folders);
    }

    public List<File> getFiles() {
        return this.files;
    }

    public List<Folder> getFolders() {
        return this.folders;
    }
}
