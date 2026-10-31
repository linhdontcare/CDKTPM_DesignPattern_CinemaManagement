package com.cinema.cinemamanagement.pattern.baseline;

import com.cinema.cinemamanagement.pattern.adapter.PaymentResult;
import com.cinema.cinemamanagement.pattern.adapter.external.MomoExternalApi;
import com.cinema.cinemamanagement.pattern.adapter.external.VNPayExternalApi;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ⚠️ LỚP NÀY PHỤC VỤ MỤC ĐÍCH NGHIÊN CỨU & SO SÁNH (BASELINE - BEFORE PATTERNS).
 *
 * Minh họa mã nguồn KHI CHƯA ÁP DỤNG Strategy Pattern và Adapter Pattern:
 * 1. Vi phạm Single Responsibility Principle (SRP): Service này vừa điều phối nghiệp vụ,
 *    vừa trực tiếp xử lý thuật toán tiền mặt, vừa gọi MoMo, vừa gọi VNPay.
 * 2. Vi phạm Open/Closed Principle (OCP): Mỗi khi rạp phim muốn thêm đối tác mới (ví dụ: ZaloPay, ShopeePay),
 *    buộc lập trình viên phải mở file này ra, viết thêm nhánh `else if`, tiềm ẩn nguy cơ làm hỏng mã nguồn cũ.
 * 3. Tight Coupling (Liên kết chặt): Phụ thuộc trực tiếp vào các SDK đối tác bên ngoài (MomoExternalApi, VNPayExternalApi).
 *    Nếu MoMo hoặc VNPay đổi tên hàm hay kiểu trả về, class này lập tức bị lỗi biên dịch.
 */
@Service
public class BaselinePaymentService {

    private final MomoExternalApi momoApi;
    private final VNPayExternalApi vnpayApi;

    public BaselinePaymentService(MomoExternalApi momoApi, VNPayExternalApi vnpayApi) {
        this.momoApi = momoApi;
        this.vnpayApi = vnpayApi;
    }

    public PaymentResult payWithoutPattern(String paymentType, String bookingCode, Double amount) {
        // [VẤN ĐỀ 1]: Chuỗi if-else / switch liên tục phình to khi có phương thức mới
        if ("CASH".equalsIgnoreCase(paymentType)) {
            // [VẤN ĐỀ 2]: Logic tiền mặt nằm trực tiếp tại đây
            String receipt = "CASH-REC-" + DateTimeFormatter.ofPattern("yyMMddHHmmss").format(LocalDateTime.now());
            return PaymentResult.success(receipt, "Thu tiền mặt tại quầy", "CASH", amount);

        } else if ("MOMO".equalsIgnoreCase(paymentType)) {
            // [VẤN ĐỀ 3]: Gọi trực tiếp MoMo SDK với tham số đặc thù của họ
            long amountLong = amount != null ? amount.longValue() : 0L;
            MomoExternalApi.MomoResponse momoRes = momoApi.createPayment("MOMO_APP", bookingCode, amountLong, "Thanh toan");
            if (momoRes != null && momoRes.getResultCode() == 0) {
                return PaymentResult.success(momoRes.getTransId(), momoRes.getMessage(), "MOMO", amount);
            } else {
                return PaymentResult.failure("Thất bại MoMo", "MOMO", amount);
            }

        } else if ("VNPAY".equalsIgnoreCase(paymentType)) {
            // [VẤN ĐỀ 4]: Gọi trực tiếp VNPay SDK với interface và mã kết quả hoàn toàn khác MoMo
            VNPayExternalApi.VNPayTransactionResult vnpayRes = vnpayApi.createTransaction(
                    "TMN_VNPAY", bookingCode, amount != null ? amount : 0.0, "Info", "https://return.url"
            );
            if (vnpayRes != null && "00".equals(vnpayRes.getVnp_ResponseCode())) {
                return PaymentResult.success(vnpayRes.getVnp_TransactionNo(), vnpayRes.getVnp_Message(), "VNPAY", amount);
            } else {
                return PaymentResult.failure("Thất bại VNPay", "VNPAY", amount);
            }

        } else {
            // [VẤN ĐỀ 5]: Muốn thêm ZaloPay? Buộc phải thêm `else if ("ZALOPAY"...)` và import ZaloPay SDK vào đây!
            throw new IllegalArgumentException("Không hỗ trợ phương thức: " + paymentType);
        }
    }
}

