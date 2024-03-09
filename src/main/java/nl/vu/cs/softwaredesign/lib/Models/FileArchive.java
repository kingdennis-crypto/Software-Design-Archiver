package nl.vu.cs.softwaredesign.lib.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileArchive {
    public List<Folder> folders;
    public Map<String, String> metadata;

    public FileArchive() {
        this.folders = new ArrayList<>();
        this.metadata = new HashMap<>();
    }

    public void addFolder(Folder folder) {
        this.folders.add(folder);
    }

    public void  addMetadata(String key, String value) {
        this.metadata.put(key, value);
    }

    public List<Folder> getFolders() {
        return this.folders;
    }

    public Map<String, String> getMetadata() {
        return this.metadata;
    }

    public String getMetadataByKey(String key) {
        return this.metadata.get(key);
    }
}
