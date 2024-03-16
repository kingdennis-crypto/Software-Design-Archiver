package nl.vu.cs.softwaredesign.lib.CompressionFormats;

import nl.vu.cs.softwaredesign.lib.Annotations.CompressionType;
import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;

import java.io.File;
import java.io.IOException;

@CompressionType(label="RAR", description="Rar description")
public class RarCompression implements ICompressionFormat {
    @Override
    public void compress(File file, String name) throws IOException {

    }

    @Override
    public void decompress() { }
}
