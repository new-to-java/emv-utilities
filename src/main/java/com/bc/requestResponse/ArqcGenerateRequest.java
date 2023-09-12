package com.bc.requestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import com.bc.enums.CryptogramVersionNumber;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class ArqcGenerateRequest {
    @JsonProperty("Pan")
    @NotBlank(message = "Pan required, please provide a value.")
    @Length(message = "Pan must be 16 to 19 digits long.", min = 16, max = 19)
    @Digits(message = "Pan must be numeric.", integer = 19, fraction = 0)
//    @CreditCardNumber(message = "Pan invalid, must be a valid card number with a Luhn check-digit.") // Temporarily suspended for testing and further build
    private String pan;
    @JsonProperty("PanSequence")
    @NotBlank(message = "PanSequence required, please provide a value.")
    @Length(message = "PanSequence must be 1 to 2 digits long.", min = 1, max = 2)
    @Digits(message = "PanSequence must be numeric.", integer = 2, fraction = 0)
    private String panSeqNbr;
    @JsonProperty("CryptogramMasterKey")
    @NotBlank(message = "CryptogramMasterKey required, please provide a value.")
    @Pattern(regexp = "^[0-9A-Fa-f]{32}$", message = "CryptogramMasterKey must be exactly 32 hexadecimal digits.")
    private String mdkAc;
    @JsonProperty("AmountAuthorised")
    @NotBlank(message = "AmountAuthorised required, please provide a value.")
    @Length(message = "AmountAuthorised must be 1 to 12 digits long.", min = 1, max = 12)
    @Digits(message = "AmountAuthorised must be numeric.", integer = 12, fraction = 0)
    private String amountAuthorised;
    @JsonProperty("AmountOther")
    @NotBlank(message = "AmountOther required, please provide a value.")
    @Length(message = "AmountOther must be 1 to 12 digits long.", min = 1, max = 12)
    @Digits(message = "AmountOther must be numeric.", integer = 12, fraction = 0)
    private String amountOther;
    @JsonProperty("TerminalCountryCode")
    @NotBlank(message = "TerminalCountryCode required, please provide a value.")
    @Pattern(regexp = "^[0-9]{3}$", message = "TerminalCountryCode must be a valid ISO 3166-1 3 digit numeric country code.")
    private String terminalCountryCode;
    @JsonProperty("TerminalVerificationResults")
    @NotBlank(message = "TerminalVerificationResults required, please provide a value.")
    @Pattern(regexp = "^[0-9A-Fa-f]{10}$", message = "TerminalVerificationResults must be exactly 10 hexadecimal digits.")
    private String terminalVerificationResults;
    @JsonProperty("TransactionCurrencyCode")
    @NotBlank(message = "TransactionCurrencyCode required, please provide a value.")
    @Pattern(regexp = "^[0-9]{3}$", message = "TransactionCurrencyCode must be a valid ISO 4217 3 digit numeric currency code.")
    private String transactionCurrencyCode;
    @JsonProperty("TransactionDate")
    @NotBlank(message = "TransactionDate required, please provide a value.")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "TransactionDate must be ISO Date in 'YYYY-MM-DD' format.")
    private String transactionDate;
    @JsonProperty("TransactionType")
    @NotBlank(message = "TransactionType required, please provide a value.")
    @Pattern(regexp = "^[0-9A-Fa-f]{2}$", message = "TransactionType must be ISO Date in 'YYYY-MM-DD' format.")
    private String transactionType;
    @JsonProperty("UnpredictableNumber")
    @NotBlank(message = "UnpredictableNumber required, please provide a value.")
    @Pattern(regexp = "^[0-9A-Fa-f]{8}$", message = "UnpredictableNumber must be exactly 8 hexadecimal digits.")
    private String unpredictableNumber;
    @JsonProperty("ApplicationInterchangeProfile")
    @NotBlank(message = "ApplicationInterchangeProfile required, please provide a value.")
    @Pattern(regexp = "^[0-9A-Fa-f]{4}$", message = "ApplicationInterchangeProfile must be exactly 4 hexadecimal digits.")
    private String applicationInterchangeProfile;
    @JsonProperty("ApplicationTransactionCounter")
    @NotBlank(message = "ApplicationTransactionCounter required, please provide a value.")
    @Pattern(regexp = "^[0-9A-Fa-f]{4}$", message = "ApplicationTransactionCounter must be exactly 4 hexadecimal digits.")
    private String applicationTransactionCounter;
    @JsonProperty("IssuerApplicationData")
    @NotBlank(message = "IssuerApplicationData required, please provide a value.")
    @Length(message = "IssuerApplicationData must be 14 to 64 digits long.", min = 14, max = 64)
    @Pattern(regexp = "^[0-9A-Fa-f]+$", message = "ApplicationTransactionCounter must be 14 to 64 hexadecimal digits.")
    private String issuerApplicationData;
    @JsonProperty("CryptogramVersionNumber")
    private CryptogramVersionNumber cryptogramVersionNumber;
}
