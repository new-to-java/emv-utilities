package com.bc.utils;

import lombok.Getter;
import lombok.Setter;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Getter
@Setter
public class CvxGen {

    private String pan;
    private String expiryDate;
    private String serviceCode;
    private String cvk;
    public String generateCvx() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

//        String cvxInputData = pan + expiryDate + getCvxRequest.getServiceCode();
        StringBuilder cvxInputData = new StringBuilder();
        cvxInputData.append(pan)
                .append(expiryDate)
                .append(serviceCode);
        String cvxInput = cvxInputData + "0".repeat(32 - cvxInputData.length());
        String cvkA = cvk.substring(0,16);
        String cvkB = cvk.substring(16,32);
        return buildCvx(cvxInput, cvkA, cvkB);
    }

    private String buildCvx(String cvxData, String cvkA, String cvkB) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        Xor xor = new Xor();

        cryptoFunctions.setKey(cvkA);
        cryptoFunctions.setInputData(cvxData);
        String cvxDataLeft = cvxData.substring(0,16);
        String cvxDataRight = cvxData.substring(16,32);
        cryptoFunctions.setKey(cvkA);
        cryptoFunctions.setInputData(cvxDataLeft);
        cvxDataLeft = cryptoFunctions.tDEAEncrypt();
        cvxDataLeft = xor.exclusiveOr(cvxDataLeft, cvxDataRight);
        cryptoFunctions.setKey(cvkA);
        cryptoFunctions.setInputData(cvxDataLeft);
        cvxDataLeft = cryptoFunctions.tDEAEncrypt();
        cryptoFunctions.setKey(cvkB);
        cryptoFunctions.setInputData(cvxDataLeft);
        cvxDataLeft = cryptoFunctions.tDEADecrypt();
        cryptoFunctions.setKey(cvkA);
        cryptoFunctions.setInputData(cvxDataLeft);
        cvxDataLeft = cryptoFunctions.tDEAEncrypt();

        StringBuilder cvxValue = new StringBuilder();

        for (int i = 0; i < cvxDataLeft.length(); i++){
            String cvxChar = cvxDataLeft.substring(i, i + 1);
            switch (cvxChar){
                case "0":
                    cvxValue.append(cvxChar);
                    break;
                case "1":
                    cvxValue.append(cvxChar);
                    break;
                case "2":
                    cvxValue.append(cvxChar);
                    break;
                case "3":
                    cvxValue.append(cvxChar);
                    break;
                case "4":
                    cvxValue.append(cvxChar);
                    break;
                case "5":
                    cvxValue.append(cvxChar);
                    break;
                case "6":
                    cvxValue.append(cvxChar);
                    break;
                case "7":
                    cvxValue.append(cvxChar);
                    break;
                case "8":
                    cvxValue.append(cvxChar);
                    break;
                case "9":
                    cvxValue.append(cvxChar);
                    break;
            }
            if (cvxValue.length() == 3){
                break;
            }
        }

        return cvxValue.toString();
    }

}
