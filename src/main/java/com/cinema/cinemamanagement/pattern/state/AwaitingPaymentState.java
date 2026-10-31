package com.cinema.cinemamanagement.pattern.state;

import com.cinema.cinemamanagement.entity.Booking;
import com.cinema.cinemamanagement.entity.BookingStatus;
import org.springframework.stereotype.Component;

/**
 * Trạng thái: AWAITING_PAYMENT (Đang chờ khách thanh toán qua cổng MoMo, VNPay hoặc tại quầy).
 */
@Component
public class AwaitingPaymentState extends AbstractBookingState {

    @Override
    public BookingStatus getStatus() {
        return BookingStatus.AWAITING_PAYMENT;
    }

    @Override
    public void pay(Booking booking) {
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

