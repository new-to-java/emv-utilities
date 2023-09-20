package com.bc.utils;

import com.bc.enums.CryptogramVersionNumber;
import com.bc.enums.KeyType;
import com.bc.enums.UdkDerivationOption;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.DecoderException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Calculate ARQC based on the input data and key
 */
@Getter
@Setter

public class ArqcGen {

    private String mdkAc;
    private String pan;
    private String panSeqNbr;
    private String amountAuthorised;
    private String amountOther;
    private String terminalCountryCode;
    private String terminalVerificationResults;
    private String transactionCurrencyCode;
    private String transactionDate;
    private String transactionType;
    private String unpredictableNumber;
    private String applicationInterchangeProfile;
    private String applicationTransactionCounter;
    private String issuerApplicationData;
    private String udk;
    private String uskLeft;
    private String uskRight;
    private UdkDerivationOption udkDerivationOption;
    private CryptogramVersionNumber cryptogramVersionNumber;
    private boolean debug;

    public void setAmountAuthorised(String amountAuthorised) {
        try{
            new BigInteger(amountAuthorised);
        }  catch (NumberFormatException exception){
            throw new NumberFormatException("Amount authorised: Numeric expected, received \""
                    + amountAuthorised + "\".");
        }
        if (amountAuthorised.length() != 12){
            throw new NumberFormatException("Amount authorised: 12 characters expected, received \""
                    + amountAuthorised.length() + " bytes\".");
        }
        this.amountAuthorised = amountAuthorised;
    }

    public void setAmountOther(String amountOther) {
        try{
            new BigInteger(amountOther);
        } catch (NumberFormatException exception){
            throw new NumberFormatException("Amount other: Numeric expected, received \""
                    + amountOther + "\".");
        }
        if (amountAuthorised.length() != 12){
            throw new NumberFormatException("Amount other: 12 characters expected, received \""
                    + amountOther.length() + " bytes\".");
        }

        this.amountOther = amountOther;
    }

    public void setTerminalCountryCode(String terminalCountryCode) {
        try{
            Integer.parseInt(terminalCountryCode);
        } catch (NumberFormatException exception){
            throw new NumberFormatException("Terminal Country Code: Numeric expected, received \""
                    + terminalCountryCode + "\".");
        }
        if (terminalCountryCode.length() != 4){
            throw new NumberFormatException("Terminal Country Code: 4 characters expected, received \""
                    + terminalCountryCode.length() + " bytes\".");
        }

        this.terminalCountryCode = terminalCountryCode;
    }

    public void setTransactionCurrencyCode(String transactionCurrencyCode) {
        try{
            Integer.parseInt(transactionCurrencyCode);
        } catch (NumberFormatException exception){
            throw new NumberFormatException("Transaction Currency Code: Numeric expected, received \""
                    + transactionCurrencyCode + "\".");
        }
        if (transactionCurrencyCode.length() != 4){
            throw new NumberFormatException("Transaction Currency Code: 4 characters expected, received \""
                    + transactionCurrencyCode.length() + " bytes\".");
        }
        this.transactionCurrencyCode = transactionCurrencyCode;
    }

    public void setApplicationTransactionCounter(String applicationTransactionCounter) {

        if (applicationTransactionCounter.length() > 4){
            System.out.println("Application Transaction Counter: Max 4 characters expected, received \""
                    + transactionCurrencyCode.length() + "\" characters.");
        } else {
            this.applicationTransactionCounter = "0".repeat(4  - applicationTransactionCounter.length())
                    + applicationTransactionCounter;
        }
    }

    public void setUdkDerivationOption(UdkDerivationOption udkDerivationOption) throws Exception {
        if (udkDerivationOption != UdkDerivationOption.Option_A
                && udkDerivationOption != UdkDerivationOption.Option_B){
            throw(new Exception("Invalid UDK derivation option, " +
                    "only UDK derivation Option_A and Option_B is supported."));
        }
        this.udkDerivationOption = udkDerivationOption;
    }

    public void setCryptogramVersionNumber(CryptogramVersionNumber cryptogramVersionNumber) throws Exception {
        if (cryptogramVersionNumber != CryptogramVersionNumber.CVN_10
        && cryptogramVersionNumber != CryptogramVersionNumber.CVN_14
        && cryptogramVersionNumber != CryptogramVersionNumber.CVN_18
        && cryptogramVersionNumber != CryptogramVersionNumber.CVN_22){
            throw(new Exception("Invalid CVN, " +
                    "only CVN_10, CVN_14 CVN_18 and CVN_22 are supported."));
        }
        this.cryptogramVersionNumber = cryptogramVersionNumber;
    }

    public String getArqc() throws NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, DecoderException {

        //Generate a Unique Derivation Key
        setUdk(getUniqueDerivationKey());
        if (debug) {
            System.out.println("UDK  : " + getUdk());
        }
        String sessionKey = getSessionKey();
        setUskLeft(sessionKey.substring(0,16));
        setUskRight(sessionKey.substring(16,32));
        if (debug) {
            System.out.println("SEKL : " + getUskLeft());
            System.out.println("SEKR : " + getUskRight());
        }

        //Build ARQC segments
        List<String> arqcSegments = buildArqcTransactionData();

        //Generate ARQC
        int loopCount = 0;
        String tempArqc = null;
        Xor  xor = new Xor();

        for (String transactionDataSegment: arqcSegments) {
            loopCount += 1;
            if (transactionDataSegment != null) {
                //Based on the document referenced, the first block must be xor'ed with "0x0000000000000000", which
                //would result in the same value being returned after xor. So I have  skipped implementing that step.
                //and instead went with direct encryption of the data segment with session key left half
                if  (loopCount == 1){
                    tempArqc = transactionDataEncrypt(transactionDataSegment, getUskLeft());
                    if (debug) {
                        System.out.println(loopCount + " " + tempArqc);
                    }
                } else{
                    tempArqc = xor.exclusiveOr(transactionDataSegment, tempArqc);
                    tempArqc = transactionDataEncrypt(tempArqc, getUskLeft());
                    if (debug) {
                        System.out.println(loopCount + " " + tempArqc);
                    }
                }
            } else {
                break;
            }
        }

        return transactionDataEncrypt(transactionDataDecrypt(tempArqc, getUskRight()), getUskLeft()).toUpperCase();
    }

    private String getUniqueDerivationKey() throws NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, DecoderException {
        // Generate UDK key for PAN and Sequence number
        KeyGenerator keyGenerator = new KeyGenerator();
        keyGenerator.pan = getPan();
        keyGenerator.panSeq = getPanSeqNbr();
        keyGenerator.mkCryptogram = getMdkAc();
        keyGenerator.keyType = KeyType.UDK_CRYPTOGRAM;
        keyGenerator.udkDerivationOption = udkDerivationOption;
        return keyGenerator.getKey();
    }

    /**
     * UDK derivation method using OPTIONA
     * @return  Returns UDK card master key based on OPTIONA
     * @throws NoSuchPaddingException Could throw this exception
     * @throws IllegalBlockSizeException Could throw this exception
     * @throws NoSuchAlgorithmException Could throw this exception
     * @throws BadPaddingException Could throw this exception
     * @throws InvalidKeyException Could throw this exception
     */
    private String getSessionKey() throws NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, DecoderException {
        // Generate session key
        KeyGenerator keyGenerator = new KeyGenerator();
        keyGenerator.keyType = KeyType.SEK_CRYPTOGRAM;
        keyGenerator.udkCryptogram = getUdk();
        keyGenerator.cryptogramVersionNumber = getCryptogramVersionNumber();
        if (debug) {
            System.out.println("ATC: " + getApplicationTransactionCounter());
        }
        keyGenerator.atc = getApplicationTransactionCounter();
        return keyGenerator.getKey();
    }

    private List <String> buildArqcTransactionData() {

        String HEX80 = "80";
        String HEX00 = "00";

        String arqcData = getAmountAuthorised() + getAmountOther() +
                getTerminalCountryCode() + getTerminalVerificationResults() +
                getTransactionCurrencyCode() + getTransactionDate() +
                getTransactionType() + getUnpredictableNumber() +
                getApplicationInterchangeProfile() + getApplicationTransactionCounter();
        switch (cryptogramVersionNumber){
            case CVN_10:
            case CVN_14:
                if (debug) {
                    System.out.println("IAD       : " + getIssuerApplicationData());
                    System.out.println("IAD INPUT      : " + getIssuerApplicationData());
                }
                arqcData += getIssuerApplicationData() +  HEX00;
                break;
        case CVN_18:
            case CVN_22:
                arqcData += getIssuerApplicationData() + HEX80;
                break;
        }

        int arqcDataLength = arqcData.length();

        if (arqcDataLength % 16 != 0){
            int nextMultiplierOf8 = ((arqcDataLength/16) + 1) * 16;
            int lengthShortBy = nextMultiplierOf8 - (arqcDataLength);
            arqcData = arqcData + "0".repeat(lengthShortBy);
        }

        if (debug) {
            System.out.println("ARQC Data: " + arqcData + " / ARQC Data Length: " + arqcData.length());
        }

        return splitArqcData(arqcData);

    }

    private List<String> splitArqcData(String arqcData){

        List<String> arqcSegments = new ArrayList<>();
        int offsetCounter = 0;

        for(int i = 0; i < (arqcData.length()/16); i++){
            arqcSegments.add(arqcData.substring(offsetCounter,offsetCounter + 16));
            offsetCounter = offsetCounter + 16;
        }

        return arqcSegments;
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