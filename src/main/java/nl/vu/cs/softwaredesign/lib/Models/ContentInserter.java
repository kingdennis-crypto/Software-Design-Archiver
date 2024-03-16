package nl.vu.cs.softwaredesign.lib.Models;

import net.lingala.zip4j.ZipFile;
import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContentInserter {

    public List<FileArchive> insertContents (ICompressionFormat format, List<ZipFile> zipFiles, String destinationPath, String password) throws IOException {
       List<File> files = format.decompress(zipFiles, destinationPath, password);
       List<FileArchive> fileArchivesList = new ArrayList<>();

       for (File file : files) {
           fileArchivesList.add(new FileArchive(file));
       }

        return fileArchivesList;
    }
}
