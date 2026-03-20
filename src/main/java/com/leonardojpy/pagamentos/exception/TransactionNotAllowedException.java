package com.leonardojpy.pagamentos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class TransactionNotAllowedException extends PagamentosException {

    private final String detail;

    public TransactionNotAllowedException(String detail) {
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_CONTENT);
        pb.setTitle("Transaction not allowed");
        pb.setDetail(detail);
        return pb;
    }
}
