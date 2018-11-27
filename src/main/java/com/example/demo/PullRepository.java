package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface PullRepository extends CrudRepository<Pull, Long> {
    Pull findById(long id);
    Pull findByTitle(String title);

}