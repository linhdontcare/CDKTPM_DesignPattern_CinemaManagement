package com.cinema.cinemamanagement.pattern.strategy;

import com.cinema.cinemamanagement.pattern.adapter.PaymentResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Concrete Strategy: Thanh toán bằng tiền mặt trực tiếp tại quầy rạp chiếu phim.
 */
@Component
public class CashPaymentStrategy implements PaymentStrategy {

    private static final Logger logger = LoggerFactory.getLogger(CashPaymentStrategy.class);
    public static final String METHOD_NAME = "CASH";

    @Override
    public PaymentResult pay(Double amount, String bookingCode) {
        logger.info("[CashPayment] Thu tiền mặt tại quầy: BookingCode={}, Amount={}", bookingCode, amount);

        if (amount == null || amount <= 0) {
            return PaymentResult.failure("Số tiền thanh toán tiền mặt không hợp lệ", METHOD_NAME, amount);
        }

        String receiptNo = "CASH-REC-" + DateTimeFormatter.ofPattern("yyMMddHHmmss").format(LocalDateTime.now());
        return PaymentResult.success(
                receiptNo,
                "Đã thu tiền mặt tại quầy rạp chiếu phim. Mã biên lai: " + receiptNo,
                METHOD_NAME,
                amount
        );
    }

    @Override
    public String getPaymentMethodName() {
        return METHOD_NAME;
    }
}

