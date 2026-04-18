package org.sohel.bookingapp.dto.error;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {


    private int status;
    private String error;
    private String message;
    private String path;
    private LocalDateTime timestamp;


}
