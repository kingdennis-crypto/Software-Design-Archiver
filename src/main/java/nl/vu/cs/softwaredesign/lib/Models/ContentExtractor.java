package nl.vu.cs.softwaredesign.lib.Models;

import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ContentExtractor {
    public FileArchive extractContents (ICompressionFormat format, List<File> files, String destinationPath, String password) throws IOException {
        return new FileArchive(format.compress(files, destinationPath, password).getFile());
    }
}
