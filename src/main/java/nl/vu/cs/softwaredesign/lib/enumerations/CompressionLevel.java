package nl.vu.cs.softwaredesign.lib.enumerations;

public enum CompressionLevel {
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low");

    public final String label;

    CompressionLevel(String label) {
        this.label = label;
    }
}
