package com.joker.module.common.encrypt.util;


import com.joker.module.common.encrypt.codec.Base64;
import com.joker.module.common.encrypt.crypto.BCrypt;
import com.joker.module.common.encrypt.crypto.SCryptUtil;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * @Author zhangjian
 * @Date 2015/10/20
 */
public class EncryptUtil {

    /**
     * md5 加密
     *
     * @param plainString 加密前密码
     * @return 加密后密码
     */
    public static String MD5(String plainString){
        String MD5 = "MD5";
        try {
            return getSecurePassword(plainString, null, MD5);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * SHA1 加密
     *
     * @param plainString 加密前密码
     * @param salt get方法获取
     * @return 加密后密码
     */
    public static String SHA_1(String plainString, String salt) {
        String SHA1 = "SHA-1";
        try {
            return getSecurePassword(plainString, salt, SHA1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * SHA256 加密
     *
     * @param plainString 加密前密码
     * @param salt get方法获取
     * @return 加密后密码
     */
    public static String SHA_256(String plainString, String salt){
        String SHA256 = "SHA-256";
        try {
            return getSecurePassword(plainString, salt, SHA256);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * SHA384 加密
     *
     * @param plainString 加密前密码
     * @param salt get方法获取
     * @return 加密后密码
     */
    public static String SHA_384(String plainString, String salt) throws NoSuchAlgorithmException {
        String SHA384 = "SHA-384";
        return getSecurePassword(plainString, salt, SHA384);
    }

    /**
     * SHA512 加密
     *
     * @param plainString 加密前密码
     * @param salt get方法获取
     * @return 加密后密码
     */
    public static String SHA_512(String plainString, String salt) throws NoSuchAlgorithmException {
        String SHA512 = "SHA-512";
        return getSecurePassword(plainString, salt, SHA512);
    }

    /**
     * 较高密码安全实现使用 PBKDF2WithHmacSHA1 算法
     *
     * @param password 需要加密的密码
     * @return 返回加密后的密码
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static String PBKDF2WithHmacSHA1(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt().getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    /**
     * 验证PBKDF2WithHmacSHA1
     *
     * @param originalPassword 加密前密码
     * @param storedPassword   加密后密码
     * @return 匹配结果 匹配true 不匹配 false
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static boolean matchPBKDF2WithHmacSHA1(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    /**
     * BCryptPassword 加密
     *
     * @param plainPassword 加密前密码
     * @return 加密后密码
     */
    public static String BCrypt(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    /**
     * 验证BCryptPassword 加密
     *
     * @param plainString  加密前密码
     * @param encodeString 加密后密码
     * @return 匹配结果 匹配true 不匹配false
     */
    public static boolean matchBCrypt(String plainString, String encodeString) {
        return BCrypt.checkpw(plainString, encodeString);
    }


    public static String SCrypt(String plainPassword) {
        return SCryptUtil.scrypt(plainPassword, 16, 16, 16);
    }

    public static boolean matchSCrypt(String plainString, String encodeString) {
        return SCryptUtil.check(plainString, encodeString);
    }

    public static String BASE64(String plainString){
       char[] chars =  Base64.encode(plainString.getBytes());
        return new String(chars);
    }

    public static String decodeBASE64(String encodeString){
        char[] chars = encodeString.toCharArray();
        byte[] bytes = Base64.decode(chars);
        return new String(bytes);
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    /**
     * 获取salt 必须保存作为下次匹配使用
     * @return
     */
    public static String getSalt() {
        return getSalt(16);
    }

    /**
     * 获取salt 必须保存作为下次匹配使用
     * @param length salt的长度
     * @return
     */
    public static String getSalt(int length) {
        //Always use a SecureRandom generator
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //Create array for salt
        byte[] salt = new byte[length];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt.toString();
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    private static String getSecurePassword(String passwordToHash, String salt, String type) throws NoSuchAlgorithmException {
        String generatedPassword = null;
        // Create MessageDigest instance for MD5
        MessageDigest md = MessageDigest.getInstance(type);

        byte[] bytes = null;
        //if it is MD5 ,not required salt
        if (salt == null) {
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            bytes = md.digest();
        } else {
            //Add password bytes to digest
            md.update(salt.getBytes());
            //Get the hash's bytes
            bytes = md.digest(passwordToHash.getBytes());
        }
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        //Get complete hashed password in hex format
        generatedPassword = sb.toString();
        return generatedPassword;
    }


    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String password = "admin";
        System.out.println("password = [" + password + "]");
        System.out.println("MD5 = [" + EncryptUtil.MD5(password) + "]");
        System.out.println("MD5 = [" + EncryptUtil.MD5(password) + "]");
        System.out.println("=======================================================");

        String salt = EncryptUtil.getSalt();
        System.out.println("SHA1 = [" + EncryptUtil.SHA_1(password, salt) + "]");
        System.out.println("SHA1 = [" + EncryptUtil.SHA_1(password, salt) + "]");
        System.out.println("=======================================================");

        System.out.println("SHA_256 = [" + EncryptUtil.SHA_256(password, salt) + "]");
        System.out.println("SHA_256 = [" + SHA_256(password, salt) + "]");
        System.out.println("=======================================================");

        System.out.println("SHA_384 = [" + SHA_384(password, salt) + "]");
        System.out.println("SHA_384 = [" + EncryptUtil.SHA_384(password, salt) + "]");
        System.out.println("=======================================================");

        System.out.println("SHA_512 = [" + EncryptUtil.SHA_512(password, salt) + "]");
        System.out.println("SHA_512 = [" + EncryptUtil.SHA_512(password, salt) + "]");
        System.out.println("=======================================================");

        String BCryptString = EncryptUtil.BCrypt(password);
        System.out.println("BCrypt = [" + BCryptString + "]");
        System.out.println("BCrypt = [" + EncryptUtil.matchBCrypt(password, "$2a$12$CEqSSsICZXbhAU62Yip/T.BYGzAsu5y1YVyO0re75nMOvaRUHGWqu") + "]");
        System.out.println("=======================================================");

        String PBKDF2WithHmacSHA1String = EncryptUtil.PBKDF2WithHmacSHA1(password);
        System.out.println("PBKDF2WithHmacSHA1String = [" + PBKDF2WithHmacSHA1String + "]");
        System.out.println("PBKDF2WithHmacSHA1String = [" + EncryptUtil.matchPBKDF2WithHmacSHA1(password, PBKDF2WithHmacSHA1String) + "]");
        System.out.println("=======================================================");

        String SCryptString = EncryptUtil.SCrypt(password);
        System.out.println("SCrypt = [" + SCryptString + "]");
        System.out.println("SCrypt = [" + EncryptUtil.matchSCrypt(password, SCryptString) + "]");

    }
}
