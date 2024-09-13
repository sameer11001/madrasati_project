package com.webapp.madrasati.auth.security;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.error.InternalServerErrorException;

@Component
public class RsaKeyLoader {

    private static final String PRIVATE_KEY_PATH = "security/keys/private_key.pem";
    private static final String PUBLIC_KEY_PATH = "security/keys/public_key.pem";

    public PrivateKey getPrivateKey() {
        try {

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            LoggerApp.error("Error loading RSA private key: " + e.getMessage());
            throw new InternalServerErrorException("Error loading RSA private key: " + e.getMessage());
        }
    }

    public PublicKey getPublicKey() {
        try {
            // Replace '\n' back with actual new lines

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            LoggerApp.error("Error loading RSA private key: " + e.getMessage());
            throw new InternalServerErrorException("Error loading RSA private key: " + e.getMessage());
        }
    }

    private byte[] decodeKey(String key, boolean isPrivateKey) {
        if (isPrivateKey) {
            String privateKeyEnv = readKeyFile(key);
            String privateKeyPem = privateKeyEnv.replace("\\n", "\n").replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "").replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(privateKeyPem);
        } else {
            String publicKeyEnv = readKeyFile(key);
            String publicKeyPem = publicKeyEnv.replace("\\n", "\n")
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(publicKeyPem);
        }
    }

    private static String readKeyFile(String filePath) throws Exception {
        Resource resource = new ClassPathResource(filePath);
        StringBuilder keyBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                keyBuilder.append(line).append("\n");
            }
        }
        return keyBuilder.toString();
    }
}
