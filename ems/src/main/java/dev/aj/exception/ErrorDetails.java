package dev.aj.exception;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ErrorDetails {
    private ZonedDateTime timestamp = ZonedDateTime.now();
    private String errorMessage;
}
