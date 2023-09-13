package com.bc.requestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArqcGenerateResponse {
    @JsonProperty("ARQC")
    private String arqc;
    @JsonProperty("ARPC")
    private String arpc;
}