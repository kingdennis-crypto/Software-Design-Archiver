package nl.vu.cs.softwaredesign.lib.Handlers;

import nl.vu.cs.softwaredesign.lib.Annotations.CompressionType;
import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CompressionHandler {
    public CompressionHandler() { }

    /**
     * Retrieves a list of available compression formats based on the annotated compression formats
     * @return A list of compression format labels.
     */
    public static List<CompressionType> getAvailableCompressionsType() {
        List<CompressionType> classNames = new ArrayList<>();

        // Get the directory path
        String path = System.getProperty("user.dir") + "/src/main/java/nl/vu/cs/softwaredesign/lib/CompressionFormats";
        File directory = new File(path);

        if (directory.exists() && directory.isDirectory()) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                // Check if the file is a Java class file
                if (file.isFile() && file.getName().endsWith(".java")) {
                    String className = file.getName().replace(".java", "");

                    try {
                        // Load the class
                        Class<?> classObj = Class.forName("nl.vu.cs.softwaredesign.lib.CompressionFormats." + className);

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
        }

        return classNames;
    }

    private List<Class<ICompressionFormat>> getAllCompressionFormats() {
        List<Class<ICompressionFormat>> classes = new ArrayList<>();

        String path = System.getProperty("user.dir") + "/src/main/java/nl/vu/cs/softwaredesign/lib/CompressionFormats";
        File directory = new File(path);

        if (directory.exists() && directory.isDirectory()) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                // Check if the file is a java class file
                if (file.isFile() && file.getName().endsWith(".java")) {
                    String className = file.getName().replace(".java", "");

                    try {
                        @SuppressWarnings("unchecked")
                        Class<ICompressionFormat> classObj =
                                (Class<ICompressionFormat>) Class.forName("nl.vu.cs.softwaredesign.lib.CompressionFormats." + className);

                        classes.add(classObj);
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        return classes;
    }

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

    public List<String> getCompressionExtensions() {
        return getAvailableCompressionTypes()
                .stream()
                .map(CompressionType::extensions)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }
}
