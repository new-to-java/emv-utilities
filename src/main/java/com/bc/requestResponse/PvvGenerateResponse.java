package com.bc.requestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PvvGenerateResponse {
    @JsonProperty("CustomerPIN")
    private String customerPin;
    @JsonProperty("PVV")
    private String pinVerificationValue;
    @JsonProperty("PINLength")
    private String pinLength;
}