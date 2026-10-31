package com.cinema.cinemamanagement.pattern.adapter.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Giả lập SDK / API đối tác MoMo.
 * Lưu ý: Signature và tham số hoàn toàn theo chuẩn của đối tác MoMo (khác với hệ thống nội bộ).
 */
@Component
public class MomoExternalApi {

    private static final Logger logger = LoggerFactory.getLogger(MomoExternalApi.class);

    public MomoResponse createPayment(String partnerCode, String orderId, long amountInVnd, String orderInfo) {
        logger.info("[MoMo SDK] Nhận yêu cầu thanh toán: Partner={}, OrderId={}, Amount={} VND, Info='{}'",
                partnerCode, orderId, amountInVnd, orderInfo);

        // Giả lập xử lý thanh toán của MoMo
        String momoTransId = "MOMO-" + System.currentTimeMillis();
        String payUrl = "https://test-payment.momo.vn/v2/gateway/pay?token=" + UUID.randomUUID();

        return new MomoResponse(0, "Thành công", momoTransId, orderId, amountInVnd, payUrl);
    }

    public static class MomoResponse {
        private final int resultCode; // 0 = Thành công theo chuẩn MoMo
        private final String message;
        private final String transId;
        private final String orderId;
        private final long amount;
        private final String payUrl;

        public MomoResponse(int resultCode, String message, String transId, String orderId, long amount, String payUrl) {
            this.resultCode = resultCode;
            this.message = message;
            this.transId = transId;
            this.orderId = orderId;
            this.amount = amount;
            this.payUrl = payUrl;
        }

        public int getResultCode() {
            return resultCode;
        }

        public String getMessage() {
            return message;
        }

        public String getTransId() {
            return transId;
        }

        public String getOrderId() {
            return orderId;
        }

        public long getAmount() {
            return amount;
        }

        public String getPayUrl() {
            return payUrl;
        }
    }
}

