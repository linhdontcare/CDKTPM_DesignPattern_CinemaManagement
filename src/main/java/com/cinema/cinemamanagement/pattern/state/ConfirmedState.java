package com.cinema.cinemamanagement.pattern.state;

import com.cinema.cinemamanagement.entity.Booking;
import com.cinema.cinemamanagement.entity.BookingStatus;
import org.springframework.stereotype.Component;

/**
 * Trạng thái: CONFIRMED (Vé đã được xác nhận, ghế đã được chuyển sang BOOKED, sẵn sàng vào rạp).
 */
@Component
public class ConfirmedState extends AbstractBookingState {

    @Override
    public BookingStatus getStatus() {
        return BookingStatus.CONFIRMED;
    }

    @Override
    public void refund(Booking booking) {
        // Cho phép hoàn tiền nếu thỏa mãn điều kiện thời gian chiếu
        booking.setStatus(BookingStatus.REFUNDED);
    }

    @Override
    public void pay(Booking booking) {
        throw new InvalidStateTransitionException("Đơn đặt vé này đã được thanh toán và xác nhận trước đó.");
    }

    @Override
    public void confirm(Booking booking) {
        throw new InvalidStateTransitionException("Đơn đặt vé đã ở trạng thái xác nhận (CONFIRMED).");
    }
}

