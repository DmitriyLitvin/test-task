package org.example.entity;

import lombok.*;

import javax.persistence.*;
@Entity(name = "orders")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name="movie_id", nullable=false)
    private Movie movie;

    private String userName;

    private String email;
}
