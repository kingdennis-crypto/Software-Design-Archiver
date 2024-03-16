package nl.vu.cs.softwaredesign.lib.Interfaces;

import net.lingala.zip4j.ZipFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ICompressionFormat {
    ZipFile compress(List<File> files, String destinationPath, String password) throws IOException;
    List<File> decompress(List<ZipFile> zipFiles, String destinationPath, String password) throws IOException;
}
