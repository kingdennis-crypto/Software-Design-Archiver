package nl.vu.cs.softwaredesign.lib.singletons;

import nl.vu.cs.softwaredesign.lib.enumerations.Status;
import nl.vu.cs.softwaredesign.lib.handlers.PathHandler;
import nl.vu.cs.softwaredesign.lib.interfaces.IProgressListener;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * ProgressLogger class implements the IProgressListener interface and provides
 * functionality to log progress updates to a logfile.
 */
public class ProgressLogger implements IProgressListener {
    private static final Logger logger = Logger.getLogger(ProgressLogger.class.getName());
    private static ProgressLogger instance = new ProgressLogger();
    private final File logFile;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the log file.
     */
    private ProgressLogger() {
        logFile = new File(PathHandler.getUserDataPath() + "logfile.txt");

        try {
            boolean fileCreated = logFile.createNewFile();
            if (fileCreated) {
                logger.info("File created successfully.");
            } else {
                logger.warning("File creation failed.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Retrieves the singleton instance of ProgressLogger.
     * If instance is null, creates a new instance and also creates the logfile
     * @return The singleton instance of ProgressLogger.
     */
    public static synchronized ProgressLogger getInstance() {
        // Synchronized to make sure we use the same instance and don't have multiple instances
        //  on multiple threads to make sure there aren't multiple instances of the file
        if (instance == null) instance = new ProgressLogger();
        return instance;
    }

    /**
     * Reads the last N lines from the logfile.
     *
     * @param numLines  The number of lines to read.
     * @return          A list containing the last N lines from the logfile.
     */
    public List<String> readLogFile(int numLines) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;

            while ((line = reader.readLine()) != null)
                lines.add(line);

            int endIndex = Math.min(lines.size(), numLines);
            lines = lines.subList(lines.size() - endIndex, lines.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return lines;
    }

    /**
     * Updates the progress status for the existing file and writes it to the logfile.
     *
     * @param status The status to be logged.
     * @param file The existing file to log the status to.
     */
    @Override
    public void update(Status status, File file) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        try (FileWriter writer = new FileWriter(logFile, true)) {
            String logLine = String.format("%s: %s at %s%n", status.label.toUpperCase(), file.getAbsolutePath(), formattedTime);
            writer.write(logLine);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}

