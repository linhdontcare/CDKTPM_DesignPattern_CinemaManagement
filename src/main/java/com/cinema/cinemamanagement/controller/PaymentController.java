package com.cinema.cinemamanagement.controller;

import com.cinema.cinemamanagement.pattern.observer.EmailNotificationObserver;
import com.cinema.cinemamanagement.pattern.observer.MemberPointObserver;
import com.cinema.cinemamanagement.pattern.observer.SmsNotificationObserver;
import com.cinema.cinemamanagement.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final EmailNotificationObserver emailObserver;
    private final SmsNotificationObserver smsObserver;
    private final MemberPointObserver pointObserver;

    public PaymentController(PaymentService paymentService,
                             EmailNotificationObserver emailObserver,
                             SmsNotificationObserver smsObserver,
                             MemberPointObserver pointObserver) {
        this.paymentService = paymentService;
        this.emailObserver = emailObserver;
        this.smsObserver = smsObserver;
        this.pointObserver = pointObserver;
    }

    @GetMapping
    public String paymentDashboard(Model model) {
        model.addAttribute("supportedMethods", paymentService.getSupportedPaymentMethods());
        model.addAttribute("emailLogs", emailObserver.getSentEmailLogs());
        model.addAttribute("smsLogs", smsObserver.getSentSmsLogs());
        model.addAttribute("pointLogs", pointObserver.getPointLogs());
        model.addAttribute("totalPoints", pointObserver.getTotalPointsAwarded());
        return "payments/dashboard";
    }
}

