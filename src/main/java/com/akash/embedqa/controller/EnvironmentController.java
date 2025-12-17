package com.akash.embedqa.controller;

import com.akash.embedqa.model.dtos.request.EnvironmentDTO;
import com.akash.embedqa.model.dtos.response.ApiResult;
import com.akash.embedqa.model.dtos.response.EnvironmentResponseDTO;
import com.akash.embedqa.service.EnvironmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author: akash
 * Date: 17/12/25
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/environments")
@RequiredArgsConstructor
@Tag(name = "Environments", description = "Manage environment variables")
public class EnvironmentController {

    private final EnvironmentService environmentService;

    @PostMapping
    @Operation(summary = "Create environment", description = "Create a new environment")
    public ResponseEntity<ApiResult<EnvironmentResponseDTO>> create(
            @Valid @RequestBody EnvironmentDTO dto) {
        log.info("Creating environment: {}", dto.getName());

        EnvironmentResponseDTO created = environmentService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.success(created, "Environment created successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all environments", description = "Retrieve all environments")
    public ResponseEntity<ApiResult<List<EnvironmentResponseDTO>>> getAll() {
        List<EnvironmentResponseDTO> environments = environmentService.getAll();
        return ResponseEntity.ok(ApiResult.success(environments));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get environment by ID", description = "Retrieve an environment with all variables")
    public ResponseEntity<ApiResult<EnvironmentResponseDTO>> getById(@PathVariable Long id) {
        EnvironmentResponseDTO environment = environmentService.getById(id);
        return ResponseEntity.ok(ApiResult.success(environment));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update environment", description = "Update an existing environment")
    public ResponseEntity<ApiResult<EnvironmentResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody EnvironmentDTO dto) {
        log.info("Updating environment: {}", id);

        EnvironmentResponseDTO updated = environmentService.update(id, dto);
        return ResponseEntity.ok(ApiResult.success(updated, "Environment updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete environment", description = "Delete an environment")
    public ResponseEntity<ApiResult<Void>> delete(@PathVariable Long id) {
        log.info("Deleting environment: {}", id);

        environmentService.delete(id);
        return ResponseEntity.ok(ApiResult.success(null, "Environment deleted successfully"));
    }
}
