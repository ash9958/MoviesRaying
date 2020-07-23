package com.ashish.controller;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ashish.entity.Movie;
import com.ashish.entity.MovieList;
import com.ashish.entity.User;
import com.ashish.execption.MovieNotFoundException;
import com.ashish.execption.ResourceNotFoundException;
import com.ashish.repository.MovieRepo;

@RestController
public class MainController {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	MovieRepo moviesDao;

	@Value("${api.key}")
	private String apiKey;

	@GetMapping("/")
	public Movie[] NowPlayingMovies() {
		MovieList list = restTemplate
				.getForObject("https://api.themoviedb.org/3/movie/now_playing" + "?api_key=" + apiKey, MovieList.class);
		return list.getResults();
	}

	@GetMapping("/movies/top_rated")
	public Movie[] getMoviePopluar() {
		MovieList list = restTemplate
				.getForObject("https://api.themoviedb.org/3/movie/top_rated" + "?api_key=" + apiKey, MovieList.class);
		return list.getResults();

	}

	@GetMapping("/movies/{movieId}")
	public Movie getMovieInfo(@PathVariable("movieId") String movieId) throws MovieNotFoundException {
		Movie movie = restTemplate.getForObject("https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey,
				Movie.class);
		return movie;
	}

	@GetMapping("/movies/popular")
	public Movie[] getTopRatedMovies() {
		MovieList list = restTemplate.getForObject("https://api.themoviedb.org/3/movie/popular" + "?api_key=" + apiKey,
				MovieList.class);
		return list.getResults();
	}

	@GetMapping("/movies/upcoming")
	public Movie[] getUpComingMovies() {
		MovieList list = restTemplate.getForObject("https://api.themoviedb.org/3/movie/upcoming" + "?api_key=" + apiKey,
				MovieList.class);
		return list.getResults();

	}

	@GetMapping("/movies/latest")
	public Movie getLatestMovies() {
		Movie movie = restTemplate.getForObject("https://api.themoviedb.org/3/movie/latest" + "?api_key=" + apiKey,
				Movie.class);
		return movie;
	}

	@GetMapping("/users/{userId}/movies/{movieName}")
	public List<Movie> getUserRatedMovies(@PathVariable long userId, @PathVariable String movieName)
			throws MovieNotFoundException {
		List<Movie> movies = new ArrayList<Movie>();
		MovieList movielist = restTemplate.getForObject(
				"https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&query=" + movieName, MovieList.class);
		if (movielist.getResults().length != 0) {
			movies = Arrays.asList(movielist.getResults());
			for (Movie mv : movies) {
				Movie userMovie = moviesDao.findByUser_IdAndId(userId, mv.getId());
				if (Objects.nonNull(userMovie)) {
					mv.setMyRating(userMovie.getMyRating());
				}
			}

		} else {

			throw new MovieNotFoundException("Movie Not Found - " + movieName);
		}
		return movies;
	}

	@PostMapping("/users/{userId}/movie")
	public List<Movie> saveUserRatedMovies(@PathVariable long userId, @RequestParam String movieId,
			@RequestParam String myRating) {
		List<Movie> mv = new ArrayList<Movie>();
		if (Objects.nonNull(movieId) && Objects.nonNull(myRating)) {
			Movie movie = restTemplate
					.getForObject("https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey, Movie.class);
			if (Objects.nonNull(movie)) {
				movie.setMyRating(myRating);
				User user = new User();
				user.setId(userId);
				movie.setUser(user);
				moviesDao.save(movie);
			} else {
				throw new ResourceNotFoundException("Movie_Id or My_Rating not found");
			}

		}

		Pageable page = (Pageable) PageRequest.of(0, 10);
		mv = moviesDao.findByUser_IdOrderByTimestampDesc(userId, (org.springframework.data.domain.Pageable) page);

		return mv;
	}

	@GetMapping("/users/{userId}/movies")
	public List<Movie> getUserRatedMovies(@PathVariable long userId) {
		List<Movie> movies = new ArrayList<Movie>();

		movies = moviesDao.findByUser_IdOrderByTimestampDesc(userId);
		return movies;
	}

	@GetMapping("/users/{userId}/movies/top_rated")
	public List<Movie> getUserTopRatedMovies(@PathVariable long userId) {
		List<Movie> movies = new ArrayList<Movie>();

		Pageable pageable = (Pageable) PageRequest.of(0, 10);
		movies = moviesDao.findByUser_IdOrderByMyRatingDesc(userId,
				(org.springframework.data.domain.Pageable) pageable);
		return movies;
	}

}