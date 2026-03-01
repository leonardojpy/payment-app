package com.leonardojpy.pagamentos.controller.dto;

import com.leonardojpy.pagamentos.exception.PagamentosException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(PagamentosException.class)
    public ProblemDetail handlePagamentosException (PagamentosException e){
        return e.toProblemDetail();
    }
}
