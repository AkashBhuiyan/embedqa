package com.akash.embedqa.model.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: akash
 * Date: 17/12/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvironmentVariableDTO {

    private Long id;

    @NotBlank(message = "Variable name is required")
    private String name;

    private String value;

    // Initial value (for exported environments)
    private String initialValue;

    // Whether this variable is enabled
    @Builder.Default
    private Boolean enabled = true;

    // Whether to mask the value (for secrets)
    @Builder.Default
    private Boolean secret = false;
}
