package com.bc.requestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import com.bc.enums.CryptogramVersionNumber;

@Getter
@Setter
public class ArqcValidateResponse {
    @NotBlank(message = "Pan required, please provide a value.")
    @JsonProperty("Pan")
    private String pan;
    @NotBlank(message = "PanSequence required, please provide a value.")
    @JsonProperty("PanSequence")
    private String panSeqNbr;
    @JsonProperty("MDKAc")
    @NotBlank(message = "Application Cryptogram Master Key required, please provide a value.")
    private String mdkAc;
    @JsonProperty("AmountAuthorised")
    private String amountAuthorised;
    @JsonProperty("AmountOther")
    private String amountOther;
    @JsonProperty("TerminalCountryCode")
    private String terminalCountryCode;
    @JsonProperty("TVR")
    private String terminalVerificationResults;
    @JsonProperty("TransactionCurrerncyCode")
    private String transactionCurrencyCode;
    @JsonProperty("TransactionDate")
    private String transactionDate;
    @JsonProperty("TransactionType")
    private String transactionType;
    @JsonProperty("UnpredictableNumber")
    private String unpredictableNumber;
    @JsonProperty("AIP")
    private String applicationInterchangeProfile;
    @JsonProperty("ATC")
    private String applicationTransactionCounter;
    @JsonProperty("IAD")
    private String issuerApplicationData;
    @JsonProperty("CVN")
    private CryptogramVersionNumber cryptogramVersionNumber;
}