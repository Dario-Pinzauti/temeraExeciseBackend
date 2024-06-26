package com.temera.movie.repositories;

import com.temera.movie.entities.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long>{

    List<Movie> findMostRatingFilmByYear(Integer year);
}
