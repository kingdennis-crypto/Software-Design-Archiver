package nl.vu.cs.softwaredesign.lib.enumerations;

public enum SettingsValue {
    COMPRESSION_FORMAT("compression_format"),
    COMPRESSION_LEVEL("compression_level"),
    EXCLUDE_FILES("exclude_files"),
    DEFAULT_OUTPUT("default_output_dir");

    public final String label;

    SettingsValue(String label) {
        this.label = label;
    }
}
