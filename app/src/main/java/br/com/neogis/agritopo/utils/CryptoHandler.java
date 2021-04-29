package br.com.neogis.agritopo.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.security.*;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoHandler {
    public static final int INIT_VECTOR_LENGTH = 16;
    private String key = Constantes.K;

    private static CryptoHandler instance = null;

    public static CryptoHandler getInstance() {

        if (instance == null) {
            instance = new CryptoHandler();
        }
        return instance;
    }

    public CryptoHandler(){
    }

    public static byte[] fromHexString(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    // String plaintext -> Base64-encoded String ciphertext
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String encrypt(String plaintext) {
        try {
            // Generate a random 16-byte initialization vector
            byte initVector[] = new byte[INIT_VECTOR_LENGTH];
            (new Random()).nextBytes(initVector);
            IvParameterSpec iv = new IvParameterSpec(initVector);

            // prep the key
            //SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            SecretKeySpec skeySpec = new SecretKeySpec(Base64.decode(key, 0), "AES");

            // prep the AES Cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            // Encode the plaintext as array of Bytes
            byte[] cipherbytes = cipher.doFinal(plaintext.getBytes());

            // Build the output message initVector + cipherbytes -> base64
            byte[] messagebytes = new byte[initVector.length + cipherbytes.length];

            System.arraycopy(initVector, 0, messagebytes, 0, INIT_VECTOR_LENGTH);
            System.arraycopy(cipherbytes, 0, messagebytes, INIT_VECTOR_LENGTH, cipherbytes.length);

            // Return the cipherbytes as a Base64-encoded string
            return messagebytes.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String decrypt(String cipherText) {
        try {
            byte[] encKey = Base64.decode(key, Base64.DEFAULT);
            byte[] cipherData = Base64.decode(cipherText, Base64.DEFAULT);
            Key secretKeySpec = new SecretKeySpec(encKey, "AES");
            Cipher decrypter = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(cipherData, 0, INIT_VECTOR_LENGTH);
            decrypter.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            String result = new String(decrypter.doFinal(cipherData, INIT_VECTOR_LENGTH, cipherData.length - INIT_VECTOR_LENGTH));
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}