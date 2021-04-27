package br.com.neogis.agritopo.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoHandler {
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

    // String plaintext -> Base64-encoded String ciphertext
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String encrypt(String plaintext) {
        try {
            // Generate a random 16-byte initialization vector
            byte initVector[] = new byte[16];
            (new Random()).nextBytes(initVector);
            IvParameterSpec iv = new IvParameterSpec(initVector);

            // prep the key
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            // prep the AES Cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            // Encode the plaintext as array of Bytes
            byte[] cipherbytes = cipher.doFinal(plaintext.getBytes());

            // Build the output message initVector + cipherbytes -> base64
            byte[] messagebytes = new byte[initVector.length + cipherbytes.length];

            System.arraycopy(initVector, 0, messagebytes, 0, 16);
            System.arraycopy(cipherbytes, 0, messagebytes, 16, cipherbytes.length);

            // Return the cipherbytes as a Base64-encoded string
            return messagebytes.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Base64-encoded String ciphertext -> String plaintext
    public String decrypt(String ciphertext) {
        try {
            byte[] cipherbytes = ciphertext.getBytes("UTF-8");

            byte[] initVector = Arrays.copyOfRange(cipherbytes,0,16);

            byte[] messagebytes = Arrays.copyOfRange(cipherbytes,16,cipherbytes.length);

            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            // Convert the ciphertext Base64-encoded String back to bytes, and
            // then decrypt
            byte[] byte_array = cipher.doFinal(messagebytes);

            // Return plaintext as String
            return new String(byte_array, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}