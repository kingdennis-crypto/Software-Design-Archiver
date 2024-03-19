package nl.vu.cs.softwaredesign.lib.Models;

import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;
import java.io.IOException;

public class ContentInserter {
    public FileArchive insertContents (ICompressionFormat format, FileArchive fileArchive, String destinationPath, String password) throws IOException {
        return format.decompress(fileArchive, destinationPath, password);
    }
}
