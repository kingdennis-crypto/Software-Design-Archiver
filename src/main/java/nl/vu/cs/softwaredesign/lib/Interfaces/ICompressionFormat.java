package nl.vu.cs.softwaredesign.lib.Interfaces;

import nl.vu.cs.softwaredesign.lib.Models.FileArchive;
import java.io.IOException;

/**
 * Interface for defining compression formats.
 */
public interface ICompressionFormat {

    /**
     * Compresses files into a compressed archive.
     *
     * @param files           The FileArchive containing files to compress.
     * @param destinationPath The destination path for the compressed archive.
     * @param password        The password for encryption (can be null if encryption is not desired).
     * @return                The compressed FileArchive.
     * @throws IOException    if an I/O error occurs while compressing the files.
     */
    FileArchive compress(FileArchive files, String destinationPath, String password) throws IOException;

    /**
     * Decompresses a compressed archive into files.
     *
     * @param compressedFiles The compressed FileArchive to decompress.
     * @param destinationPath The destination path for the decompressed files.
     * @param password        The password for decryption (can be null if encryption was not used during compression).
     * @return                The decompressed FileArchive.
     * @throws IOException if an I/O error occurs while decompressing the archive.
     */
    FileArchive decompress(FileArchive compressedFiles, String destinationPath, String password) throws IOException;
}
