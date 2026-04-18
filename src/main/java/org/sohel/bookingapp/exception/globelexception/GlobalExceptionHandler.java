package org.sohel.bookingapp.exception.globelexception;


import org.sohel.bookingapp.dto.error.ErrorResponseDto;
import org.sohel.bookingapp.dto.error.MethodArgumentNotFoundErrorDto;
import org.sohel.bookingapp.exception.custoomexception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request)
    {

        ErrorResponseDto o=new ErrorResponseDto(

                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name(),
                ex.getMessage(),
                request.getDescription(false),
                LocalDateTime.now()

        );

        return  new ResponseEntity<>(o, HttpStatus.NOT_FOUND);



    }

    // illegal argument exception impl here


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request){


        ErrorResponseDto o=new ErrorResponseDto(

                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage(),
                request.getDescription(false),
                LocalDateTime.now()


        );

        return new ResponseEntity<>(o,HttpStatus.BAD_REQUEST);
    }




// Handler all other error exception here

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex, WebRequest request){

        ErrorResponseDto o=new ErrorResponseDto(

                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                ex.getMessage(),
                request.getDescription(false),
                LocalDateTime.now()

        );

        return new ResponseEntity<>(o, HttpStatus.INTERNAL_SERVER_ERROR);
    }


// validation exception here impl


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MethodArgumentNotFoundErrorDto> handlevalidationException(MethodArgumentNotValidException ex, WebRequest request){


        Map<String, String> validationerrors=new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error-> validationerrors.put(error.getField(), error.getDefaultMessage()));

        MethodArgumentNotFoundErrorDto o=new MethodArgumentNotFoundErrorDto(

                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                validationerrors,
                request.getDescription(false),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(o,HttpStatus.BAD_REQUEST);
    }





}
