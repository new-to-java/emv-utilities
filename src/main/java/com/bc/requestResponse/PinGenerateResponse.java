package com.bc.requestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PinGenerateResponse {
    @JsonProperty("PIN")
    private String pin;
    @JsonProperty("NaturalPIN")
    private String naturalPin;
    @JsonProperty("PINLength")
    private String pinLength;
    @JsonProperty("PINOffset")
    private String pinOffset;
}