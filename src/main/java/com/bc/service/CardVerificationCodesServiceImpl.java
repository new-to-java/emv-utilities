package com.bc.service;

import com.bc.requestResponse.CvxGenerateRequest;
import com.bc.requestResponse.CvxGenerateResponse;
import com.bc.utils.CvxGen;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CardVerificationCodesServiceImpl {

    /**
     * Driver method for generating Card Verification Codes (CVx) and Authorisation Response Cryptogram (ARPC)
     * @param cvxGenerateRequest CvxGenerateRequest object containing values required for CVx generation
     * @return CvxGenerateResponse object containing the CVx alue generated
     */
    public static CvxGenerateResponse generateCvx(CvxGenerateRequest cvxGenerateRequest)
            throws Exception {
        CvxGenerateResponse cvxGenerateResponse = new CvxGenerateResponse();
        CvxGen cvxGen = new CvxGen();
        cvxGen.setPan(cvxGenerateRequest.getPan());
        cvxGen.setExpiryDate(cvxGenerateRequest.getExpiryDate());
        cvxGen.setServiceCode(cvxGenerateRequest.getServiceCode());
        cvxGen.setCvk(cvxGenerateRequest.getCvk());
        cvxGenerateResponse.setCvx(cvxGen.generateCvx());
        if (cvxGenerateRequest.getServiceCode().equals("000")){
            cvxGenerateResponse.setCvxType("CVx2");
        } else if (cvxGenerateRequest.getServiceCode().equals("999")){
            cvxGenerateResponse.setCvxType("iCVx");
        } else {
            cvxGenerateResponse.setCvxType("CVx");
        }
        return cvxGenerateResponse;
    }
}