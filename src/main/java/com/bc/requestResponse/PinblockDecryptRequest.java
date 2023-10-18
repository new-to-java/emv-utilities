package com.bc.requestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static com.bc.constants.CommonPatterns.*;

@Getter
@Setter
public class PinblockDecryptRequest {
    @JsonProperty("Pan")
    @NotBlank(message = "Pan required, please provide a value.")
    @Pattern(regexp = DECIMAL_16TO19_DIGITS, message = "Pan is required, must be numeric and 16 to 19 digits long.")
    public String pan;
    @JsonProperty("PINblock")
    @NotBlank(message = "PINblock required, please provide a value.")
    @Pattern(regexp = HEXADECIMAL_16_DIGITS, message = "PINblock must be exactly 16 hexadecimal digits.")
    public String pinBlock;
    @JsonProperty("ZonePinKey")
    @NotBlank(message = "ZonePinKey required, please provide a value.")
    @Pattern(regexp = HEXADECIMAL_32_DIGITS, message = "ZonePinKey must be exactly 32 hexadecimal digits.")
    public String zonePinKey;
}