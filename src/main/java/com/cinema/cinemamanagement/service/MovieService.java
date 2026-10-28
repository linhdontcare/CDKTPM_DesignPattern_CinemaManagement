package com.cinema.cinemamanagement.service;

import com.cinema.cinemamanagement.dto.MovieDto;
import com.cinema.cinemamanagement.entity.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> getAllMovies();
    Movie getMovieById(Long id);
    Movie createMovie(MovieDto movieDto);
    Movie updateMovie(Long id, MovieDto movieDto);
    void deleteMovie(Long id);
    List<Movie> searchMovies(String keyword);
    long countMovies();
}
