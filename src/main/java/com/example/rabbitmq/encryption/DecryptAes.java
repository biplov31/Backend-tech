package com.example.rabbitmq.encryption;

import com.example.rabbitmq.student.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class DecryptAes {
    private static SecretKey key;
    private static final int TAG_LEN = 128;
    private static final String IV = "yQUTV6S70IfPvmW3"; // Base64 encoded IV
    private static final String BASE64_KEY = "mO9A1uYDH4F29Q8jfFQxoQ=="; // Base64 encoded secret key

    public static void init() throws Exception {
        byte[] keyBytes = decode(BASE64_KEY);
        key = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
    }

    public static String decrypt(String encryptedMessage) throws Exception {
        byte[] messageInBytes = decode(encryptedMessage);
        byte[] ivInBytes = decode(IV); // Decode the hardcoded IV

        Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LEN, ivInBytes);
        decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] decryptedBytes = decryptionCipher.doFinal(messageInBytes);
        return new String(decryptedBytes);
    }

    private static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private static byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    public static void main(String[] args) {
        try {
            init();

            // Encrypted message (Base64 encoded)
            String encryptedMessage = "X2bq7YNwfEXnL9iPWakdnAZsPXMWpIX8QFfTsD8VSxNgEXnVOThFZ5TFQyxdPnGZ56PsG+K1jVcTRiKrsJZk0UdaZgzZXMw1qEIkEy7StG+BrBO9kpjVC1s88LiM";

            // Use the hardcoded IV to decrypt
            System.out.println("Encrypted message: " + encryptedMessage);
            String decryptedMessage = decrypt(encryptedMessage);
            System.out.println("Decrypted message: " + decryptedMessage);

            Student student = convertJsonStringToObject(decryptedMessage);
            // Store.saveStudent(student); // Non-static field cannot be referenced from a static context

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Student convertJsonStringToObject(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        Student student = new Student();
        try {
            student = objectMapper.readValue(jsonString, Student.class);
            System.out.println(student.toString());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return student;
    }
}
