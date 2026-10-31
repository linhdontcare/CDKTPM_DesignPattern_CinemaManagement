package com.cinema.cinemamanagement.pattern.adapter.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Giả lập SDK / API đối tác VNPay.
 * Lưu ý: Signature, tham số và mã phản hồi hoàn toàn theo chuẩn của VNPay (vnp_ResponseCode: "00", vnp_TxnRef,...).
 */
@Component
public class VNPayExternalApi {

    private static final Logger logger = LoggerFactory.getLogger(VNPayExternalApi.class);

    public VNPayTransactionResult createTransaction(String vnp_TmnCode,
                                                    String vnp_TxnRef,
                                                    double vnp_Amount,
                                                    String vnp_OrderInfo,
                                                    String vnp_ReturnUrl) {
        logger.info("[VNPay SDK] Nhận yêu cầu khởi tạo giao dịch: TmnCode={}, TxnRef={}, Amount={}, Info='{}', ReturnUrl='{}'",
                vnp_TmnCode, vnp_TxnRef, vnp_Amount, vnp_OrderInfo, vnp_ReturnUrl);

        // Giả lập chuẩn mã của VNPay
        String transactionNo = "VNPAY" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        return new VNPayTransactionResult("00", "Giao dịch thành công", transactionNo, vnp_TxnRef, vnp_Amount);
    }

    public static class VNPayTransactionResult {
        private final String vnp_ResponseCode; // "00" = Thành công theo chuẩn VNPay
        private final String vnp_Message;
        private final String vnp_TransactionNo;
        private final String vnp_TxnRef;
        private final double vnp_Amount;

        public VNPayTransactionResult(String vnp_ResponseCode, String vnp_Message, String vnp_TransactionNo, String vnp_TxnRef, double vnp_Amount) {
            this.vnp_ResponseCode = vnp_ResponseCode;
            this.vnp_Message = vnp_Message;
            this.vnp_TransactionNo = vnp_TransactionNo;
            this.vnp_TxnRef = vnp_TxnRef;
            this.vnp_Amount = vnp_Amount;
        }

        public String getVnp_ResponseCode() {
            return vnp_ResponseCode;
        }

        public String getVnp_Message() {
            return vnp_Message;
        }

        public String getVnp_TransactionNo() {
            return vnp_TransactionNo;
        }

        public String getVnp_TxnRef() {
            return vnp_TxnRef;
        }

        public double getVnp_Amount() {
            return vnp_Amount;
        }
    }
}

