package nl.vu.cs.softwaredesign.lib.Handlers;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.io.*;
import java.util.Arrays;

public class EncryptionHandler {
    public EncryptionHandler() {}

    /**
     * Encrypts a file using AES/GCM/NoPadding algorithm.
     *
     * @param path   The path to the file to be encrypted.
     * @param secret The secret key used for encryption.
     * @param iv     The initialization vector for encryption
     */
    public void encryptFile(String path, SecretKey secret, byte[] iv) throws FileNotFoundException {
        try {
            File inputFile = new File(path);
            File outputFile = new File(path);

            if (!inputFile.exists())
                throw new FileNotFoundException(String.format("File %s does not exist", inputFile.getName()));

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secret, new GCMParameterSpec(128, iv));

            try (FileInputStream inputStream = new FileInputStream(inputFile);
                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {

                ByteArrayOutputStream encryptedContent = fileToByteArray(inputStream, cipher, new ByteArrayOutputStream());
                outputStream.write(encryptedContent.toByteArray());
            }

        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Decrypts a file using the AES/GCM/NoPadding algorithm.
     *
     * @param path   The path to the file to be encrypted.
     * @param secret The secret key used for decryption.
     * @param iv     The initialization vector for decryption.
     */
    public void decryptFile(String path, SecretKey secret, byte[] iv) throws FileNotFoundException {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new GCMParameterSpec(128, iv));

            File inputFile = new File(path);
            ByteArrayOutputStream encryptedContent = new ByteArrayOutputStream();

            if (!inputFile.exists())
                throw new FileNotFoundException(String.format("File %s does not exist", inputFile.getName()));

            try (InputStream inputStream = new FileInputStream(inputFile)) {
                fileToByteArray(inputStream, cipher, encryptedContent);
            }

            int position = 0;

            try (OutputStream outputStream = new FileOutputStream(inputFile.getAbsoluteFile())) {
                byte[] fileContent = Arrays.copyOfRange(encryptedContent.toByteArray(), position, encryptedContent.size());
                outputStream.write(fileContent);

                if (!inputFile.delete())
                    System.out.println("Something went wrong trying to delete this file");
            }
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Converts data from an {@link InputStream} to a byte array using the provided {@link Cipher}.
     *
     * @param inputStream   The InputStream containing the input data.
     * @param cipher        The Cipher instance used for transformation.
     * @param outputStream  The ByteArrayOutputStream for the transformed data.
     * @return              The byte array containing the transformed data.
     * @throws IOException                  If an I/O error occurs during reading from the InputStream.
     * @throws IllegalBlockSizeException    If the block size of the Cipher is invalid during transformation.
     * @throws BadPaddingException          If padding is expected but not properly applied during transformation.
     */
    private ByteArrayOutputStream fileToByteArray(InputStream inputStream, Cipher cipher, ByteArrayOutputStream outputStream)
            throws IOException, IllegalBlockSizeException, BadPaddingException {
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);

            if (output != null)
                outputStream.write(output);
        }

        byte[] outputBytes = cipher.doFinal();

        if (outputBytes != null) {
            outputStream.write(outputBytes);
        }

        return outputStream;
    }
}
