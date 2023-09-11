package com.bc.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * This class defines methods for performing cryptographic functions that are needed at various EMV data processing
 * options.
 * Note: This class only supports Master Key Derivation OPTION_A as of now
 *
 */
public class CryptoFunctions {

    private String key;
    private String inputData;

    public void setKey(String key) {
        this.key = key;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }

    /**
     * TDEA encrypt the input data based on the TDEA hexKey supplied.
     * @return Encrypted data
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String tDEAEncrypt() throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        final String DES_EDE = "DESede";
        final String ECB = "ECB";
        final String NO_PADDING = "NoPadding";

        // Since DES works on byte level data blocks, convert key and string to byte data before input
        byte [] hexKey = HexFormat.of().parseHex(formatTDEAKey());
        byte [] data = HexFormat.of().parseHex(inputData);

        SecretKeySpec masterKey = new SecretKeySpec(hexKey, DES_EDE);
        Cipher cryptography = Cipher.getInstance(DES_EDE + "/" + ECB + "/" + NO_PADDING);
        cryptography.init(Cipher.ENCRYPT_MODE, masterKey);
        byte [] encryptedData =  cryptography.doFinal(data);
        return  HexFormat.of().formatHex(encryptedData);
//        return Hex.encodeHexString(encryptedData);

    }

    public String tDEADecrypt() throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        final String DES_EDE = "DESede";
        final String ECB = "ECB";
        final String NO_PADDING = "NoPadding";

        // Since DES works on byte level data blocks, convert key and string to byte data before input
        byte [] hexKey = HexFormat.of().parseHex(formatTDEAKey());
        byte [] data = HexFormat.of().parseHex(inputData);

        SecretKeySpec masterKey = new SecretKeySpec(hexKey, DES_EDE);
        Cipher cryptography = Cipher.getInstance(DES_EDE + "/" + ECB + "/" + NO_PADDING);
        cryptography.init(Cipher.DECRYPT_MODE, masterKey);
        byte [] encryptedData =  cryptography.doFinal(data);
        return  HexFormat.of().formatHex(encryptedData);
//        return Hex.encodeHexString(encryptedData);

    }

    /**
     * Converts a Single or Double length TDEA key into a triple length key.
     * @return      Parsed TDEA key, that would be converted to triple length
     */
    private String formatTDEAKey(){
        switch (key.length()){
            case 16:    //  Repeat the single length DES key twice and form triple length DES key
                return key + key.repeat(2);
            case 32:    //  Append the first 16 bytes of the key to itself and form triple length DES key
                return key + (key.substring(0, 16));
        }
        return null;

    }

    /**
     * Calculate a SHA-1 hash for a given data
     * @return SHA-1 hash
     */
    public String calcSha1Hash() throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        return (new BigInteger(1, messageDigest.digest(inputData.getBytes()))).toString(16);

    }

}