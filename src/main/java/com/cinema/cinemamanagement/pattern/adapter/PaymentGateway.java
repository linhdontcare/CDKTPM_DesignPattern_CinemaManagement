package com.cinema.cinemamanagement.pattern.adapter;

/**
 * Target Interface trong Adapter Pattern.
 * Hệ thống nội bộ chỉ tương tác với Interface chuẩn này,
 * hoàn toàn không phụ thuộc trực tiếp vào SDK hoặc API bên thứ ba.
 */
public interface PaymentGateway {

    /**
     * Xử lý thanh toán chuẩn hóa cho hệ thống rạp chiếu phim.
     *
     * @param bookingCode Mã đơn đặt vé
     * @param amount      Số tiền thanh toán
     * @param description Mô tả giao dịch
     * @return PaymentResult chuẩn hóa
     */
    PaymentResult pay(String bookingCode, Double amount, String description);
}

