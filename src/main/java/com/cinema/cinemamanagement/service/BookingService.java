package com.cinema.cinemamanagement.service;

import com.cinema.cinemamanagement.entity.Booking;

import java.util.List;

public interface BookingService {

    /**
     * Khởi tạo đơn đặt vé mới và chuyển ghế sang trạng thái HELD (Giữ chỗ).
     */
    Booking createBooking(Long showtimeId, List<Long> seatIds, String customerName, String customerEmail, String customerPhone);

    /**
     * Thực hiện thanh toán cho đơn đặt vé thông qua Strategy Pattern & State Pattern.
     */
    Booking payBooking(Long bookingId, String paymentMethod);

    /**
     * Xác nhận đơn đặt vé (CONFIRMED), cập nhật ghế sang BOOKED và phát sự kiện BookingConfirmedEvent qua Observer Pattern.
     */
    Booking confirmBooking(Long bookingId);

    /**
     * Hủy đơn đặt vé khi còn hợp lệ (PENDING / AWAITING_PAYMENT), giải phóng ghế về AVAILABLE.
     */
    Booking cancelBooking(Long bookingId);

    /**
     * Hoàn tiền cho đơn đặt vé (PAID / CONFIRMED), giải phóng ghế về AVAILABLE.
     */
    Booking refundBooking(Long bookingId);

    Booking getBookingById(Long id);

    Booking getBookingByCode(String code);

    List<Booking> getAllBookings();

    long countBookings();
}

