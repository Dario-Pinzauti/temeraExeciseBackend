package com.temera.movie.conrollers;

import com.temera.movie.entities.Movie;
import com.temera.movie.helper.ActorHelper;
import com.temera.movie.helper.MovieHelper;
import com.temera.movie.models.MovieDTO;
import com.temera.movie.repositories.MovieRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private MovieRepository movieRepository;

    @Autowired
    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/")
    public Set<MovieDTO> getMovies() {

        Iterable<Movie> all = movieRepository.findAll();
        Set<MovieDTO> mm = new HashSet<>();
        all.forEach(m -> mm.add(MovieHelper.toDTO(m)));

        return mm;
    }

    @GetMapping("/{id}")
    public MovieDTO getMovie(@PathVariable Long id) throws ResponseStatusException {

        Optional<Movie> byId = movieRepository.findById(id);

        return byId.map(MovieHelper::toDTO).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie with id: " + id + " not found"));
    }

    @PostMapping("/")
    public MovieDTO addMovie(@RequestBody MovieDTO movie) {
            Movie save = movieRepository.save(MovieHelper.toEntity(movie));
            return MovieHelper.toDTO(save);
    }

    @PutMapping("/{id}")
    public MovieDTO updateMovie(@PathVariable Long id, @RequestBody MovieDTO movie) {
        movieRepository.findById(id).ifPresentOrElse(
                m -> {
                    m.setName(movie.getName());
                    m.setYear(movie.getYear());
                    m.setRating(movie.getRating());
                    if (movie.getActors() != null)
                        m.setActors(MovieHelper.toActorEntity(movie.getActors()));
                    try {
                        movieRepository.save(m);
                    } catch (ConstraintViolationException e) {
                        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
                    }
                },
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie with id: " + id + " not found");
                }
        );
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        try {
            movieRepository.deleteById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie with id: " + id + " not found");
        }

    }

    @GetMapping("/mostRating/{year}")
    public List<MovieDTO> getMostRatingMovieByYear(@PathVariable int year) {
        List<Movie> movies = movieRepository.findMostRatingFilmByYear(year);

        Set<MovieDTO> dto = MovieHelper.toDTO(new HashSet<>(movies));

        ArrayList<MovieDTO> movieDTOS = new ArrayList<>(dto);
        Collections.sort(movieDTOS, Comparator.comparing(MovieDTO::getRating).reversed());
        return  movieDTOS;
    }


}
