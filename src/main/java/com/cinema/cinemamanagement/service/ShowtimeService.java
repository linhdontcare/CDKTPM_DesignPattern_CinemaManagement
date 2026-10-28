package com.cinema.cinemamanagement.service;

import com.cinema.cinemamanagement.dto.ShowtimeDto;
import com.cinema.cinemamanagement.entity.Seat;
import com.cinema.cinemamanagement.entity.Showtime;

import java.util.List;

public interface ShowtimeService {
    List<Showtime> getAllShowtimes();
    Showtime getShowtimeById(Long id);
    List<Showtime> getShowtimesByMovieId(Long movieId);
    Showtime createShowtime(ShowtimeDto dto);
    Showtime updateShowtime(Long id, ShowtimeDto dto);
    void deleteShowtime(Long id);
    List<Seat> getSeatsForShowtime(Long showtimeId);
    long countShowtimes();
}
