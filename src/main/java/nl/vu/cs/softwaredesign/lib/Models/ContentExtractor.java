package nl.vu.cs.softwaredesign.lib.Models;

import net.lingala.zip4j.ZipFile;
import nl.vu.cs.softwaredesign.lib.CompressionFormats.ZipCompression;
import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;

import java.io.IOException;

public class ContentExtractor {
    private FileMode fileMode;

    public ContentExtractor(FileMode mode){
        this.fileMode = mode;
    }

    public FileArchive extractContents (ICompressionFormat format, String sourcePath, String destinationPath) throws IOException {
        format.compress(sourcePath, destinationPath);

        return fileArchive;
    }
}
