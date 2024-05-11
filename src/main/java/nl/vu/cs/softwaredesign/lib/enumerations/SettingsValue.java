package nl.vu.cs.softwaredesign.lib.enumerations;

public enum SettingsValue {
    COMPRESSION_FORMAT("compression_format"),
    COMPRESSION_LEVEL("compression_level"),
    EXCLUDE_FILES("exclude_files"),
    DEFAULT_COMPRESS_OUTPUT("default_compress_output"),
    DEFAULT_DECOMPRESS_OUTPUT("default_decompress_output");

    public final String label;

    SettingsValue(String label) {
        this.label = label;
    }
}
