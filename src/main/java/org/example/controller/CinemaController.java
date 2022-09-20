package org.example.controller;

import org.example.dto.MovieDto;
import org.example.dto.OrderRequestDto;
import org.example.dto.OrderResponseDto;
import org.example.entity.Movie;
import org.example.entity.Order;
import org.example.mapper.MovieMapper;
import org.example.mapper.OrderMapper;
import org.example.service.MovieService;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("cinema")
public class CinemaController {
    private final MovieService movieService;
    private final OrderService orderService;

    @Autowired
    public CinemaController(MovieService movieService, OrderService orderService) {
        this.movieService = movieService;
        this.orderService = orderService;
    }

    @GetMapping(value = "/movies")
    public List<MovieDto> getMovies() {
        return movieService.findAll().stream().map(MovieMapper::mapToMovieDto).collect(Collectors.toList());
    }


    @GetMapping(value = "/movie/{id}/")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable("id") int id) {
        Movie movie = movieService.findById(id);
        if (movie != null) {
            return ResponseEntity.ok(MovieMapper.mapToMovieDto(movie));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping(value = "/movie")
    public void createMovie(@RequestBody MovieDto movieDto) {
        movieService.create(MovieMapper.mapToMovie(movieDto));
    }

    @DeleteMapping(value = "/movie/{id}/")
    public boolean deleteMovieById(@PathVariable("id") int id) {
        return movieService.delete(id);
    }

    @PutMapping(value = "/movie/{id}/")
    public void updateMovieById(@PathVariable("id") int id, @RequestBody MovieDto movieDto) {
        movieService.update(movieDto, id);
    }

    @GetMapping(value = "/movie")
    public List<MovieDto> getMovieByName(@RequestParam("name") String name,
                                         @RequestParam(value = "pageNumber") final int pageNumber,
                                         @RequestParam(value = "pageSize") final int pageSize,
                                         @RequestParam(value = "isCriteria") final boolean isCriteria) {
        if (isCriteria) {
            return movieService.findMovieByNameCriteria(name, new PageRequest(pageNumber, pageSize)).getContent().stream().map(MovieMapper::mapToMovieDto).collect(Collectors.toList());
        } else {
            return movieService.findMovieByNameSpecification(name, new PageRequest(pageNumber, pageSize)).getContent().stream().map(MovieMapper::mapToMovieDto).collect(Collectors.toList());
        }
    }

    @GetMapping(value = "/order/{id}/")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable("id") int id) {
        Order order = orderService.findById(id);
        if (order != null) {
            return ResponseEntity.ok(OrderMapper.mapToOrderDto(order));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping(value = "/order")
    public void createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        Integer movieId = orderRequestDto.getMovieId();
        if (movieService.findById(movieId) != null) {
            orderService.create(OrderMapper.mapToOrder(orderRequestDto));
        }
    }

    @PutMapping(value = "/order/{id}/")
    public void updateOrderById(@PathVariable("id") int id, @RequestBody OrderRequestDto orderRequestDto) {
        Integer movieId = orderRequestDto.getMovieId();
        if (movieService.findById(movieId) != null) {
            orderService.update(orderRequestDto, id);
        }
    }

    @DeleteMapping(value = "/order/{id}/")
    public boolean deleteOrderById(@PathVariable("id") int id) {
        return orderService.delete(id);
    }


    @GetMapping(value = "/order")
    public List<OrderResponseDto> getOrderByUserName(@RequestParam("userName") String userName,
                                                     @RequestParam(value = "pageNumber") final int pageNumber,
                                                     @RequestParam(value = "pageSize") final int pageSize,
                                                     @RequestParam(value = "isCriteria") final boolean isCriteria) {
        if (isCriteria) {
            return orderService.findOrderByUserNameCriteria(userName, new PageRequest(pageNumber, pageSize)).getContent().stream().map(OrderMapper::mapToOrderDto).collect(Collectors.toList());
        } else {
            return orderService.findOrderByUserNameSpecification(userName, new PageRequest(pageNumber, pageSize)).getContent().stream().map(OrderMapper::mapToOrderDto).collect(Collectors.toList());
        }
    }
}
