package nl.vu.cs.softwaredesign.lib.Handlers;

import nl.vu.cs.softwaredesign.lib.Models.KeyProperties;

import javax.crypto.KeyGenerator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class KeyHandler {
    /**
     * Creates a new key with the specified name.
     * @return The path to the newly created key file.
     * @throws IOException If an I/O error occurs.
     * @throws NoSuchAlgorithmException If the specified cryptographic algorithm is not available.
     */
    public void createAndSetMainKey() throws IOException, NoSuchAlgorithmException {
        Path dirPath = Paths.get(PathHandler.getUserDataPath());

        String keyName = "archive.key";
        Path keyPath = Paths.get(dirPath + "/" + keyName);

        Files.createFile(keyPath);

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256, SecureRandom.getInstanceStrong());

        byte[] secret = keyGen.generateKey().getEncoded();
        byte[] nonce = getRandomNonce();

        byte[] content = new byte[nonce.length + secret.length];

        for (int i = 0; i < content.length; i++) {
            content[i] = i < nonce.length ? nonce[i] : secret[i - nonce.length];
        }

        Files.write(keyPath, content);
    }

    /**
     * Retrieves the {@link KeyProperties} for the specified key name.
     * @return The {@link KeyProperties} containing the key's secret and nonce.
     * @throws IOException If an I/O error occurs.
     */
    public KeyProperties getKey() throws IOException, NullPointerException {
        Path dirPath = Paths.get(PathHandler.getUserDataPath());

        String keyName = "archive.key";
        Path keyPath = Paths.get(dirPath + "/" + keyName);

        if (!Files.exists(keyPath))
            throw new FileNotFoundException(keyPath.toString());

        byte[] keyContent = Files.readAllBytes(keyPath);

        byte[] nonce = new byte[12];
        byte[] secret = new byte[keyContent.length - 12];

        for (int i = 0; i < keyContent.length; i++) {
            if (i < 12) {
                nonce[i] = keyContent[i];
            } else {
                secret[i - nonce.length] = keyContent[i];
            }
        }

        return new KeyProperties(secret, nonce);
    }

    /**
     * Generates a random nonce for the IV part of the key.
     * @return The generated nonce.
     */
    private byte[] getRandomNonce() {
        byte[] nonce = new byte[12];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }
}
