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
        if (validatePinRequest()){
            cryptoFunctions.setKey(zonePinKey);
            cryptoFunctions.setInputData(pinBlock);
            decryptedPinBlock = cryptoFunctions.tDEADecrypt().toUpperCase();
            pinBlockFormat = decryptedPinBlock.charAt(0);
            pinLength = decryptedPinBlock.charAt(1);
            clearPin = derivePinFromPinBlock();
        }
    }

    /**
     * Validate the Pinblock functions request object
     * @return True if request object is valid, else return False
     */
    private boolean validatePinRequest(){

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
}