package com.cinema.cinemamanagement.pattern;

import com.cinema.cinemamanagement.pattern.adapter.MomoPaymentAdapter;
import com.cinema.cinemamanagement.pattern.adapter.PaymentResult;
import com.cinema.cinemamanagement.pattern.adapter.VNPayPaymentAdapter;
import com.cinema.cinemamanagement.pattern.adapter.external.MomoExternalApi;
import com.cinema.cinemamanagement.pattern.adapter.external.VNPayExternalApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdapterPatternTest {

    @Test
    @DisplayName("Adapter Pattern: MomoPaymentAdapter chuyển đổi đúng lời gọi sang MomoExternalApi")
    void testMomoPaymentAdapter() {
        MomoExternalApi mockMomoApi = Mockito.mock(MomoExternalApi.class);
        MomoExternalApi.MomoResponse mockResponse = new MomoExternalApi.MomoResponse(
                0, "Thành công", "MOMO-999888", "BK-TEST-01", 100000L, "https://momo.pay"
        );

        when(mockMomoApi.createPayment(anyString(), eq("BK-TEST-01"), eq(100000L), anyString()))
                .thenReturn(mockResponse);

        MomoPaymentAdapter adapter = new MomoPaymentAdapter(mockMomoApi);
        PaymentResult result = adapter.pay("BK-TEST-01", 100000.0, "Thanh toan ve test");

        assertNotNull(result);
        assertTrue(result.isSuccess(), "PaymentResult phải ở trạng thái thành công");
        assertEquals("MOMO-999888", result.getTransactionId());
        assertEquals("MOMO", result.getPaymentMethod());

        // Xác nhận Adapter đã gọi đúng hàm và signature của SDK bên ngoài
        verify(mockMomoApi).createPayment(anyString(), eq("BK-TEST-01"), eq(100000L), anyString());
    }

    @Test
    @DisplayName("Adapter Pattern: VNPayPaymentAdapter chuyển đổi đúng lời gọi sang VNPayExternalApi")
    void testVNPayPaymentAdapter() {
        VNPayExternalApi mockVNPayApi = Mockito.mock(VNPayExternalApi.class);
        VNPayExternalApi.VNPayTransactionResult mockResult = new VNPayExternalApi.VNPayTransactionResult(
                "00", "Giao dịch thành công", "VNPAY-TRANS-12345", "BK-TEST-02", 150000.0
        );

        when(mockVNPayApi.createTransaction(anyString(), eq("BK-TEST-02"), eq(150000.0), anyString(), anyString()))
                .thenReturn(mockResult);

        VNPayPaymentAdapter adapter = new VNPayPaymentAdapter(mockVNPayApi);
        PaymentResult result = adapter.pay("BK-TEST-02", 150000.0, "Thanh toan ve test");

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("VNPAY-TRANS-12345", result.getTransactionId());
        assertEquals("VNPAY", result.getPaymentMethod());

        // Xác nhận Adapter đã gọi đúng hàm và signature của VNPay SDK
        verify(mockVNPayApi).createTransaction(anyString(), eq("BK-TEST-02"), eq(150000.0), anyString(), anyString());
    }
}

