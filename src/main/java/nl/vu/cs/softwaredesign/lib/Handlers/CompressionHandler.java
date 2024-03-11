package nl.vu.cs.softwaredesign.lib.Handlers;

import nl.vu.cs.softwaredesign.lib.Annotations.CompressionType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompressionHandler {
    /**
     * Retrieves a list of available compression formats based on the annotated compression formats
     * @return A list of compression format labels.
     */
    public static List<CompressionType> getAvailableCompressions() {
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
}
