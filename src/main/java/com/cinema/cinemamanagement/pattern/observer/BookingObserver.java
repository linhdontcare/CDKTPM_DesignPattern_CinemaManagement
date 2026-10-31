package com.cinema.cinemamanagement.pattern.observer;

/**
 * Observer Interface trong Observer Pattern.
 * Định nghĩa hợp đồng cho các Subscriber phản ứng lại sự kiện BookingConfirmedEvent.
 */
public interface BookingObserver {

    /**
     * Phương thức phản ứng khi nhận được sự kiện xác nhận vé.
     */
    void onBookingConfirmed(BookingConfirmedEvent event);

    /**
     * Tên định danh của Observer (dùng cho logging và kiểm thử).
     */
    String getObserverName();
}

