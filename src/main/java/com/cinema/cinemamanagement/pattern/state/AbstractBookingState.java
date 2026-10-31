package com.cinema.cinemamanagement.pattern.state;

import com.cinema.cinemamanagement.entity.Booking;

/**
 * Lớp trừu tượng cơ sở cho các State.
 * Mặc định ném InvalidStateTransitionException cho tất cả hành động không được phép tại trạng thái đó.
 */
public abstract class AbstractBookingState implements BookingState {

    @Override
    public void awaitPayment(Booking booking) {
        throw new InvalidStateTransitionException("Không thể chuyển sang chờ thanh toán khi đơn đang ở trạng thái: " + getStatus());
    }

    @Override
    public void pay(Booking booking) {
        throw new InvalidStateTransitionException("Không thể thực hiện thanh toán khi đơn đang ở trạng thái: " + getStatus());
    }

    @Override
    public void confirm(Booking booking) {
        throw new InvalidStateTransitionException("Không thể xác nhận (Confirm) khi đơn đang ở trạng thái: " + getStatus());
    }

    @Override
    public void cancel(Booking booking) {
        throw new InvalidStateTransitionException("Không thể hủy đơn đặt vé khi đang ở trạng thái: " + getStatus());
    }

    @Override
    public void refund(Booking booking) {
        throw new InvalidStateTransitionException("Không thể hoàn tiền khi đơn đang ở trạng thái: " + getStatus());
    }

    @Override
    public void expire(Booking booking) {
        throw new InvalidStateTransitionException("Không thể đánh dấu hết hạn khi đơn đang ở trạng thái: " + getStatus());
    }
}

