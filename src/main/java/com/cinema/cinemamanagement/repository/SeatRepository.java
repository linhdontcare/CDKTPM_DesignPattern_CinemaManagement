package com.cinema.cinemamanagement.repository;

import com.cinema.cinemamanagement.entity.Seat;
import com.cinema.cinemamanagement.entity.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShowtimeIdOrderBySeatNumberAsc(Long showtimeId);
    List<Seat> findByShowtimeIdAndStatus(Long showtimeId, SeatStatus status);
}
