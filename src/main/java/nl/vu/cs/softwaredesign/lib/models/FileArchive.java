package nl.vu.cs.softwaredesign.lib.models;

import java.io.File;
import java.util.*;

/**
 * Represents an archive of files and folders and provides metadata.
 */
public class FileArchive {
    private final File root;
    private final Map<String, String> metadata;

    public FileArchive(File root) {
        this.root = root;
        this.metadata = new HashMap<>();
    }

    public FileArchive(File root, Map<String, String> metadata) {
        this.root = root;
        this.metadata = metadata;
    }

    /**
     * Adds metadata to the file archive.
     * @param key   The key for the metadata entry.
     * @param value The value associated with the key.
     */
    public void addMetadata(String key, String value) {
        this.metadata.put(key, value);
    }

    public void addMetadata(Map<String, String> values) {
        this.metadata.putAll(values);
    }

    /**
     * Gets the metadata associated with the archive.
     * @return A map containing the metadata.
     */
    public Map<String, String> getMetadata() {
        return this.metadata;
    }

    /**
     * Gets the value associated with a specific metadata key.
     * @param key The key for the metadata entry.
     * @return The value associated with the specified key, or null if the key is not present
     */
    public String getMetadataByKey(String key) {
        return this.metadata.get(key);
    }

    /**
     * Gets the total number of files in the file archive.
     * @return The number of files in the file archive.
     */
    public int getFileAmount() {
        return Objects.requireNonNull(this.root.listFiles()).length;
    }

    /**
     * Gets the total size of all files in the file archive in bytes.
     * @return The total size of all files in bytes.
     */
    private double getSizeInBytes() {
        return Arrays.stream(Objects.requireNonNull(this.root.listFiles()))
                .filter(File::isFile)
                .mapToLong(File::length)
                .sum();
    }

    /**
     * Gets the root directory of the file archive.
     * @return The root directory of the file archive.
     */
    public File getROOT() {
        return root;
    }

    /**
     * Gets the total size of all files in the file archive in kilobytes.
     * @return The total size of all files in kilobytes.
     */
    public double getSizeInKiloBytes() {
        return this.getSizeInBytes() / 1024.0;
    }

    /**
     * Gets the total size of all files in the file archive in megabytes.
     * @return The total size of all files in megabytes.
     */
    public double getSizeInMegaBytes() {
        return this.getSizeInBytes() / (1024.0 * 1024.0);
    }

    public String generateFileRepresentation() {
        StringBuilder stringBuilder = new StringBuilder();
        generateFileRepresentationHelper(getROOT(), 0, stringBuilder);
        return stringBuilder.toString();
    }

    private void generateFileRepresentationHelper(File directory, int depth, StringBuilder stringBuilder) {
        if (directory.isDirectory()) {
            stringBuilder
                    .append(getIndent(depth))
                    .append("[")
                    .append(directory.getName())
                    .append("]")
                    .append("\n");

            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    generateFileRepresentationHelper(file, depth + 1, stringBuilder);
                }
            }
        } else {
            stringBuilder
                    .append(getIndent(depth))
                    .append("- ")
                    .append(directory.getName())
                    .append("\n");
        }
    }

    private String getIndent(int depth) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            indent.append(" ");
        }
        return indent.toString();
    }
}
