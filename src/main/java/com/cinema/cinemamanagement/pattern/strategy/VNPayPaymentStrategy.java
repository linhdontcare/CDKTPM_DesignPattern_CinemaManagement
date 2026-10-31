package com.cinema.cinemamanagement.pattern.strategy;

import com.cinema.cinemamanagement.pattern.adapter.PaymentResult;
import com.cinema.cinemamanagement.pattern.adapter.VNPayPaymentAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Concrete Strategy: Thanh toán trực tuyến qua Cổng VNPay.
 * Tận dụng VNPayPaymentAdapter để giao tiếp với hệ thống VNPay.
 */
@Component
public class VNPayPaymentStrategy implements PaymentStrategy {

    private static final Logger logger = LoggerFactory.getLogger(VNPayPaymentStrategy.class);
    public static final String METHOD_NAME = "VNPAY";

    private final VNPayPaymentAdapter vnPayPaymentAdapter;

    public VNPayPaymentStrategy(VNPayPaymentAdapter vnPayPaymentAdapter) {
        this.vnPayPaymentAdapter = vnPayPaymentAdapter;
    }

    @Override
    public PaymentResult pay(Double amount, String bookingCode) {
        logger.info("[VNPayPayment] Xử lý chiến lược thanh toán VNPay cho đơn {}", bookingCode);
        return vnPayPaymentAdapter.pay(bookingCode, amount, "Thanh toan ve don " + bookingCode + " qua VNPay");
    }

    @Override
    public String getPaymentMethodName() {
        return METHOD_NAME;
    }
}

