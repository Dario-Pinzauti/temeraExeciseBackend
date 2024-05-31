package com.temera.movie.models;

import java.util.HashSet;
import java.util.Set;

public class MovieActorDTO {
    private Long id;
    private String name;

    public MovieActorDTO() {
    }

    public MovieActorDTO(Long id, String name) {
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
}
