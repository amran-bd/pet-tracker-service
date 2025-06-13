package com.tractive.pet.tracker.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Md Amran Hossain on 13/6/2025 AD
 * @Project pet-tracker-service
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
public class ComApiResponse<T> implements Serializable {
    private @Builder.Default int statusCode = HttpStatus.OK.value();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private @Builder.Default LocalDateTime timestamp = LocalDateTime.now();
    private @Builder.Default String message = "Request Successfully Executed";
    private T data;
    private String addInfo;
}
