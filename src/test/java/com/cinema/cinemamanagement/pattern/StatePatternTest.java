package com.cinema.cinemamanagement.pattern;

import com.cinema.cinemamanagement.entity.Booking;
import com.cinema.cinemamanagement.entity.BookingStatus;
import com.cinema.cinemamanagement.pattern.state.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatePatternTest {

    private BookingStateFactory stateFactory;
    private Booking booking;

    @BeforeEach
    void setUp() {
        List<BookingState> states = List.of(
                new PendingState(),
                new AwaitingPaymentState(),
                new PaidState(),
                new ConfirmedState(),
                new CancelledState(),
                new RefundedState(),
                new ExpiredState()
        );
        stateFactory = new BookingStateFactory(states);

        booking = new Booking();
        booking.setBookingCode("BK-TEST-STATE");
        booking.setStatus(BookingStatus.PENDING);
    }

    @Test
    @DisplayName("State Pattern: Chuyển đổi tuần tự hợp lệ: PENDING -> AWAITING_PAYMENT -> PAID -> CONFIRMED")
    void testValidStateTransitions() {
        assertEquals(BookingStatus.PENDING, booking.getStatus());

        // 1. Pending -> AwaitingPayment
        BookingState state = stateFactory.getState(booking);
        state.awaitPayment(booking);
        assertEquals(BookingStatus.AWAITING_PAYMENT, booking.getStatus());

        // 2. AwaitingPayment -> Paid
        state = stateFactory.getState(booking);
        state.pay(booking);
        assertEquals(BookingStatus.PAID, booking.getStatus());

        // 3. Paid -> Confirmed
        state = stateFactory.getState(booking);
        state.confirm(booking);
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }

    @Test
    @DisplayName("State Pattern: PENDING có thể Cancelled")
    void testPendingCanCancel() {
        BookingState state = stateFactory.getState(booking);
        state.cancel(booking);
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }

    @Test
    @DisplayName("State Pattern: CANCELLED không được phép thanh toán (pay)")
    void testCancelledCannotPay() {
        booking.setStatus(BookingStatus.CANCELLED);
        BookingState state = stateFactory.getState(booking);

        assertThrows(InvalidStateTransitionException.class, () -> state.pay(booking),
                "Đơn đã hủy không được phép thanh toán");
    }

    @Test
    @DisplayName("State Pattern: CONFIRMED không được thanh toán lại lần thứ hai")
    void testConfirmedCannotPayAgain() {
        booking.setStatus(BookingStatus.CONFIRMED);
        BookingState state = stateFactory.getState(booking);

        assertThrows(InvalidStateTransitionException.class, () -> state.pay(booking),
                "Đơn đã xác nhận thành công không được thanh toán lại");
    }

    @Test
    @DisplayName("State Pattern: CONFIRMED không thể gọi confirm lần nữa")
    void testConfirmedCannotConfirmAgain() {
        booking.setStatus(BookingStatus.CONFIRMED);
        BookingState state = stateFactory.getState(booking);

        assertThrows(InvalidStateTransitionException.class, () -> state.confirm(booking),
                "Đơn đã xác nhận không thể confirm lại");
    }

    @Test
    @DisplayName("State Pattern: CONFIRMED có thể Hoàn tiền (Refunded)")
    void testConfirmedCanRefund() {
        booking.setStatus(BookingStatus.CONFIRMED);
        BookingState state = stateFactory.getState(booking);

        state.refund(booking);
        assertEquals(BookingStatus.REFUNDED, booking.getStatus());
    }
}

