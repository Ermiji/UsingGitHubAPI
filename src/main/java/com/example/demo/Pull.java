package com.example.demo;


import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Pull {

    @Id
    private long id;
    private String title;
    private int Counter;

    public int getCounter() {
        return Counter;
    }

    public void setCounter(int counter) {
        Counter = counter;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
