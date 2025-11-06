package ru.dan.eduinstitution.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для обновления статуса записи студента.
 */
@Data
@Schema(description = "DTO for updating enrollment status")
public class UpdateEnrollmentStatusDto {

    @Schema(description = "New status for the enrollment", example = "Completed", allowableValues = {"Active", "Completed", "Dropped"})
    private String status;
}