package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Contents {
    @Id
    private  String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
