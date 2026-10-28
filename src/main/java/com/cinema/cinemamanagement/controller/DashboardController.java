package com.cinema.cinemamanagement.controller;

import com.cinema.cinemamanagement.service.MovieService;
import com.cinema.cinemamanagement.service.ShowtimeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final MovieService movieService;
    private final ShowtimeService showtimeService;

    public DashboardController(MovieService movieService, ShowtimeService showtimeService) {
        this.movieService = movieService;
        this.showtimeService = showtimeService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("movieCount", movieService.countMovies());
        model.addAttribute("showtimeCount", showtimeService.countShowtimes());
        model.addAttribute("recentShowtimes", showtimeService.getAllShowtimes());
        model.addAttribute("movies", movieService.getAllMovies());
        return "dashboard";
    }
}
