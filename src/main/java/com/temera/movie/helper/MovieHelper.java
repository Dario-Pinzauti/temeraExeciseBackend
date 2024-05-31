package com.temera.movie.helper;

import com.temera.movie.entities.Actor;
import com.temera.movie.entities.Movie;
import com.temera.movie.models.ActorMovieDTO;
import com.temera.movie.models.MovieActorDTO;
import com.temera.movie.models.MovieDTO;

import java.util.Set;
import java.util.stream.Collectors;

public class MovieHelper {

    public static MovieDTO toDTO(Movie movie) {
        if (movie == null) {
            return null;
        }
        Set<MovieActorDTO> collect = null;
        if (movie.getActors() != null ) {
            collect = movie.getActors().stream().map(MovieHelper::toActorDto).collect(Collectors.toSet());
        }

        return new MovieDTO(movie.getId(), movie.getName(), movie.getYear(), movie.getRating(), collect);
    }

    public static Set<MovieDTO> toDTO(Set<Movie> movies) {

        return movies.stream().map(MovieHelper::toDTO).collect(Collectors.toSet());
    }

    public static Movie toEntity(MovieDTO movieDTO) {
        if (movieDTO == null) {
            return null;
        }
        Set<Actor> collect = null;
        if (movieDTO.getActors() != null){
            collect = movieDTO.getActors().stream().map(a-> new Actor(a.getId(), a.getName())).collect(Collectors.toSet());
        }

        return new Movie(movieDTO.getId(), movieDTO.getName(), movieDTO.getYear(), movieDTO.getRating(), collect);
    }

    public static Set<Movie> toEntity(Set<MovieDTO> movieDTOS) {

        return movieDTOS.stream().map(MovieHelper::toEntity).collect(Collectors.toSet());
    }


    public static Actor toActorEntity(MovieActorDTO actorMovieDTO) {

        return new Actor(actorMovieDTO.getId(), actorMovieDTO.getName());
    }
    public static Set<Actor> toActorEntity(Set<MovieActorDTO> actorMovieDTOs) {

        return actorMovieDTOs.stream().map(MovieHelper::toActorEntity).collect(Collectors.toSet());
    }

    public static MovieActorDTO toActorDto(Actor actor){
        return new MovieActorDTO(actor.getId(), actor.getName());
    }

    public static Set<MovieActorDTO> toActorDto(Set<Actor> actor){
        return actor.stream().map(MovieHelper::toActorDto).collect(Collectors.toSet());
    }


}
