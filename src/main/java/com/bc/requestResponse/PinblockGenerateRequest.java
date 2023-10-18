package com.bc.requestResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static com.bc.constants.CommonPatterns.*;

@Getter
@Setter
public class PinblockGenerateRequest {
    @JsonProperty("Pan")
    @NotBlank(message = "Pan required, please provide a value.")
    @Pattern(regexp = DECIMAL_16TO19_DIGITS, message = "Pan is required, must be numeric and 16 to 19 digits long.")
    public String pan;
    @JsonProperty("Pin")
    @NotBlank(message = "Pin required, please provide a value.")
    @Pattern(regexp = DECIMAL_4TO12_DIGITS, message = "Pin must be 4 to 12 decimal digits.")
    public String pin;
    @JsonProperty("PinblockFormat")
    @NotBlank(message = "PinblockFormat required, please provide a value.")
    @Pattern(regexp = PIN_BLOCK_FORMAT, message = "PinblockFormat must be 0 or 1.")
    public String pinBlockFormat;
    @JsonProperty("ZonePinKey")
    @NotBlank(message = "ZonePinKey required, please provide a value.")
    @Pattern(regexp = HEXADECIMAL_32_DIGITS, message = "ZonePinKey must be exactly 32 hexadecimal digits.")
    public String zonePinKey;
}