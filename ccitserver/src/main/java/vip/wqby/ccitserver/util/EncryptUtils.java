package vip.wqby.ccitserver.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

public class EncryptUtils {

    public static String getMd5(String str) {
        StringBuilder res = new StringBuilder("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest();
            int d;
            for (byte b : bytes) {
                d = b & 0xff;
                if (d < 16) res.append("0");
                res.append(Integer.toHexString(d));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return res.toString();
    }

    public static String getURLEncoderString(String str, String encode) {
        String result = null;
        try {
            result = URLEncoder.encode(str, encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String encryptAESNoPadding(String content, String password, String iv) throws Exception {
        final String CHARSET_NAME = "utf-8";
        final String PADDING = "AES/CBC/NoPadding";
        final String AES = "AES";

        byte[] raw = password.getBytes(CHARSET_NAME);
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        Cipher cipher = Cipher.getInstance(PADDING);//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(content.getBytes(CHARSET_NAME));
        return Base64.getEncoder().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    public static String encryptAESPKCS5Padding(String content, String password, byte[] iv) throws Exception {
        final String CHARSET_NAME = "utf-8";
        final String PADDING = "AES/CBC/PKCS5Padding";
        final String AES = "AES";

        byte[] raw = password.getBytes(CHARSET_NAME);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        Cipher cipher = Cipher.getInstance(PADDING);//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(content.getBytes(CHARSET_NAME));
        return Base64.getEncoder().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    public static String encryptDESPKCS5Padding(String content, String password, byte[] iv) throws Exception {
        final String CHARSET_NAME = "utf-8";
        final String PADDING = "DES/CBC/PKCS5Padding";
        final String DES = "DES";
        byte[] raw = password.getBytes(CHARSET_NAME);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, DES);
        Cipher cipher = Cipher.getInstance(PADDING);//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(content.getBytes(CHARSET_NAME));
        return Base64.getEncoder().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }


//    public static String getUnicode(String str) {
//        StringBuilder result = new StringBuilder();
//        for (int i = 0; i < str.length(); i++) {
//            int chr1 = (char) str.charAt(i);
//            if (chr1=='\u3001'||chr1=='\u3002'||chr1=='\u201c'||chr1=='\u201d'||(chr1 >= 19968 && chr1 <= 171941)) {//汉字范围 \u4e00-\u9fa5 (中文)
//                result.append("\\u" + Integer.toHexString(chr1));
//            } else {
//                result.append(str.charAt(i));
//            }
//        }
//        return result.toString();
//    }

}
