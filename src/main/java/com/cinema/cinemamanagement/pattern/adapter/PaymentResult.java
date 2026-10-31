package com.cinema.cinemamanagement.pattern.adapter;

import java.time.LocalDateTime;

/**
 * Kết quả thanh toán chuẩn hóa của hệ thống nội bộ.
 * Tất cả các cổng thanh toán (Cash, MoMo, VNPay...) sau khi xử lý đều quy về kết quả này.
 */
public class PaymentResult {

    private final boolean success;
    private final String transactionId;
    private final String message;
    private final String paymentMethod;
    private final Double amount;
    private final LocalDateTime timestamp;

    public PaymentResult(boolean success, String transactionId, String message, String paymentMethod, Double amount) {
        this.success = success;
        this.transactionId = transactionId;
        this.message = message;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public static PaymentResult success(String transactionId, String message, String paymentMethod, Double amount) {
        return new PaymentResult(true, transactionId, message, paymentMethod, amount);
    }

    public static PaymentResult failure(String message, String paymentMethod, Double amount) {
        return new PaymentResult(false, null, message, paymentMethod, amount);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getMessage() {
        return message;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "PaymentResult{" +
                "success=" + success +
                ", transactionId='" + transactionId + '\'' +
                ", message='" + message + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}

