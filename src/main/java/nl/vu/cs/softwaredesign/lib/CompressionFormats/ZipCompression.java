package nl.vu.cs.softwaredesign.lib.CompressionFormats;

import nl.vu.cs.softwaredesign.lib.Annotations.CompressionType;
import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;

@CompressionType(label="ZIP", description="Zip description")
public class ZipCompression implements ICompressionFormat {
    @Override
    public void compress() { }

    @Override
    public void decompress() { }
}
