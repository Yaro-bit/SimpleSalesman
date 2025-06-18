package com.simplesalesman.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Utility class for simple AES encryption and decryption.
 *
 * WARNING: This implementation uses a hardcoded secret key and AES/ECB mode,
 * which is NOT secure for production use.
 * Please CHANGE the secret key immediately before deployment!
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.4
 */
@Component
public class EncryptionUtil {

    private static final String ALGORITHM = "AES";

    //CHANGEIT before production!
    private static final String SECRET_KEY = "changeitchangeit"; // 16 chars = 128 bit key

    private final SecretKeySpec secretKeySpec;

    public EncryptionUtil() {
        this.secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
    }

    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM); // AES/ECB/PKCS5Padding by default
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed — please CHANGE the secret key!", e);
        }
    }

    public String decrypt(String cipherText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed — please CHANGE the secret key!", e);
        }
    }
}
