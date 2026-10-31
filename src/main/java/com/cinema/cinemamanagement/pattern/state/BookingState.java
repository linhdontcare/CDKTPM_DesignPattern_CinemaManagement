package com.cinema.cinemamanagement.pattern.state;

import com.cinema.cinemamanagement.entity.Booking;
import com.cinema.cinemamanagement.entity.BookingStatus;

/**
 * State Interface trong State Pattern.
 * Định nghĩa các hành vi mà một Booking có thể thực hiện tùy theo trạng thái vòng đời.
 */
public interface BookingState {

    /**
     * Lấy trạng thái định danh tương ứng trong Enum.
     */
    BookingStatus getStatus();

    /**
     * Chuyển đơn sang trạng thái chờ thanh toán (Pending -> AwaitingPayment).
     */
    void awaitPayment(Booking booking);

    /**
     * Thực hiện thanh toán (AwaitingPayment -> Paid).
     */
    void pay(Booking booking);

    /**
     * Xác nhận đơn đặt vé sau khi thanh toán thành công (Paid -> Confirmed).
     */
    void confirm(Booking booking);

    /**
     * Hủy đơn đặt vé khi còn hợp lệ (Pending / AwaitingPayment -> Cancelled).
     */
    void cancel(Booking booking);

    /**
     * Hoàn tiền cho đơn đặt vé (Paid / Confirmed -> Refunded).
     */
    void refund(Booking booking);

    /**
     * Đơn bị hết hạn giữ chỗ do quá thời gian chờ thanh toán (-> Expired).
     */
    void expire(Booking booking);
}

