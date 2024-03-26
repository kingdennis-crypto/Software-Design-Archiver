package nl.vu.cs.softwaredesign.lib.enumerations;

/**
 * Status of the progress process
 * COMPILING: the process is still running
 * FINISHED: the process has finished
 */
public enum Status {
    COMPILING("Compiling"),
    FINISHED("Finished");

    public final String label;

    Status(String label) {this.label = label;}

}

