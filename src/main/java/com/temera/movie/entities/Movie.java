package com.temera.movie.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.Set;

@Entity
@NamedQuery(name = "Movie.findMostRatingFilmByYear",
        query = "select m from Movie m  WHERE m.year = ?1 order by m.rating DESC LIMIT 3")
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue
    @Column(name = "movie_id")
    private Long id;

    @Column(name = "movie_name")
    private String name;

    @Column(name = "movie_year")
    private Integer year;

    @Column(name = "movie_rating")
    @Range(min=0, max=10,message ="movie rating must be between 0 and 10")
    private Integer rating;

    @JsonIgnoreProperties("movies")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_actor",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    Set<Actor> actors = new HashSet<>();

    public Movie() {
    }

    public Movie(Long id, String name, Integer year, Integer rating, Set<Actor> actors) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.rating = rating;
        this.actors = actors;
    }

    public Movie(Long id, String name, Integer year, Integer rating) {
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

    public Set<Actor> getActors() {
        return actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }
}
