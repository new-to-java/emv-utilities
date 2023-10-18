package com.bc.requestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PinblockDecryptResponse {
    @JsonProperty("DecryptedPinblock")
    private String decryptedPinblock;
    @JsonProperty("ClearPin")
    private String ClearPin;
    @JsonProperty("PinLength")
    private char pinLength;
    @JsonProperty("PinblockFormat")
    private char pinblockFormat;
}