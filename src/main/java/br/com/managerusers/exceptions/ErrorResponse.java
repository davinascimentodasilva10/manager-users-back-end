package br.com.managerusers.exceptions;

import java.time.Instant;

public record ErrorResponse(
        Integer status,
        String error,
        String message,
        Instant timestamp
) {

}
