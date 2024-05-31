package com.temera.movie.models;

import java.util.HashSet;
import java.util.Set;

public class ActorMovieDTO {
    private Long id;
    private String name;
    private Integer year;
    private Integer rating;

    public ActorMovieDTO() {
    }


    public ActorMovieDTO(Long id, String name, Integer year, Integer rating) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

}
