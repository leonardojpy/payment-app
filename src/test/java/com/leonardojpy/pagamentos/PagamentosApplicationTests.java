package com.leonardojpy.pagamentos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PagamentosApplicationTests {

    @Test
    void applicationClassShouldBeInstantiable() {
        assertDoesNotThrow(PagamentosApplication::new);
    }
}
