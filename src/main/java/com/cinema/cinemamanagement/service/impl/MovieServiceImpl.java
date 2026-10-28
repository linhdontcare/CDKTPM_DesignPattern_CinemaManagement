package com.cinema.cinemamanagement.service.impl;

import com.cinema.cinemamanagement.dto.MovieDto;
import com.cinema.cinemamanagement.entity.Movie;
import com.cinema.cinemamanagement.repository.MovieRepository;
import com.cinema.cinemamanagement.service.MovieService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phim với ID: " + id));
    }

    @Override
    public Movie createMovie(MovieDto movieDto) {
        Movie movie = new Movie();
        movie.setTitle(movieDto.getTitle().trim());
        movie.setDuration(movieDto.getDuration());
        movie.setGenre(movieDto.getGenre().trim());
        movie.setDescription(movieDto.getDescription());
        movie.setPosterUrl(movieDto.getPosterUrl());
        return movieRepository.save(movie);
    }

    @Override
    public Movie updateMovie(Long id, MovieDto movieDto) {
        Movie movie = getMovieById(id);
        movie.setTitle(movieDto.getTitle().trim());
        movie.setDuration(movieDto.getDuration());
        movie.setGenre(movieDto.getGenre().trim());
        movie.setDescription(movieDto.getDescription());
        if (movieDto.getPosterUrl() != null && !movieDto.getPosterUrl().isBlank()) {
            movie.setPosterUrl(movieDto.getPosterUrl());
        }
        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy phim với ID: " + id);
        }
        movieRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> searchMovies(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllMovies();
        }
        return movieRepository.findByTitleContainingIgnoreCase(keyword.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public long countMovies() {
        return movieRepository.count();
    }
}
