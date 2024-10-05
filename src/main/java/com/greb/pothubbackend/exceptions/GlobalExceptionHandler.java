package com.greb.pothubbackend.exceptions;

import com.greb.pothubbackend.dtos.ErrorDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private final Logger LOGGER=LoggerFactory.getLogger(Exception.class);

	@ExceptionHandler({BadRequestException.class,
		IllegalArgumentException.class,
		MissingServletRequestParameterException.class})
    public ResponseEntity<ErrorDto> handleRequestException(Exception e) {
		LOGGER.error(e.getMessage());
		var errorDto= new ErrorDto(HttpStatus.BAD_REQUEST,"Bad request Exception",e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

	@ExceptionHandler({
			UnauthorizedException.class,
			AuthenticationCredentialsNotFoundException.class,
			BadCredentialsException.class,
			InternalAuthenticationServiceException.class
	})
    public ResponseEntity<ErrorDto> handleTokenException(Exception e) {
		LOGGER.error(e.getMessage());
		var errorDto= new ErrorDto(HttpStatus.UNAUTHORIZED,"Unauthorized Exception",e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorDto> handleValidationException(BindException ex){
		LOGGER.error(ex.getMessage());
		Map<String, String> errors = new HashMap();
		ex.getBindingResult().getFieldErrors().forEach((error) -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});
		var errorDto= new ErrorDto(HttpStatus.BAD_REQUEST, "Validation error","", errors);
		return ResponseEntity.badRequest().body(errorDto);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorDto> handleConstraintException(ConstraintViolationException ex){
		LOGGER.error(ex.getMessage());
		Map<String, String> errors = new HashMap();
		Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
		for(var violation : violations){
			errors.put(violation.getPropertyPath().toString(), violation.getMessage());
		}
		var errorDto= new ErrorDto(HttpStatus.BAD_REQUEST, "Validation error","", errors);
		return ResponseEntity.badRequest().body(errorDto);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDto> handleUnknownException(Exception ex){
		LOGGER.error(ex.getMessage());
		var errorDto= new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error",ex.getMessage());
		return ResponseEntity.status(500).body(errorDto);
	}
}
