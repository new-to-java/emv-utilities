package com.bc.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Parse issuer application data
 */

public class IADParser {
    /**
     * Parse issuer application data based on payment scheme
     * @param iad Issued Application Data received in the reuqest
     * @return List of strings containing parsed IAD data
    */
    public static List<String> parse(String iad, boolean isVisaPan) {
        mchipIadParser(iad);
        if (isVisaPan){
            return visaIAD(iad); // Visa IADs
        } else if (iad.length() == 64){ // CCD compliant IAD
            return ccdCompliantIAD(iad);
        } else return mchipIadParser(iad); // MasterCard IADs
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
    private static List<String> ccdCompliantIAD(String iad){
        List<String> ccdCompliantIad = new ArrayList<>();
        ccdCompliantIad.add("Format-A"); // 0 - IAD format
        ccdCompliantIad.add(iad.substring(0,2)); // 1 - Length indicator: 1 byte
        ccdCompliantIad.add(iad.substring(2,4)); // 2 - Common Core Identifier: 1 byte
        ccdCompliantIad.add(iad.substring(4,6)); // 3 - Derivation Key Index: 1 byte
        ccdCompliantIad.add(iad.substring(6,16)); // 4 - Card Verification Results: 5 bytes
        ccdCompliantIad.add(iad.substring(16,32)); // 5 - Counters: 8 bytes
        ccdCompliantIad.add(iad.substring(32,34)); // 6 - Issuer Discretionary Data length: 1 byte
        ccdCompliantIad.add(iad.substring(34)); // 7 - Issuer Discretionary Data: 15 bytes
        return ccdCompliantIad;
    }

/*    /**
     * Check if a CCD compliant IAD's IAD and IDD length indicators are set to 0xOF
     * @param iadLengthIndicator IAD length indicator extracted, must be set to 0x0F for CCD compliant IAD
     * @param iddLengthIndicator IDD length indicator extracted, must be set to 0x0F for CCD compliant IAD
     * @return True if valid, else return False
     */
/*    private static boolean isCcdIadIddLengthIndicatorValid(String iadLengthIndicator, String iddLengthIndicator){
        String HEX_0F = "0F";
        return iadLengthIndicator.equalsIgnoreCase(HEX_0F) && iddLengthIndicator.equalsIgnoreCase(HEX_0F);
    }
*/
/*    /**
     * Check if a CCD compliant IAD has valid Common Core Identifier set
     * @param commonCoreIdentifier Common Core Identifier value extracted from IAD, format is:
     * - Left nibble: must be 0X0A
     * - Right nibble: must be:
     *   - 0X05: CCD TDEA Version 4.1 Cryptogram Version
     *   - 0x06: CCD AES Version 4.1 Cryptogram Version
     * @return True if valid, else return False
     */
/*    private static boolean isCcdIadCciValid(String commonCoreIdentifier){
        String FORMAT_A_TDEA_41 = "A5";
        String FORMAT_A_AES_41 = "A6";
        return commonCoreIdentifier.equalsIgnoreCase(FORMAT_A_AES_41) ||
                commonCoreIdentifier.equalsIgnoreCase(FORMAT_A_TDEA_41);
    }
*/
    /**
     * Parser method for a Visa Format 0/1/3 and Format 2 IAD
     * @param iad Issuer Application Data (9F10)
     * @return Map with parser IAD
     */
    private static List<String> visaIAD(String iad){
        switch (iad.length()/2){
            case 32 -> {
                return visaFormat2IadParser(iad);
            }
            default -> {
                return visaFormat013IadParser(iad);
            }
        }
    }

    /**
     * Parse a Visa Format 0/1/3 IAD into components. Reference: Visa VIS 1.6 Table A-1 tag 9F10 details
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
     * Mastercard m/Chip advance IAD parser driver method
     * @param iad Issuer application data (9F10)
     * @return Parsed list of strings comprised of IAD components
     */
    private static List<String> mchipIadParser(String iad){
        switch (iad.length()/2){
            case 8 -> {
                return mChip2122IadParser(iad);
            }
            case 9 -> {
                return mChip205IadParser(iad);
            }
            default -> {
                return mChip4IadParser(iad);
            }
        }
    }

    /**
     * Parse Mastercard m/Chip4 IAD into components. Reference: M/Chip advance card application specification
     * Key                  Value
     * ---------            ----------------------
     * IAD Format           M/Chip4
     * DKI                  Derivation Key Index
     * CVN                  Cryptogram Version Number
     * CVR                  6 byte Card Verification Results (Not parsed, used CVR parser to parse)
     * DAC/ICC              DAC/ICC Dynamic Number
     * Counters+LO-ATC      Plaintext/Encrypted counters & Last Online ATC
     *
     * @param iad Issuer application data (9F10)
     * @return Parsed list of strings comprised of IAD components
     */
    private static List<String> mChip4IadParser(String iad) {
        List<String> mastecardStandardFormatIad = new ArrayList<>();
        mastecardStandardFormatIad.add(iad.substring(0,2)); // 0 - Key Derivation Index
        mastecardStandardFormatIad.add(iad.substring(2,4)); // 1 - Cryptogram Version Number
        mastecardStandardFormatIad.add(iad.substring(4,16)); // 2 - Card Verification Results
        mastecardStandardFormatIad.add(iad.substring(16,20)); // 3 - DAC/ICC Dynamic Number
        mastecardStandardFormatIad.add(iad.substring(20)); // 4 - Plaintext/Encrypted Counters and LOATC
        return mastecardStandardFormatIad;
    }

    /**
     * Parse Mastercard m/Chip2.05 IAD into components. Reference: M/Chip advance card application specification
     * Key                  Value
     * ---------            ----------------------
     * IAD Format           M/Chip2.05
     * Length               IAD Length
     * DKI                  Derivation Key Index
     * CVN                  Cryptogram Version Number
     * CVR                  4 byte Card Verification Results (Not parsed, used CVR parser to parse)
     * DAC/ICC              DAC/ICC Dynamic Number
     *
     * @param iad Issuer application data (9F10)
     * @return Parsed list of strings comprised of IAD components
     */
    private static List<String> mChip205IadParser(String iad) {
        List<String> mastecardStandardFormatIad = new ArrayList<>();
        mastecardStandardFormatIad.add(iad.substring(0,2)); // 0 - Length
        mastecardStandardFormatIad.add(iad.substring(2,4)); // 1 - Key Derivation Index
        mastecardStandardFormatIad.add(iad.substring(4,6)); // 2 - Cryptogram Version Number
        mastecardStandardFormatIad.add(iad.substring(6,14)); // 3 - Card Verification Results
        mastecardStandardFormatIad.add(iad.substring(14,18)); // 4 - DAC/ICC Dynamic Number
        return mastecardStandardFormatIad;
    }

    /**
     * Parse Mastercard m/Chip2.10 IAD into components. Reference: M/Chip advance card application specification
     * Key                  Value
     * ---------            ----------------------
     * IAD Format           M/Chip2.10
     * DKI                  Derivation Key Index
     * CVN                  Cryptogram Version Number
     * CVR                  4 byte Card Verification Results (Not parsed, used CVR parser to parse)
     * DAC/ICC              DAC/ICC Dynamic Number
     *
     * @param iad Issuer application data (9F10)
     * @return Parsed list of strings comprised of IAD components
     */
    private static List<String> mChip2122IadParser(String iad) {
        List<String> mastecardStandardFormatIad = new ArrayList<>();
        mastecardStandardFormatIad.add(iad.substring(0,2)); // 0 - Key Derivation Index
        mastecardStandardFormatIad.add(iad.substring(2,4)); // 1 - Cryptogram Version Number
        mastecardStandardFormatIad.add(iad.substring(4,12)); // 2 - Card Verification Results
        mastecardStandardFormatIad.add(iad.substring(12,16)); // 3 - DAC/ICC Dynamic Number
        return mastecardStandardFormatIad;
    }

/*    /**
     * Perform IAD length check
     * @param iad Issuer Application Data to be validated
     * @param expectedLength Expected length of the IAD
     * @return True if length is valid, else return False
     */
/*    private static boolean isIadLengthValid(String iad, int expectedLength){
        return (iad.length()/2) == expectedLength;
    }
*/
}