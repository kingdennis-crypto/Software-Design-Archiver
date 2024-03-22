package nl.vu.cs.softwaredesign.lib.CompressionFormats;

import nl.vu.cs.softwaredesign.lib.Annotations.CompressionType;
import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;

@CompressionType(label="RAR", description="Rar description", extensions = { ".rar" })
public class RarCompression implements ICompressionFormat {
    @Override
    public void compress() { }

    @Override
    public void decompress() { }
}
