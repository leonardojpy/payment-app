package com.leonardojpy.pagamentos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class WalletTypeNotFoundException extends PagamentosException {

    private final String detail;

    public WalletTypeNotFoundException(String detail) {
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pb.setTitle("Wallet type not found");
        pb.setDetail(detail);
        return pb;
    }
}
