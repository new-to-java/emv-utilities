package com.bc.constants;

/**
 * Definitions for all standard patterns that are used by the emv-utilities
 */
public class Patterns {
    // Decimal patterns
    public static final String DECIMAL_3_DIGITS = "^\\d{3}$";
    public static final String DECIMAL_4_DIGITS = "^\\d{4}$";
    public static final String DECIMAL_1TO2_DIGITS = "^[0-9]{1,2}$";
    public static final String DECIMAL_12_DIGITS = "^[0-9]{1,12}$";
    public static final String DECIMAL_16TO19_DIGITS = "^[0-9]{16}$";
    // Hexadecimal patterns
    public static final String HEXADECIMAL_14_64_DIGITS = "^[0-9A-Fa-f]{14,64}$";
    public static final String HEXADECIMAL_2_DIGITS = "^[0-9A-Fa-f]{2}$";
    public static final String HEXADECIMAL_4_DIGITS = "^[0-9A-Fa-f]{4}$";
    public static final String HEXADECIMAL_8_DIGITS = "^[0-9A-Fa-f]{8}$";
    public static final String HEXADECIMAL_10_DIGITS = "^[0-9A-Fa-f]{10}$";
    public static final String HEXADECIMAL_32_DIGITS = "^[0-9A-Fa-f]{32}$";
    public static final String ISO_DATE = "^\\d{4}-\\d{2}-\\d{2}$";
}