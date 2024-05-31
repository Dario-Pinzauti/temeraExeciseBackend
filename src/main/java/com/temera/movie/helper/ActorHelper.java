package com.temera.movie.helper;

import com.temera.movie.entities.Actor;
import com.temera.movie.entities.Movie;
import com.temera.movie.models.ActorDTO;
import com.temera.movie.models.ActorMovieDTO;
import com.temera.movie.models.MovieActorDTO;
import com.temera.movie.models.MovieDTO;

import java.util.Set;
import java.util.stream.Collectors;

public class ActorHelper {

    public static ActorDTO toDTO(Actor actor) {
        if (actor == null) {
            return null;
        }
        Set<ActorMovieDTO> collect = null;
        if (actor.getMovies() != null && !actor.getMovies().isEmpty()) {
            collect = actor.getMovies().stream().map(ActorHelper::toMovieDto).collect(Collectors.toSet());
        }

        return new ActorDTO(actor.getId(), actor.getName(), collect);
    }

    public static Set<ActorDTO> toDTO(Set<Actor> actors) {

        return actors.stream().map(ActorHelper::toDTO).collect(Collectors.toSet());
    }

    public static Actor toEntity(ActorDTO actorDTO) {
        if (actorDTO == null) {
            return null;
        }
        Set<Movie> collect = null;
        if (actorDTO.getMovies() != null && !actorDTO.getMovies().isEmpty()){
            collect = actorDTO.getMovies().stream().map(a-> new Movie(a.getId(),a.getName(),a.getYear(),a.getRating(),null)).collect(Collectors.toSet());
        }

        return new Actor(actorDTO.getId(), actorDTO.getName(), collect);
    }

    public static Set<Actor> toEntity(Set<ActorDTO> actorDTOS) {

        return actorDTOS.stream().map(ActorHelper::toEntity).collect(Collectors.toSet());
    }

    public static Movie toMovieEntity(ActorMovieDTO actorMovieDTO) {
        if (actorMovieDTO == null) {
            return null;
        }


        return new Movie(actorMovieDTO.getId(), actorMovieDTO.getName(),actorMovieDTO.getYear(), actorMovieDTO.getRating());
    }

    public static Set<Movie> toMovieEntity(Set<ActorMovieDTO> actorMovieDTOS){
        return actorMovieDTOS.stream().map(ActorHelper::toMovieEntity).collect(Collectors.toSet());
    }

    public static ActorMovieDTO toMovieDto(Movie movie){
        return new ActorMovieDTO(movie.getId(), movie.getName(), movie.getYear(), movie.getRating());
    }

    public static Set<ActorMovieDTO> toMovieDto(Set<Movie> movies){
        return movies.stream().map(ActorHelper::toMovieDto).collect(Collectors.toSet());
    }


}
