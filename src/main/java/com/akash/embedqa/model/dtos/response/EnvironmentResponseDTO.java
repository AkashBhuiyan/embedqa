package com.akash.embedqa.model.dtos.response;

import com.akash.embedqa.model.dtos.request.EnvironmentVariableDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author: akash
 * Date: 17/12/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvironmentResponseDTO {

    private Long id;
    private String name;
    private String description;
    private List<EnvironmentVariableDTO> variables;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
