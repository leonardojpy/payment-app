package com.leonardojpy.pagamentos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class WalletNotFoundException extends PagamentosException {

    private final String detail;

    public WalletNotFoundException(String detail) {
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pb.setTitle("Wallet not found");
        pb.setDetail(detail);
        return pb;
    }
}
