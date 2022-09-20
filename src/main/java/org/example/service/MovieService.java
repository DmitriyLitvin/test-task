package org.example.service;

import org.example.dto.MovieDto;
import org.example.entity.Movie;
import org.example.mapper.MovieMapper;
import org.example.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    private final EntityManager em;

    @Autowired
    public MovieService(MovieRepository movieRepository, EntityManager em) {
        this.movieRepository = movieRepository;
        this.em = em;
    }


    public void create(Movie movie) {
        movieRepository.save(movie);
    }

    @Transactional
    public boolean delete(Integer id) {
        Movie movie = movieRepository.findOne(id);
        if (movie != null) {
            movieRepository.delete(id);
            return !movieRepository.exists(id);
        }

        return false;
    }

    public List<Movie> findAll(){
        return movieRepository.findAll();
    }

    public Page<Movie> findMovieByNameCriteria(String name, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);

        Root<Movie> movieRoot = cq.from(Movie.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(movieRoot.get("movieName"), name));

        cq.where(cb.and(predicates.toArray( new Predicate[predicates.size()])));

        List<Movie> movies = em.createQuery(cq).setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Movie> movieRootCount = countQuery.from(Movie.class);
        countQuery.select(cb.count(movieRootCount)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        Long count = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(movies, pageable, count);
    }

    private static Specification<Movie> hasName(String name) {
        return (movie, cq, cb) -> cb.equal(movie.get("movieName"), name);
    }

    public Page<Movie> findMovieByNameSpecification(String name, Pageable pageable) {
        return movieRepository.findAll(hasName(name), pageable);
    }


    public Movie findById(Integer id) {
        return movieRepository.findOne(id);
    }

    @Transactional
    public void update(MovieDto movieDto, Integer id) {
        Movie storedMovie = findById(id);
        if (storedMovie != null) {
            Movie movie =  MovieMapper.mapToMovie(movieDto);
            movie.setId(id);
            movie.setOrders(storedMovie.getOrders());
            movieRepository.save(movie);
        }
    }
}
