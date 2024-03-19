package nl.vu.cs.softwaredesign.lib.Models;

import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;
import java.io.IOException;

public class ContentExtractor {
    public FileArchive extractContents (ICompressionFormat format, FileArchive fileArchive, String destinationPath, String password) throws IOException {
        return format.compress(fileArchive, destinationPath, password);
    }
}
