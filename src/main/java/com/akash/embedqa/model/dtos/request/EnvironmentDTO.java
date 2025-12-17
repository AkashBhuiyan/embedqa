package com.akash.embedqa.model.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: akash
 * Date: 17/12/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvironmentDTO {

    private Long id;

    @NotBlank(message = "Environment name is required")
    private String name;

    private String description;

    @Valid
    @Builder.Default
    private List<EnvironmentVariableDTO> variables = new ArrayList<>();

    // Whether this is the active environment
    @Builder.Default
    private Boolean active = false;
}
