package com.bc.service;

import com.bc.enums.CryptogramVersionNumber;
import com.bc.enums.UdkDerivationOption;
import com.bc.requestResponse.ArqcGenerateRequest;
import com.bc.requestResponse.ArqcGenerateResponse;
import com.bc.utils.ArqcGen;
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
        boolean isValidArqcGenerationRequest = validateArqcGenerateRequest(arqcGenerateRequest);
        if (isValidArqcGenerationRequest){
            ArqcGen arqcGen = new ArqcGen();
            mapArqcGenerateRequest(arqcGenerateRequest, arqcGen);
            String arqc = arqcGen.getArqc();
            arqcGenerateResponse.setArqc(arqc);
            arqcGenerateResponse.setArpc("Wait for implmentation");
        }
        return arqcGenerateResponse;
    }

    /**
     * Map ArqcGenerateRequest object to ArqcGen object attributes
     *
     * @param arqcGenerateRequest ArqcGenerateRequest API request object
     * @param arqcGen             Arqc generation utility request object
     * @throws Exception Exception ??? what to put here
     */
    private static void mapArqcGenerateRequest(ArqcGenerateRequest arqcGenerateRequest, ArqcGen arqcGen) // Call by reference??
            throws Exception {

        arqcGen.setMdkAc(arqcGenerateRequest.getMdkAc());
        arqcGen.setPan(arqcGenerateRequest.getPan());
        arqcGen.setPanSeqNbr(arqcGen.getPanSeqNbr());
        arqcGen.setAmountAuthorised(arqcGenerateRequest.getAmountAuthorised());
        arqcGen.setAmountOther(arqcGenerateRequest.getAmountOther());
        arqcGen.setTerminalCountryCode(arqcGenerateRequest.getTerminalCountryCode());
        arqcGen.setTerminalVerificationResults(arqcGenerateRequest.getTerminalVerificationResults());
        arqcGen.setTransactionCurrencyCode(arqcGenerateRequest.getTransactionCurrencyCode());
        arqcGen.setTransactionDate(arqcGenerateRequest.getTransactionDate());
        arqcGen.setTransactionType(arqcGenerateRequest.getTransactionType());
        arqcGen.setUnpredictableNumber(arqcGenerateRequest.getUnpredictableNumber());
        arqcGen.setApplicationInterchangeProfile(arqcGenerateRequest.getApplicationInterchangeProfile());
        arqcGen.setIssuerApplicationData(arqcGenerateRequest.getIssuerApplicationData());
        arqcGen.setUdkDerivationOption(UdkDerivationOption.Option_A); // Possibly need adding in the API request object
        arqcGen.setCryptogramVersionNumber(CryptogramVersionNumber.CVN_18); // Possibly need adding in the API request object

    }

    private static boolean validateArqcGenerateRequest(ArqcGenerateRequest arqcGenerateRequest) {
        return true;
    }
}
