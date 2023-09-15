package com.bc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parse issuer application data
 */

public class IADParser {
    /**
     * Parse issuer application data based on payment scheme
     * @param iad Issued Application Data received in the reuqest
     * @return List of strings containing parsed IAD data
    */
    public static void parse(String iad, boolean isVisaPan) {

//        if (isVisaPan) return visaIadParser(iad);
//        else return mastercardIadParser(iad);
        ccdCompliantIAD(iad);
        visaIAD("06011203A000000F0300001030800000046920AE952C48");

    }

    /**
     * Parser method for an EMV CCD compliant IAD
     * @param iad Issuer Application Data (9F10)
     * @return Map with parser IAD as follows:
     * Key                      Value
     * ---------                ----------------------
     * IAD Format               "A"
     * LengthIndicator          "0F"
     * CCI                      Common Core Identifier
     * DKI                      Derivation Key Index
     * CVR                      5 byte Card Verification Results (Not parsed, used CVR parser to parse)
     * Length Indicator         Discretionary Data length
     * DD                       Discretionary Data (Not parsed)
     */
    //private static Map ccdCompliantIAD(String iad){
    private static void ccdCompliantIAD(String iad){
        Map ccdIad = new HashMap();
        List<String> ccdCompliantIad = new ArrayList<>();
        ccdCompliantIad.add("Format-A"); // 0 - IAD format
        ccdCompliantIad.add(iad.substring(0,2)); // 1 - Length indicator: 1 byte
        ccdCompliantIad.add(iad.substring(2,4)); // 2 - Common Core Identifier: 1 byte
        ccdCompliantIad.add(iad.substring(4,6)); // 3 - Derivation Key Index: 1 byte
        ccdCompliantIad.add(iad.substring(6,16)); // 4 - Card Verification Results: 5 bytes
        ccdCompliantIad.add(iad.substring(16,32)); // 5 - Counters: 8 bytes
        ccdCompliantIad.add(iad.substring(32,34)); // 6 - Issuer Discretionary Data length: 1 byte
        ccdCompliantIad.add(iad.substring(34)); // 7 - Issuer Discretionary Data: 15 bytes
        int iadLength = Integer.parseInt(ccdCompliantIad.get(1), 16);
        int iddLength = Integer.parseInt(ccdCompliantIad.get(6), 16);
        int iadExpectedTotalLength = iadLength + iddLength + 2; // length increased by 2 to account for 1 byte each of two length indicators
        System.out.println("CCD Compliant IAD");
        System.out.println("-----------------------------------------------------------------------------");
        for (String string: ccdCompliantIad
             ) {
            System.out.println("IAD: " + string);
        }
        System.out.println("Valid IAD CCI: " + isCcdIadCciValid(ccdCompliantIad.get(2)));
        System.out.println("Valid IAD Length indicators: " + isCcdIadIddLengthIndicatorValid(ccdCompliantIad.get(1), ccdCompliantIad.get(6)));
        System.out.println("Valid IAD total length: " + isIadLengthValid(iad, iadExpectedTotalLength));
    }

    /**
     * Check if a CCD compliant IAD's IAD and IDD length indicators are set to 0xOF
     * @param iadLengthIndicator IAD length indicator extracted, must be set to 0x0F for CCD compliant IAD
     * @param iddLengthIndicator IDD length indicator extracted, must be set to 0x0F for CCD compliant IAD
     * @return True if valid, else return False
     */
    private static boolean isCcdIadIddLengthIndicatorValid(String iadLengthIndicator, String iddLengthIndicator){
        String HEX_0F = "0F";
        return iadLengthIndicator.equalsIgnoreCase(HEX_0F) && iddLengthIndicator.equalsIgnoreCase(HEX_0F);
    }

    /**
     * Check if a CCD compliant IAD has valid Common Core Identifier set
     * @param commonCoreIdentifier Common Core Identifier value extracted from IAD, format is:
     * - Left nibble: must be 0X0A
     * - Right nibble: must be:
     *   - 0X05: CCD TDEA Version 4.1 Cryptogram Version
     *   - 0x06: CCD AES Version 4.1 Cryptogram Version
     * @return True if valid, else return False
     */
    private static boolean isCcdIadCciValid(String commonCoreIdentifier){
        String FORMAT_A_TDEA_41 = "A5";
        String FORMAT_A_AES_41 = "A6";
        return commonCoreIdentifier.equalsIgnoreCase(FORMAT_A_AES_41) ||
                commonCoreIdentifier.equalsIgnoreCase(FORMAT_A_TDEA_41);
    }

    /**
     * Parser method for a Visa Format 0/1/3 and Format 2 IAD
     * @param iad Issuer Application Data (9F10)
     * @return Map with parser IAD
     */
    private static void visaIAD(String iad){
        List <String> format013Iad = visaFormat013IadParser(iad);
        System.out.println("Format-0/1/3 IAD");
        System.out.println("-----------------------------------------------------------------------------");
        for (String string: format013Iad
             ) {
            System.out.println("IAD: " + string);
        } ;
        int iadLength = Integer.parseInt(format013Iad.get(1), 16);
        int iddLength = Integer.parseInt(format013Iad.get(5), 16);
        int iadExpectedTotalLength = iadLength + iddLength + 2; // length increased by 2 to account for 1 byte each of two length indicators
        System.out.println("Visa Format 2 IAD");
        for (String string: format013Iad
        ) {
            System.out.println("IAD: " + string);
        }
        System.out.println("Valid IAD total length: " + isIadLengthValid(iad, iadExpectedTotalLength));

        List <String> format2Iad = visaFormat2IadParser("1F011203A000000F0300001030800000046920AE952C48000000000000000000");
        System.out.println("Format-2 IAD");
        System.out.println("-----------------------------------------------------------------------------");
        for (String string: format2Iad
        ) {
            System.out.println("IAD: " + string);
        }
        iadLength = Integer.parseInt(format2Iad.get(1), 16);
        iddLength = Integer.parseInt(format2Iad.get(5), 16);
        iadExpectedTotalLength = iadLength + iddLength + 2; // length increased by 2 to account for 1 byte each of two length indicators
        System.out.println("Valid IAD total length: " + isIadLengthValid(iad, iadExpectedTotalLength));
    }

    /**
     * Parse a Visa Format 0/1/3 IAD into components. Reference: Visa VIS 1.6 Table A-1 tag 9F10 details
     * Key                      Value
     * ---------                ----------------------
     * IAD Format               Format-0/1/3
     * LengthIndicator          "06" - Format-0/1/3
     * DKI                      Derivation Key Index
     * CryptogramVersion        10/18/22 (in Decimal)
     * CVR                      4 byte Card Verification Results (Not parsed, used CVR parser to parse), including 1 byte length.
     * IDD Length Indicator     Issuer Discretionary Data length
     * IDD                      Issuer Discretionary Data (Not parsed)
     *
     * @param iad Issuer application data (9F10)
     * @return Parsed list of strings comprised of IAD components
     */
    private static List<String> visaFormat013IadParser(String iad) {
        // IAD format 0/1/3
        List<String> visaFormat013Iad = new ArrayList<>();
        visaFormat013Iad.add("Format-0/1/3"); // 0 - IAD Format hardcoded to Format-0/1/3
        visaFormat013Iad.add(iad.substring(0,2)); // 1- Visa Discretionary Data length, must be "06"
        visaFormat013Iad.add(iad.substring(2, 4)); // 2 - Derivation Key Index
        visaFormat013Iad.add(String.valueOf(Integer.parseInt(iad.substring(4,6),16))); // 3 - Cryptogram Version Number
        visaFormat013Iad.add(iad.substring(6,14)); // 4 - 1 byte Card Verification Results length + Card Verification Results
        visaFormat013Iad.add(iad.substring(14,16)); // 5 - Issuer Discretionary Data length
        visaFormat013Iad.add(iad.substring(16)); // 6 - Issuer Discretionary Data
        return visaFormat013Iad;
    }

    /**
     * Parse a Visa Format 2 IAD into components. Reference: Visa VIS 1.6 Table A-1 tag 9F10 details
     * Key                  Value
     * ---------            ----------------------
     * ErrorCode            null - No errors/4 character error code
     * ErrorDescription     null - No errors/Error description
     * IAD Format           Format-2
     * LengthIndicator      "1F" - Format 2
     * CryptogramVersion    Left nibble - IAD format/Right Nibble CVN
     * DKI                  Derivation Key Index
     * CVR                  5 byte Card Verification Results (Not parsed, used CVR parser to parse)
     * IDD                  Issuer Discretionary Data (Not parsed)
     *
     * @param iad Issuer application data (9F10)
     * @return Parsed list of strings comprised of IAD components
     */
    private static List<String> visaFormat2IadParser(String iad) {
        List<String> visaFormat013Iad = new ArrayList<>();
        // IAD format 2 is not is use yet
        visaFormat013Iad.add("Format-2"); // 0 - IAD Format hardcoded to Format-2
        visaFormat013Iad.add(iad.substring(0,2)); // 1 - IAD length indicator, must be "1F"
        visaFormat013Iad.add(iad.substring(2,4)); // 2 - Left nibble IAD format/Right nibble Cryptogram Version Number
        visaFormat013Iad.add(iad.substring(4,6)); // 3 - Derivation Key Index
        visaFormat013Iad.add(iad.substring(6,16)); // 4-  5 byte Card Verification Results
        visaFormat013Iad.add(iad.substring(14,16)); // 5 - Issuer Discretionary Data length
        visaFormat013Iad.add(iad.substring(16)); // 6 - Issuer Discretionary Data
        return visaFormat013Iad;
    }

    /**
     * Perform IAD length check
     * @param iad Issuer Application Data to be validated
     * @param expectedLength Expected length of the IAD
     * @return True if length is valid, else return False
     */
    private static boolean isIadLengthValid(String iad, int expectedLength){
        return (iad.length()/2) == expectedLength;
    }

}