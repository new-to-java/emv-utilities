package com.bc.constants;

/**
 * Defines constants used by various PIN generation algorithms
 */
public class PINFunctions {
    public static final String [] DEFAULT_DECIMALISATION_TABLE = {"0:0", "1:1", "2:2", "3:3", "4:4", "5:5", "6:6",
    "7:7","8:8", "9:9", "A:0", "B:1", "C:2", "D:3", "E:4", "F:5"}; //Probably should replace with a MAP
    public static final int MIN_PIN_LENGTH = 4;
    public static final int MAX_PIN_LENGTH = 16;
    public static final String PAD_CHAR = "0";
    public static final int MAX_PVV_PAN_LEN = 11;
    public static final int MAX_PVV_TSP_PIN_LEN = 4;
}