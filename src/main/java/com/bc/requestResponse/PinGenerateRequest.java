package com.bc.requestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static com.bc.constants.Patterns.*;

@Getter
@Setter
public class PinGenerateRequest {
    @JsonProperty("Pan")
    @NotBlank(message = "Pan required, please provide a value.")
    @Pattern(regexp = DECIMAL_16TO19_DIGITS, message = "Pan is required, must be numeric and 16 to 19 digits long.")
    public String pan;
    @JsonProperty("PINLength")
    @NotBlank(message = "PINLength required, please provide a value.")
    @Pattern(regexp = DECIMAL_1TO2_DIGITS, message = "PINLength required, must be numeric and 1 to 2 digits long.")
    public String pinLength;
    @JsonProperty("PINOffset")
    @NotBlank(message = "PINOffset required, please provide a value.")
    @Pattern(regexp = DECIMAL_4TO12_DIGITS, message = "PINOffset required, must be numeric and 4 to 12 digits long.")
    public String pinOffset;
    @JsonProperty("PVK")
    @NotBlank(message = "PVK required, please provide a value.")
    @Pattern(regexp = HEXADECIMAL_32_DIGITS, message = "PVK must be exactly 32 hexadecimal digits.")
    public String pinVerificationKey;
}
