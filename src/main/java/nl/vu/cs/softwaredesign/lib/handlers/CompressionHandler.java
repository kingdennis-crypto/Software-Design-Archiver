package nl.vu.cs.softwaredesign.lib.handlers;

import nl.vu.cs.softwaredesign.lib.annotations.CompressionType;
import nl.vu.cs.softwaredesign.lib.interfaces.ICompressionFormat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CompressionHandler {
    private static final String JAVA_FILE = ".java";

    /**
     * Retrieves a list of available compression formats based on the annotated compression formats.
     * @return A list of compression format labels.
     */
    public static List<CompressionType> getAvailableCompressionsType() {
        List<CompressionType> classNames = new ArrayList<>();

        // Get the directory path
        String path = System.getProperty("user.dir") + "/src/main/java/nl/vu/cs/softwaredesign/lib/compressionformats";
        File directory = new File(path);

        if (directory.exists() && directory.isDirectory()) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                // Check if the file is a Java class file
                processFile(file, classNames);
            }
        }

        return classNames;
    }

    /**
     * Processes a given file to extract class names annotated with CompressionType.
     *
     * @param file       The file to process.
     * @param classNames The list to store the names of classes annotated with CompressionType.
     */
    private static void processFile(File file, List<CompressionType> classNames) {
        if (file.isFile() && file.getName().endsWith(JAVA_FILE)) {
            String className = file.getName().replace(JAVA_FILE, "");

            try {
                // Load the class
                Class<?> classObj = Class.forName("nl.vu.cs.softwaredesign.lib.compressionformats." + className);

                // Check if the class has a CompressionType annotation
                if (classObj.isAnnotationPresent(CompressionType.class)) {
                    CompressionType compressionType = classObj.getAnnotation(CompressionType.class);
                    classNames.add(compressionType);
                }
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Retrieves a list of all available compression formats.
     * @return A list of classes representing available compression formats.
     */
    private List<Class<ICompressionFormat>> getAllCompressionFormats() {
        List<Class<ICompressionFormat>> classes = new ArrayList<>();

        String path = System.getProperty("user.dir") + "/src/main/java/nl/vu/cs/softwaredesign/lib/compressionformats";
        File directory = new File(path);

        if (directory.exists() && directory.isDirectory()) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                // Check if the file is a java class file
                if (file.isFile() && file.getName().endsWith(JAVA_FILE)) {
                    String className = file.getName().replace(JAVA_FILE, "");

                    try {
                        @SuppressWarnings("unchecked")
                        Class<ICompressionFormat> classObj =
                                (Class<ICompressionFormat>) Class.forName("nl.vu.cs.softwaredesign.lib.compressionformats." + className);

                        classes.add(classObj);
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        return classes;
    }

    /**
     * Retrieves the compression format class by its label.
     * @param label The label of the compression format.
     * @return      The compression format class corresponding to the label, or null if not found.
     */
    public Class<ICompressionFormat> getCompressionFormatByLabel(String label) {
        List<Class<ICompressionFormat>> compressions = this.getAllCompressionFormats();

        for (Class<ICompressionFormat> compression : compressions) {
            if (compression.isAnnotationPresent(CompressionType.class)
                    && compression.getAnnotation(CompressionType.class).label().equals(label)) {
                return compression;
            }
        }

        return null;
    }

    /**
     * Retrieves a list of available compression types.
     * @return A list of CompressionType annotations representing available compression types.
     */
    public List<CompressionType> getAvailableCompressionTypes() {
        List<Class<ICompressionFormat>> compressions = this.getAllCompressionFormats();
        List<CompressionType> classNames = new ArrayList<>();

        for (Class<ICompressionFormat> compression : compressions) {
            if (compression.isAnnotationPresent(CompressionType.class)) {
                CompressionType compressionType = compression.getAnnotation(CompressionType.class);
                classNames.add(compressionType);
            }
        }

        return classNames;
    }

    /**
     * Retrieves a list of compression extensions.
     * @return A list of compression extensions.
     */
    public List<String> getCompressionExtensions() {
        return getAvailableCompressionTypes()
                .stream()
                .map(CompressionType::extension)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the file extension associated with the compression format label.
     *
     * @param label The label of the compression format.
     * @return      The file extension related to the compression format
     */
    public String getExtensionByLabel(String label) {
        return this.getCompressionFormatByLabel(label)
                .getAnnotation(CompressionType.class)
                .extension();
    }
}
