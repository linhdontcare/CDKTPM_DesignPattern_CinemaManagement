package com.cinema.cinemamanagement.pattern;

import com.cinema.cinemamanagement.pattern.adapter.MomoPaymentAdapter;
import com.cinema.cinemamanagement.pattern.adapter.PaymentResult;
import com.cinema.cinemamanagement.pattern.adapter.VNPayPaymentAdapter;
import com.cinema.cinemamanagement.pattern.strategy.CashPaymentStrategy;
import com.cinema.cinemamanagement.pattern.strategy.MomoPaymentStrategy;
import com.cinema.cinemamanagement.pattern.strategy.PaymentStrategy;
import com.cinema.cinemamanagement.pattern.strategy.VNPayPaymentStrategy;
import com.cinema.cinemamanagement.service.PaymentService;
import com.cinema.cinemamanagement.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class StrategyPatternTest {

    private CashPaymentStrategy cashStrategy;
    private MomoPaymentStrategy momoStrategy;
    private VNPayPaymentStrategy vnPayStrategy;
    private MomoPaymentAdapter mockMomoAdapter;
    private VNPayPaymentAdapter mockVNPayAdapter;
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        cashStrategy = new CashPaymentStrategy();

        mockMomoAdapter = Mockito.mock(MomoPaymentAdapter.class);
        momoStrategy = new MomoPaymentStrategy(mockMomoAdapter);

        mockVNPayAdapter = Mockito.mock(VNPayPaymentAdapter.class);
        vnPayStrategy = new VNPayPaymentStrategy(mockVNPayAdapter);

        List<PaymentStrategy> strategies = List.of(cashStrategy, momoStrategy, vnPayStrategy);
        paymentService = new PaymentServiceImpl(strategies);
    }

    @Test
    @DisplayName("Strategy Pattern: Kiểm thử CashPaymentStrategy trực tiếp tại quầy")
    void testCashPaymentStrategy() {
        PaymentResult result = cashStrategy.pay(85000.0, "BK-CASH-01");
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("CASH", result.getPaymentMethod());
        assertTrue(result.getTransactionId().startsWith("CASH-REC-"));
    }

    @Test
    @DisplayName("Strategy Pattern: Kiểm thử MomoPaymentStrategy thông qua adapter")
    void testMomoPaymentStrategy() {
        when(mockMomoAdapter.pay(eq("BK-MOMO-01"), eq(90000.0), anyString()))
                .thenReturn(PaymentResult.success("MOMO-123456", "Thành công", "MOMO", 90000.0));

        PaymentResult result = momoStrategy.pay(90000.0, "BK-MOMO-01");
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("MOMO", result.getPaymentMethod());
        assertEquals("MOMO-123456", result.getTransactionId());
    }

    @Test
    @DisplayName("Strategy Pattern: Kiểm thử VNPayPaymentStrategy thông qua adapter")
    void testVNPayPaymentStrategy() {
        when(mockVNPayAdapter.pay(eq("BK-VNPAY-01"), eq(120000.0), anyString()))
                .thenReturn(PaymentResult.success("VNPAY-9999", "Thành công", "VNPAY", 120000.0));

        PaymentResult result = vnPayStrategy.pay(120000.0, "BK-VNPAY-01");
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("VNPAY", result.getPaymentMethod());
        assertEquals("VNPAY-9999", result.getTransactionId());
    }

    @Test
    @DisplayName("Strategy Pattern: PaymentService tự động điều hướng đúng Strategy mà không dùng if/else")
    void testPaymentServiceRouting() {
        when(mockMomoAdapter.pay(anyString(), anyDouble(), anyString()))
                .thenReturn(PaymentResult.success("MOMO-TRANS", "OK", "MOMO", 75000.0));

        PaymentResult cashRes = paymentService.processPayment("CASH", "BK-01", 50000.0);
        assertTrue(cashRes.isSuccess());
        assertEquals("CASH", cashRes.getPaymentMethod());

        PaymentResult momoRes = paymentService.processPayment("MOMO", "BK-02", 75000.0);
        assertTrue(momoRes.isSuccess());
        assertEquals("MOMO", momoRes.getPaymentMethod());

        // Báo lỗi rõ ràng khi truyền phương thức không tồn tại
        assertThrows(IllegalArgumentException.class, () ->
                paymentService.processPayment("BITCOIN", "BK-03", 100000.0));
    }

    @Test
    @DisplayName("Thực nghiệm OCP (Theo Slide 5): Thêm phương thức mới ZaloPay mà KHÔNG sửa PaymentService")
    void testOpenClosedPrincipleWithZaloPay() {
        // Giả sử thêm cổng ZaloPay mới: chỉ việc tạo class implements PaymentStrategy
        PaymentStrategy zaloPayStrategy = new PaymentStrategy() {
            @Override
            public PaymentResult pay(Double amount, String bookingCode) {
                return PaymentResult.success("ZALOPAY-111", "Thành công qua ZaloPay", "ZALOPAY", amount);
            }

            @Override
            public String getPaymentMethodName() {
                return "ZALOPAY";
            }
        };

        // Danh sách chiến lược được bổ sung thêm ZaloPay
        List<PaymentStrategy> extendedStrategies = new ArrayList<>(List.of(cashStrategy, momoStrategy, vnPayStrategy));
        extendedStrategies.add(zaloPayStrategy);

        // PaymentService tự động tương thích mà không phải thay đổi 1 dòng code nào trong PaymentServiceImpl!
        PaymentService extendedPaymentService = new PaymentServiceImpl(extendedStrategies);

        PaymentResult zaloRes = extendedPaymentService.processPayment("ZALOPAY", "BK-ZALO-01", 99000.0);
        assertTrue(zaloRes.isSuccess());
        assertEquals("ZALOPAY", zaloRes.getPaymentMethod());
        assertEquals("ZALOPAY-111", zaloRes.getTransactionId());
        assertTrue(extendedPaymentService.getSupportedPaymentMethods().contains("ZALOPAY"));
    }
}

