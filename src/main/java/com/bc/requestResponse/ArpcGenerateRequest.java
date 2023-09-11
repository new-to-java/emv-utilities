package com.bc.requestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import com.bc.enums.CryptogramVersionNumber;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class ArpcGenerateRequest {
    @JsonProperty("Pan")
    @NotBlank(message = "Pan required and must be 16 to 19 digits, please provide a value.")
    @Min(message = "Pan must be at least 16 digits long.", value = 16)
    @Max(message = "Pan cannot be greater than 19 digits long.", value = 16)
//    @Digits(message = "", integer = 16, fraction = 0)
    @CreditCardNumber(message = "Pan check digit validation failed, please provide a valid value.")
    private String pan;
    @JsonProperty("PanSequence")
    @NotBlank(message = "PanSequence required and must be at least 1 digit, please provide a value.")
    @Range(message = "PanSequence must be in the range 0 to 99", min = 0, max = 99)
    @Length(message = "PanSequence must be ", min = 1, max = 2)
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