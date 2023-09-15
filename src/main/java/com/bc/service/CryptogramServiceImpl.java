package com.bc.service;

import com.bc.enums.CryptogramVersionNumber;
import com.bc.enums.UdkDerivationOption;
import com.bc.requestResponse.ArqcGenerateRequest;
import com.bc.requestResponse.ArqcGenerateResponse;
import com.bc.utils.ArpcGen;
import com.bc.utils.ArqcGen;
import com.bc.utils.IADParser;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.logging.Logger;

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
        boolean isValidArqcGenerationRequest = validateArqcGenerateRequest(arqcGenerateRequest);
        boolean isVisaPan = checkForVisaPan(arqcGenerateRequest.getPan());
        System.out.println("Visa PAN: " + isVisaPan);
//        List<String> parsedIad = issuerApplicationDataParser(arqcGenerateRequest.getIssuerApplicationData(), isVisaPan);
//        String cryptogramVersionNumber =  parsedIad.get(1);

        if (isValidArqcGenerationRequest){
            ArqcGen arqcGen = new ArqcGen();
            ArpcGen arpcGen = new ArpcGen();
            mapArqcGenerateRequest(arqcGenerateRequest, arqcGen); // Is this a call by reference??
            String arqc = arqcGen.getArqc().toUpperCase();
            arqcGenerateResponse.setArqc(arqc);
            arpcGen.setArqc(arqc);
            arpcGen.setCsuMethod(true);
            arpcGen.setArcOrCsu("00800000");
            arpcGen.setSessionKey(arqcGen.getUskLeft() + arqcGen.getUskRight());
            arqcGenerateResponse.setArpc(arpcGen.getArpc().toUpperCase());
        }
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
    private static void mapArqcGenerateRequest(ArqcGenerateRequest arqcGenerateRequest, ArqcGen arqcGen) throws Exception
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
        arqcGen.setCryptogramVersionNumber(CryptogramVersionNumber.CVN_18); // Need to move this to the API request object and introduce CSU for older CVNs
        arqcGen.setDebug(false);
        IADParser.parse(arqcGenerateRequest.getIssuerApplicationData(), false);

    }

    private static boolean validateArqcGenerateRequest(ArqcGenerateRequest arqcGenerateRequest) {
        return true;
    }
}