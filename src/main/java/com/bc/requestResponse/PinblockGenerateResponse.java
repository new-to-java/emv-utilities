package com.bc.requestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PinblockGenerateResponse {
    @JsonProperty("EncryptedPinblock")
    private String encryptedPinblock;
    @JsonProperty("ClearPinblock")
    private String clearPinblock;
    @JsonProperty("PinLength")
    private char pinLength;
    @JsonProperty("PinblockFormat")
    private char pinblockFormat;
}