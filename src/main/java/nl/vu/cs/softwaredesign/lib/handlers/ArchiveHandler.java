package nl.vu.cs.softwaredesign.lib.handlers;

import nl.vu.cs.softwaredesign.lib.interfaces.ICompressionFormat;
import nl.vu.cs.softwaredesign.lib.models.FileArchive;
import nl.vu.cs.softwaredesign.lib.models.KeyProperties;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.Map;

public class ArchiveHandler {
    /**
     * Inserts contents into a FileArchive using the provided compression format.
     *
     * @param format          The compression format to use for insertion.
     * @param fileArchive     The FileArchive into which contents will be inserted.
     * @param destinationPath The destination path for the inserted contents.
     * @return The FileArchive containing the inserted contents.
     * @throws IOException if an I/O error occurs during insertion.
     */
    public static FileArchive insertContents(ICompressionFormat format, FileArchive fileArchive, String destinationPath)
            throws IOException {

        FileArchive archive = format.compress(fileArchive, destinationPath);
        var files = fileArchive.generateFileRepresentation();

        archive.addMetadata("content", files);
        archive.addMetadata(fileArchive.getMetadata());

        KeyHandler keyHandler = new KeyHandler();
        KeyProperties keyProperties = keyHandler.getKey();

        EncryptionHandler.encryptFile(archive.getROOT().getAbsolutePath(), keyProperties.getSecretKey(), keyProperties.getNonce(), archive.getMetadata());

        return archive;
    }

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
    public static FileArchive extractContents(ICompressionFormat format, FileArchive fileArchive, String destinationPath,
                                              String password) throws IOException {

        KeyHandler keyHandler = new KeyHandler();
        KeyProperties keyProperties = keyHandler.getKey();

        Map<String, String> metadata = EncryptionHandler.readMetadataFromFile(fileArchive.getROOT().getAbsolutePath());

        assert metadata != null;
        if (!metadata.isEmpty() && metadata.containsKey("password")) {
            String storedPassword = metadata.get("password");

            if (!password.equals(storedPassword))
                throw new InvalidObjectException("Invalid password provided");
        }

        EncryptionHandler.decryptFile(fileArchive.getROOT().getAbsolutePath(), keyProperties.getSecretKey(), keyProperties.getNonce());

        FileArchive deArchived = format.decompress(fileArchive, destinationPath);

        // TODO: Add delete check with exception
        fileArchive.getROOT().delete();

        return deArchived;
    }
}