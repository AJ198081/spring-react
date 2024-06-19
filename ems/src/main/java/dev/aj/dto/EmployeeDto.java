package dev.aj.dto;

import jakarta.validation.constraints.Email;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class EmployeeDto {
    private long id;
    private String fullName;
    @Email
    private String email;
    private Long departmentId;
    private Instant createdDate;
    private Instant lastUpdatedDate;
}
