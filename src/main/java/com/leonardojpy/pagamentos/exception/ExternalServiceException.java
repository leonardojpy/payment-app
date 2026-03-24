package com.leonardojpy.pagamentos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ExternalServiceException extends PagamentosException {

    private final String detail;

    public ExternalServiceException(String detail) {
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);
        pb.setTitle("External service error");
        pb.setDetail(detail);
        return pb;
    }
}
