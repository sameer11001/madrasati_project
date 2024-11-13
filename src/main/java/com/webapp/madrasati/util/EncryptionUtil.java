package com.webapp.madrasati.util;

import com.webapp.madrasati.core.error.InternalServerErrorException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class EncryptionUtil {
    private static final String SECRET_KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SALT_ALGORITHM = "SHA1PRNG";
    private static final int IV_LENGTH = 16;
    private static final int SALT_LENGTH = 16;
    private static final int KEY_LENGTH = 256;
    private static final int ITERATION_COUNT = 65536;

    private static final EncryptionUtil instance = new EncryptionUtil();
    public static final EncryptionUtil Instance = instance;

    private EncryptionUtil() {}

    public String encrypt(String plainText, String password) {
        try {
            String salt = instance.generateSalt();
            SecretKey key = instance.generateKey(password, salt);
            IvParameterSpec iv = instance.generateIv();

            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            String encodedCipherText = Base64.getEncoder().encodeToString(cipherText);
            String encodedIv = Base64.getEncoder().encodeToString(iv.getIV());

            // Return cipher text, IV, and salt concatenated (could be separated/stored differently)
            return encodedCipherText + ":" + encodedIv + ":" + salt;
        } catch (Exception e) {
            throw new InternalServerErrorException("Encryption failed", e);
        }
    }

    public String decrypt(String cipherText, String password) {
        try {
            // Split cipher text into components
            String[] parts = cipherText.split(":");
            String encryptedData = parts[0];
            String ivString = parts[1];
            String salt = parts[2];

            SecretKey key = instance.generateKey(password, salt);
            IvParameterSpec iv = instance.decodeIv(ivString);

            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(plainText, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new InternalServerErrorException("Decryption failed", e);
        }
    }

    private String generateSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance(SALT_ALGORITHM);
        byte[] salt = new byte[SALT_LENGTH];
        sr.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private SecretKey generateKey(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, SECRET_KEY_ALGORITHM);
    }

    // Private method to generate a random initialization vector (IV)
    private IvParameterSpec generateIv() {
        byte[] iv = new byte[IV_LENGTH];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private IvParameterSpec decodeIv(String ivString) {
        byte[] iv = Base64.getDecoder().decode(ivString);
        return new IvParameterSpec(iv);
    }
}
