package com.cinema.cinemamanagement.pattern.state;

import com.cinema.cinemamanagement.entity.BookingStatus;
import org.springframework.stereotype.Component;

/**
 * Trạng thái: REFUNDED (Đơn đã được hoàn tiền cho khách hàng).
 */
@Component
public class RefundedState extends AbstractBookingState {

    @Override
    public BookingStatus getStatus() {
        return BookingStatus.REFUNDED;
    }
}

