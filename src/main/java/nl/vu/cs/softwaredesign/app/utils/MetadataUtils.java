package nl.vu.cs.softwaredesign.app.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.HashMap;
import java.util.Map;

public class MetadataUtils {
    private static MetadataUtils instance;
    private ObservableMap<String, String> metadata;
    private Map.Entry<String, String> selectedEntry;

    private MetadataUtils() {
        this.metadata = FXCollections.observableMap(new HashMap<>());
    }

    public static MetadataUtils getInstance() {
        if (MetadataUtils.instance == null) {
            MetadataUtils.instance = new MetadataUtils();
        }

        return MetadataUtils.instance;
    }

    public void addKeyValue(String key, String value) {
        this.metadata.put(key, value);
    }

    public void addKeyValue(Map<String, String> map) {
        this.metadata.putAll(map);
    }

    public void clear() {
        this.metadata.clear();
    }

    public ObservableMap<String, String> getMetadata() {
        return this.metadata;
    }

    public void setSelectedEntry(String key) {
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            if (entry.getKey().equals(key)) {
                selectedEntry = entry;
            }
        }
    }

    public Map.Entry<String, String> getSelectedEntry() {
        return this.selectedEntry;
    }

    public void clearSelectedEntry() {
        this.selectedEntry = null;
    }
}
