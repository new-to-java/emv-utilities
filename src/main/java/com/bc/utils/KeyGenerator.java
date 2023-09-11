package com.bc.utils;

import com.bc.enums.CryptogramVersionNumber;
import com.bc.enums.KeyType;
import com.bc.enums.UdkDerivationOption;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class KeyGenerator {
    public String pan;
    public String panSeq;
    public String atc;
    public String mkCryptogram;
    public String udkCryptogram;
    public KeyType keyType;
    public UdkDerivationOption udkDerivationOption;
    public CryptogramVersionNumber cryptogramVersionNumber;
    public boolean forceOdd;
    public boolean debug;

    public String getKey() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        switch (keyType){
            case UDK_CRYPTOGRAM:
                switch (udkDerivationOption){
                    case Option_A -> {
                        return getUniqueDerivationKeyOptionA();
                    }
                    case Option_B -> {
                        return getUniqueDerivationKeyOptionB();
                    }
                }

            case SEK_CRYPTOGRAM:
                return getUniqueSessionKey();
        }
        return null;
    }

    /**
     * Generate and return a UDK using Option A for a given PAN and PAN Sequence Number based on a Cryptogram MasterKey
     * @return UDK derived using method A
     * @throws NoSuchPaddingException Could throw this exception
     * @throws IllegalBlockSizeException Could throw this exception
     * @throws NoSuchAlgorithmException Could throw this exception
     * @throws BadPaddingException Could throw this exception
     * @throws InvalidKeyException Could throw this exception
     */
    private String getUniqueDerivationKeyOptionA() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String udkLeft;
        String udkRight;
        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        cryptoFunctions.setInputData(buildUDKLeftComponentData());
        cryptoFunctions.setKey(mkCryptogram);
        udkLeft = cryptoFunctions.tDEAEncrypt();
        cryptoFunctions.setInputData(buildUDKRightComponentData());
        udkRight = cryptoFunctions.tDEAEncrypt();
//        cryptoFunctions.setInputData(buildUDKLeftComponentData());
        if (debug){
            System.out.println("Method A:");
            System.out.println("Udk left: " + udkLeft);
            System.out.println("Udk right: " + udkRight);
        }
        return udkLeft + udkRight;
    }

    /**
     * Generate and return a UDK for a given PAN and PAN Sequence Number based on a Cryptogram MasterKey
     * @return Card specific Unique derivation key
     * @throws NoSuchPaddingException Could throw this exception
     * @throws IllegalBlockSizeException Could throw this exception
     * @throws NoSuchAlgorithmException Could throw this exception
     * @throws BadPaddingException Could throw this exception
     * @throws InvalidKeyException Could throw this exception
     */
    private String getUniqueDerivationKeyOptionB() throws NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String udkLeft;
        String udkRight;
        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        cryptoFunctions.setInputData(buildUDKLeftComponentData());
        String sha1Hash = cryptoFunctions.calcSha1Hash();
        StringBuilder hashResult = new StringBuilder();
        // Extract numeric digits from SHA1 hash till 16 digits are extracted or SHA1 hash fully parsed
        for(int i = 0; i < sha1Hash.length(); i++){
            char sha1Char = sha1Hash.charAt(i);
            switch (sha1Char){
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> hashResult.append(sha1Char);
            }
            if (hashResult.length() == 16){
                break;
            }
        }
        // If initial extract of numeric digits from SHA1 hash did not yield 16 digits, then decimalise the
        // hexadecimal digits using following decimalisation table, until 16 digits are yielded
        // 1. 'a' = '0';
        // 2. 'b' = '1';
        // 3. 'c' = '2';
        // 4. 'd' = '3';
        // 5. 'e' = '4';
        // 6. 'f' = '5';
        if (hashResult.toString().length() < 16){
            for(int i = 0; i < sha1Hash.length(); i++){
                char sha1Char = sha1Hash.charAt(i);
                switch (sha1Char){
                    case 'a' -> hashResult.append("0");
                    case 'b' -> hashResult.append("1");
                    case 'c' -> hashResult.append("2");
                    case 'd' -> hashResult.append("3");
                    case 'e' -> hashResult.append("4");
                    case 'f' -> hashResult.append("5");
                }
                if(hashResult.length() == 16){
                    break;
                }
            }
        }
        cryptoFunctions.setInputData(hashResult.toString());
        cryptoFunctions.setKey(mkCryptogram);
        udkLeft = cryptoFunctions.tDEAEncrypt();
        cryptoFunctions.setInputData(exclusiveOr(hashResult.toString()));
        udkRight = cryptoFunctions.tDEAEncrypt();
        cryptoFunctions.setInputData(hashResult.toString());
        if (debug) {
            System.out.println("Method B:");
            System.out.println("Sha1 Hash: " + sha1Hash);
            System.out.println("Hash Result: " + hashResult);
            System.out.println("Udk left: " + udkLeft);
            System.out.println("Udk left:  " + udkRight);
        }

        return udkLeft + udkRight;
    }

    private String getUniqueSessionKey() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        //Return udk as session key for CVN_10 & CNV_14
        if ((cryptogramVersionNumber == CryptogramVersionNumber.CVN_10)
        || (cryptogramVersionNumber == CryptogramVersionNumber.CVN_14)) return udkCryptogram;
        // For CVN_18 and CVN_22 derive a sessionkey from udk
        String uskLeft;
        String uskRight;
        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        cryptoFunctions.setInputData(buildSEKLeftComponentData());
        cryptoFunctions.setKey(udkCryptogram);
        uskLeft = cryptoFunctions.tDEAEncrypt();
        cryptoFunctions.setInputData(buildSEKRightComponentData());
        uskRight = cryptoFunctions.tDEAEncrypt();
        if (debug) {
            System.out.println("Session Key left: " + uskLeft);
            System.out.println("Session Key right: " + uskRight);
        }

        return uskLeft + uskRight;
    }

    /**
     * Builds left component data that will be encrypted using the Cryptogram Master Key to derive card level UDK
     * left component
     * @return left component data for UDK
     */
    private String buildUDKLeftComponentData() {
        if (debug) {
            System.out.println("UDK left component data: " + udkInputData(pan, panSeq, true));
        }
        return udkInputData(pan, panSeq, true);
    }

    /**
     * Builds right component data that will be encrypted using the Cryptogram Master Key to derive card level UDK
     * right component
     * @return right component data for UDK
     */
    private String buildUDKRightComponentData() {
        if (debug) {
            System.out.println("UDK right component data: " + udkInputData(pan, panSeq, false));
        }
        return udkInputData(pan, panSeq, false);
    }

    /**
     * Reformat card data to be inline with UDK derivation. Built as per EMV 4.3 Book 2: Security and Key Management
     * OPTION_A for Master Key derivation.
     *
     * @param pan      PAN number associated with the card
     * @param panSeq   PAN sequence number associated with the card
     * @return         PAN and PAN Sequence number combined as per OPTION_A
     */
    private String udkInputData(String pan, String panSeq, boolean udkA){

        String panAndSeq = (pan + panSeq);
        String padChar = "0";
        int UDK_DATA_LEN = 16;

        if (panAndSeq.length() < UDK_DATA_LEN) {
            panAndSeq = padChar.repeat(UDK_DATA_LEN - panAndSeq.length()) + panAndSeq;
        }else {
            panAndSeq = panAndSeq.substring((panAndSeq.length() - UDK_DATA_LEN)) ;
        }

        if (udkA) {
            return panAndSeq;
        } else{
            return exclusiveOr(panAndSeq);
        }

    }

    /**
     * Perform exclusive or operation on UDK component L with 16 F and return the xOred result as a String
     * @param panAndSeq This value will be set to the same value as UDK left component data
     * @return Exclusive or data
     */
    private String exclusiveOr(String panAndSeq){

        String ALLF = "FFFFFFFFFFFFFFFF";

        byte [] panAndSeqBytes = HexFormat.of().parseHex(panAndSeq); // Convert panAndSeq to Hexadecimal byte array
        byte [] ALL16FBytes = HexFormat.of().parseHex(ALLF); // Convert 16F chars to Hexadecimal byte array
        byte [] xOredPanAndSeq = new byte [ALL16FBytes.length]; // New byte array to store xored value

        for(int i = 0; i < panAndSeqBytes.length; i++){
            xOredPanAndSeq[i] = (byte) (panAndSeqBytes[i] ^ ALL16FBytes[i]);
        }
        if (debug){
            System.out.println("Hexformat xored PAN and Sequence: " + HexFormat.of().formatHex(xOredPanAndSeq));
        }

        return HexFormat.of().formatHex(xOredPanAndSeq);
    }

    /**
     * Builds left component data that will be encrypted using the UDK key to derive session key
     * left component
     * @return left component data for session key
     */
    private String buildSEKLeftComponentData() {
        String atcString = atc;
        return "0".repeat(4  - atcString.length()) + atcString + "F00000000000";
    }

    /**
     * Builds right component data that will be encrypted using the Cryptogram Master Key to derive card level UDK
     * right component
     * @return right component data for UDK
     */
    private String buildSEKRightComponentData() {
        String atcString = atc;
        return "0".repeat(4  - atcString.length()) + atcString + "0F0000000000";
    }

    /**
     * Force a key to odd parity by setting LSB of each byte of the key adjusted to ensure there are odd number of
     * bits set to 1.
     * @param key Key to be force converted to odd parity
     * @return Returns an updated key value with odd parity
     */
    private String translateKeyToOddParity(String key){
        int j = 0;
        StringBuilder oddParityKey = new StringBuilder();
        for(int i = 0; i < (key.length() / 2); i++)
        {
            int keyByte = Integer.parseInt(key.substring(j, j + 2), 16);
            if (isOddParity(keyByte)){
                keyByte ^= 1;
            }
            String hexByte = Integer.toHexString(keyByte);
            oddParityKey.append("0".repeat(2 - hexByte.length())).append(Integer.toHexString(keyByte));
            System.out.println(key.substring(j, j + 2) + " " + Integer.toHexString(keyByte) );
            j += 2;
        }
        return oddParityKey.toString();
    }

    /**
     * Check an integer passed to verify if an odd or even number of bits are set
     * @param checkByte Integer to be checked for bit parity
     * @return Return true if the integer has odd parity.
     */
    public boolean isOddParity(int checkByte){
        return (Integer.bitCount(checkByte) & 1) != 1;
    }

}