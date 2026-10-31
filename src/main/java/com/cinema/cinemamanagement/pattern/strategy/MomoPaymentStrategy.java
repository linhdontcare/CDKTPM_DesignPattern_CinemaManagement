package com.cinema.cinemamanagement.pattern.strategy;

import com.cinema.cinemamanagement.pattern.adapter.MomoPaymentAdapter;
import com.cinema.cinemamanagement.pattern.adapter.PaymentResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Concrete Strategy: Thanh toán trực tuyến qua Ví MoMo.
 * Tận dụng MomoPaymentAdapter để giao tiếp với hệ thống MoMo.
 */
@Component
public class MomoPaymentStrategy implements PaymentStrategy {

    private static final Logger logger = LoggerFactory.getLogger(MomoPaymentStrategy.class);
    public static final String METHOD_NAME = "MOMO";

    private final MomoPaymentAdapter momoPaymentAdapter;

    public MomoPaymentStrategy(MomoPaymentAdapter momoPaymentAdapter) {
        this.momoPaymentAdapter = momoPaymentAdapter;
    }

    @Override
    public PaymentResult pay(Double amount, String bookingCode) {
        logger.info("[MomoPayment] Xử lý chiến lược thanh toán MoMo cho đơn {}", bookingCode);
        return momoPaymentAdapter.pay(bookingCode, amount, "Thanh toan ve don " + bookingCode + " qua MoMo");
    }

    @Override
    public String getPaymentMethodName() {
        return METHOD_NAME;
    }
}

