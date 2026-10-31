package com.cinema.cinemamanagement.pattern.observer;

import com.cinema.cinemamanagement.entity.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Concrete Observer: Tự động gửi Email xác nhận vé & mã đặt chỗ cho khách hàng.
 */
@Component
public class EmailNotificationObserver implements BookingObserver {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationObserver.class);
    private final List<String> sentEmailLogs = Collections.synchronizedList(new ArrayList<>());

    @Override
    @EventListener
    public void onBookingConfirmed(BookingConfirmedEvent event) {
        Booking booking = event.getBooking();
        String recipient = (booking.getCustomerEmail() != null && !booking.getCustomerEmail().isBlank())
                ? booking.getCustomerEmail()
                : "customer@cinema.vn";

        String logMessage = String.format("[EMAIL SENT] Gửi vé điện tử đến %s | Mã vé: %s | Phim: %s | Tổng tiền: %s VNĐ",
                recipient, booking.getBookingCode(), booking.getShowtime().getMovie().getTitle(), booking.getTotalAmount());

        sentEmailLogs.add(logMessage);
        logger.info(logMessage);
    }

    @Override
    public String getObserverName() {
        return "EmailNotificationObserver";
    }

    public List<String> getSentEmailLogs() {
        return new ArrayList<>(sentEmailLogs);
    }
}

