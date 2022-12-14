package org.example.repository;

import org.example.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public
interface MovieRepository extends JpaRepository<Movie, Integer> , JpaSpecificationExecutor<Movie> {

}
