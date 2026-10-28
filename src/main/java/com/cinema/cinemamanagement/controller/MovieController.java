package com.cinema.cinemamanagement.controller;

import com.cinema.cinemamanagement.dto.MovieDto;
import com.cinema.cinemamanagement.entity.Movie;
import com.cinema.cinemamanagement.service.MovieService;
import com.cinema.cinemamanagement.service.ShowtimeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final ShowtimeService showtimeService;

    public MovieController(MovieService movieService, ShowtimeService showtimeService) {
        this.movieService = movieService;
        this.showtimeService = showtimeService;
    }

    @GetMapping
    public String listMovies(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Movie> movies = (keyword != null && !keyword.isBlank())
                ? movieService.searchMovies(keyword)
                : movieService.getAllMovies();
        model.addAttribute("movies", movies);
        model.addAttribute("keyword", keyword);
        return "movies/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("movieDto")) {
            model.addAttribute("movieDto", new MovieDto());
        }
        return "movies/form";
    }

    @PostMapping("/new")
    public String createMovie(@Valid @ModelAttribute("movieDto") MovieDto movieDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "movies/form";
        }
        movieService.createMovie(movieDto);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm phim mới thành công!");
        return "redirect:/movies";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Movie movie = movieService.getMovieById(id);
        MovieDto dto = new MovieDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDuration(),
                movie.getGenre(),
                movie.getDescription(),
                movie.getPosterUrl()
        );
        model.addAttribute("movieDto", dto);
        model.addAttribute("isEdit", true);
        return "movies/form";
    }

    @PostMapping("/edit/{id}")
    public String updateMovie(@PathVariable Long id,
                              @Valid @ModelAttribute("movieDto") MovieDto movieDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "movies/form";
        }
        movieService.updateMovie(id, movieDto);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin phim thành công!");
        return "redirect:/movies";
    }

    @PostMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            movieService.deleteMovie(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa phim thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa phim: " + e.getMessage());
        }
        return "redirect:/movies";
    }

    @GetMapping("/{id}")
    public String viewDetail(@PathVariable Long id, Model model) {
        Movie movie = movieService.getMovieById(id);
        model.addAttribute("movie", movie);
        model.addAttribute("showtimes", showtimeService.getShowtimesByMovieId(id));
        return "movies/detail";
    }
}
