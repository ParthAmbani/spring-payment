package com.payment.gateways.payGateways.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.gateways.payGateways.model.PaymentRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.util.Base64;

public class EncryptionUtil {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String SECRET_KEY = "6a0fcbf6ba755ffb14d0ab4e1bda6c7dd8f309abf2f684fa4f9c1c91d25fbc9b"; // Use your own key
    private static final String IV = "a3e1c1a2f7b1c9d9f0c6a2d3b4e1a1f9"; // Use your own IV
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static PaymentRequest decrypt(String encryptedData) throws Exception {
//        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] key = hexStringToByteArray(SECRET_KEY);
        byte[] iv = hexStringToByteArray(IV);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Decrypt
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        String decryptedText = new String(decrypted, "UTF-8");
        System.out.println("Decrypted: " + decryptedText);

        // Convert JSON to PaymentRequest
        return objectMapper.readValue(decryptedText, PaymentRequest.class);
    }

    // Helper method to convert hex string to byte array
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
