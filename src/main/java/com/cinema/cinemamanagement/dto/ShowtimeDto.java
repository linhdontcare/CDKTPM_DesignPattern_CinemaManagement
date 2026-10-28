package com.cinema.cinemamanagement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class ShowtimeDto {

    private Long id;

    @NotNull(message = "Vui lòng chọn phim")
    private Long movieId;

    @NotNull(message = "Vui lòng chọn ngày giờ chiếu")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;

    @NotNull(message = "Vui lòng nhập giá vé cơ bản")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá vé phải lớn hơn 0")
    private Double basePrice;

    private String roomName = "Hall 1";

    public ShowtimeDto() {
    }

    public ShowtimeDto(Long id, Long movieId, LocalDateTime startTime, Double basePrice, String roomName) {
        this.id = id;
        this.movieId = movieId;
        this.startTime = startTime;
        this.basePrice = basePrice;
        this.roomName = roomName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
