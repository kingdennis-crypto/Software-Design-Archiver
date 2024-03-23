package nl.vu.cs.softwaredesign.lib.Models;

import nl.vu.cs.softwaredesign.lib.Handlers.EncryptionHandler;
import nl.vu.cs.softwaredesign.lib.Handlers.KeyHandler;
import nl.vu.cs.softwaredesign.lib.Interfaces.ICompressionFormat;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.Map;

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
    public static FileArchive extractContents(ICompressionFormat format, FileArchive fileArchive, String destinationPath,
                                       String password) throws IOException, InvalidObjectException {
        KeyHandler keyHandler = new KeyHandler();
        KeyProperties keyProperties = keyHandler.getKey();

        Map<String, String> metadata = EncryptionHandler.readMetadataFromFile(fileArchive.getROOT().getAbsolutePath());

        if (metadata != null && metadata.containsKey("password") && !metadata.get("password").equals(password))
            throw new InvalidObjectException("Invalid password provided");

        EncryptionHandler.decryptFile(fileArchive.getROOT().getAbsolutePath(), keyProperties.getSecretKey(), keyProperties.getNonce());

        return format.decompress(fileArchive, destinationPath);
    }
}
