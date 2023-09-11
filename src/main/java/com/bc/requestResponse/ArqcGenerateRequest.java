package com.bc.requestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import com.bc.enums.CryptogramVersionNumber;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class ArqcGenerateRequest {
    @JsonProperty("Pan")
    @NotBlank(message = "Pan required and must be 16 to 19 digits, please provide a value.")
    @Length(message = "Pan must be numeric and minimum 16 digits and maximum 19 digits long.", min = 16, max = 19)
    @Digits(message = "Pan must be numeric.", integer = 16, fraction = 0)
//    @CreditCardNumber(message = "Pan invalid, must be a valid card number with a Luhn check-digit.") // Temporarily suspended for testing and further build
    private String pan;
    @JsonProperty("PanSequence")
    @NotBlank(message = "PanSequence required and must be 1 to 2 digits, please provide a value.")
    @Length(message = "PanSequence must be numeric and minimum 1 digit and maximum 2 digits long.", min = 1, max = 2)
    @Digits(message = "PanSequence must be numeric.", integer = 1, fraction = 0)
    private String panSeqNbr;
    @JsonProperty("CryptogramMasterKey")
    @NotBlank(message = "CryptogramMasterKey required, please provide a value.")
    @Length(message = "CryptogramMasterKey must be exactly 32 hexadecimal digits.", min = 32, max = 32)
    private String mdkAc;
    @JsonProperty("AmountAuthorised")
    @NotBlank(message = "AmountAuthorised required, please provide a value.")
    @Digits(message = "AmountAuthorised must be numeric and a minimum of 1 and maximum of 12 digits.", integer = 12, fraction = 0)
    private String amountAuthorised;
    @JsonProperty("AmountOther")
    @NotBlank(message = "AmountOther required, please provide a value.")
    @Digits(message = "AmountOther must be numeric and a minimum of 1 and maximum of 12 digits.", integer = 12, fraction = 0)
    private String amountOther;
    @JsonProperty("TerminalCountryCode")
    @NotBlank(message = "TerminalCountryCode required, please provide a value.")
    @Digits(message = "TerminalCountryCode must be ISO 3166-1 3 digit numeric country code.", integer = 3, fraction = 0)
    private String terminalCountryCode;
    @JsonProperty("TerminalVerificationResults")
    @NotBlank(message = "TerminalCountryCode required, please provide a value.")
    @Digits(message = "TerminalCountryCode must be ISO 3166-1 3 digit numeric country code.", integer = 3, fraction = 0)
    private String terminalVerificationResults;
    @JsonProperty("TransactionCurrencyCode")
    private String transactionCurrencyCode;
    @JsonProperty("TransactionDate")
    private String transactionDate;
    @JsonProperty("TransactionType")
    private String transactionType;
    @JsonProperty("UnpredictableNumber")
    private String unpredictableNumber;
    @JsonProperty("ApplicationInterchangeProfile")
    private String applicationInterchangeProfile;
    @JsonProperty("ApplicationTransactionCounter")
    private String applicationTransactionCounter;
    @JsonProperty("IssuerApplicationData")
    private String issuerApplicationData;
    @JsonProperty("CryptogramVersionNumber")
    private CryptogramVersionNumber cryptogramVersionNumber;
}