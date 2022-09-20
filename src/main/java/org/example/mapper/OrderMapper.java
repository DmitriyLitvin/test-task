package org.example.mapper;

import org.example.dto.OrderRequestDto;
import org.example.dto.OrderResponseDto;
import org.example.entity.Movie;
import org.example.entity.Order;

public class OrderMapper {

    public static Order mapToOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order();
        Movie movie = MovieMapper.mapToMovie(orderRequestDto.getMovieDto());
        movie.setId(orderRequestDto.getMovieId());
        order.setMovie(movie);
        order.setUserName(orderRequestDto.getUserName());
        order.setEmail(orderRequestDto.getEmail());
        return order;
    }

    public static OrderResponseDto mapToOrderDto(Order order) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setMovieDto(MovieMapper.mapToMovieDto(order.getMovie()));
        orderResponseDto.setUserName(order.getUserName());
        orderResponseDto.setEmail(order.getEmail());
        return orderResponseDto;
    }
}
