package org.example.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "movies")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "orders")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String movieName;

    private String description;

    private Double rating;

    private Double price;

    private String type;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

}
