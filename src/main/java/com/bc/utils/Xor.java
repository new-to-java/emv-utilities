package com.bc.utils;

import java.util.HexFormat;

public class Xor {
    /**
     * Perform exclusive or operation on ARQC data segments and return the xOred result as a String
     * @param leftOperand   Value to be used as the left operand in the xOr function
     * @param rightOperand  Value to be used as the right operand in the xOr function
     * @return Exclusive or data
     */
    public String exclusiveOr(String leftOperand, String rightOperand){

        byte [] leftOperandBytes = HexFormat.of().parseHex(leftOperand); // Convert leftOperand to Hexadecimal byte array
        byte [] rightOperandBytes = HexFormat.of().parseHex(rightOperand); // Convert rightOperand to Hexadecimal byte array
        byte [] xOredData = new byte [leftOperandBytes.length]; // New byte array to store xored value

        for(int i = 0; i < leftOperandBytes.length; i++){
            xOredData[i] = (byte) (leftOperandBytes[i] ^ rightOperandBytes[i]);
        }

        return HexFormat.of().formatHex(xOredData);
    }
}
