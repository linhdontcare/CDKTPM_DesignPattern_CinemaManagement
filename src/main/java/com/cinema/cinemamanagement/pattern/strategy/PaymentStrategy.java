package com.cinema.cinemamanagement.pattern.strategy;

import com.cinema.cinemamanagement.pattern.adapter.PaymentResult;

/**
 * Strategy Interface trong Strategy Pattern.
 * Đóng gói thuật toán/quy trình thanh toán cho từng phương thức riêng biệt.
 */
public interface PaymentStrategy {

    /**
     * Thực hiện thanh toán cho đơn đặt vé.
     *
     * @param amount      Số tiền cần thanh toán
     * @param bookingCode Mã đặt vé
     * @return Kết quả thanh toán PaymentResult
     */
    PaymentResult pay(Double amount, String bookingCode);

    /**
     * Trả về tên định danh của phương thức (ví dụ: CASH, MOMO, VNPAY).
     */
    String getPaymentMethodName();

    /**
     * Kiểm tra xem Strategy này có hỗ trợ mã phương thức thanh toán yêu cầu không.
     */
    default boolean supports(String paymentMethod) {
        return getPaymentMethodName().equalsIgnoreCase(paymentMethod);
    }
}

