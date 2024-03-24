package nl.vu.cs.softwaredesign.lib.models;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class KeyProperties {
    private final SecretKey secretKey;
    private final byte[] nonce;

    public KeyProperties(byte[] secret, byte[] nonce) {
        this.secretKey = new SecretKeySpec(secret, "AES");
        this.nonce = nonce;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public byte[] getNonce() {
        return nonce;
    }
}
