package com.ujjwalkumar.eventticket.controllers;

import com.ujjwalkumar.eventticket.dtos.response.ErrorDto;
import com.ujjwalkumar.eventticket.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException e){
        log.error("User Not Found Exception occurred: ", e);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("User not found error.");
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(QrCodeGenerationException.class)
    public ResponseEntity<ErrorDto> handleQrCodeGenerationException(QrCodeGenerationException e){
        log.error("QrCode Generation Exception occurred: ", e);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("unable to generate QR code.");
        return ResponseEntity.internalServerError().body(errorDto);
    }

    @ExceptionHandler(QrCodeNotFoundException.class)
    public ResponseEntity<ErrorDto> handleQrCodeNotFoundException(QrCodeNotFoundException e){
        log.error("QrCode Not Found Exception occurred: ", e);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("Qr code not found.");
        return ResponseEntity.internalServerError().body(errorDto);
    }

    @ExceptionHandler(TicketSoldOutException.class)
    public ResponseEntity<ErrorDto> handleTicketSoldOutException(TicketSoldOutException e){
        log.error("Ticket Sold Out Exception occurred: ", e);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("Ticket are sold out for this ticket type.");
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEventNotFoundException(EventNotFoundException e){
        log.error("Event Not Found Exception occurred: ", e);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("Event not found error.");
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(TicketTypeNotFoundException.class)
    public ResponseEntity<ErrorDto> handleTicketTypeNotFoundException(TicketTypeNotFoundException e){
        log.error("TicketType Not Found Exception occurred: ", e);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("TicketType not found error.");
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ErrorDto> handleTicketNotFoundException(TicketNotFoundException e){
        log.error("Ticket Not Found Exception occurred: ", e);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("Ticket not found error.");
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(EventUpdateException.class)
    public ResponseEntity<ErrorDto> handleUpdateEventException(EventUpdateException e){
        log.error("Event Update Exception occurred: ", e);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("Unable to update an event.");
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("MethodArgumentNotValidException occurred: ", e);
        ErrorDto errorDto = new ErrorDto();
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        String errorMsg = fieldErrors
                .stream()
                .findFirst()
                .map(fieldError ->
                        fieldError.getField() + " : " + fieldError.getDefaultMessage()
                ).orElse("Method Argument Validation failed.");
        errorDto.setError(errorMsg);
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException e){
        log.error("ConstraintViolationException occurred: ", e);
        ErrorDto errorDto = new ErrorDto();
        String errorMsg = e.getConstraintViolations()
                .stream()
                .findFirst()
                .map(violation ->
                        violation.getPropertyPath() + " : " + violation.getMessage()
                ).orElse("Constraint violation occurred.");
        errorDto.setError(errorMsg);
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception e){
        log.error("Exception occurred: ", e);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("An unexpected/unknown error occurred.");
        return ResponseEntity.internalServerError().body(errorDto);
    }
}
