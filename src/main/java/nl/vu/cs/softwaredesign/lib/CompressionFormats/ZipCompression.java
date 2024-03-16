package nl.vu.cs.softwaredesign.lib.CompressionFormats;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import nl.vu.cs.softwaredesign.lib.Annotations.CompressionType;
import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;

import java.io.*;

@CompressionType(label="ZIP", description="Zip description")
public class ZipCompression implements ICompressionFormat {
    /**
     * Compresses the files in the given source directory into a ZIP archive with the specified destination path.
     *
     * @param sourcePath      The path to the source directory to compress.
     * @param destinationPath The path of the resulting ZIP archive.
     * @throws IOException If an I/O error occurs during compression.
     */
    @Override
    public void compress(String sourcePath, String destinationPath) throws IOException {

            File file = new File(sourcePath);
            ZipFile zipFile = new ZipFile(destinationPath + "/" + file.getName());
            addToZip(file, zipFile);
            zipFile.close();
    }

    public void addToZip(File file, ZipFile zipFile) throws ZipException {
        if (file.isDirectory()){
            zipFile.addFolder(file);
        }
        else {
            zipFile.addFile(file);
        }
    }

    /**
     * Decompresses a file (not implemented yet).
     * This method does not perform any action as decompression is pending.
     */
    @Override
    public void decompress(String sourcePath, String destinationPath) throws IOException {
        ZipFile zipFile = new ZipFile(sourcePath);
        zipFile.extractAll(destinationPath);
        zipFile.close();
    }

}
