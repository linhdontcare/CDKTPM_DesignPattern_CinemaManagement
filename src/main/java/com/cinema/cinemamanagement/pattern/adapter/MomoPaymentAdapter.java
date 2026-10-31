package com.cinema.cinemamanagement.pattern.adapter;

import com.cinema.cinemamanagement.pattern.adapter.external.MomoExternalApi;
import org.springframework.stereotype.Component;

/**
 * Adapter chuyển đổi giao diện MoMo SDK sang PaymentGateway nội bộ.
 */
@Component
public class MomoPaymentAdapter implements PaymentGateway {

    private static final String PARTNER_CODE = "MOMO_CINEMA_APP";
    private final MomoExternalApi momoExternalApi;

    public MomoPaymentAdapter(MomoExternalApi momoExternalApi) {
        this.momoExternalApi = momoExternalApi;
    }

    @Override
    public PaymentResult pay(String bookingCode, Double amount, String description) {
        // Chuyển đổi dữ liệu từ hệ thống rạp phim sang chuẩn tham số của MoMo
        long amountInVnd = (amount != null) ? amount.longValue() : 0L;
        String orderInfo = (description != null && !description.isBlank())
                ? description
                : "Thanh toan ve xem phim cho don " + bookingCode;

        // Gọi trực tiếp SDK bên ngoài thông qua adapter
        MomoExternalApi.MomoResponse response = momoExternalApi.createPayment(
                PARTNER_CODE,
                bookingCode,
                amountInVnd,
                orderInfo
        );

        // Chuyển đổi kết quả từ chuẩn MoMo về PaymentResult chuẩn nội bộ
        if (response != null && response.getResultCode() == 0) {
            return PaymentResult.success(
                    response.getTransId(),
                    "Thanh toán qua Ví MoMo thành công. Link: " + response.getPayUrl(),
                    "MOMO",
                    amount
            );
        } else {
            return PaymentResult.failure(
                    "Thanh toán MoMo thất bại: " + (response != null ? response.getMessage() : "Không phản hồi"),
                    "MOMO",
                    amount
            );
        }
    }
}

