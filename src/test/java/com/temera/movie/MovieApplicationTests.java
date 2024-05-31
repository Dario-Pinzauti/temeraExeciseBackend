package com.temera.movie;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.temera.movie.models.ActorDTO;
import com.temera.movie.models.ActorMovieDTO;
import com.temera.movie.models.MovieActorDTO;
import com.temera.movie.models.MovieDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class MovieApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testGetUserByIdNotFound() throws Exception {

		long movieId = 1010L;


		ResultActions result = mockMvc.perform(get("/movies/{id}", movieId).with(httpBasic("user","password")));


		result.andExpect(status().isNotFound());

	}

	@Test
	public void testCreateActorAndMovieAndAssignActorToMovie() throws Exception {

		ActorDTO randomActor = getRandomActor();
		ActorDTO randomActor2 = getRandomActor();


		//Insert first actor

		ResultActions resultFirstActor = mockMvc.perform(post("/actors/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(randomActor)));
		resultFirstActor.andExpect(status().isOk());

		//Insert second actor

		ResultActions resultSecondActor = mockMvc.perform(post("/actors/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(randomActor2)));
		resultSecondActor.andExpect(status().isOk());


		//Read all Actors
		ResultActions re = mockMvc.perform(get("/actors/").with(httpBasic("user","password")));

		List<ActorDTO> actorResponse = objectMapper.readValue(re.andReturn().getResponse().getContentAsString(),new TypeReference<List<ActorDTO>>(){});

		Assertions.assertEquals(actorResponse.size(),2);




		MovieDTO randomMovie = getRandomMovie();
		HashSet<MovieActorDTO> actors = new HashSet<>();
		MovieActorDTO e = new MovieActorDTO();
		e.setId(actorResponse.get(0).getId());
		actors.add(e);
		randomMovie.setActors(actors);
		String movie = objectMapper.writeValueAsString(randomMovie);


		//crate movie
		 mockMvc.perform(post("/movies/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(movie)).andExpect(status().isOk());

		 //read all Movie
		ResultActions moviesResponse = mockMvc.perform(get("/movies/").with(httpBasic("user","password")));

		ResultActions resultActionsMovies = moviesResponse.andExpect(status().isOk());


		List<MovieDTO> movieDTOList = objectMapper.readValue(resultActionsMovies.andReturn().getResponse().getContentAsString(),new TypeReference<List<MovieDTO>>(){});

		Assertions.assertEquals(movieDTOList.size(),1);
		Assertions.assertEquals(movieDTOList.get(0).getName(),randomMovie.getName());
		Assertions.assertEquals(movieDTOList.get(0).getYear(),randomMovie.getYear());
		Assertions.assertEquals(movieDTOList.get(0).getRating(),randomMovie.getRating());
		Set<MovieActorDTO> movieActors =  movieDTOList.get(0).getActors();
		Assertions.assertEquals(movieActors.size(),1);
		Optional<MovieActorDTO> first = movieActors.stream().findFirst();
		Assertions.assertEquals(first.get().getName(),actorResponse.get(0).getName());



	}



	@Test
	public void testCreateActorAndModify() throws Exception {

		ActorDTO randomActor = getRandomActor();
		ActorDTO randomActor2 = getRandomActor();


		//Insert first actor

		ResultActions resultFirstActor = mockMvc.perform(post("/actors/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(randomActor)));
		resultFirstActor.andExpect(status().isOk());

		//Insert second actor

		ResultActions resultSecondActor = mockMvc.perform(post("/actors/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(randomActor2)));
		resultSecondActor.andExpect(status().isOk());


		//Read all Actors
		ResultActions re = mockMvc.perform(get("/actors/").with(httpBasic("user","password")));

		List<ActorDTO> actorResponse = objectMapper.readValue(re.andReturn().getResponse().getContentAsString(),new TypeReference<List<ActorDTO>>(){});

		Assertions.assertEquals(actorResponse.size(),2);


		ActorDTO actorDTO = actorResponse.get(0);
		actorDTO.setName("Mara Maionchi");

		ResultActions modifyActor = mockMvc.perform(put("/actors/{id}",actorDTO.getId()).with(httpBasic("user","password")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actorDTO)));

		modifyActor.andExpect(status().isOk());

		ResultActions getActor = mockMvc.perform(get("/actors/{id}",actorDTO.getId()).with(httpBasic("user","password")));
		ActorDTO actorDTO1 = objectMapper.readValue(getActor.andReturn().getResponse().getContentAsString(), new TypeReference<ActorDTO>() {});

		Assertions.assertEquals(actorDTO1.getName(),"Mara Maionchi","The actorName shuld be Mara Maionchi");

	}


	@Test
	public void testCreateMovieAndModify() throws Exception {
		MovieDTO randomMovie = getRandomMovie();
		String movie = objectMapper.writeValueAsString(randomMovie);


		//crate movie
		mockMvc.perform(post("/movies/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(movie)).andExpect(status().isOk());

		//read all Movie
		ResultActions moviesResponse = mockMvc.perform(get("/movies/").with(httpBasic("user","password")));

		ResultActions resultActionsMovies = moviesResponse.andExpect(status().isOk());


		List<MovieDTO> movieDTOList = objectMapper.readValue(resultActionsMovies.andReturn().getResponse().getContentAsString(),new TypeReference<List<MovieDTO>>(){});

		Assertions.assertEquals(movieDTOList.size(),1);
		MovieDTO movieDTO = movieDTOList.get(0);
		Assertions.assertEquals(movieDTO.getName(),randomMovie.getName());
		Assertions.assertEquals(movieDTO.getYear(),randomMovie.getYear());
		Assertions.assertEquals(movieDTO.getRating(),randomMovie.getRating());

		movieDTO.setName("Test Film Modify");

		 mockMvc.perform(put("/movies/{id}", movieDTO.getId()).with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(movieDTO))).andExpect(status().isOk());

		ResultActions perform = mockMvc.perform(get("/movies/{id}", movieDTO.getId()).with(httpBasic("user","password")));



		MovieDTO movieDTOAfterModify = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), MovieDTO.class);

		Assertions.assertEquals(movieDTOAfterModify.getName(),"Test Film Modify","Film name shuld be 'Test Film Modify'");


	}

	@Test
	public void testCreateMovieAndDelete() throws Exception {
		MovieDTO randomMovie = getRandomMovie();
		String movie = objectMapper.writeValueAsString(randomMovie);


		//crate movie
		mockMvc.perform(post("/movies/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(movie)).andExpect(status().isOk());

		//read all Movie
		ResultActions moviesResponse = mockMvc.perform(get("/movies/").with(httpBasic("user","password")));

		ResultActions resultActionsMovies = moviesResponse.andExpect(status().isOk());


		List<MovieDTO> movieDTOList = objectMapper.readValue(resultActionsMovies.andReturn().getResponse().getContentAsString(),new TypeReference<List<MovieDTO>>(){});

		Assertions.assertEquals(movieDTOList.size(),1);
		MovieDTO movieDTO = movieDTOList.get(0);
		Assertions.assertEquals(movieDTO.getName(),randomMovie.getName());
		Assertions.assertEquals(movieDTO.getYear(),randomMovie.getYear());
		Assertions.assertEquals(movieDTO.getRating(),randomMovie.getRating());

		movieDTO.setName("Test Film Modify");

		mockMvc.perform(delete("/movies/{id}", movieDTO.getId()).with(httpBasic("user","password")));


		ResultActions result = mockMvc.perform(get("/movies/{id}", movieDTO.getId()).with(httpBasic("user","password")));

		result.andExpect(status().isNotFound());

	}

	@Test
	public void testCreateMoviesAndGet() throws Exception {
		MovieDTO randomMovie = getRandomMovie();
		MovieDTO randomMovie2 = getRandomMovie();
		String movie = objectMapper.writeValueAsString(randomMovie);
		String movie2 = objectMapper.writeValueAsString(randomMovie2);


		//crate movie
		mockMvc.perform(post("/movies/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(movie)).andExpect(status().isOk());

		mockMvc.perform(post("/movies/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(movie2)).andExpect(status().isOk());


		//read all Movie
		ResultActions moviesResponse = mockMvc.perform(get("/movies/").with(httpBasic("user","password")));

		ResultActions resultActionsMovies = moviesResponse.andExpect(status().isOk());


		List<MovieDTO> movieDTOList = objectMapper.readValue(resultActionsMovies.andReturn().getResponse().getContentAsString(),new TypeReference<List<MovieDTO>>(){});

		Assertions.assertEquals(movieDTOList.size(),2);

	}



	@Test
	public void testCreateActorsAndGet() throws Exception {

		ActorDTO randomActor = getRandomActor();
		ActorDTO randomActor2 = getRandomActor();


		//Insert first actor

		ResultActions resultFirstActor = mockMvc.perform(post("/actors/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(randomActor)));
		resultFirstActor.andExpect(status().isOk());

		//Insert second actor

		ResultActions resultSecondActor = mockMvc.perform(post("/actors/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(randomActor2)));
		resultSecondActor.andExpect(status().isOk());



		//Read all Actors
		ResultActions re = mockMvc.perform(get("/actors/").with(httpBasic("user","password")));

		List<ActorDTO> actorResponse = objectMapper.readValue(re.andReturn().getResponse().getContentAsString(),new TypeReference<List<ActorDTO>>(){});

		Assertions.assertEquals(actorResponse.size(),2);

	}


	@Test
	public void testCreateActorAndDelete() throws Exception {

		ActorDTO randomActor = getRandomActor();


		//Insert first actor

		ResultActions resultFirstActor = mockMvc.perform(post("/actors/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(randomActor)));
		resultFirstActor.andExpect(status().isOk());

		ResultActions re = mockMvc.perform(get("/actors/").with(httpBasic("user","password")));

		List<ActorDTO> actorResponse = objectMapper.readValue(re.andReturn().getResponse().getContentAsString(),new TypeReference<List<ActorDTO>>(){});


		mockMvc.perform(delete("/actors/{id}",actorResponse.get(0).getId()).with(httpBasic("user","password")));


		//Read all Actors
		ResultActions actorsResponse = mockMvc.perform(get("/actors/").with(httpBasic("user","password")));

		List<ActorDTO> actorDTOList = objectMapper.readValue(actorsResponse.andReturn().getResponse().getContentAsString(),new TypeReference<List<ActorDTO>>(){});

		Assertions.assertEquals(actorDTOList.size(),0);

	}

	@Test
	public void testCreateActorAndMovieAndAssignMovieToActor() throws Exception {

		ActorDTO randomActor = getRandomActor();


		//Insert first actor

		ResultActions resultFirstActor = mockMvc.perform(post("/actors/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(randomActor)));
		resultFirstActor.andExpect(status().isOk());

		ResultActions re = mockMvc.perform(get("/actors/").with(httpBasic("user","password")));

		List<ActorDTO> actorResponse = objectMapper.readValue(re.andReturn().getResponse().getContentAsString(),new TypeReference<List<ActorDTO>>(){});


		ActorDTO actorDTO = actorResponse.get(0);


		MovieDTO randomMovie = getRandomMovie();
		String movie = objectMapper.writeValueAsString(randomMovie);


		//crate movie
		mockMvc.perform(post("/movies/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(movie)).andExpect(status().isOk());


		//read all Movie
		ResultActions moviesResponse = mockMvc.perform(get("/movies/").with(httpBasic("user","password")));

		ResultActions resultActionsMovies = moviesResponse.andExpect(status().isOk());


		List<MovieDTO> movieDTOList = objectMapper.readValue(resultActionsMovies.andReturn().getResponse().getContentAsString(),new TypeReference<List<MovieDTO>>(){});


		HashSet<ActorMovieDTO> actorMovieDTOS = new HashSet<>();
		ActorMovieDTO e = new ActorMovieDTO();
		e.setId(movieDTOList.get(0).getId());
		actorMovieDTOS.add(e);
		actorDTO.setMovies(actorMovieDTOS);

		ResultActions resultSecondActor = mockMvc.perform(post("/actors/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actorDTO)));
		resultSecondActor.andExpect(status().isOk());

		ResultActions actorsResponse = mockMvc.perform(get("/actors/").with(httpBasic("user","password")));

		List<ActorDTO> actorDTOList = objectMapper.readValue(actorsResponse.andReturn().getResponse().getContentAsString(),new TypeReference<List<ActorDTO>>(){});

		Assertions.assertEquals(actorDTOList.get(0).getMovies().size(),1);

	}


	@Test
	public void testCreateActorAssignMovieAndReassignMovie() throws Exception {

		ActorDTO randomActor = getRandomActor();


		//Insert first actor

		ResultActions resultFirstActor = mockMvc.perform(post("/actors/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(randomActor)));
		resultFirstActor.andExpect(status().isOk());



		ResultActions re = mockMvc.perform(get("/actors/").with(httpBasic("user","password")));

		List<ActorDTO> actorResponse = objectMapper.readValue(re.andReturn().getResponse().getContentAsString(),new TypeReference<List<ActorDTO>>(){});


		ActorDTO actorDTO = actorResponse.get(0);


		MovieDTO randomMovie = getRandomMovie();
		String movie = objectMapper.writeValueAsString(randomMovie);
		MovieDTO randomMovie2 = getRandomMovie();
		String movie2 = objectMapper.writeValueAsString(randomMovie);


		//crate movie
		mockMvc.perform(post("/movies/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(movie)).andExpect(status().isOk());

		mockMvc.perform(post("/movies/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(movie2)).andExpect(status().isOk());


		//read all Movie
		ResultActions moviesResponse = mockMvc.perform(get("/movies/").with(httpBasic("user","password")));

		ResultActions resultActionsMovies = moviesResponse.andExpect(status().isOk());


		List<MovieDTO> movieDTOList = objectMapper.readValue(resultActionsMovies.andReturn().getResponse().getContentAsString(),new TypeReference<List<MovieDTO>>(){});


		HashSet<ActorMovieDTO> actorMovieDTOS = new HashSet<>();
		ActorMovieDTO e = new ActorMovieDTO();
		e.setId(movieDTOList.get(0).getId());
		actorMovieDTOS.add(e);
		actorDTO.setMovies(actorMovieDTOS);

		ResultActions resultSecondActor = mockMvc.perform(post("/actors/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actorDTO)));
		resultSecondActor.andExpect(status().isOk());



		ResultActions actorsResponse = mockMvc.perform(get("/actors/").with(httpBasic("user","password")));

		List<ActorDTO> actorDTOList = objectMapper.readValue(actorsResponse.andReturn().getResponse().getContentAsString(),new TypeReference<List<ActorDTO>>(){});

		Assertions.assertEquals(actorDTOList.get(0).getMovies().size(),1);

		HashSet<ActorMovieDTO> actorMovieDTOSReassign = new HashSet<>();
		ActorMovieDTO eReassign = new ActorMovieDTO();
		eReassign.setId(movieDTOList.get(1).getId());
		actorMovieDTOSReassign.add(eReassign);
		actorDTO.setMovies(actorMovieDTOSReassign);

		 mockMvc.perform(put("/actors/{id}",actorDTOList.get(0).getId()).with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actorDTO)));

		ResultActions actorsResponseReassign = mockMvc.perform(get("/actors/").with(httpBasic("user","password")));

		List<ActorDTO> actorDTOListReassign = objectMapper.readValue(actorsResponseReassign.andReturn().getResponse().getContentAsString(),new TypeReference<List<ActorDTO>>(){});

		Set<ActorMovieDTO> movies = actorDTOListReassign.get(0).getMovies();
		Optional<ActorMovieDTO> first = movies.stream().findFirst();


		Assertions.assertEquals(first.get().getId(),movieDTOList.get(1).getId());
	}




	@Test
	public void testCreateMovieAssignActorAndReassignActor() throws Exception {

		ActorDTO randomActor = getRandomActor();
		ActorDTO randomActor2 = getRandomActor();


		//Insert first actor

		ResultActions resultFirstActor = mockMvc.perform(post("/actors/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(randomActor)));
		resultFirstActor.andExpect(status().isOk());

		ResultActions resultSecondActor = mockMvc.perform(post("/actors/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(randomActor)));
		resultSecondActor.andExpect(status().isOk());




		ResultActions re = mockMvc.perform(get("/actors/").with(httpBasic("user","password")));

		List<ActorDTO> actorResponse = objectMapper.readValue(re.andReturn().getResponse().getContentAsString(),new TypeReference<List<ActorDTO>>(){});


		ActorDTO actorDTO = actorResponse.get(0);
		ActorDTO actorReplace = actorResponse.get(1);


		MovieDTO randomMovie = getRandomMovie();
		Set<MovieActorDTO>  list = new HashSet<>();
		list.add(new MovieActorDTO(actorReplace.getId(),actorReplace.getName()));
		randomMovie.setActors(list);
		String movie = objectMapper.writeValueAsString(randomMovie);


		//crate movie
		mockMvc.perform(post("/movies/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(movie)).andExpect(status().isOk());



		ResultActions moviesResponse = mockMvc.perform(get("/movies/").with(httpBasic("user","password")));

		ResultActions resultActionsActors = moviesResponse.andExpect(status().isOk());


		List<MovieDTO> movieDTOList = objectMapper.readValue(resultActionsActors.andReturn().getResponse().getContentAsString(),new TypeReference<List<MovieDTO>>(){});


		MovieDTO movieDTO = movieDTOList.get(0);
		MovieActorDTO madto = new MovieActorDTO(actorReplace.getId(),actorReplace.getName());
		Set<MovieActorDTO> movieActorDTOS = new HashSet<>();
		movieActorDTOS.add(madto);
		movieDTO.setActors(movieActorDTOS);


		mockMvc.perform(put("/movies/{id}",movieDTO.getId()).with(httpBasic("user","password")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(movieDTO)));


		ResultActions moviesChangeResponse = mockMvc.perform(get("/movies/").with(httpBasic("user","password")));

		ResultActions resChangeResp = moviesChangeResponse.andExpect(status().isOk());


		List<MovieDTO> finalMovie = objectMapper.readValue(resChangeResp.andReturn().getResponse().getContentAsString(),new TypeReference<List<MovieDTO>>(){});

		Optional<MovieActorDTO> first = finalMovie.get(0).getActors().stream().findFirst();
		Assertions.assertEquals(first.get().getId(),actorReplace.getId());


	}


	@Test
	public void insertMoviesAndGetMostRatedByYear() throws Exception {

		MovieDTO randomMovie = getRandomMovie();
		MovieDTO randomMovie1 = getRandomMovie();
		MovieDTO randomMovie2 = getRandomMovie();
		MovieDTO randomMovie3 = getRandomMovie();

		randomMovie3.setYear(2022);
		randomMovie3.setRating(1);
		randomMovie2.setYear(2022);
		randomMovie2.setRating(8);
		randomMovie1.setYear(2022);
		randomMovie1.setRating(9);
		randomMovie.setYear(2022);
		randomMovie.setRating(7);




		//crate movie
		mockMvc.perform(post("/movies/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(randomMovie))).andExpect(status().isOk());

		mockMvc.perform(post("/movies/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(randomMovie1))).andExpect(status().isOk());

		mockMvc.perform(post("/movies/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(randomMovie2))).andExpect(status().isOk());

		mockMvc.perform(post("/movies/").with(httpBasic("user","password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(randomMovie3))).andExpect(status().isOk());



		ResultActions moviesChangeResponse = mockMvc.perform(get("/movies/").with(httpBasic("user","password")));

		ResultActions resChangeResp = moviesChangeResponse.andExpect(status().isOk());


		List<MovieDTO> finalMovie = objectMapper.readValue(resChangeResp.andReturn().getResponse().getContentAsString(),new TypeReference<List<MovieDTO>>(){});

		Assertions.assertEquals(finalMovie.size(),4);

		ResultActions moviesRating = mockMvc.perform(get("/movies/mostRating/{year}",2022).with(httpBasic("user","password")));

		List<MovieDTO> rating = objectMapper.readValue(moviesRating.andReturn().getResponse().getContentAsString(),new TypeReference<List<MovieDTO>>(){});

		Assertions.assertEquals(rating.size(),3);

		List<Integer> list = rating.stream().map(MovieDTO::getRating).toList();
		Assertions.assertEquals(list.get(0),9);
		Assertions.assertEquals(list.get(1),8);
		Assertions.assertEquals(list.get(2),7);


	}


	//insertmoviesAndgetratingbyyear





	private MovieDTO getRandomMovie(){

		List<String> filmName = Arrays.asList("Nuovo Cinema Paradiso", "Oppenheimer","Top Gun: Maverick","Barbie","Fast X");
		List<Integer> filmYear = Arrays.asList(2022,2021,2013,1014,2019,2018);
		List<Integer> filmRating = Arrays.asList(1,2,3,4,5);


		MovieDTO movieDTO = new MovieDTO();
		movieDTO.setName(filmName.get(getRandomIntWithMax(filmName.size())));
		movieDTO.setRating(filmRating.get(getRandomIntWithMax(filmRating.size())));
		movieDTO.setYear(filmYear.get(getRandomIntWithMax(filmYear.size())));

		return movieDTO;

	}

	private int getRandomIntWithMax(int max){
		return (int) (Math.random()* max );
	}
	private ActorDTO getRandomActor(){
		List<String> actorName = Arrays.asList("Ania TailorJoy", "Humphrey Bogart","James Stewart","Clark Gable","Clark Gable");

		ActorDTO actorDTO = new ActorDTO();
		actorDTO.setName(actorName.get(getRandomIntWithMax(actorName.size())));

		return actorDTO;
	}
}
