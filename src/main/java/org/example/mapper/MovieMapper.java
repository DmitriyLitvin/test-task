package org.example.mapper;


import org.example.dto.MovieDto;
import org.example.entity.Movie;

public class MovieMapper {

    public static Movie mapToMovie(MovieDto movieDto) {
        Movie movie = new Movie();
        movie.setMovieName(movieDto.getMovieName());
        movie.setType(movieDto.getType());
        movie.setDescription(movieDto.getDescription());
        movie.setPrice(movieDto.getPrice());
        movie.setRating(movieDto.getRating());
        return movie;
    }

    public static MovieDto mapToMovieDto(Movie movie) {
        MovieDto movieDto = new MovieDto();
        movieDto.setMovieName(movie.getMovieName());
        movieDto.setType(movie.getType());
        movieDto.setDescription(movie.getDescription());
        movieDto.setPrice(movie.getPrice());
        movieDto.setRating(movie.getRating());
        return movieDto;
    }
}
