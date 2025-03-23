package util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Properties;

public class EncryptionUtil {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12; // Recommended IV size for AES-GCM
    private static final int SALT_LENGTH = 16;
    private static final int ITERATION_COUNT = 100000;
    private static final int KEY_LENGTH = 256;
    private static final int TAG_LENGTH = 128; // Authentication tag length in bits

    private static String masterKey;

    static {
        loadMasterKey();
    }

    private static void loadMasterKey() {
        try (InputStream input = EncryptionUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("Config file not found");
            }
            Properties prop = new Properties();
            prop.load(input);

            String encryptedMasterKey = prop.getProperty("EncryptMasterKey");
            String aesKey = prop.getProperty("AESKey");

            if (encryptedMasterKey == null || aesKey == null) {
                throw new IOException("Master key or AES key missing in config");
            }

            masterKey = decryptMasterKey(encryptedMasterKey, aesKey);
            if (masterKey == null) {
                throw new IOException("Failed to decrypt master key.");
            }
        } catch (IOException e) {
            System.err.println("Error loading master key: " + e.getMessage());
        }
    }

    private static String decryptMasterKey(String encryptedMasterKey, String aesKey) {
        try {
            String[] parts = encryptedMasterKey.split(":");
            if (parts.length != 2) {
                System.err.println("Invalid encrypted master key format.");
                return null;
            }

            byte[] iv = Base64.getDecoder().decode(parts[0]);
            byte[] cipherText = Base64.getDecoder().decode(parts[1]);

            SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(aesKey), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH, iv));
            byte[] decryptedBytes = cipher.doFinal(cipherText);

            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("Error decrypting master key: " + e.getMessage());
            return null;
        }
    }
    
    public static String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String encrypt(String plainText) {
        if (masterKey == null) {
            System.err.println("Encryption failed: Master key is not available.");
            return null;
        }
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            secureRandom.nextBytes(salt);
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);

            SecretKey secretKey = generateKey(masterKey, salt);
            if (secretKey == null) {
                System.err.println("Encryption failed: Secret key generation error.");
                return null;
            }

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH, iv));
            byte[] encryptedText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(salt) + ":" +
                   Base64.getEncoder().encodeToString(iv) + ":" +
                   Base64.getEncoder().encodeToString(encryptedText);
        } catch (Exception e) {
            System.err.println("Error encrypting text: " + e.getMessage());
            return null;
        }
    }

    public static String decrypt(String encryptedText) {
        if (masterKey == null) {
            System.err.println("Decryption failed: Master key is not available.");
            return null;
        }
        try {
            String[] parts = encryptedText.split(":");
            if (parts.length != 3) {
                System.err.println("Invalid encrypted text format.");
                return null;
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] iv = Base64.getDecoder().decode(parts[1]);
            byte[] cipherText = Base64.getDecoder().decode(parts[2]);

            SecretKey secretKey = generateKey(masterKey, salt);
            if (secretKey == null) {
                System.err.println("Decryption failed: Secret key generation error.");
                return null;
            }

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH, iv));
            byte[] decryptedText = cipher.doFinal(cipherText);

            return new String(decryptedText, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("Error decrypting text: " + e.getMessage());
            return null;
        }
    }

    private static SecretKey generateKey(String masterKey, byte[] salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(masterKey.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            return new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
        } catch (Exception e) {
            System.err.println("Error generating key: " + e.getMessage());
            return null;
        }
    }
}
