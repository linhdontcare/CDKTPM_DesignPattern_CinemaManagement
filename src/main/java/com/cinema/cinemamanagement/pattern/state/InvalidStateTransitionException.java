package com.cinema.cinemamanagement.pattern.state;

/**
 * Ngoại lệ được ném ra khi có một hành động không hợp lệ đối với trạng thái hiện tại của Booking.
 * Ví dụ: Đơn đã hủy (Cancelled) không được phép thanh toán.
 */
public class InvalidStateTransitionException extends IllegalStateException {

    public InvalidStateTransitionException(String message) {
        super(message);
    }
}

