package com.temera.movie.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.context.annotation.Lazy;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "actor")
public class Actor {

    @Id
    @GeneratedValue
    @Column(name = "actor_id")
    private Long id;

    @Column(name = "actor_name")
    private String name;

    @JsonIgnoreProperties("actors")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_actor",
            joinColumns = @JoinColumn(name = "actor_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")

    )
    Set<Movie> movies = new HashSet<>();

    public Actor() {
    }

    public Actor(Long id, String name, Set<Movie> movies) {
        this.id = id;
        this.name = name;
        this.movies = movies;
    }

    public Actor(Long id, String name) {
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

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }
}
