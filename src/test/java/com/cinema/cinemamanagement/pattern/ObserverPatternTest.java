package com.cinema.cinemamanagement.pattern;

import com.cinema.cinemamanagement.entity.Booking;
import com.cinema.cinemamanagement.entity.Movie;
import com.cinema.cinemamanagement.entity.Showtime;
import com.cinema.cinemamanagement.pattern.observer.BookingConfirmedEvent;
import com.cinema.cinemamanagement.pattern.observer.EmailNotificationObserver;
import com.cinema.cinemamanagement.pattern.observer.MemberPointObserver;
import com.cinema.cinemamanagement.pattern.observer.SmsNotificationObserver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ObserverPatternTest {

    @Test
    @DisplayName("Observer Pattern: BookingConfirmedEvent được đồng thời các Observer (Email, SMS, MemberPoint) tiếp nhận và xử lý")
    void testBookingConfirmedEventTriggersAllObservers() {
        // Khởi tạo các Observer độc lập
        EmailNotificationObserver emailObserver = new EmailNotificationObserver();
        SmsNotificationObserver smsObserver = new SmsNotificationObserver();
        MemberPointObserver pointObserver = new MemberPointObserver();

        // Tạo đối tượng Booking mẫu
        Movie movie = new Movie("Avatar 3", 190, "Sci-Fi", "Description");
        Showtime showtime = new Showtime(movie, LocalDateTime.now().plusDays(1), 100000.0, "IMAX 01");

        Booking booking = new Booking();
        booking.setBookingCode("BK-OBSERVER-999");
        booking.setCustomerName("Nguyễn Văn A");
        booking.setCustomerEmail("nguyenvana@gmail.com");
        booking.setCustomerPhone("0987654321");
        booking.setShowtime(showtime);
        booking.setTotalAmount(200000.0); // 200,000 VND -> tương đương 20 điểm

        // Tạo sự kiện BookingConfirmedEvent
        BookingConfirmedEvent event = new BookingConfirmedEvent(this, booking);

        // Kích hoạt sự kiện tới các Subscriber
        emailObserver.onBookingConfirmed(event);
        smsObserver.onBookingConfirmed(event);
        pointObserver.onBookingConfirmed(event);

        // 1. Kiểm tra EmailNotificationObserver đã nhận và lưu log
        assertFalse(emailObserver.getSentEmailLogs().isEmpty());
        assertTrue(emailObserver.getSentEmailLogs().get(0).contains("nguyenvana@gmail.com"));
        assertTrue(emailObserver.getSentEmailLogs().get(0).contains("BK-OBSERVER-999"));

        // 2. Kiểm tra SmsNotificationObserver đã nhận và lưu log
        assertFalse(smsObserver.getSentSmsLogs().isEmpty());
        assertTrue(smsObserver.getSentSmsLogs().get(0).contains("0987654321"));
        assertTrue(smsObserver.getSentSmsLogs().get(0).contains("BK-OBSERVER-999"));

        // 3. Kiểm tra MemberPointObserver đã tính đúng điểm (200,000 VND / 10,000 = 20 điểm)
        assertEquals(20, pointObserver.getTotalPointsAwarded());
        assertFalse(pointObserver.getPointLogs().isEmpty());
        assertTrue(pointObserver.getPointLogs().get(0).contains("20 điểm thưởng"));
    }
}

