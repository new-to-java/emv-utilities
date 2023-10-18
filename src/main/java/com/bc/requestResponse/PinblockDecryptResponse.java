package com.bc.requestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PinblockDecryptResponse {
    @JsonProperty("DecryptedPINBlock")
    private String decryptedPinBlock;
    @JsonProperty("ClearPIN")
    private String ClearPin;
    @JsonProperty("PINLength")
    private char pinLength;
    @JsonProperty("PINBlockFormat")
    private char pinBlockFormat;
}