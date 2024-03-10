package nl.vu.cs.softwaredesign.lib.Handlers;

import nl.vu.cs.softwaredesign.lib.Enumerations.SettingsValue;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ConfigurationHandler {
    private static ConfigurationHandler instance;
    private final Properties properties;

    private ConfigurationHandler() {
        this.properties = new Properties();

        try {
            // Get the configuration directory path and file path
            Path configDir = Path.of(PathHandler.getUserDataPath());
            Path configFile = Path.of(configDir + "/config.properties");

            if (Files.notExists(configDir)) {
                Files.createDirectories(configDir);
            }

            // Check if the config file exists
            if (Files.exists(configFile)) {
                // Load the existing properties
                this.properties.load(new FileInputStream(String.valueOf(configFile)));
            } else {
                // Create a new config file
                Files.createFile(configFile);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets the singleton instance of the ConfigurationHandler.
     * @return The ConfigurationHandler instance.
     */
    public static ConfigurationHandler getInstance() {
        if (ConfigurationHandler.instance == null) {
            ConfigurationHandler.instance = new ConfigurationHandler();
        }

        return ConfigurationHandler.instance;
    }

    /**
     * Retrieves the value associated with the specified key from the configuration.
     * @param key The key for which to retrieve the value.
     * @return The value associated with the key, or null if the key is not found.
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Sets a value in the configuration with the specified key.
     * @param key The key for the property.
     * @param value The value to be associated with the key.
     */
    public void setProperty(SettingsValue key, String value) {
        properties.setProperty(key.label, value);
    }

    /**
     * Saves the current configuration properties to the configuration file.
     */
    public void saveProperties() {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(PathHandler.getUserDataPath() + "/config.properties"))) {
            properties.store(writer, "Updated properties");
        } catch (IOException ex) {
            System.err.println("Error saving properties file: " + ex.getMessage());
        }
    }

    /**
     * Resets the current properties to the values stored in the configuration file.
     */
    public void resetSavedProperties() {
        try {
            this.properties.load(new FileInputStream(PathHandler.getUserDataPath() + "/config.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
