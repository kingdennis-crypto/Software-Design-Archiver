package nl.vu.cs.softwaredesign.lib.Models;

import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;

import java.io.File;
import java.io.IOException;

public class ContentInserter {

    private FileOptions fileOptions;

    public ContentInserter(FileOptions fileOptions){
        this.fileOptions = fileOptions;
    }

    public FileArchive insertContents (ICompressionFormat format, String sourcePath, String destinationPath) throws IOException {
        format.compress(sourcePath, destinationPath);
        FileArchive fileArchive = new FileArchive("");
        //Following the UML it needs to return a FileArchive
        return fileArchive;
    }
}
