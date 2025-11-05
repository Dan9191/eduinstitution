package ru.dan.eduinstitution.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
@Schema(description = "EnrollmentId entity representing a composite primary key for enrollment")
public class EnrollmentId implements Serializable {

    @Schema(description = "ID of the user in the enrollment", example = "1")
    private Long userId;
    
    @Schema(description = "ID of the course in the enrollment", example = "1")
    private Long courseId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnrollmentId that)) return false;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, courseId);
    }
}
