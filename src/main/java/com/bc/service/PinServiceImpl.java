package com.bc.service;

import com.bc.constants.PINFunctions;
import com.bc.requestResponse.*;
import com.bc.utils.IBM3624Pin;
import com.bc.utils.PinblockFunctions;
import com.bc.utils.VisaPvv;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.codec.DecoderException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
     */
    public static PinblockGenerateResponse generatePinblock(PinblockGenerateRequest pinblockGenerateRequest) throws DecoderException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        PinblockFunctions pinblockFunctions = new PinblockFunctions();
        PinblockGenerateResponse pinblockGenerateResponse = new PinblockGenerateResponse();
        mapPinblockGenerateRequest(pinblockGenerateRequest, pinblockFunctions);
        pinblockFunctions.generatePinblock();
        mapPinblockGenerateResponse(pinblockGenerateResponse, pinblockFunctions);
        return pinblockGenerateResponse;
    }


    /**
     * Driver method for decrypting a PIN block
     * @param pinblockDecryptRequest object containing values required for PIN generation
     * @return pinblockDecryptResponse object containing the PIN and PIN Offset values generated
     */
    public static PinblockDecryptResponse decryptPinblock(PinblockDecryptRequest pinblockDecryptRequest)
            throws Exception {
        PinblockFunctions pinblockFunctions = new PinblockFunctions();
        PinblockDecryptResponse pinblockDecryptResponse = new PinblockDecryptResponse();
        mapPinblockDecryptRequest(pinblockDecryptRequest, pinblockFunctions);
        pinblockFunctions.decryptPinblock();
        mapPinblockDecryptResponse(pinblockDecryptResponse, pinblockFunctions);
        return pinblockDecryptResponse;
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
     */
    private static void mapPinblockDecryptRequest(PinblockDecryptRequest pinblockDecryptRequest, PinblockFunctions pinblockFunctions){
        pinblockFunctions.setPan(pinblockDecryptRequest.getPan());
        pinblockFunctions.setPinBlock(pinblockDecryptRequest.getPinBlock());
        pinblockFunctions.setZonePinKey(pinblockDecryptRequest.getZonePinKey());
    }

    /**
     */
    private static void mapPinblockDecryptResponse(PinblockDecryptResponse pinblockDecryptResponse, PinblockFunctions pinblockFunctions){
        pinblockDecryptResponse.setDecryptedPinblock(pinblockFunctions.getDecryptedPinBlock());
        pinblockDecryptResponse.setClearPin(pinblockFunctions.getClearPin());
        pinblockDecryptResponse.setPinLength(pinblockFunctions.getPinLength());
        pinblockDecryptResponse.setPinblockFormat(pinblockFunctions.getPinBlockFormat());
    }


    /**
     */
    private static void mapPinblockGenerateRequest
    (PinblockGenerateRequest pinblockGenerateRequest, PinblockFunctions pinblockFunctions){
        pinblockFunctions.setPan(pinblockGenerateRequest.getPan());
        pinblockFunctions.setPin(pinblockGenerateRequest.getPin());
        pinblockFunctions.setPinBlockFormat(pinblockGenerateRequest.getPinBlockFormat().charAt(0));
        pinblockFunctions.setZonePinKey(pinblockGenerateRequest.getZonePinKey());
    }

    /**
     */
    private static void mapPinblockGenerateResponse(PinblockGenerateResponse pinblockGenerateResponse, PinblockFunctions pinblockFunctions){
        pinblockGenerateResponse.setEncryptedPinblock(pinblockFunctions.getPinBlock());
        pinblockGenerateResponse.setClearPinblock(pinblockFunctions.getDecryptedPinBlock());
        pinblockGenerateResponse.setPinblockFormat(pinblockFunctions.getPinBlockFormat());
        pinblockGenerateResponse.setPinLength(pinblockFunctions.getPinLength());
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