package com.cinema.cinemamanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookingPlaceholderController {

    @GetMapping("/bookings")
    public String bookingsRoadmap(Model model) {
        model.addAttribute("title", "Quản lý Đặt vé (Booking)");
        model.addAttribute("patternName", "State Pattern & Observer Pattern");
        model.addAttribute("description", "Module này sẽ được kích hoạt tại Phase tiếp theo để quản lý vòng đời Booking (Pending, Awaiting Payment, Paid, Confirmed, Cancelled, Refunded, Expired) và phát sự kiện BookingConfirmedEvent đến các Observer (Email, SMS, MemberPoint).");
        return "placeholder";
    }

    @GetMapping("/payments")
    public String paymentsRoadmap(Model model) {
        model.addAttribute("title", "Quản lý Thanh toán (Payments)");
        model.addAttribute("patternName", "Strategy Pattern & Adapter Pattern");
        model.addAttribute("description", "Module này sẽ được kích hoạt tại Phase tiếp theo để xử lý thanh toán đa phương thức (Cash, MoMo, VNPay) thông qua Strategy Pattern và tích hợp API đối tác qua Adapter Pattern.");
        return "placeholder";
    }
}
