package com.cinema.cinemamanagement.controller;

import com.cinema.cinemamanagement.entity.Booking;
import com.cinema.cinemamanagement.entity.Seat;
import com.cinema.cinemamanagement.entity.Showtime;
import com.cinema.cinemamanagement.service.BookingService;
import com.cinema.cinemamanagement.service.PaymentService;
import com.cinema.cinemamanagement.service.ShowtimeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final ShowtimeService showtimeService;
    private final PaymentService paymentService;

    public BookingController(BookingService bookingService,
                             ShowtimeService showtimeService,
                             PaymentService paymentService) {
        this.bookingService = bookingService;
        this.showtimeService = showtimeService;
        this.paymentService = paymentService;
    }

    @GetMapping
    public String listBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "bookings/list";
    }

    @GetMapping("/new")
    public String showBookingForm(@RequestParam(value = "showtimeId", required = false) Long showtimeId, Model model) {
        if (showtimeId == null) {
            List<Showtime> showtimes = showtimeService.getAllShowtimes();
            if (!showtimes.isEmpty()) {
                showtimeId = showtimes.get(0).getId();
            } else {
                return "redirect:/showtimes";
            }
        }

        Showtime showtime = showtimeService.getShowtimeById(showtimeId);
        List<Seat> seats = showtimeService.getSeatsForShowtime(showtimeId);

        model.addAttribute("showtime", showtime);
        model.addAttribute("seats", seats);
        model.addAttribute("allShowtimes", showtimeService.getAllShowtimes());
        return "bookings/select-seats";
    }

    @PostMapping("/new")
    public String createBooking(@RequestParam("showtimeId") Long showtimeId,
                                @RequestParam(value = "seatIds", required = false) List<Long> seatIds,
                                @RequestParam("customerName") String customerName,
                                @RequestParam("customerEmail") String customerEmail,
                                @RequestParam("customerPhone") String customerPhone,
                                RedirectAttributes redirectAttributes) {
        try {
            if (seatIds == null || seatIds.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng click chọn ít nhất 1 ghế trống trước khi tiếp tục!");
                return "redirect:/bookings/new?showtimeId=" + showtimeId;
            }

            Booking booking = bookingService.createBooking(showtimeId, seatIds, customerName, customerEmail, customerPhone);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Đã tạo đơn đặt vé " + booking.getBookingCode() + " thành công (Trạng thái: PENDING, Ghế tạm giữ HELD). Hãy tiến hành thanh toán!");
            return "redirect:/bookings/" + booking.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi tạo đặt vé: " + e.getMessage());
            return "redirect:/bookings/new?showtimeId=" + showtimeId;
        }
    }

    @GetMapping("/{id}")
    public String viewBookingDetail(@PathVariable Long id, Model model) {
        Booking booking = bookingService.getBookingById(id);
        model.addAttribute("booking", booking);
        model.addAttribute("supportedMethods", paymentService.getSupportedPaymentMethods());
        return "bookings/detail";
    }

    @PostMapping("/{id}/pay")
    public String payBooking(@PathVariable Long id,
                             @RequestParam("paymentMethod") String paymentMethod,
                             RedirectAttributes redirectAttributes) {
        try {
            Booking booking = bookingService.payBooking(id, paymentMethod);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Thanh toán thành công qua " + paymentMethod.toUpperCase() + "! Trạng thái đơn: PAID. Vui lòng nhấn Xác nhận vé để hoàn tất.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thanh toán thất bại: " + e.getMessage());
        }
        return "redirect:/bookings/" + id;
    }

    @PostMapping("/{id}/confirm")
    public String confirmBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Booking booking = bookingService.confirmBooking(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Xác nhận vé thành công (Trạng thái: CONFIRMED). Sự kiện BookingConfirmedEvent đã được phát đến Email, SMS và Tích điểm!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Xác nhận vé thất bại: " + e.getMessage());
        }
        return "redirect:/bookings/" + id;
    }

    @PostMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Booking booking = bookingService.cancelBooking(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Đã hủy đơn " + booking.getBookingCode() + " thành công. Các ghế đã được trả về AVAILABLE!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể hủy đơn: " + e.getMessage());
        }
        return "redirect:/bookings/" + id;
    }

    @PostMapping("/{id}/refund")
    public String refundBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Booking booking = bookingService.refundBooking(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Đã hoàn tiền cho đơn " + booking.getBookingCode() + " thành công (Trạng thái: REFUNDED). Ghế đã được giải phóng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể hoàn tiền: " + e.getMessage());
        }
        return "redirect:/bookings/" + id;
    }
}

