package nl.vu.cs.softwaredesign.lib.Models;

import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;
import java.io.IOException;

/**
 * Class to extract contents using a compression format.
 */
public class ContentExtractor {
    /**
     * Extracts contents from a FileArchive using the provided compression format.
     *
     * @param format          The compression format to use for extraction.
     * @param fileArchive     The FileArchive from which to extract contents.
     * @param destinationPath The destination path for the extracted contents.
     * @param password        The password for decryption (can be null if encryption was not used during compression).
     * @return                The FileArchive containing the extracted contents.
     * @throws IOException    if an I/O error occurs during extraction.
     */
    public FileArchive extractContents(ICompressionFormat format, FileArchive fileArchive, String destinationPath,
                                       String password) throws IOException {
        return format.decompress(fileArchive, destinationPath, password);
    }
}
