package com.example.demo;

import javax.persistence.Id;

public class Views {
    @Id
    private long uniques;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    private long count;


    public long getUniques() {
        return uniques;
    }

    public void setUniques(long uniques) {
        this.uniques = uniques;
    }
}
