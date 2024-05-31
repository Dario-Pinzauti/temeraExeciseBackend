package com.temera.movie.models;

import java.util.HashSet;
import java.util.Set;

public class MovieDTO {
    private Long id;
    private String name;
    private Integer year;
    private Integer rating;
    private Set<MovieActorDTO> actors = new HashSet<>();

    public MovieDTO() {
    }


    public MovieDTO(Long id, String name, Integer year, Integer rating) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.rating = rating;
    }

    public MovieDTO(Long id, String name, Integer year, Integer rating, Set<MovieActorDTO> actors) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.rating = rating;
        this.actors = actors;
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

    public Set<MovieActorDTO> getActors() {
        return actors;
    }

    public void setActors(Set<MovieActorDTO> actors) {
        this.actors = actors;
    }
}
