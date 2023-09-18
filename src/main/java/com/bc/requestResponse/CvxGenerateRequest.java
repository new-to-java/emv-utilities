package com.bc.requestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import static com.bc.constants.CommonPatterns.*;

@Getter
@Setter
public class CvxGenerateRequest {
    @JsonProperty("Pan")
    @NotBlank(message = "Pan required, please provide a value.")
    @Pattern(regexp = DECIMAL_16TO19_DIGITS, message = "Pan is required, must be numeric and 16 digits long.")
    private String pan;
    @JsonProperty("ExpiryDate")
    @NotBlank(message = "ExpiryDate required, please provide a value.")
    @Pattern(regexp = DECIMAL_4_DIGITS, message = "ExpiryDate must be exactly for decimal digits, in 'YYMM' or 'MMYY' format.")
    private String expiryDate;
    @JsonProperty("ServiceCode")
    @NotBlank(message = "ServiceCode required, please provide a value.")
    @Pattern(regexp = DECIMAL_3_DIGITS, message = "ServiceCode must be exactly 3 digit decimal number.")
    private String serviceCode;
    @JsonProperty("CardVerificationKey")
    @NotBlank(message = "CardVerificationKey required, please provide a value.")
    @Pattern(regexp = HEXADECIMAL_32_DIGITS, message = "CardVerificationKey must be exactly 32 hexadecimal digits.")
    private String cvk;
}