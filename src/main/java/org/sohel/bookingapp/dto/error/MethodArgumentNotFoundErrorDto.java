package org.sohel.bookingapp.dto.error;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MethodArgumentNotFoundErrorDto {


    private int status;
    private String error;
    private Map<String, String> validationErrors = new HashMap<>();
    private String path;
    private LocalDateTime timestamp;

}
