package com.cinema.cinemamanagement.pattern.state;

import com.cinema.cinemamanagement.entity.Booking;
import com.cinema.cinemamanagement.entity.BookingStatus;
import org.springframework.stereotype.Component;

/**
 * Trạng thái: PAID (Đã nhận tiền thành công, chờ hệ thống xác nhận vé và xuất vé).
 */
@Component
public class PaidState extends AbstractBookingState {

    @Override
    public BookingStatus getStatus() {
        return BookingStatus.PAID;
    }

    @Override
    public void confirm(Booking booking) {
        booking.setStatus(BookingStatus.CONFIRMED);
    }

    @Override
    public void refund(Booking booking) {
        booking.setStatus(BookingStatus.REFUNDED);
    }
}

