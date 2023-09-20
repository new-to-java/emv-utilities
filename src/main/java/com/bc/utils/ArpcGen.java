package com.bc.utils;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Calculate ARPC based on the input data, key and method
 */
@Getter
@Setter
public class ArpcGen {
    private String sessionKey;
    private String arcOrCsu;
    private String arqc;
    private boolean csuMethod;
    private boolean isDebug;

    public String getArpc() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
            BadPaddingException, InvalidKeyException, DecoderException {
        if (csuMethod){
            return generateArpcWithCsu().toUpperCase();
        } else {
            return generateArpcWithArc().toUpperCase();
        }
    }

    /**
     * Compute ARPC using ARQC XOR ARC method
     * @return computed ARPC
     * @throws NoSuchPaddingException May throw this exception
     * @throws IllegalBlockSizeException May throw this exception
     * @throws NoSuchAlgorithmException May throw this exception
     * @throws BadPaddingException May throw this exception
     * @throws InvalidKeyException May throw this exception
     */
    private String generateArpcWithArc() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, DecoderException {

        Xor xor = new Xor();
        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        cryptoFunctions.setKey(sessionKey);
        if (isDebug){
            System.out.println("Session Key: " + getSessionKey());
            System.out.println("ARQC: " + getArqc());
            System.out.println("Hex ARC: " + convertStringTOHex(getArcOrCsu(), 12, "0", false));
        }
        cryptoFunctions.setInputData(xor.exclusiveOr(convertStringTOHex(
                getArcOrCsu(), 12, "0", false), getArqc()));
        return cryptoFunctions.tDEAEncrypt();
    }

    /**
     * Converts a string to equivalent hexadecimal string
     * @param input String to convert and pad
     * @param padLength Number of pad characters to insert
     * @param padChar Character to be used for padding
     * @param padLeft When set to true pad left, else pad right
     * @return String converted to hexadecimal format and padded using padChar
     */
    public String convertStringTOHex(String input, int padLength, String padChar, boolean padLeft){
        if (padLeft) {
//            return padChar.repeat(padLength) + HexFormat.of().formatHex(input.getBytes(StandardCharsets.UTF_8));
            return padChar.repeat(padLength) + Hex.encodeHexString(input.getBytes(StandardCharsets.UTF_8));
        } else {
            return Hex.encodeHexString((input.getBytes(StandardCharsets.UTF_8))) + padChar.repeat(padLength);
//            return HexFormat.of().formatHex(input.getBytes(StandardCharsets.UTF_8)) + padChar.repeat(padLength);
        }
    }

    private   String generateArpcWithCsu() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, DecoderException {

        //Build ARPC segments
        List<String> arpcSegments = buildArpcTransactionData();
        String uskLeft = getSessionKey().substring(0, 16);
        String uskRight = getSessionKey().substring(16);
        //Generate ARPC
        int loopCount = 0;
        String tempArpc = null;
        Xor  xor = new Xor();

        for (String transactionDataSegment: arpcSegments) {
            loopCount += 1;
            if (transactionDataSegment != null) {
                //Based on the document referenced, the first block must be xor'ed with "0x0000000000000000", which
                //would result in the same value being returned after xor. So I have  skipped implementing that step.
                if  (loopCount == 1){
                    tempArpc = transactionDataEncrypt(transactionDataSegment, uskLeft);
                    if (isDebug()) {
                        System.out.println(loopCount + " " + tempArpc);
                    }
                } else{
                    tempArpc = xor.exclusiveOr(transactionDataSegment, tempArpc);
                    tempArpc = transactionDataEncrypt(tempArpc, uskLeft);
                    if (isDebug()) {
                        System.out.println(loopCount + " " + tempArpc);
                    }
                }
            } else {
                break;
            }
        }
        return transactionDataEncrypt(transactionDataDecrypt(tempArpc, uskRight),
                uskLeft).substring(0,8)
                + getArcOrCsu();
    }

    private List<String> buildArpcTransactionData() {

        String HEX80 = "80";

        String arpcData = getArqc() + getArcOrCsu() + HEX80;
        int arpcDataLength = arpcData.length();

        if (arpcDataLength % 16 != 0){
            int nextMultiplierOf8 = ((arpcDataLength/16) + 1) * 16;
            int lengthShortBy = nextMultiplierOf8 - (arpcDataLength);
            arpcData = arpcData + "0".repeat(lengthShortBy);
        }

        if (isDebug()) {
            System.out.println("ARPC Data: " + arpcData + " / ARPC Data Length: " + arpcData.length());
        }

        return splitArpcData(arpcData);

    }

    private List<String> splitArpcData(String arpcData){

        List<String> arpcSegments = new ArrayList<>();
        int offsetCounter = 0;

        for(int i = 0; i < (arpcData.length()/16); i++){
            arpcSegments.add(arpcData.substring(offsetCounter,offsetCounter + 16));
            offsetCounter = offsetCounter + 16;
        }

        return arpcSegments;
    }

    private String transactionDataEncrypt(String data, String key) throws NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, DecoderException {

        CryptoFunctions cryptoFunctions = new CryptoFunctions();

        cryptoFunctions.setInputData(data);
        cryptoFunctions.setKey(key);
        return  cryptoFunctions.tDEAEncrypt();

    }

    private String transactionDataDecrypt(String data, String key) throws NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, DecoderException {

        CryptoFunctions cryptoFunctions = new CryptoFunctions();

        cryptoFunctions.setInputData(data);
        cryptoFunctions.setKey(key);
        return  cryptoFunctions.tDEADecrypt();

    }


}

