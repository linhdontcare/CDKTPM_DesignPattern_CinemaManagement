package com.cinema.cinemamanagement.pattern.observer;

import com.cinema.cinemamanagement.entity.Booking;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * Event đại diện cho sự kiện một đơn đặt vé xem phim được xác nhận thành công (CONFIRMED).
 */
public class BookingConfirmedEvent extends ApplicationEvent {

    private final Booking booking;
    private final LocalDateTime confirmedAt;

    public BookingConfirmedEvent(Object source, Booking booking) {
        super(source);
        this.booking = booking;
        this.confirmedAt = LocalDateTime.now();
    }

    public Booking getBooking() {
        return booking;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }
}

