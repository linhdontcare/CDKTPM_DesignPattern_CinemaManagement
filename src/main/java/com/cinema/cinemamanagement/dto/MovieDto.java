package com.cinema.cinemamanagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MovieDto {

    private Long id;

    @NotBlank(message = "Tên phim không được để trống")
    private String title;

    @NotNull(message = "Thời lượng phim không được để trống")
    @Min(value = 1, message = "Thời lượng phim phải lớn hơn 0 phút")
    private Integer duration;

    @NotBlank(message = "Thể loại phim không được để trống")
    private String genre;

    private String description;

    private String posterUrl;

    public MovieDto() {
    }

    public MovieDto(Long id, String title, Integer duration, String genre, String description, String posterUrl) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.genre = genre;
        this.description = description;
        this.posterUrl = posterUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
