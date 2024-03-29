package nl.vu.cs.softwaredesign.lib.handlers;

import nl.vu.cs.softwaredesign.lib.models.FileArchive;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.io.*;
import java.util.Collections;
import java.util.Map;

public class EncryptionHandler {
    private EncryptionHandler() { }

    /**
     * Encrypts a file using AES/GCM/NoPadding algorithm and stores the metadata in plain text.
     *
     * @param path The path to the file to be encrypted.
     * @param secret The secret key used for encryption.
     * @param iv The initialization vector for encryption.
     * @param metadata The metadata to be associated with the encrypted file.
     * @throws FileNotFoundException If the specified file does not exist.
     */
    public static void encryptFile(String path, SecretKey secret, byte[] iv, Map<String, String> metadata) throws FileNotFoundException {
        try {
            File inputFile = new File(path);
            File outputFile = new File(path + ".enc");

            if (!inputFile.exists())
                throw new FileNotFoundException(String.format("File %s does not exist", inputFile.getName()));

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secret, new GCMParameterSpec(128, iv));

            try (FileInputStream inputStream = new FileInputStream(inputFile);
                    FileOutputStream outputStream = new FileOutputStream(outputFile)) {

                // Write metadata to the output file
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(metadata);

                // Encrypt file contents
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byte[] encryptedBytes = cipher.update(buffer, 0, bytesRead);
                    if (encryptedBytes != null)
                        outputStream.write(encryptedBytes);
                }

                byte[] finalBytes = cipher.doFinal();
                if (finalBytes != null)
                    outputStream.write(finalBytes);
            }

            // Move the encrypted file to the input file
            if (!outputFile.renameTo(inputFile))
                throw new IOException("Failed to rename the encrypted file to the input file.");

        } catch(FileNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Decrypts a file encrypted with AES/GCM/NoPadding algorithm and extracts the associated metadata.
     *
     * The decrypted content will be written back to the input file
     * @param path The path to the file to be decrypted.
     * @param secret The secret key used for decryption.
     * @param iv The initialization vector for decryption.
     * @return A {@link FileArchive} Object containing the decrypted file and its associated metadata.
     * @throws FileNotFoundException If the specified file does not exist/
     */
    public static FileArchive decryptFile(String path, SecretKey secret, byte[] iv) throws FileNotFoundException {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new GCMParameterSpec(128, iv));

            File inputFile = new File(path);
            Map<String, String> metadata;

            if (!inputFile.exists())
                throw new FileNotFoundException(String.format("File %s does not exist", inputFile.getName()));

            try (FileInputStream inputStream = new FileInputStream(inputFile)) {
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                metadata = (Map<String, String>) objectInputStream.readObject();

                ByteArrayOutputStream decryptedContent = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byte[] decryptedBytes = cipher.update(buffer, 0, bytesRead);

                    if (decryptedBytes != null)
                        decryptedContent.write(decryptedBytes);
                }

                byte[] finalBytes = cipher.doFinal();
                if (finalBytes != null)
                    decryptedContent.write(finalBytes);

                try (FileOutputStream outputStream = new FileOutputStream(inputFile)) {
                    outputStream.write(decryptedContent.toByteArray());
                }

                return new FileArchive(inputFile, metadata);
            }
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Reads metadata from a file without decrypting its content.
     *
     * @param path The path to the encrypted file.
     * @return A map containing the metadata extracted from the file.
     */
    public static Map<String, String> readMetadataFromFile(String path) {
        try (FileInputStream inputStream = new FileInputStream(path);
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            return (Map<String, String>) objectInputStream.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyMap();
        }
    }

    /**
     * Checks if the file at the specified path is password-protected.
     *
     * @param path  The path to the file.
     * @return      True if the file is password-protected, false otherwise.
     */
    public static boolean isPasswordProtected(String path) {
        return EncryptionHandler.readMetadataFromFile(path).containsKey("password");
    }
}
