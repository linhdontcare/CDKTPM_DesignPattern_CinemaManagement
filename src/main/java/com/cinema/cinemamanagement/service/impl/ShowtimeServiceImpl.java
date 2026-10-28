package com.cinema.cinemamanagement.service.impl;

import com.cinema.cinemamanagement.dto.ShowtimeDto;
import com.cinema.cinemamanagement.entity.Movie;
import com.cinema.cinemamanagement.entity.Seat;
import com.cinema.cinemamanagement.entity.SeatStatus;
import com.cinema.cinemamanagement.entity.Showtime;
import com.cinema.cinemamanagement.repository.MovieRepository;
import com.cinema.cinemamanagement.repository.SeatRepository;
import com.cinema.cinemamanagement.repository.ShowtimeRepository;
import com.cinema.cinemamanagement.service.ShowtimeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final SeatRepository seatRepository;

    public ShowtimeServiceImpl(ShowtimeRepository showtimeRepository,
                               MovieRepository movieRepository,
                               SeatRepository seatRepository) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAllByOrderByStartTimeAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public Showtime getShowtimeById(Long id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy suất chiếu với ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Showtime> getShowtimesByMovieId(Long movieId) {
        return showtimeRepository.findByMovieId(movieId);
    }

    @Override
    public Showtime createShowtime(ShowtimeDto dto) {
        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phim với ID: " + dto.getMovieId()));

        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setStartTime(dto.getStartTime());
        showtime.setBasePrice(dto.getBasePrice());
        showtime.setRoomName(dto.getRoomName() != null && !dto.getRoomName().isBlank() ? dto.getRoomName() : "Hall 1");

        Showtime savedShowtime = showtimeRepository.save(showtime);

        // Generate seats: A1-A10, B1-B10, C1-C10
        generateSeatsForShowtime(savedShowtime);

        return savedShowtime;
    }

    @Override
    public Showtime updateShowtime(Long id, ShowtimeDto dto) {
        Showtime showtime = getShowtimeById(id);
        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phim với ID: " + dto.getMovieId()));

        showtime.setMovie(movie);
        showtime.setStartTime(dto.getStartTime());
        showtime.setBasePrice(dto.getBasePrice());
        if (dto.getRoomName() != null && !dto.getRoomName().isBlank()) {
            showtime.setRoomName(dto.getRoomName());
        }

        return showtimeRepository.save(showtime);
    }

    @Override
    public void deleteShowtime(Long id) {
        if (!showtimeRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy suất chiếu với ID: " + id);
        }
        // Seats with showtime will be deleted or handled
        List<Seat> seats = seatRepository.findByShowtimeIdOrderBySeatNumberAsc(id);
        seatRepository.deleteAll(seats);
        showtimeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Seat> getSeatsForShowtime(Long showtimeId) {
        return seatRepository.findByShowtimeIdOrderBySeatNumberAsc(showtimeId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countShowtimes() {
        return showtimeRepository.count();
    }

    private void generateSeatsForShowtime(Showtime showtime) {
        List<Seat> seats = new ArrayList<>();
        String[] rows = {"A", "B", "C"};
        for (String row : rows) {
            for (int i = 1; i <= 10; i++) {
                String seatNumber = row + i;
                seats.add(new Seat(seatNumber, row, SeatStatus.AVAILABLE, showtime));
            }
        }
        seatRepository.saveAll(seats);
    }
}
