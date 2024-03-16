package nl.vu.cs.softwaredesign.lib.CompressionFormats;

import net.lingala.zip4j.ZipFile;
import nl.vu.cs.softwaredesign.lib.Annotations.CompressionType;
import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;

import java.io.File;
import java.io.IOException;
import java.util.List;

@CompressionType(label="RAR", description="Rar description")
public class RarCompression implements ICompressionFormat {

    @Override
    public ZipFile compress(List<File> files, String destinationPath, String password) throws IOException {
        return null;
    }

    @Override
    public List<File> decompress(List<ZipFile> zipFiles, String destinationPath, String password) throws IOException {
        return null;
    }
}
