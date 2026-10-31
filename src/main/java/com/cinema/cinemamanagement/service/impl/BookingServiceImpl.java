package com.cinema.cinemamanagement.service.impl;

import com.cinema.cinemamanagement.entity.*;
import com.cinema.cinemamanagement.pattern.adapter.PaymentResult;
import com.cinema.cinemamanagement.pattern.observer.BookingConfirmedEvent;
import com.cinema.cinemamanagement.pattern.state.BookingState;
import com.cinema.cinemamanagement.pattern.state.BookingStateFactory;
import com.cinema.cinemamanagement.repository.BookingRepository;
import com.cinema.cinemamanagement.repository.SeatRepository;
import com.cinema.cinemamanagement.repository.ShowtimeRepository;
import com.cinema.cinemamanagement.service.BookingService;
import com.cinema.cinemamanagement.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final BookingStateFactory stateFactory;
    private final PaymentService paymentService;
    private final ApplicationEventPublisher eventPublisher;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              ShowtimeRepository showtimeRepository,
                              SeatRepository seatRepository,
                              BookingStateFactory stateFactory,
                              PaymentService paymentService,
                              ApplicationEventPublisher eventPublisher) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
        this.seatRepository = seatRepository;
        this.stateFactory = stateFactory;
        this.paymentService = paymentService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Booking createBooking(Long showtimeId, List<Long> seatIds, String customerName, String customerEmail, String customerPhone) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy suất chiếu ID: " + showtimeId));

        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn ít nhất 1 ghế ngồi.");
        }

        List<Seat> seats = seatRepository.findAllById(seatIds);
        for (Seat seat : seats) {
            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                throw new IllegalStateException("Ghế " + seat.getSeatNumber() + " không còn khả dụng (Trạng thái: " + seat.getStatus() + ").");
            }
            // Tạm giữ ghế
            seat.setStatus(SeatStatus.HELD);
        }
        seatRepository.saveAll(seats);

        Booking booking = new Booking();
        booking.setBookingCode("BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        booking.setShowtime(showtime);
        booking.setSeats(seats);
        booking.setCustomerName(customerName != null ? customerName.trim() : "Khách xem phim");
        booking.setCustomerEmail(customerEmail != null ? customerEmail.trim() : "");
        booking.setCustomerPhone(customerPhone != null ? customerPhone.trim() : "");
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalAmount(showtime.getBasePrice() * seats.size());

        Booking saved = bookingRepository.save(booking);
        logger.info("Tạo booking thành công: Code={}, Trạng thái=PENDING, Tổng tiền={}", saved.getBookingCode(), saved.getTotalAmount());
        return saved;
    }

    @Override
    public Booking payBooking(Long bookingId, String paymentMethod) {
        Booking booking = getBookingById(bookingId);

        // 1. Dùng State Pattern để xác thực và chuyển đổi trạng thái (tránh if-else trạng thái)
        BookingState currentState = stateFactory.getState(booking);
        currentState.pay(booking); // Chuyển sang PAID nếu hợp lệ, ném ngoại lệ nếu không hợp lệ

        // 2. Dùng Strategy Pattern để thanh toán (tránh if-else phương thức thanh toán)
        PaymentResult paymentResult = paymentService.processPayment(paymentMethod, booking.getBookingCode(), booking.getTotalAmount());
        if (!paymentResult.isSuccess()) {
            throw new IllegalStateException("Thanh toán thất bại: " + paymentResult.getMessage());
        }

        booking.setPaymentMethod(paymentMethod.toUpperCase());
        Booking saved = bookingRepository.save(booking);
        logger.info("Thanh toán thành công cho đơn {}: Method={}, TransId={}",
                saved.getBookingCode(), paymentMethod, paymentResult.getTransactionId());
        return saved;
    }

    @Override
    public Booking confirmBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);

        // 1. Dùng State Pattern để chuyển sang CONFIRMED
        BookingState currentState = stateFactory.getState(booking);
        currentState.confirm(booking);

        // 2. Cập nhật các ghế sang BOOKED
        for (Seat seat : booking.getSeats()) {
            seat.setStatus(SeatStatus.BOOKED);
        }
        seatRepository.saveAll(booking.getSeats());

        Booking saved = bookingRepository.save(booking);
        logger.info("Xác nhận đơn đặt vé thành công: {}", saved.getBookingCode());

        // 3. OBSERVER PATTERN: Chỉ phát sự kiện, không gọi trực tiếp Email/SMS/Point service
        eventPublisher.publishEvent(new BookingConfirmedEvent(this, saved));

        return saved;
    }

    @Override
    public Booking cancelBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);

        // State Pattern kiểm soát việc hủy
        BookingState currentState = stateFactory.getState(booking);
        currentState.cancel(booking);

        // Giải phóng ghế về AVAILABLE
        for (Seat seat : booking.getSeats()) {
            seat.setStatus(SeatStatus.AVAILABLE);
        }
        seatRepository.saveAll(booking.getSeats());

        Booking saved = bookingRepository.save(booking);
        logger.info("Đã hủy đơn đặt vé {}. Các ghế đã được giải phóng.", saved.getBookingCode());
        return saved;
    }

    @Override
    public Booking refundBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);

        // State Pattern kiểm soát việc hoàn tiền
        BookingState currentState = stateFactory.getState(booking);
        currentState.refund(booking);

        // Giải phóng ghế về AVAILABLE
        for (Seat seat : booking.getSeats()) {
            seat.setStatus(SeatStatus.AVAILABLE);
        }
        seatRepository.saveAll(booking.getSeats());

        Booking saved = bookingRepository.save(booking);
        logger.info("Đã hoàn tiền cho đơn {}. Các ghế đã được giải phóng.", saved.getBookingCode());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn đặt vé với ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getBookingByCode(String code) {
        return bookingRepository.findByBookingCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn đặt vé với mã: " + code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getAllBookings() {
        return bookingRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public long countBookings() {
        return bookingRepository.count();
    }
}

