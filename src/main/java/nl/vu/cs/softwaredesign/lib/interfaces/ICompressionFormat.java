package nl.vu.cs.softwaredesign.lib.interfaces;

import nl.vu.cs.softwaredesign.lib.models.FileArchive;
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
     * @return                The compressed FileArchive.
     * @throws IOException    if an I/O error occurs while compressing the files.
     */
    FileArchive compress(FileArchive files, String destinationPath) throws IOException;

    /**
     * Decompresses a compressed archive into files.
     *
     * @param compressedFiles The compressed FileArchive to decompress.
     * @param destinationPath The destination path for the decompressed files.
     * @return                The decompressed FileArchive.
     * @throws IOException if an I/O error occurs while decompressing the archive.
     */
    FileArchive decompress(FileArchive compressedFiles, String destinationPath) throws IOException;
}
