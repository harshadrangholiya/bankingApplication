package com.example.banking.exception;

import com.example.banking.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link RuntimeException} exceptions.
     *
     * <p>Examples include validation failures, "Username already taken!",
     * or "Role not found". Returns HTTP 400 Bad Request.</p>
     *
     * @param ex the runtime exception thrown
     * @return a {@link ResponseEntity} containing an {@link ApiResponse}
     * with the error message and status code 400
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException ex) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles all other uncaught exceptions.
     *
     * <p>Returns a generic error message with HTTP 500 Internal Server Error
     * to indicate unexpected failures.</p>
     *
     * @param ex the exception thrown
     * @return a {@link ResponseEntity} containing an {@link ApiResponse}
     * with a generic error message and status code 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception ex) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Something went wrong: " + ex.getMessage())
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
