package com.cinema.cinemamanagement.pattern.state;

import com.cinema.cinemamanagement.entity.BookingStatus;
import org.springframework.stereotype.Component;

/**
 * Trạng thái: EXPIRED (Đơn đã hết hạn giữ chỗ do quá thời gian thanh toán quy định).
 */
@Component
public class ExpiredState extends AbstractBookingState {

    @Override
    public BookingStatus getStatus() {
        return BookingStatus.EXPIRED;
    }
}

