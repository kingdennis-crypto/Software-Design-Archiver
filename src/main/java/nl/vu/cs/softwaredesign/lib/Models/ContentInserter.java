package nl.vu.cs.softwaredesign.lib.Models;

import nl.vu.cs.softwaredesign.lib.Handlers.EncryptionHandler;
import nl.vu.cs.softwaredesign.lib.Handlers.KeyHandler;
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
     * @return The FileArchive containing the inserted contents.
     * @throws IOException if an I/O error occurs during insertion.
     */
    public static FileArchive insertContents(ICompressionFormat format, FileArchive fileArchive, String destinationPath) throws IOException {
        FileArchive archive = format.compress(fileArchive, destinationPath);
        var files = fileArchive.generateFileRepresentation();

        archive.addMetadata("content", files);
        archive.addMetadata(fileArchive.getMetadata());

        KeyHandler keyHandler = new KeyHandler();
        KeyProperties keyProperties = keyHandler.getKey();

        EncryptionHandler.encryptFile(archive.getROOT().getAbsolutePath(), keyProperties.getSecretKey(), keyProperties.getNonce(), archive.getMetadata());

        return archive;
    }
}
