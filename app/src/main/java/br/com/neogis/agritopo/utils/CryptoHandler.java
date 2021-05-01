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

    public String encrypt(String plaintext) {
        try {
            byte initVector[] = new byte[INIT_VECTOR_LENGTH];
            (new Random()).nextBytes(initVector);
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(Base64.decode(key, 0), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] cipherbytes = cipher.doFinal(plaintext.getBytes());
            byte[] messagebytes = new byte[initVector.length + cipherbytes.length];
            System.arraycopy(initVector, 0, messagebytes, 0, INIT_VECTOR_LENGTH);
            System.arraycopy(cipherbytes, 0, messagebytes, INIT_VECTOR_LENGTH, cipherbytes.length);
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
            return new String(decrypter.doFinal(cipherData, INIT_VECTOR_LENGTH, cipherData.length - INIT_VECTOR_LENGTH));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}