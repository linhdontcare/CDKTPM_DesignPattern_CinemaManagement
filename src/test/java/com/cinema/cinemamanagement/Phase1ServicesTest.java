package com.cinema.cinemamanagement;

import com.cinema.cinemamanagement.dto.MovieDto;
import com.cinema.cinemamanagement.dto.ShowtimeDto;
import com.cinema.cinemamanagement.entity.Movie;
import com.cinema.cinemamanagement.entity.Seat;
import com.cinema.cinemamanagement.entity.SeatStatus;
import com.cinema.cinemamanagement.entity.Showtime;
import com.cinema.cinemamanagement.service.MovieService;
import com.cinema.cinemamanagement.service.ShowtimeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class Phase1ServicesTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ShowtimeService showtimeService;

    @Test
    @DisplayName("Test Movie CRUD: Thêm, tìm kiếm, cập nhật và xóa phim")
    void testMovieCrud() {
        // Create
        MovieDto dto = new MovieDto(null, "Batman Begins", 140, "Action", "The dark knight story", null);
        Movie created = movieService.createMovie(dto);

        assertNotNull(created.getId());
        assertEquals("Batman Begins", created.getTitle());

        // Read
        Movie found = movieService.getMovieById(created.getId());
        assertEquals(140, found.getDuration());

        // Update
        dto.setTitle("The Dark Knight");
        dto.setDuration(152);
        Movie updated = movieService.updateMovie(created.getId(), dto);
        assertEquals("The Dark Knight", updated.getTitle());
        assertEquals(152, updated.getDuration());

        // Search
        List<Movie> searchResult = movieService.searchMovies("Knight");
        assertFalse(searchResult.isEmpty());

        // Delete
        movieService.deleteMovie(created.getId());
        assertThrows(IllegalArgumentException.class, () -> movieService.getMovieById(created.getId()));
    }

    @Test
    @DisplayName("Test Showtime & Auto-generation of 30 seats (A1-C10)")
    void testShowtimeAndSeatGeneration() {
        // Get seeded movie
        List<Movie> movies = movieService.getAllMovies();
        assertFalse(movies.isEmpty(), "Sample movies should have been initialized");
        Movie movie = movies.get(0);

        // Create new showtime
        LocalDateTime time = LocalDateTime.now().plusDays(2);
        ShowtimeDto dto = new ShowtimeDto(null, movie.getId(), time, 95000.0, "Hall VIP");
        Showtime created = showtimeService.createShowtime(dto);

        assertNotNull(created.getId());
        assertEquals("Hall VIP", created.getRoomName());

        // Verify seats: exactly 30 seats generated
        List<Seat> seats = showtimeService.getSeatsForShowtime(created.getId());
        assertEquals(30, seats.size(), "Each showtime must automatically initialize 30 seats");

        // Verify rows A, B, C and status AVAILABLE
        long availableSeats = seats.stream().filter(s -> s.getStatus() == SeatStatus.AVAILABLE).count();
        assertEquals(30, availableSeats, "All newly generated seats must be in AVAILABLE status");

        assertTrue(seats.stream().anyMatch(s -> s.getSeatNumber().equals("A1")));
        assertTrue(seats.stream().anyMatch(s -> s.getSeatNumber().equals("B5")));
        assertTrue(seats.stream().anyMatch(s -> s.getSeatNumber().equals("C10")));
    }
}
