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
 * Concrete Observer: Tự động gửi SMS mã vé xem phim đến số điện thoại khách hàng.
 */
@Component
public class SmsNotificationObserver implements BookingObserver {

    private static final Logger logger = LoggerFactory.getLogger(SmsNotificationObserver.class);
    private final List<String> sentSmsLogs = Collections.synchronizedList(new ArrayList<>());

    @Override
    @EventListener
    public void onBookingConfirmed(BookingConfirmedEvent event) {
        Booking booking = event.getBooking();
        String phone = (booking.getCustomerPhone() != null && !booking.getCustomerPhone().isBlank())
                ? booking.getCustomerPhone()
                : "0900000000";

        String logMessage = String.format("[SMS SENT] Gửi tin nhắn SMS đến SĐT %s: Ban da dat ve thanh cong ma %s tai Rap Cinema. Chuc ban xem phim vui ve!",
                phone, booking.getBookingCode());

        sentSmsLogs.add(logMessage);
        logger.info(logMessage);
    }

    @Override
    public String getObserverName() {
        return "SmsNotificationObserver";
    }

    public List<String> getSentSmsLogs() {
        return new ArrayList<>(sentSmsLogs);
    }
}

