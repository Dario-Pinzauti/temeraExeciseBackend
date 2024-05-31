package com.temera.movie.conrollers;

import com.temera.movie.entities.Actor;
import com.temera.movie.helper.ActorHelper;
import com.temera.movie.helper.MovieHelper;
import com.temera.movie.models.ActorDTO;
import com.temera.movie.repositories.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/actors")
public class ActorsController {

    private ActorRepository actorRepository;

    @Autowired
    public ActorsController(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @GetMapping("/")
    public Set<ActorDTO> getActors() {

        ArrayList<Actor> all = (ArrayList<Actor>) actorRepository.findAll();

        return ActorHelper.toDTO(new HashSet<>(all));
    }

    @GetMapping("/{id}")
    public ActorDTO getActor(@PathVariable Long id) throws ResponseStatusException{

        Optional<Actor> byId = actorRepository.findById(id);

        return byId.map(ActorHelper::toDTO).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor with id: "+id+" not found"));
    }

    @PostMapping("/")
    public ActorDTO addActor(@RequestBody ActorDTO actor) {
        Actor save = actorRepository.save(ActorHelper.toEntity(actor));
        return ActorHelper.toDTO(save);
    }

    @PutMapping("/{id}")
    public ActorDTO updateActor (@PathVariable Long id, @RequestBody ActorDTO actor) {
        actorRepository.findById(id).ifPresentOrElse(
                a -> {
                    a.setName(actor.getName());
                    if(actor.getMovies() != null)
                        a.setMovies(ActorHelper.toMovieEntity(actor.getMovies()));
                    actorRepository.save(a);
                },
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor with id: "+id+" not found");
                }
        );
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteActor(@PathVariable Long id) {
        try{
            actorRepository.deleteById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor with id: "+id+" not found");
        }
    }


}
