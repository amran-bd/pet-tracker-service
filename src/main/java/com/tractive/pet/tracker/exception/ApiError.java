package com.tractive.pet.tracker.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Md Amran Hossain on 13/6/2025 AD
 * @Project pet-tracker-service
 */
@Builder
public record ApiError(
        HttpStatus status,
        int statusCode,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        LocalDateTime timestamp,
        String message,
        String debugMessage,
        List<ApiSubError> subErrorList
) {}
