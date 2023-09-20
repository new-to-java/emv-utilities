package com.bc.service;

import com.bc.requestResponse.PinGenerateRequest;
import com.bc.requestResponse.PinGenerateResponse;
import com.bc.requestResponse.PvvGenerateRequest;
import com.bc.requestResponse.PvvGenerateResponse;
import com.bc.utils.IBM3624Pin;
import com.bc.utils.VisaPvv;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PinServiceImpl {
    /**
     * Driver method for generating PIN based on an Offset
     * @param pinGenerateRequest PinGenerateRequest object containing values required for PIN generation
     * @return PinGenerateResponse object containing the PIN and PIN Offset values generated
     */
    public static PinGenerateResponse generatePin(PinGenerateRequest pinGenerateRequest)
            throws Exception {
        IBM3624Pin ibm3624Pin = new IBM3624Pin();
        PinGenerateResponse pinGenerateResponse = new PinGenerateResponse();
        mapPinGenerationRequest(pinGenerateRequest, ibm3624Pin);
        ibm3624Pin.generateIBM3624Pin();
        mapPinGenerationResponse(pinGenerateResponse, ibm3624Pin);
        return pinGenerateResponse;
    }

    /**
     * Driver method for generating PVV based on Visa PVV method
     * @param pvvGenerateRequest PvvGenerateRequest object containing values required for PVV generation
     * @return PvvGenerateResponse object containing the PVV value generated
     */
    public static PvvGenerateResponse generatePvv(PvvGenerateRequest pvvGenerateRequest)
            throws Exception {
        VisaPvv visaPvv = new VisaPvv();
        PvvGenerateResponse pvvGenerateResponse = new PvvGenerateResponse();
        mapPvvGenerationRequest(pvvGenerateRequest, visaPvv);
        visaPvv.generateVisaPvv();
        mapPvvGenerationResponse(pvvGenerateResponse, visaPvv);
        return pvvGenerateResponse;
    }

    /**
     * Map the PIN generation API request object to the IBM 3624 PIN Offset generation object attributes
     * @param pinGenerateRequest PIN generation response object
     * @param ibm3624Pin IBM 3624 PIN generation request object
     */
    private static void mapPinGenerationRequest(PinGenerateRequest pinGenerateRequest, IBM3624Pin ibm3624Pin){
        ibm3624Pin.setPan(pinGenerateRequest.getPan());
        ibm3624Pin.setPinLength(pinGenerateRequest.getPinLength());
        ibm3624Pin.setPinOffset(pinGenerateRequest.getPinOffset());
        ibm3624Pin.setPvk(pinGenerateRequest.getPinVerificationKey());
        ibm3624Pin.setGenerateNaturalPin(false);
    }

    /**
     * Map the PIN generation response from the IBM 3624 PIN Offset after calling the generateIBM3624Pin function
     * @param pinGenerateResponse PIN generation response object
     * @param ibm3624Pin IBM 3624 PIN generation object with PIN generated
     */
    private static void mapPinGenerationResponse(PinGenerateResponse pinGenerateResponse, IBM3624Pin ibm3624Pin){
        pinGenerateResponse.setPin(ibm3624Pin.getPin());
        pinGenerateResponse.setPinLength(ibm3624Pin.getPinLength());
        pinGenerateResponse.setPinOffset(ibm3624Pin.getPinOffset());
        pinGenerateResponse.setNaturalPin(ibm3624Pin.getNaturalPin());
    }

    /**
     * Map the PVV generation API request object to the Visa PVV generation object attributes
     * @param pvvGenerateRequest PVV generation request object
     * @param visaPvv Visa PVV generation request object
     */
    private static void mapPvvGenerationRequest(PvvGenerateRequest pvvGenerateRequest, VisaPvv visaPvv){
        visaPvv.setPan(pvvGenerateRequest.getPan());
        visaPvv.setPin(pvvGenerateRequest.getPin());
        visaPvv.setKey(pvvGenerateRequest.getPinVerificationKey());
        visaPvv.setKeyIndex(pvvGenerateRequest.getPinVerificationKeyIndex());
    }

    /**
     * Map the PVV generation response from the Visa PVV after calling the generateVisaPvv function
     * @param pvvGenerateResponse PVV generation response object
     * @param visaPvv Visa PVV generation object with PVV generated
     */
    private static void mapPvvGenerationResponse(PvvGenerateResponse pvvGenerateResponse, VisaPvv visaPvv){
        pvvGenerateResponse.setPin(visaPvv.getPin());
        pvvGenerateResponse.setPinLength(String.valueOf(visaPvv.getPin().length()));
        pvvGenerateResponse.setPinVerificationValue(visaPvv.getPinVerificationValue());
    }

}