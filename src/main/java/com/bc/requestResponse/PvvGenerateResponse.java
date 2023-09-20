package com.bc.requestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PvvGenerateResponse {
    @JsonProperty("PIN")
    private String pin;
    @JsonProperty("PVV")
    private String pinVerificationValue;
    @JsonProperty("PINLength")
    private String pinLength;
}