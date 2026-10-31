package com.cinema.cinemamanagement.service.impl;

import com.cinema.cinemamanagement.pattern.adapter.PaymentResult;
import com.cinema.cinemamanagement.pattern.strategy.PaymentStrategy;
import com.cinema.cinemamanagement.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Triển khai PaymentService áp dụng Strategy Pattern.
 *
 * ĐẶC ĐIỂM THIẾT KẾ:
 * 1. KHÔNG chứa bất kỳ khối lệnh `if-else` hoặc `switch-case` nào để phân loại phương thức thanh toán.
 * 2. Tuân thủ triệt để nguyên lý Open/Closed Principle (OCP): Khi cần thêm phương thức mới (ví dụ: ZaloPay, ShopeePay),
 *    chỉ cần tạo một class mới implements `PaymentStrategy` và đánh dấu `@Component`.
 *    Class này sẽ tự động được Spring Boot phát hiện và đăng ký mà KHÔNG PHẢI SỬA BẤT KỲ DÒNG CODE NÀO tại đây!
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    // Bảng tra cứu linh hoạt các Strategy được tự động tiêm bởi Spring
    private final Map<String, PaymentStrategy> strategyMap;

    public PaymentServiceImpl(List<PaymentStrategy> strategies) {
        // Thu thập toàn bộ các Strategy hiện có thành Map theo tên phương thức viết hoa
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        strategy -> strategy.getPaymentMethodName().toUpperCase(),
                        Function.identity()
                ));
        logger.info("PaymentService đã nạp {} phương thức thanh toán: {}",
                strategyMap.size(), strategyMap.keySet());
    }

    @Override
    public PaymentResult processPayment(String paymentMethod, String bookingCode, Double amount) {
        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new IllegalArgumentException("Vui lòng chọn phương thức thanh toán.");
        }

        String normalizedMethod = paymentMethod.trim().toUpperCase();
        PaymentStrategy strategy = strategyMap.get(normalizedMethod);

        if (strategy == null) {
            throw new IllegalArgumentException("Hệ thống chưa hỗ trợ phương thức thanh toán: " + paymentMethod
                    + ". Các phương thức hợp lệ: " + getSupportedPaymentMethods());
        }

        logger.info("Ủy quyền xử lý thanh toán cho Strategy: [{}]", strategy.getClass().getSimpleName());
        return strategy.pay(amount, bookingCode);
    }

    @Override
    public List<String> getSupportedPaymentMethods() {
        return strategyMap.keySet().stream().sorted().collect(Collectors.toList());
    }
}

