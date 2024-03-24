package nl.vu.cs.softwaredesign.lib.singletons;

import nl.vu.cs.softwaredesign.lib.enumerations.Status;
import nl.vu.cs.softwaredesign.lib.interfaces.IProgressListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * ProgressLogger class implements the IProgressListener interface and provides
 * functionality to log progress updates to a logfile.
 */
public class ProgressLogger implements IProgressListener {
    private static ProgressLogger instance;
    private File logFile;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the log file.
     */
    private ProgressLogger() {
        logFile = new File("logfile.txt");
    }

    /**
     * Retrieves the singleton instance of ProgressLogger.
     * If instance is null, creates a new instance and also creates the logfile
     * @return The singleton instance of ProgressLogger.
     */
    public static ProgressLogger getInstance() {
        if (instance == null) instance = new ProgressLogger();
        return instance;
    }

    /**
     * Updates the progress status for the existing file and writes it to the file.
     * @param status The status to be logged.
     * @param file The existing file to log the status to.
     */
    @Override
    public void update(Status status, File file) {
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(status.toString() + "\n");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}

