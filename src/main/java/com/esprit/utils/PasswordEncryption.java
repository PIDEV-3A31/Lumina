/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esprit.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

public class PasswordEncryption {

    final static String key = "ThatWay-2023";

    public static String encryptPass(String plaintext) throws Exception {
        // Hash the key using SHA-256
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hashedKey = sha256.digest(key.getBytes("UTF-8"));

        // Use the first 16 bytes of the hash as the IV
        byte[] iv = new byte[16];
        System.arraycopy(hashedKey, 0, iv, 0, 16);

        SecretKeySpec secretKeySpec = new SecretKeySpec(hashedKey, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encrypted = cipher.doFinal(plaintext.getBytes("UTF-8"));

        // Concatenate IV and encrypted data
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    public static String decryptPass(String base64Encrypted) throws Exception {
        // Step 1: Convert key to MD5
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] keyHash = md.digest(key.getBytes("UTF-8"));

        // Decode base64
        byte[] combined = Base64.getDecoder().decode(base64Encrypted);

        // Extract the IV and encrypted data
        byte[] iv = new byte[16];
        System.arraycopy(combined, 0, iv, 0, 16);

        byte[] encryptedData = new byte[combined.length - 16];
        System.arraycopy(combined, 16, encryptedData, 0, encryptedData.length);

        SecretKeySpec secretKeySpec = new SecretKeySpec(keyHash, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] decrypted = cipher.doFinal(encryptedData);
        return new String(decrypted, "UTF-8");
    }

    public static String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@!=+-_";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
}
