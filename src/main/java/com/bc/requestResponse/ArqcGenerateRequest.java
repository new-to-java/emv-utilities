package com.bc.requestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import static com.bc.constants.Patterns.*;

@Getter
@Setter
public class ArqcGenerateRequest {
    @JsonProperty("Pan")
    @NotBlank(message = "Pan required, please provide a value.")
    @Pattern(regexp = DECIMAL_16TO19_DIGITS, message = "Pan is required, must be numeric and 16 digits long.")
    private String pan;
    @JsonProperty("PanSequence")
    @NotBlank(message = "PanSequence required, please provide a value.")
    @Pattern(regexp = DECIMAL_1TO2_DIGITS, message = "PanSequence required, must be numeric and 1 to 2 digits long.")
    private String panSeqNbr;
    @JsonProperty("CryptogramMasterKey")
    @NotBlank(message = "CryptogramMasterKey required, please provide a value.")
    @Pattern(regexp = HEXADECIMAL_32_DIGITS, message = "CryptogramMasterKey must be exactly 32 hexadecimal digits.")
    private String mdkAc;
    @JsonProperty("AmountAuthorised")
    @NotBlank(message = "AmountAuthorised required, please provide a value.")
    @Pattern(regexp = DECIMAL_12_DIGITS, message = "AmountAuthorised must be 1 to 12 digits long.")
    private String amountAuthorised;
    @JsonProperty("AmountOther")
    @NotBlank(message = "AmountOther required, please provide a value.")
    @Pattern(regexp = DECIMAL_12_DIGITS, message = "AmountOther must be 1 to 12 digits long.")
    private String amountOther;
    @JsonProperty("TerminalCountryCode")
    @NotBlank(message = "TerminalCountryCode required, please provide a value.")
    @Pattern(regexp = DECIMAL_3_DIGITS, message = "TerminalCountryCode must be a valid ISO 3166-1 3 digit numeric country code.")
    private String terminalCountryCode;
    @JsonProperty("TerminalVerificationResults")
    @NotBlank(message = "TerminalVerificationResults required, please provide a value.")
    @Pattern(regexp = HEXADECIMAL_10_DIGITS, message = "TerminalVerificationResults must be exactly 10 hexadecimal digits.")
    private String terminalVerificationResults;
    @JsonProperty("TransactionCurrencyCode")
    @NotBlank(message = "TransactionCurrencyCode required, please provide a value.")
    @Pattern(regexp = DECIMAL_3_DIGITS, message = "TransactionCurrencyCode must be a valid ISO 4217 3 digit numeric currency code.")
    private String transactionCurrencyCode;
    @JsonProperty("TransactionDate")
    @NotBlank(message = "TransactionDate required, please provide a value.")
    @Pattern(regexp = ISO_DATE, message = "TransactionDate must be ISO Date in 'YYYY-MM-DD' format.")
    private String transactionDate;
    @JsonProperty("TransactionType")
    @NotBlank(message = "TransactionType required, please provide a value.")
    @Pattern(regexp = HEXADECIMAL_2_DIGITS, message = "TransactionType must be ISO Date in 'YYYY-MM-DD' format.")
    private String transactionType;
    @JsonProperty("UnpredictableNumber")
    @NotBlank(message = "UnpredictableNumber required, please provide a value.")
    @Pattern(regexp = HEXADECIMAL_8_DIGITS, message = "UnpredictableNumber must be exactly 8 hexadecimal digits.")
    private String unpredictableNumber;
    @JsonProperty("ApplicationInterchangeProfile")
    @NotBlank(message = "ApplicationInterchangeProfile required, please provide a value.")
    @Pattern(regexp = HEXADECIMAL_4_DIGITS, message = "ApplicationInterchangeProfile must be exactly 4 hexadecimal digits.")
    private String applicationInterchangeProfile;
    @JsonProperty("ApplicationTransactionCounter")
    @NotBlank(message = "ApplicationTransactionCounter required, please provide a value.")
    @Pattern(regexp = HEXADECIMAL_4_DIGITS, message = "ApplicationTransactionCounter must be exactly 4 hexadecimal digits.")
    private String applicationTransactionCounter;
    @JsonProperty("IssuerApplicationData")
    @NotBlank(message = "IssuerApplicationData required, please provide a value.")
    @Pattern(regexp = HEXADECIMAL_14_64_DIGITS, message = "IssuerApplicationData must be 14 to 64 hexadecimal digits.")
    private String issuerApplicationData;
    @JsonProperty("CSUorARC") // Replace with IAD parser to derive CVN
    @NotBlank(message = "CSUorARC required, please provide either value based on the CVN.")
    private String arcOrCsu;
}