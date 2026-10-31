package com.cinema.cinemamanagement.service;

import com.cinema.cinemamanagement.pattern.adapter.PaymentResult;

import java.util.List;

public interface PaymentService {

    /**
     * Xử lý thanh toán thông qua Strategy Pattern (không chứa if-else/switch).
     */
    PaymentResult processPayment(String paymentMethod, String bookingCode, Double amount);

    /**
     * Danh sách các phương thức thanh toán đang được hệ thống hỗ trợ.
     */
    List<String> getSupportedPaymentMethods();
}

