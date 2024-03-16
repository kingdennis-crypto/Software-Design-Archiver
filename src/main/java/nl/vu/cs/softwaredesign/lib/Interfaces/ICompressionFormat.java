package nl.vu.cs.softwaredesign.lib.Interfaces;

import java.io.IOException;

public interface ICompressionFormat {
    void compress(String sourcePath, String destinationPath) throws IOException;
    void decompress(String sourcePath, String destinationPath) throws IOException;
}
