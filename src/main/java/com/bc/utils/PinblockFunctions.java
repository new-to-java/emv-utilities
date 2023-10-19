package com.bc.utils;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.DecoderException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class defines attributes and methods required for supporting various functions on PIN block.
 * Functions supported:
 * - Decrypt PIN Block - Method that will decrypt a PIN block and derive the PIN, Format and PIN length.
 */

@Getter
@Setter
public class PinblockFunctions {

    private String pan;
    private String pin;
    private String pinBlock;
    private String zonePinKey;
    private String decryptedPinBlock;
    private char pinBlockFormat;
    private char pinLength;
    private String clearPin;

    /**
     * Decrypt a PIN block supplied and generate the following:
     * - Clear Pin
     * - PIN Block Format
     * - PIN Length
     *
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @throws DecoderException
     */
    public void decryptPinblock() throws NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, DecoderException {

        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        if (validPinblockDecryptRequest()){
            cryptoFunctions.setKey(zonePinKey);
            cryptoFunctions.setInputData(pinBlock);
            decryptedPinBlock = cryptoFunctions.tDEADecrypt().toUpperCase();
            pinBlockFormat = decryptedPinBlock.charAt(0);
            pinLength = decryptedPinBlock.charAt(1);
            clearPin = derivePinFromPinBlock();
        }
    }

    public String generatePinblock() throws DecoderException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        if (validPinblockGenerateRequest()){
            decryptedPinBlock = generatePinblockDriver();
            cryptoFunctions.setInputData(decryptedPinBlock);
            cryptoFunctions.setKey(zonePinKey);
            pinBlock = cryptoFunctions.tDEAEncrypt().toUpperCase();
            pinLength = convertIntegerToHex(pin.length()).charAt(0);
            return generateISOFormat0PINblock();
        }
        return null;
    }

    /**
     * Validate the attributes required for Pinblock decrypt request
     * @return True if request object is valid, else return False
     */
    private boolean validPinblockDecryptRequest(){

        List<Boolean> validRequest = new ArrayList<>();
        validRequest.add(DataChecker.isNumeric(pan));
        validRequest.add(DataChecker.isHexadecimal(pinBlock));
        validRequest.add(DataChecker.isHexadecimal(zonePinKey));
        //Check if any of the validations have failed, if yes, return false, else at end of loop, return true
        for (Boolean aBoolean : validRequest) {
            if (!aBoolean) {
                return false;
            }
        }
        return true;
    }


    /**
     * Validate the attributes required for Pinblock decrypt request
     * @return True if request object is valid, else return False
     */
    private boolean validPinblockGenerateRequest(){

        List<Boolean> validRequest = new ArrayList<>();
        validRequest.add(DataChecker.isNumeric(pan));
        validRequest.add(DataChecker.isNumeric(pin));
        validRequest.add(DataChecker.isNumeric(String.valueOf(pinBlockFormat)));
        validRequest.add(DataChecker.isHexadecimal(zonePinKey));
        //Check if any of the validations have failed, if yes, return false, else at end of loop, return true
        for (Boolean aBoolean : validRequest) {
            if (!aBoolean) {
                return false;
            }
        }
        return true;
    }

    /**
     * Driver method for deriving Pinblock. This method supports derivation of the following PIN block formats:
     * - ISO Format-0
     * - ISO Format-1
     */

    private String derivePinFromPinBlock() throws DecoderException {
        switch (pinBlockFormat){
            case '0': return deriveISOFormat0PIN();
            case '1': return deriveISOFormat1PIN();
            default: return "Unsupported PIN Block format.";
        }
    }

    /**
     * Driver method for generating Pinblock. This method supports generation of the following PIN block formats:
     * - ISO Format-0
     * - ISO Format-1
     */

    private String generatePinblockDriver() throws DecoderException {
        switch (pinBlockFormat){
            case '0': return generateISOFormat0PINblock();
            case '1': return generateISOFormat1PINblock();
            default: return "Unsupported PIN Block format.";
        }
    }

    /**
     * Derive a clear PIN from an ISO Format-0 PIN block.
     * @return Clear PIN
     */
    private String deriveISOFormat0PIN() throws DecoderException {
        String format0Pan = "0000" + pan.substring(3,15);
        Xor xor =  new Xor();
        String xorPinBlock = xor.exclusiveOr(format0Pan, decryptedPinBlock);
        return xorPinBlock.substring(2, (xorPinBlock.charAt(1) - '0') + 2);
    }

    /**
     * Derive a clear PIN from an ISO Format-1 PIN block.
     * @return Clear PIN
     */
    private String deriveISOFormat1PIN(){
        return decryptedPinBlock.substring(2, (decryptedPinBlock.charAt(1) - '0') + 2);
    }

    /**
     * Derive an ISO Format-0 PIN block
     * @return ISO Format-0 PIN block
     */
    private String generateISOFormat0PINblock() throws DecoderException {

        String format0Pan = "0000" + pan.substring(3,15);
        String tempFormat0Pin = pinBlockFormat
                + convertIntegerToHex(pin.length())
                + pin;
        String format0Pin = tempFormat0Pin + "F".repeat(16 - tempFormat0Pin.length());
        Xor xor =  new Xor();
        return xor.exclusiveOr(format0Pan, format0Pin).toUpperCase();
    }

    /**
     * Derive an ISO Format-1 PIN block
     * @return ISO Format-1 PIN block
     */
    private String generateISOFormat1PINblock() throws DecoderException {

        String tempFormat1Pinblock = pinBlockFormat
                + convertIntegerToHex(pin.length())
                + pin;
        return tempFormat1Pinblock + randomValue();
    }

    /**
     * Generate a random n length hexadecimal string
     * @return Random hexadecimal string
     */
    private String randomValue(){
        Random random = new Random();
        int MAX = 15;
        int MIN = 1;
        StringBuilder randomPaddingValue = new StringBuilder();
        for (int i = 1; i <= 16 - (pin.length() + 2); i++){
            randomPaddingValue.append(convertIntegerToHex(random.nextInt(MAX - MIN) + MIN));
        }
        return randomPaddingValue.toString();
    }


    /**
     * Convert integer to equivalent Hexadecimal string
     * @return Hexadecimal string
     */
    private String convertIntegerToHex(int pinLength){
        return String.valueOf("0123456789ABCDEF".charAt(pinLength));
    }

}