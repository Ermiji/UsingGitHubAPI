package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface CollaboratorsRepository extends CrudRepository<Collaborators, Long> {
    Collaborators findByLogin(String login);
}