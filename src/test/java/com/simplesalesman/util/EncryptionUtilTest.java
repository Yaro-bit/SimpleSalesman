package com.simplesalesman.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionUtilTest {

    private EncryptionUtil encryptionUtil;

    @BeforeEach
    void setUp() {
        encryptionUtil = new EncryptionUtil();
    }

    @Test
    @DisplayName("Encrypt und Decrypt liefern originalen Klartext zurück")
    void encryptAndDecrypt_shouldReturnOriginalText() {
        // Arrange
        String originalText = "Hello Simple Salesman!";

        // Act
        String encrypted = encryptionUtil.encrypt(originalText);
        String decrypted = encryptionUtil.decrypt(encrypted);

        // Assert
        assertNotNull(encrypted, "Verschlüsselter Text sollte nicht null sein");
        assertNotEquals(originalText, encrypted, "Verschlüsselter Text sollte sich vom Klartext unterscheiden");
        assertEquals(originalText, decrypted, "Entschlüsselter Text sollte dem Original entsprechen");
    }

    @Test
    @DisplayName("Decrypt mit ungültigem CipherText wirft Exception")
    void decrypt_withInvalidCipherText_shouldThrowException() {
        // Arrange
        String invalidCipherText = "ungültig!!!";

        // Assert
        assertThrows(RuntimeException.class, () -> encryptionUtil.decrypt(invalidCipherText),
                "Sollte eine Exception werfen bei ungültigem CipherText");
    }
}
