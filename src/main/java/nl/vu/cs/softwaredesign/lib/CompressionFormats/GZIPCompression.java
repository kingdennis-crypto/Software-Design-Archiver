package nl.vu.cs.softwaredesign.lib.CompressionFormats;

import nl.vu.cs.softwaredesign.lib.Annotations.CompressionType;
import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;
import nl.vu.cs.softwaredesign.lib.Models.FileArchive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * A class implementing the GZIP compression format.
 * This class provides methods to compress and decompress files using GZIP compression algorithm.
 */
@CompressionType(label = "GZIP", description = "GZip description", extensions = {})
public class GZIPCompression implements ICompressionFormat {

    /**
     * Compresses the given file archive using GZIP compression algorithm.
     *
     * @param fileArchive     The file archive to compress.
     * @param destinationPath The destination path for the compressed file.
     * @param password        The password (not used in this implementation, can be null).
     * @return                A FileArchive object representing the compressed file.
     * @throws IOException    If an I/O error occurs.
     */
    @Override
    public FileArchive compress(FileArchive fileArchive, String destinationPath, String password) throws IOException {
        byte[] buffer = new byte[1024];

        try (GZIPOutputStream outputStream = new GZIPOutputStream(new FileOutputStream(destinationPath))) {
            try (FileInputStream inputStream = new FileInputStream(fileArchive.getROOT())) {
                int totalSize;
                while ((totalSize = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, totalSize);
                }
            }

            outputStream.finish();
        }

        return new FileArchive(new File(destinationPath));
    }

    /**
     * Decompresses the given compressed file archive using GZIP decompression algorithm.
     *
     * @param compressedFiles The compressed file archive to decompress.
     * @param destinationPath The destination path for the decompressed files.
     * @param password        The password (not used in this implementation, can be null).
     * @return                A FileArchive object representing the decompressed files.
     * @throws IOException    If an I/O error occurs.
     */
    @Override
    public FileArchive decompress(FileArchive compressedFiles, String destinationPath, String password) throws IOException {
        byte[] buffer = new byte[1024];

        try (GZIPInputStream is = new GZIPInputStream(new FileInputStream(compressedFiles.getROOT()))) {
            try (FileOutputStream out = new FileOutputStream((destinationPath))) {
                int totalSize;
                while ((totalSize = is.read(buffer)) > 0) {
                    out.write(buffer, 0, totalSize);
                }
            }
        }

        return new FileArchive(new File(destinationPath));
    }
}
