package com.bc.requestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CvxGenerateResponse {
    @JsonProperty("CVX")
    private String cvx;
    @JsonProperty("Type")
    private String cvxType;
}