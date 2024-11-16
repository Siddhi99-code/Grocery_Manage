package com.project.grocery.Exception;


import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.project.grocery.utils.LoggingUtils;


// /**
//  * GlobalExceptionHandler handles various exceptions that can occur within the application.
//  * It provides a centralized place for handling exceptions and returns standardized responses.
//  */
// @RestControllerAdvice
// public class GlobalExceptionHandler {

//     /**
//      * Handles ApiRequestFailedException, which occurs when an API request fails.
//      * Logs the error and returns an ApiResponse with NOT_FOUND status.
//      * 
//      * @param ex The exception that was thrown
//      * @return A ResponseEntity containing the ApiResponse
//      */
//     @ExceptionHandler(ApiRequestFailedException.class)
//     public ResponseEntity<ApiResponse> handleApiRequestFailedException(ApiRequestFailedException ex) {
//         LoggingUtils.logError("apiRequestFailed", ex.getMessage());
//         ApiResponse apiResponse = new ApiResponse(ex.getMessage(), false, HttpStatus.NOT_FOUND.value(), ex.getDetails());
//         return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
//     }

//     /**
//      * Handles general exceptions that are not specifically handled by other methods.
//      * Logs the error and returns an ApiResponse with INTERNAL_SERVER_ERROR status.
//      * 
//      * @param ex The exception that was thrown
//      * @return A ResponseEntity containing the ApiResponse
//      */
//     @ExceptionHandler(Exception.class)
//     public ResponseEntity<ApiResponse> handleException(Exception ex) {
//         LoggingUtils.logError("unexpectedError", ex);
//         ApiResponse apiResponse = new ApiResponse("An unexpected error occurred", false, HttpStatus.INTERNAL_SERVER_ERROR.value());
//         return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//     }

//     /**
//      * Handles NoHandlerFoundException, which occurs when no handler is found for a request.
//      * Logs the error and returns an ApiResponse with NOT_FOUND status.
//      * 
//      * @param ex The exception that was thrown
//      * @return A ResponseEntity containing the ApiResponse
//      */
//     @ExceptionHandler(NoHandlerFoundException.class)
//     public ResponseEntity<ApiResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
//         LoggingUtils.logError("noHandlerFound", ex.getRequestURL());
//         ApiResponse apiResponse = new ApiResponse("Resource not found..URL is wrong please enter correct", false, HttpStatus.NOT_FOUND.value(), ex.getMessage());
//         return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
//     }

//     /**
//      * Handles MethodArgumentNotValidException, which occurs when method arguments are not valid.
//      * Collects validation errors, logs the error, and returns an ApiResponse with BAD_REQUEST status.
//      * 
//      * @param ex The exception that was thrown
//      * @return A ResponseEntity containing the ApiResponse
//      */
//     @ExceptionHandler(MethodArgumentNotValidException.class)
//     public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
//         Map<String, String> errors = new HashMap<>();
//         ex.getBindingResult().getAllErrors().forEach(error -> {
//             String fieldName = ((FieldError) error).getField();
//             String errorMessage = error.getDefaultMessage();
//             errors.put(fieldName, errorMessage);
//         });
//         LoggingUtils.logError("validationFailed");
//         ApiResponse apiResponse = new ApiResponse("Validation failed", false, HttpStatus.BAD_REQUEST.value(), errors);
//         return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
//     }


//     @ExceptionHandler(DataIntegrityViolationException.class)
//     public ResponseEntity<ApiResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
//         LoggingUtils.logError("dataIntegrityViolation", ex.getMessage());
//         ApiResponse apiResponse = new ApiResponse("Database error: integrity constraint violation", false, HttpStatus.BAD_REQUEST.value(), ex.getMessage());
//         return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
//     }

    
// }
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiRequestFailedException.class)
    public ResponseEntity<ApiResponse> handleApiRequestFailedException(ApiRequestFailedException ex) {
        LoggingUtils.logError("apiRequestFailed", ex.getMessage());
        ApiResponse apiResponse = new ApiResponse(ex.getMessage(), false, HttpStatus.NOT_FOUND.value(), ex.getDetails());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException1(Exception ex) {
        LoggingUtils.logError("unexpectedError", ex);
        ApiResponse apiResponse = new ApiResponse("An unexpected error occurred", false, HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

   
    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<ApiResponse> handleException(Exception ex) {
    //     ApiResponse response = new ApiResponse("An error occurred", false, HttpStatus.INTERNAL_SERVER_ERROR.value());

    //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .body(response);
    // }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        LoggingUtils.logError("noHandlerFound", ex.getRequestURL());
        ApiResponse apiResponse = new ApiResponse("Resource not found. URL is wrong, please enter correct", false, HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        LoggingUtils.logError("validationFailed");
        ApiResponse apiResponse = new ApiResponse("Validation failed", false, HttpStatus.BAD_REQUEST.value(), errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        LoggingUtils.logError("dataIntegrityViolation", ex.getMessage());
        ApiResponse apiResponse = new ApiResponse("Database error: integrity constraint violation", false, HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }
}
