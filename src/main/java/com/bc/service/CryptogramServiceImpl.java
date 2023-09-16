package com.bc.service;

import com.bc.enums.CryptogramVersionNumber;
import com.bc.enums.UdkDerivationOption;
import com.bc.requestResponse.ArqcGenerateRequest;
import com.bc.requestResponse.ArqcGenerateResponse;
import com.bc.utils.ArpcGen;
import com.bc.utils.ArqcGen;
import com.bc.utils.IADParser;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CryptogramServiceImpl {

    /**
     * Driver method for generating Authorisation Request Cryptogram (ARQC) and Authorisation Response Cryptogram (ARPC)
     * @param arqcGenerateRequest ArqcGenerateRequest object containing values required for ARQC and ARPC generation
     * @return ArqcGenerateResponse object containing the Arqc and ARPC values generated
     */
    public static ArqcGenerateResponse generateArqcAndArpc(ArqcGenerateRequest arqcGenerateRequest)
            throws Exception {
        ArqcGenerateResponse arqcGenerateResponse = new ArqcGenerateResponse();
        ArqcGen arqcGen = new ArqcGen();
        ArpcGen arpcGen = new ArpcGen();
        // Parse IAD and derive CVN
        IADParser iadParser = new IADParser();
        iadParser.parse(arqcGenerateRequest.getIssuerApplicationData(), checkForVisaPan(arqcGenerateRequest.getPan()));
        mapArqcGenerateRequest(arqcGenerateRequest, arqcGen, iadParser); // Is this a call by reference??
        String arqc = arqcGen.getArqc().toUpperCase();
        arqcGenerateResponse.setArqc(arqc);
        arpcGen.setArqc(arqc);
        arpcGen.setCsuMethod(!iadParser.getCvn().equals("10") && !iadParser.getCvn().equals("14"));
        arpcGen.setArcOrCsu(arqcGenerateRequest.getArcOrCsu());
        arpcGen.setSessionKey(arqcGen.getUskLeft() + arqcGen.getUskRight());
        arqcGenerateResponse.setArpc(arpcGen.getArpc().toUpperCase());
        return arqcGenerateResponse;
    }

    /**
     * Check if a given Pan is Visa pan or not by checking first character of PAN
     * @param pan Pan to be verified
     * @return True if visa pan, else return false
     */
    private static boolean checkForVisaPan(String pan){
        return pan.charAt(0) == '4';
    }


    /**
     * Map ArqcGenerateRequest object to ArqcGen object attributes
     * @param arqcGenerateRequest ArqcGenerateRequest API request object
     * @param arqcGen             Arqc generation utility request object
     * @throws Exception Exception ??? what to put here
     */
    private static void mapArqcGenerateRequest(ArqcGenerateRequest arqcGenerateRequest, ArqcGen arqcGen, IADParser iadParser) throws Exception
             {
        arqcGen.setMdkAc(arqcGenerateRequest.getMdkAc());
        arqcGen.setPan(arqcGenerateRequest.getPan());
        arqcGen.setPanSeqNbr("0".repeat(2 - arqcGenerateRequest.getPanSeqNbr().length()) + arqcGenerateRequest.getPanSeqNbr());
        arqcGen.setAmountAuthorised("0".repeat(12 - arqcGenerateRequest.getAmountAuthorised().length())
                + arqcGenerateRequest.getAmountAuthorised());
        arqcGen.setAmountOther("0".repeat(12 - arqcGenerateRequest.getAmountOther().length())
                + arqcGenerateRequest.getAmountOther());;
        arqcGen.setTerminalCountryCode("0".repeat(4 - arqcGenerateRequest.getTerminalCountryCode().length())
                + arqcGenerateRequest.getTerminalCountryCode());
        arqcGen.setTerminalVerificationResults(arqcGenerateRequest.getTerminalVerificationResults());
        arqcGen.setTransactionCurrencyCode("0".repeat(4 - arqcGenerateRequest.getTransactionCurrencyCode().length())
                + arqcGenerateRequest.getTransactionCurrencyCode());
        arqcGen.setTransactionDate(arqcGenerateRequest.getTransactionDate().substring(2,4)
                + arqcGenerateRequest.getTransactionDate().substring(5,7)
                + arqcGenerateRequest.getTransactionDate().substring(8));
        arqcGen.setTransactionType(arqcGenerateRequest.getTransactionType());
        arqcGen.setUnpredictableNumber(arqcGenerateRequest.getUnpredictableNumber());
        arqcGen.setApplicationInterchangeProfile(arqcGenerateRequest.getApplicationInterchangeProfile());
        arqcGen.setIssuerApplicationData(arqcGenerateRequest.getIssuerApplicationData());
        arqcGen.setApplicationTransactionCounter(arqcGenerateRequest.getApplicationTransactionCounter());
        arqcGen.setUdkDerivationOption(UdkDerivationOption.Option_A); // Eventually need an attribute in the request
        // Set CVN for ARQC generation
        switch (iadParser.getCvn()){
            case "10" -> arqcGen.setCryptogramVersionNumber(CryptogramVersionNumber.CVN_10);
            case "18" -> arqcGen.setCryptogramVersionNumber(CryptogramVersionNumber.CVN_18);
            case "22" -> arqcGen.setCryptogramVersionNumber(CryptogramVersionNumber.CVN_22);
        }
         // Need to move this to the API request object and introduce CSU for older CVNs
    }
}