package com.cinema.cinemamanagement.controller;

import com.cinema.cinemamanagement.dto.ShowtimeDto;
import com.cinema.cinemamanagement.entity.Movie;
import com.cinema.cinemamanagement.entity.Seat;
import com.cinema.cinemamanagement.entity.Showtime;
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
@RequestMapping("/showtimes")
public class ShowtimeController {

    private final ShowtimeService showtimeService;
    private final MovieService movieService;

    public ShowtimeController(ShowtimeService showtimeService, MovieService movieService) {
        this.showtimeService = showtimeService;
        this.movieService = movieService;
    }

    @GetMapping
    public String listShowtimes(Model model) {
        model.addAttribute("showtimes", showtimeService.getAllShowtimes());
        return "showtimes/list";
    }

    @GetMapping("/new")
    public String showCreateForm(@RequestParam(value = "movieId", required = false) Long movieId, Model model) {
        ShowtimeDto dto = new ShowtimeDto();
        if (movieId != null) {
            dto.setMovieId(movieId);
        }
        model.addAttribute("showtimeDto", dto);
        model.addAttribute("movies", movieService.getAllMovies());
        return "showtimes/form";
    }

    @PostMapping("/new")
    public String createShowtime(@Valid @ModelAttribute("showtimeDto") ShowtimeDto showtimeDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("movies", movieService.getAllMovies());
            return "showtimes/form";
        }
        showtimeService.createShowtime(showtimeDto);
        redirectAttributes.addFlashAttribute("successMessage", "Tạo suất chiếu và khởi tạo 30 ghế ngồi (A1-C10) thành công!");
        return "redirect:/showtimes";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Showtime showtime = showtimeService.getShowtimeById(id);
        ShowtimeDto dto = new ShowtimeDto(
                showtime.getId(),
                showtime.getMovie().getId(),
                showtime.getStartTime(),
                showtime.getBasePrice(),
                showtime.getRoomName()
        );
        model.addAttribute("showtimeDto", dto);
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("isEdit", true);
        return "showtimes/form";
    }

    @PostMapping("/edit/{id}")
    public String updateShowtime(@PathVariable Long id,
                                 @Valid @ModelAttribute("showtimeDto") ShowtimeDto showtimeDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("isEdit", true);
            return "showtimes/form";
        }
        showtimeService.updateShowtime(id, showtimeDto);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật suất chiếu thành công!");
        return "redirect:/showtimes";
    }

    @PostMapping("/delete/{id}")
    public String deleteShowtime(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            showtimeService.deleteShowtime(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa suất chiếu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa suất chiếu: " + e.getMessage());
        }
        return "redirect:/showtimes";
    }

    @GetMapping("/{id}/seats")
    public String viewSeats(@PathVariable Long id, Model model) {
        Showtime showtime = showtimeService.getShowtimeById(id);
        List<Seat> seats = showtimeService.getSeatsForShowtime(id);
        model.addAttribute("showtime", showtime);
        model.addAttribute("seats", seats);
        return "showtimes/seats";
    }
}
