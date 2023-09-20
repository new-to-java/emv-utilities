package com.bc.requestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static com.bc.constants.CommonPatterns.*;

@Getter
@Setter
public class PvvGenerateRequest {
    @JsonProperty("Pan")
    @NotBlank(message = "Pan required, please provide a value.")
    @Pattern(regexp = DECIMAL_16TO19_DIGITS, message = "Pan is required, must be numeric and 16 to 19 digits long.")
    public String pan;
    @JsonProperty("PIN")
    @NotBlank(message = "PIN required, please provide a value.")
    @Pattern(regexp = DECIMAL_4TO12_DIGITS, message = "PIN required, must be numeric and 4 to 12 digits long.")
    public String pin;
    @JsonProperty("PVK")
    @NotBlank(message = "PVK required, please provide a value.")
    @Pattern(regexp = HEXADECIMAL_32_DIGITS, message = "PVK must be exactly 32 hexadecimal digits.")
    public String pinVerificationKey;
    @JsonProperty("PVKIndex")
    @NotBlank(message = "PVKIndex required, please provide a value.")
    @Pattern(regexp = DECIMAL_1_DIGIT, message = "PVKIndex must be a value in the range 0 to 9.")
    public String pinVerificationKeyIndex;
}