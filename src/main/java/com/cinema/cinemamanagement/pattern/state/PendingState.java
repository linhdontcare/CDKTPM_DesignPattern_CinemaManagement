package com.cinema.cinemamanagement.pattern.state;

import com.cinema.cinemamanagement.entity.Booking;
import com.cinema.cinemamanagement.entity.BookingStatus;
import org.springframework.stereotype.Component;

/**
 * Trạng thái: PENDING (Đơn vừa được khởi tạo, chưa xử lý thanh toán).
 */
@Component
public class PendingState extends AbstractBookingState {

    @Override
    public BookingStatus getStatus() {
        return BookingStatus.PENDING;
    }

    @Override
    public void awaitPayment(Booking booking) {
        booking.setStatus(BookingStatus.AWAITING_PAYMENT);
    }

    @Override
    public void pay(Booking booking) {
        // Cho phép thanh toán trực tiếp từ PENDING
        booking.setStatus(BookingStatus.PAID);
    }

    @Override
    public void cancel(Booking booking) {
        booking.setStatus(BookingStatus.CANCELLED);
    }

    @Override
    public void expire(Booking booking) {
        booking.setStatus(BookingStatus.EXPIRED);
    }
}

