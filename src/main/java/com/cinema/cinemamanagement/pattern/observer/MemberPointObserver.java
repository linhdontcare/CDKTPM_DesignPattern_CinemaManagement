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
 * Concrete Observer: Tự động tính và cộng điểm thưởng thành viên (Loyalty Points) sau khi đơn đặt vé hoàn tất.
 */
@Component
public class MemberPointObserver implements BookingObserver {

    private static final Logger logger = LoggerFactory.getLogger(MemberPointObserver.class);
    private final List<String> pointLogs = Collections.synchronizedList(new ArrayList<>());
    private int totalPointsAwarded = 0;

    @Override
    @EventListener
    public void onBookingConfirmed(BookingConfirmedEvent event) {
        Booking booking = event.getBooking();
        // Quy tắc: 10,000 VND = 1 điểm thưởng
        int points = (int) (booking.getTotalAmount() / 10000.0);
        totalPointsAwarded += points;

        String customer = (booking.getCustomerName() != null && !booking.getCustomerName().isBlank())
                ? booking.getCustomerName()
                : "Khách vãng lai";

        String logMessage = String.format("[MEMBER POINT] Khách hàng '%s' (Mã vé %s) được cộng +%d điểm thưởng. Tổng điểm tích lũy: %d điểm",
                customer, booking.getBookingCode(), points, totalPointsAwarded);

        pointLogs.add(logMessage);
        logger.info(logMessage);
    }

    @Override
    public String getObserverName() {
        return "MemberPointObserver";
    }

    public List<String> getPointLogs() {
        return new ArrayList<>(pointLogs);
    }

    public int getTotalPointsAwarded() {
        return totalPointsAwarded;
    }
}

