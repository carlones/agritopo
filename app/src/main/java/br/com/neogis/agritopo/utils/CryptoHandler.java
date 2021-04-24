package br.com.neogis.agritopo.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

import static br.com.neogis.agritopo.utils.Utils.substring;


public class CryptoHandler {
    private String SecretKey = Constantes.K;
    private String IV = "jfioq34ju98jqfaw";

    private static CryptoHandler instance = null;

    public static CryptoHandler getInstance() {

        if (instance == null) {
            instance = new CryptoHandler();
        }
        return instance;
    }

    public String encrypt(String message) throws NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException,
            UnsupportedEncodingException, InvalidAlgorithmParameterException {

        byte[] srcBuff = message.getBytes("UTF8");
        //here using substring because AES takes only 16 or 24 or 32 byte of key
        SecretKeySpec skeySpec = new
                SecretKeySpec(SecretKey.substring(0,32).getBytes(), "AES");
        IvParameterSpec ivSpec = new
                IvParameterSpec(IV.substring(0,16).getBytes());
        Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
        byte[] ivByte = IV.substring(0,16).getBytes();
        byte[] dstBuff = ecipher.doFinal(srcBuff);
        byte[] mensagem_criptografada = new byte[ivByte.length + dstBuff.length];
        System.arraycopy(ivByte, 0, mensagem_criptografada, 0, ivByte.length);
        System.arraycopy(dstBuff, 0, mensagem_criptografada, ivByte.length, dstBuff.length);
        String base64 = Base64.encodeToString(mensagem_criptografada, Base64.DEFAULT);
        return base64;
    }

    public String decrypt(String encrypted) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException {
        SecretKeySpec skeySpec = new
                SecretKeySpec(SecretKey.substring(0,32).getBytes(), "AES");
        //IvParameterSpec ivSpec = new
        //        IvParameterSpec(IV.substring(0,16).getBytes());
        byte[] raw = encrypted.getBytes();//Base64.decode(encrypted, Base64.DEFAULT);
        IvParameterSpec ivSpec = new IvParameterSpec(substring(raw,0,16));
        Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        ecipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
        byte[] mensagem_codificada = substring(raw,16,48);
        byte[] originalBytes = ecipher.doFinal(mensagem_codificada);
        String original = new String(originalBytes, "UTF8");
        return original;
    }
}