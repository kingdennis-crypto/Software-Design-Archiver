package nl.vu.cs.softwaredesign.lib.Interfaces;

import nl.vu.cs.softwaredesign.lib.Models.FileArchive;
import java.io.IOException;

public interface ICompressionFormat {
    FileArchive compress(FileArchive files, String destinationPath, String password) throws IOException;
    FileArchive decompress(FileArchive compressedFiles, String destinationPath, String password) throws IOException;
}
