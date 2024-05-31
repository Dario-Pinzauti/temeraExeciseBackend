package com.temera.movie;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.temera.movie.entities.Actor;
import com.temera.movie.entities.Movie;
import com.temera.movie.helper.MovieHelper;
import com.temera.movie.models.ActorDTO;
import com.temera.movie.models.MovieActorDTO;
import com.temera.movie.models.MovieDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest

class ActorHelperTests {



	@Test
	void testToDTONullMovie() {
		Movie m = null;
		MovieDTO result = MovieHelper.toDTO(m);
		assertNull(result, "The result should be null when the input movie is null");
	}

	@Test
	void testToDTOMovieWithNoActors() {
		Movie movie = new Movie();
		movie.setId(1L);
		movie.setName("Test Movie");
		movie.setYear(2020);
		movie.setRating(4);
		movie.setActors(Collections.emptySet());

		MovieDTO result = MovieHelper.toDTO(movie);

		assertNotNull(result, "The result should not be null");
		assertEquals(movie.getId(), result.getId(), "The IDs should match");
		assertEquals(movie.getName(), result.getName(), "The names should match");
		assertEquals(movie.getYear(), result.getYear(), "The years should match");
		assertEquals(movie.getRating(), result.getRating(), "The ratings should match");
		assertTrue(result.getActors().isEmpty(), "The actors set should be empty");
	}

	@Test
	void testToDTOMovieWithActors() {
		Actor actor1 = new Actor();
		actor1.setName("Actor Test 1");
		actor1.setId(1L);
		Actor actor2 = new Actor();
		actor2.setName("Actor Test 2");
		actor2.setId(2L);


		Set<Actor> actors = new HashSet<>();
		actors.add(actor1);
		actors.add(actor2);

		Movie movie = new Movie();
		movie.setId(1L);
		movie.setName("Test Movie");
		movie.setYear(2020);
		movie.setRating(5);
		movie.setActors(actors);

		MovieDTO result = MovieHelper.toDTO(movie);

		assertNotNull(result, "The result should not be null");
		assertEquals(movie.getId(), result.getId(), "The IDs should match");
		assertEquals(movie.getName(), result.getName(), "The names should match");
		assertEquals(movie.getYear(), result.getYear(), "The years should match");
		assertEquals(movie.getRating(), result.getRating(), "The ratings should match");
		assertNotNull(result.getActors(), "The actors set should not be null");
		assertEquals(2, result.getActors().size(), "The actors set should contain 2 actors");
	}

	@Test
	void testToDTOSetOfMovies() {
		Movie movie1 = new Movie();
		movie1.setId(1L);
		movie1.setName("Movie 1");
		movie1.setYear(2020);
		movie1.setRating(4);
		movie1.setActors(Collections.emptySet());

		Movie movie2 = new Movie();
		movie2.setId(2L);
		movie2.setName("Movie 2");
		movie2.setYear(2021);
		movie2.setRating(4);
		movie2.setActors(Collections.emptySet());

		Set<Movie> movies = new HashSet<>();
		movies.add(movie1);
		movies.add(movie2);

		Set<MovieDTO> result = MovieHelper.toDTO(movies);

		assertNotNull(result, "The result should not be null");
		assertEquals(2, result.size(), "The result set should contain 2 movies");
		assertTrue(result.stream().anyMatch(dto -> dto.getId().equals(1L)), "The result should contain Movie 1");
		assertTrue(result.stream().anyMatch(dto -> dto.getId().equals(2L)), "The result should contain Movie 2");
	}

	@Test
	void testToEntityNullMovieDTO() {
		MovieDTO movieDTO = null;
		Movie result = MovieHelper.toEntity(movieDTO);
		assertNull(result, "The result should be null when the input movieDTO is null");
	}

	@Test
	void testToEntityMovieDTOWithNoActors() {
		MovieDTO movieDTO = new MovieDTO(1L, "Test Movie", 2020, 4, Collections.emptySet());

		Movie result = MovieHelper.toEntity(movieDTO);

		assertNotNull(result, "The result should not be null");
		assertEquals(movieDTO.getId(), result.getId(), "The IDs should match");
		assertEquals(movieDTO.getName(), result.getName(), "The names should match");
		assertEquals(movieDTO.getYear(), result.getYear(), "The years should match");
		assertEquals(movieDTO.getRating(), result.getRating(), "The ratings should match");
		assertTrue(result.getActors().isEmpty(), "The actors set should be empty");
	}

	@Test
	void testToEntityMovieDTOWithActors() {
		MovieActorDTO actorDTO1 = new MovieActorDTO(1L, "Actor 1");
		MovieActorDTO actorDTO2 = new MovieActorDTO(2L, "Actor 2");

		Set<MovieActorDTO> actorDTOs = new HashSet<>();
		actorDTOs.add(actorDTO1);
		actorDTOs.add(actorDTO2);

		MovieDTO movieDTO = new MovieDTO(1L, "Test Movie", 2020, 5, actorDTOs);

		Movie result = MovieHelper.toEntity(movieDTO);

		assertNotNull(result, "The result should not be null");
		assertEquals(movieDTO.getId(), result.getId(), "The IDs should match");
		assertEquals(movieDTO.getName(), result.getName(), "The names should match");
		assertEquals(movieDTO.getYear(), result.getYear(), "The years should match");
		assertEquals(movieDTO.getRating(), result.getRating(), "The ratings should match");
		assertNotNull(result.getActors(), "The actors set should not be null");
		assertEquals(2, result.getActors().size(), "The actors set should contain 2 actors");
	}

	@Test
	void testToEntitySetOfMovieDTOs() {
		MovieDTO movieDTO1 = new MovieDTO(1L, "Movie 1", 2020, 4, Collections.emptySet());
		MovieDTO movieDTO2 = new MovieDTO(2L, "Movie 2", 2021, 4, Collections.emptySet());

		Set<MovieDTO> movieDTOs = new HashSet<>();
		movieDTOs.add(movieDTO1);
		movieDTOs.add(movieDTO2);

		Set<Movie> result = MovieHelper.toEntity(movieDTOs);

		assertNotNull(result, "The result should not be null");
		assertEquals(2, result.size(), "The result set should contain 2 movies");
		assertTrue(result.stream().anyMatch(movie -> movie.getId().equals(1L)), "The result should contain Movie 1");
		assertTrue(result.stream().anyMatch(movie -> movie.getId().equals(2L)), "The result should contain Movie 2");
	}

	@Test
	void testToActorDto() {
		Actor actor = new Actor(1L, "Actor 1");

		MovieActorDTO result = MovieHelper.toActorDto(actor);

		assertNotNull(result, "The result should not be null");
		assertEquals(actor.getId(), result.getId(), "The IDs should match");
		assertEquals(actor.getName(), result.getName(), "The names should match");
	}

	@Test
	void testToActorDtoSetOfActors() {
		Actor actor1 = new Actor(1L, "Actor 1");
		Actor actor2 = new Actor(2L, "Actor 2");

		Set<Actor> actors = new HashSet<>();
		actors.add(actor1);
		actors.add(actor2);

		Set<MovieActorDTO> result = MovieHelper.toActorDto(actors);

		assertNotNull(result, "The result should not be null");
		assertEquals(2, result.size(), "The result set should contain 2 actors");
		assertTrue(result.stream().anyMatch(dto -> dto.getId().equals(1L)), "The result should contain Actor 1");
		assertTrue(result.stream().anyMatch(dto -> dto.getId().equals(2L)), "The result should contain Actor 2");
	}

	@Test
	void testToActorEntity() {
		MovieActorDTO actorDTO = new MovieActorDTO(1L, "Actor 1");

		Actor result = MovieHelper.toActorEntity(actorDTO);

		assertNotNull(result, "The result should not be null");
		assertEquals(actorDTO.getId(), result.getId(), "The IDs should match");
		assertEquals(actorDTO.getName(), result.getName(), "The names should match");
	}

	@Test
	void testToActorEntitySetOfActorDTOs() {
		MovieActorDTO actorDTO1 = new MovieActorDTO(1L, "Actor 1");
		MovieActorDTO actorDTO2 = new MovieActorDTO(2L, "Actor 2");

		Set<MovieActorDTO> actorDTOs = new HashSet<>();
		actorDTOs.add(actorDTO1);
		actorDTOs.add(actorDTO2);

		Set<Actor> result = MovieHelper.toActorEntity(actorDTOs);

		assertNotNull(result, "The result should not be null");
		assertEquals(2, result.size(), "The result set should contain 2 actors");
		assertTrue(result.stream().anyMatch(actor -> actor.getId().equals(1L)), "The result should contain Actor 1");
		assertTrue(result.stream().anyMatch(actor -> actor.getId().equals(2L)), "The result should contain Actor 2");
	}

}
