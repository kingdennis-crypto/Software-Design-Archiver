package nl.vu.cs.softwaredesign.lib.handlers;

public class PathHandler {
    /**
     * Constructs and returns the full path for the data folder within the user data directory.
     * @return A string representing the full path to the data directory
     */
    public static String getUserDataPath() {
        return String.format("%s/%s/", getUserDataDirectory(), ".archiver");
    }

    /**
     * Retrieves the user data directory based on the operating system
     * @return A string representing the user data directory
     */
    private static String getUserDataDirectory() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            // Windows
            return System.getenv("APPDATA");
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            // Unix/Linux/Mac
            return System.getProperty("user.home");
        } else {
            // Unknown operating system
            return null;
        }
    }
}
