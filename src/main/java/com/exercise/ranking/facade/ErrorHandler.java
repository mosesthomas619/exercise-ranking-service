package com.exercise.ranking.facade;

import com.exercise.ranking.service.exception.ConflictException;
import com.exercise.ranking.service.exception.NotFoundException;
import com.exercise.ranking.service.exception.SecurityException;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public final ResponseEntity<ErrorResponse> handleIllegalArgument(final IllegalArgumentException ex,
      final WebRequest request) {
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidDefinitionException.class)
  public final ResponseEntity<ErrorResponse> handleInvalidDefinition(final InvalidDefinitionException ex,
      final WebRequest request) {
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConflictException.class)
  public final ResponseEntity<ErrorResponse> handleConflict(final ConflictException ex,
      final WebRequest request) {
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  public final ResponseEntity<ErrorResponse> handleNotFound(final NotFoundException ex,
      final WebRequest request) {
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(SecurityException.class)
  public final ResponseEntity<ErrorResponse> handleSecurity(final SecurityException ex,
      final WebRequest request) {
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
  }


  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    List<String> details = new ArrayList<>();
    for(ObjectError error : ex.getBindingResult().getAllErrors()) {
      details.add(error.getDefaultMessage());
    }
    ErrorResponse error = new ErrorResponse("Validation Failed: " + details);
    return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
  }
}