package com.cinema.cinemamanagement.pattern.adapter;

import com.cinema.cinemamanagement.pattern.adapter.external.VNPayExternalApi;
import org.springframework.stereotype.Component;

/**
 * Adapter chuyển đổi giao diện VNPay SDK sang PaymentGateway nội bộ.
 */
@Component
public class VNPayPaymentAdapter implements PaymentGateway {

    private static final String TMN_CODE = "CINEMA_VNPAY_TMN";
    private static final String RETURN_URL = "https://cinema.vn/api/payments/vnpay-callback";
    private final VNPayExternalApi vnPayExternalApi;

    public VNPayPaymentAdapter(VNPayExternalApi vnPayExternalApi) {
        this.vnPayExternalApi = vnPayExternalApi;
    }

    @Override
    public PaymentResult pay(String bookingCode, Double amount, String description) {
        // Chuyển đổi dữ liệu từ hệ thống rạp phim sang chuẩn tham số của VNPay
        double vnpAmount = (amount != null) ? amount : 0.0;
        String vnpOrderInfo = (description != null && !description.isBlank())
                ? description
                : "Thanh toan ve don hang " + bookingCode;

        // Gọi trực tiếp SDK bên ngoài thông qua adapter
        VNPayExternalApi.VNPayTransactionResult result = vnPayExternalApi.createTransaction(
                TMN_CODE,
                bookingCode,
                vnpAmount,
                vnpOrderInfo,
                RETURN_URL
        );

        // Chuyển đổi mã phản hồi của VNPay ("00") sang PaymentResult nội bộ
        if (result != null && "00".equals(result.getVnp_ResponseCode())) {
            return PaymentResult.success(
                    result.getVnp_TransactionNo(),
                    "Thanh toán qua Cổng VNPay thành công (" + result.getVnp_Message() + ")",
                    "VNPAY",
                    amount
            );
        } else {
            return PaymentResult.failure(
                    "Thanh toán VNPay thất bại: " + (result != null ? result.getVnp_Message() : "Không phản hồi"),
                    "VNPAY",
                    amount
            );
        }
    }
}

