package com.example.trivia_backend.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.example.trivia_backend.models.Question;

public class HashUtils {

    public static String hashQuestion(Question q) {
        String data = q.question() + q.correctAnswer() + String.join(",", q.answers()) + q.category() + q.difficulty();
        return sha256(data);
    }

    private static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert to hex string
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

//    public static String hashQuestion(String questionText) {
//        try {
//            MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            byte[] hash = digest.digest(questionText.trim().toLowerCase().getBytes());
//            return Base64.getEncoder().encodeToString(hash);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("SHA-256 not available", e);
//        }
//    }
}
