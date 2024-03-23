package nl.vu.cs.softwaredesign.lib.Models;

import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;
import java.io.IOException;

/**
 * Class to insert contents using a compression format.
 */
public class ContentInserter {

    /**
     * Inserts contents into a FileArchive using the provided compression format.
     *
     * @param format          The compression format to use for insertion.
     * @param fileArchive     The FileArchive into which contents will be inserted.
     * @param destinationPath The destination path for the inserted contents.
     * @param password        The password for encryption (can be null if encryption is not desired).
     * @return The FileArchive containing the inserted contents.
     * @throws IOException if an I/O error occurs during insertion.
     */
    public FileArchive insertContents(ICompressionFormat format, FileArchive fileArchive, String destinationPath,
                                      String password) throws IOException {
        return format.compress(fileArchive, destinationPath, password);
    }
}
