package com.temera.movie.models;

import java.util.Set;

public class ActorDTO {
    private Long id;
    private String name;
    private Set<ActorMovieDTO> movies ;

    public ActorDTO() {
    }

    public ActorDTO(Long id, String name, Set<ActorMovieDTO> movies) {
        this.id = id;
        this.name = name;
        this.movies = movies;
    }

    public ActorDTO(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public Set<ActorMovieDTO> getMovies() {
        return movies;
    }

    public void setMovies(Set<ActorMovieDTO> movies) {
        this.movies = movies;
    }
}
