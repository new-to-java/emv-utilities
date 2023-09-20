package com.bc.utils;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.DecoderException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import static com.bc.constants.PINFunctions.*;

/**
 * Generate a VISA PVV based on an input PAN, PIN Verification Key Index, PIN and PIN Verification Key
 * When generating PVV from encrypted Transformation Security Parameter, the following apply:
 * - During the initial scan from left to right, numbers 0 through 9 will be selected
 * - When PVV is less than 4, second scan from left to right is done,taking into account hexadecimal digits A through F
 * - A substituted with 0
 * - B substituted with 1
 * - C substituted with 2
 * - D substituted with 3
 * - E substituted with 4
 * - F substituted with 5
 */

@Getter
@Setter
public class VisaPvv {

    private String key;
    private String keyIndex;
    private String pan;
    private String pin;
    private String pinVerificationValue;

    /**
     * Calculate Visa PIN Verification Value
     * @throws NoSuchPaddingException When invalid padding is supplied
     * @throws IllegalBlockSizeException When block size of the data or the key is invalid
     * @throws NoSuchAlgorithmException When and invalid algorithm is specified
     * @throws BadPaddingException When data padding is invalid
     * @throws InvalidKeyException When key passed is invalid
     */
    public void generateVisaPvv() throws NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, DecoderException {

        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        StringBuilder pvv = new StringBuilder();
        cryptoFunctions.setInputData(deriveTsp());
        cryptoFunctions.setKey(key);
        String encryptedTsp = cryptoFunctions.tDEAEncrypt();
        // Extract numeric digits, if any from the encrypted TSP data
        for (int i = 0; i < encryptedTsp.length(); i++) {
            String pvvDigit = encryptedTsp.substring(i, i + 1);
            if (DataChecker.isNumeric(pvvDigit)) {
                pvv.append(pvvDigit);
            }
            if (pvv.length() == 4) {
                break;
            }
        }
        // If PVV length is less than 4 digits, convert A through F hex chars to numbers by substituting x'10'
        if (pvv.length() < 4) {
            for (int i = 0; i < encryptedTsp.length(); i++) {
                String pvvDigit = encryptedTsp.substring(i, i + 1);
                if (!DataChecker.isNumeric(pvvDigit)) {
                    pvv.append(convertHexToDigits(pvvDigit));
                }
                if (pvv.length() == 4) {
                    break;
                }
            }
        }
        pinVerificationValue = pvv.toString();
    }

    /**
     * Derive Transformation Security Parameter based on PAN, PIN and PIN Verification Key Index
     * @return Derived TSP
     */
    private String deriveTsp(){

        System.out.println("TSP: " + pan.substring(((pan.length() - 1)
                - MAX_PVV_PAN_LEN), pan.length() - 1)
                + keyIndex
                + (pin.substring(0, MAX_PVV_TSP_PIN_LEN)));

        return pan.substring(((pan.length() - 1)
               - MAX_PVV_PAN_LEN), pan.length() - 1)
               + keyIndex
               + (pin.substring(0, MAX_PVV_TSP_PIN_LEN));

    }

    /**
     * Convert hexadecimal character to numeric digit by subtracting x"0" from hexadecimal char
     * @param hexChar Hexadecimal character
     * @return Converted numeric digit
     */
    private String convertHexToDigits(String hexChar){
         switch (hexChar) {
             case "A":
                 return  "0";
             case "B":
                 return  "1";
             case "C":
                 return  "2";
             case "D":
                 return  "3";
             case "E":
                 return  "4";
             case "F":
                 return  "5";
             default:
                 return null;
        }
    }
}
