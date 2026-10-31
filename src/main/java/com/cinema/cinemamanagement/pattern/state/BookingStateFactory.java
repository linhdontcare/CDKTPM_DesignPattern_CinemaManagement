package com.cinema.cinemamanagement.pattern.state;

import com.cinema.cinemamanagement.entity.Booking;
import com.cinema.cinemamanagement.entity.BookingStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * State Factory: Cung cấp đối tượng State tương ứng cho một Booking dựa trên trạng thái hiện tại.
 */
@Component
public class BookingStateFactory {

    private final Map<BookingStatus, BookingState> stateMap;

    public BookingStateFactory(List<BookingState> states) {
        this.stateMap = states.stream()
                .collect(Collectors.toMap(BookingState::getStatus, Function.identity()));
    }

    public BookingState getState(BookingStatus status) {
        BookingState state = stateMap.get(status);
        if (state == null) {
            throw new IllegalArgumentException("Không tìm thấy State handler cho trạng thái: " + status);
        }
        return state;
    }

    public BookingState getState(Booking booking) {
        return getState(booking.getStatus());
    }
}

