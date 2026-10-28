package com.cinema.cinemamanagement.config;

import com.cinema.cinemamanagement.dto.ShowtimeDto;
import com.cinema.cinemamanagement.entity.Movie;
import com.cinema.cinemamanagement.repository.MovieRepository;
import com.cinema.cinemamanagement.service.MovieService;
import com.cinema.cinemamanagement.service.ShowtimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final MovieRepository movieRepository;
    private final ShowtimeService showtimeService;

    public DataInitializer(MovieRepository movieRepository, ShowtimeService showtimeService) {
        this.movieRepository = movieRepository;
        this.showtimeService = showtimeService;
    }

    @Override
    public void run(String... args) {
        if (movieRepository.count() > 0) {
            logger.info("Database already initialized with movies. Skipping sample data creation.");
            return;
        }

        logger.info("Initializing sample data for Cinema Management System...");

        // 1. Create Movies
        Movie avengers = new Movie(
                "Avengers: Endgame",
                181,
                "Hành Động / Khoa Học Viễn Tưởng",
                "Trận chiến hoành tráng của các siêu anh hùng vũ trụ nhằm đảo ngược cú búng tay của Thanos và giải cứu một nửa sinh linh vũ trụ.",
                "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=500&auto=format&fit=crop&q=60"
        );

        Movie interstellar = new Movie(
                "Interstellar",
                169,
                "Khoa Học Viễn Tưởng / Phiêu Lưu",
                "Hành trình xuyên qua lỗ sâu ngoài không gian xa xôi để tìm kiếm một hành tinh mới có thể duy trì sự sống cho toàn nhân loại.",
                "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=500&auto=format&fit=crop&q=60"
        );

        Movie spiderMan = new Movie(
                "Spider-Man: No Way Home",
                148,
                "Hành Động / Siêu Anh Hùng",
                "Danh tính bị lộ diện, Peter Parker nhờ Doctor Strange làm phép nhưng vô tình làm rách ranh giới Đa Vũ Trụ.",
                "https://images.unsplash.com/photo-1635805737707-575885ab0820?w=500&auto=format&fit=crop&q=60"
        );

        avengers = movieRepository.save(avengers);
        interstellar = movieRepository.save(interstellar);
        spiderMan = movieRepository.save(spiderMan);

        // 2. Create Showtimes (ShowtimeService will automatically generate A1-C10 seats)
        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);

        // Avengers showtimes
        ShowtimeDto st1 = new ShowtimeDto(null, avengers.getId(), now.plusHours(2), 85000.0, "Phòng Chiếu 01 (IMAX)");
        ShowtimeDto st2 = new ShowtimeDto(null, avengers.getId(), now.plusDays(1).withHour(19).withMinute(30), 90000.0, "Phòng Chiếu 01 (IMAX)");
        showtimeService.createShowtime(st1);
        showtimeService.createShowtime(st2);

        // Interstellar showtimes
        ShowtimeDto st3 = new ShowtimeDto(null, interstellar.getId(), now.plusHours(4), 80000.0, "Phòng Chiếu 02 (Standard)");
        ShowtimeDto st4 = new ShowtimeDto(null, interstellar.getId(), now.plusDays(1).withHour(20).withMinute(0), 85000.0, "Phòng Chiếu 02 (Standard)");
        showtimeService.createShowtime(st3);
        showtimeService.createShowtime(st4);

        // Spider-Man showtimes
        ShowtimeDto st5 = new ShowtimeDto(null, spiderMan.getId(), now.plusHours(6), 75000.0, "Phòng Chiếu 03 (Standard)");
        showtimeService.createShowtime(st5);

        logger.info("Sample data initialized successfully! Movies: {}, Showtimes: 5 with seats A1-A10, B1-B10, C1-C10 generated.", movieRepository.count());
    }
}
