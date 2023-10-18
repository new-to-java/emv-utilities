package com.bc.requestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PinblockDecryptResponse {
    @JsonProperty("DecryptedPinblock")
    private String decryptedPinblock;
    @JsonProperty("ClearPIN")
    private String ClearPin;
    @JsonProperty("PINLength")
    private char pinLength;
    @JsonProperty("PINBlockFormat")
    private char pinblockFormat;
}