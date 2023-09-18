package com.bc.utils;

import com.bc.constants.PINFunctions;
import lombok.Getter;
import lombok.Setter;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.bc.constants.PINFunctions.*;

/**
 * This class generates an IBM 3624 compatible PIN and Offset
 * Functions:
 * 1. Generate an IBM 3624 Natural PIN(Offset zero).
 * 2. Generate an  IBM 3624 PIN based on a supplied PIN Offset.
 * 3. Option to supply an external decimalisation table, if none supplied, the class will use the default
 * decimalisation table, which will substite only alpha Hex characters as follows:
 * - Numbers 0 through 9 will be retained as is
 * - A substituted with 0
 * - B substituted with 1
 * - C substituted with 2
 * - D substituted with 3
 * - E substituted with 4
 * - F substituted with 5
 * This class supports methods and attributes for generating an IBM 3624 compatible PIN and Offset, with a minimum
 * length of 4, and supports a maximum PIN length of 16.
 */

@Getter
@Setter
public class IBM3624Pin {

    private String pvk;
    private String pan;
    private String [] decimalisationTable = null;
    private String pinOffset;
    private String pinLength;
    private boolean generateNaturalPin;
    private String pin;
    private String naturalPin;

    public void generateIBM3624Pin() throws NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        if (validatePinRequest()){
            cryptoFunctions.setKey(pvk);
            cryptoFunctions.setInputData(pan);
            pin = (calculateIntermediatePin(cryptoFunctions.tDEAEncrypt().toUpperCase(), decimalisationTable));
            if (!generateNaturalPin){
                naturalPin = pin.substring(0, Integer.parseInt(pinLength));
                pin = addOffset(pin, pinOffset);
            } else {
                pin = pin.substring(0, Integer.parseInt(pinLength));
                naturalPin = pin;
            }
        }
    }

    /**pan.substring(panLength - 13, panLength -1)
     * Derives PIN validation data from PAN
     */
    private String derivePinValidationData(String pan){

        int panLength = pan.length();
        if(panLength > 15) return pan + PINFunctions.PAD_CHAR.repeat(16 - panLength);
        else return pan.substring(16 - panLength, panLength);
    }

    /**
     * Derive natural PIN from encrypted PIN verification data, by substituting digits based on decimalisation table.
     * @param encryptedPinVerificationData PIN validation data, usually PAN, encrypted under PVK, 16 chars long
     * @param decimalisationTable Decimalisation table to be used for substitution of encrypted pin verification data
     *                            , primarily used to convert hexadecimal alphabetic chars A through F. However, this
     *                            may be used to substitute numeric characters as well
     * @return Derived intermediate PIN
     */
    private String calculateIntermediatePin(String encryptedPinVerificationData, String [] decimalisationTable){
        for (int i = 0; i < encryptedPinVerificationData.length(); i++){
            for (int j = 0; j < encryptedPinVerificationData.length(); j++){
                if (decimalisationTable[j].contains(String.valueOf(encryptedPinVerificationData.charAt(i)))) {
                    encryptedPinVerificationData = encryptedPinVerificationData
                            .replace(encryptedPinVerificationData.charAt(i), decimalisationTable[j].charAt(2));
                    break;
                }
            }

        }
        return encryptedPinVerificationData;
    }

    /**
     * Calculate PIN based on an input offset
     * @param naturalPin Natural PIN associated with the PAN
     * @param offset Offset value to be added to the PIN
     * @return PIN based on input offset
     */
    private String addOffset(String naturalPin, String offset){

        StringBuilder offsetAdjustedPin = new StringBuilder();
        String adjustedPinOffset = pinOffset.substring(pinOffset.length() - Integer.parseInt(pinLength));
        for(int i = 0; i < adjustedPinOffset.length(); i++){
            int pinDigit = Integer.parseInt(String.valueOf(naturalPin.charAt(i)));
            int offsetDigit = Integer.parseInt(String.valueOf(adjustedPinOffset.charAt(i)));
            offsetAdjustedPin.append((pinDigit + offsetDigit) % 10);
        }

        return offsetAdjustedPin.toString();

    }

    /**
     * Derive an IBM PIN offset from a natural PIN and customer selected PIN
     * @param customerPin Customer selected PIN
     * @param naturalPin Natural PIN
     * @return Derived PIN Offset value
     */
    public String deriveOffset(String customerPin, String naturalPin){
        StringBuilder pinOffset = new StringBuilder();
        if (customerPin.length() != naturalPin.length()){
            System.out.println("ERRR: NCPI01: Natural PIN and Customer PIN length must match.");
            return null;
        }
        for(int i = 0; i < naturalPin.length(); i++){
            int cpinDigit = Integer.parseInt(customerPin.substring(i, i + 1));
            int nPinDigit = Integer.parseInt(naturalPin.substring(i, i + 1));
            if (cpinDigit < nPinDigit) {
                cpinDigit += 10;
            }
            pinOffset.append(cpinDigit - nPinDigit);
        }
        return pinOffset.toString();
    }

    /** offset
     * Derive a Natural PIN from a customer selected PIN and PIN
     * @param customerPin Customer selected PIN
     * @param pinOffset PIN Offset
     * @return Natural PIN
     */

    public String deriveNaturalPin(String customerPin, String pinOffset){

        StringBuilder naturalPin = new StringBuilder();
        String adjustedPinOffset = pinOffset.substring(pinOffset.length() - Integer.parseInt(pinLength));
        if (customerPin.length() != adjustedPinOffset.length()){
            System.out.println("ERRR: OFFC01: Customer PIN and PIN offset length must match.");
            return null;
        }

        for(int i = 0; i < adjustedPinOffset.length(); i++){
            int cpinDigit = Integer.parseInt(customerPin.substring(i, i + 1));
            int pinOffsetDigit = Integer.parseInt(adjustedPinOffset.substring(i, i + 1));
            if (cpinDigit < pinOffsetDigit) {
                cpinDigit += 10;
            }
            naturalPin.append(cpinDigit - pinOffsetDigit);
        }

        return naturalPin.toString();

    }

    /**
     * Validate the PIN generation request object
     * @return True if request object is valid, else return False
     */
    public boolean validatePinRequest(){

        List<Boolean> validRequest = new ArrayList<>();
        validRequest.add(isNumeric(pan));
        validRequest.add(isNumeric(pinOffset));
        validRequest.add(isNumeric(pinLength));
        validRequest.add(isHexadecimal(pvk));
        if (decimalisationTable == null){
            System.out.println("WARN: DECE01: No decimalisation table supplied, using system default table.");
            System.out.println("Default Table: " + Arrays.toString(DEFAULT_DECIMALISATION_TABLE));
            decimalisationTable = DEFAULT_DECIMALISATION_TABLE;
        } else if (getDecimalisationTable().length != 16) {
            System.out.println("WARN: DECE02: Invalid decimalisation table supplied, using system default table.");
            System.out.println("Default Table: " + Arrays.toString(DEFAULT_DECIMALISATION_TABLE));
            decimalisationTable = DEFAULT_DECIMALISATION_TABLE;
        }
        validRequest.add(isHexadecimal(Arrays.toString(
                getDecimalisationTable()).replaceAll("[\\[\\]s :,]", "")));
        //Check if any of the validations have failed, if yes, return false, else at end of loop, return true
        for (Boolean aBoolean : validRequest) {
            if (!aBoolean) {
                return false;
            }
        }
        if (pinLength.length() > 2){
            pinLength = Integer.toString(MAX_PIN_LENGTH);
            System.out.println("WARN: PINL01: PIN length cannot exceed 16, resetting PIN length to 16.");
        }
        if (Integer.parseInt(pinLength) < MIN_PIN_LENGTH) {
            pinLength = Integer.toString(MIN_PIN_LENGTH);
            System.out.println("WARN: PINL02: PIN length cannot be less than 4, resetting PIN length to 4.");
        } else if (Integer.parseInt(pinLength) > MAX_PIN_LENGTH) {
            pinLength = Integer.toString(MAX_PIN_LENGTH);
            System.out.println("WARN: PINL03: PIN length cannot exceed 16, resetting PIN length to 16.");
        }
        // Basic validations passed, now ensure that the assigned PIN length is less than or equal to
        // the PIN offset length, else return error
        if (!generateNaturalPin && !(Integer.parseInt(pinLength) <= pinOffset.length())) {
            System.out.println("ERRR: PINOFF: PIN length must be less than or equal PIN offset length.");
            return false;
        }
        return true;
    }

    /**
     * Check if an input data supplied is numeric
     * @param checkData Input data to be verified as numeric
     * @return Returns true, if input data is numeric
     */
    public boolean isNumeric(String checkData){
        String NUM_PATTERN = "[0-9]+";
        Pattern hexPattern = Pattern.compile(NUM_PATTERN);
        Matcher hexPatternMatcher = hexPattern.matcher(checkData);
        return hexPatternMatcher.matches();
    }

    /**
     * Verifies if an input data supplied contains valid hexadecimal characters only
     * @param checkData Input data to be verified as hexadecimal
     * @return Returns true, if input data contains valid hexadecimal characters
     */
    public boolean isHexadecimal(String checkData){
        String HEX_PATTERN = "[0-9a-fA-F]+";
        Pattern hexPattern = Pattern.compile(HEX_PATTERN);
        Matcher hexPatternMatcher = hexPattern.matcher(checkData);
        return hexPatternMatcher.matches();
    }

}