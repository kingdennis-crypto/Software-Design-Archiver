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
     *
     * @param key   The key for the metadata entry.
     * @param value The value associated with the key.
     */
    public void addMetadata(String key, String value) {
        this.metadata.put(key, value);
    }

    /**
     * Adds multiple metadata entries to the file archive using the provided map of key-value pairs.
     *
     * @param values the map containing metadata entries to be added.
     */
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
     * Generates a string representation of the file structure starting from the root directory.
     * Each directory and file is represented with appropriate indentation to reflect its hierarchical position.
     *
     * @return A string representation of the file structure.
     */
    public String generateFileRepresentation() {
        StringBuilder stringBuilder = new StringBuilder();
        generateFileRepresentationHelper(getROOT(), 0, stringBuilder);
        return stringBuilder.toString();
    }

    /**
     * Helper method to recursively generate the file representation.
     *
     * @param directory     The current directory or file.
     * @param depth         The depth of the directory in the file structure.
     * @param stringBuilder The StringBuilder to append the representation to.
     */
    private void generateFileRepresentationHelper(File directory, int depth, StringBuilder stringBuilder) {
        if (directory.isDirectory()) {
            stringBuilder
                    .append(getIndent(depth))
                    .append("[")
                    .append(directory.getName())
                    .append("]")
                    .append(System.lineSeparator());

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
                    .append(System.lineSeparator());
        }
    }

    /**
     * Generates indentation based on the depth of the directory in the file structure.
     *
     * @param depth The depth of the directory.
     * @return      The indentation string.
     */
    private String getIndent(int depth) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            indent.append(" ");
        }
        return indent.toString();
    }
}
