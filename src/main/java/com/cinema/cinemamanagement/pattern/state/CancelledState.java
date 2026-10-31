package com.cinema.cinemamanagement.pattern.state;

import com.cinema.cinemamanagement.entity.BookingStatus;
import org.springframework.stereotype.Component;

/**
 * Trạng thái: CANCELLED (Đơn đã bị hủy).
 */
@Component
public class CancelledState extends AbstractBookingState {

    @Override
    public BookingStatus getStatus() {
        return BookingStatus.CANCELLED;
    }
}

