package com.API.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Table;

import java.util.Objects;

// Giả sử bạn có một lớp như sau
//@Table(name = "test")
class test {
    private int id;
    private String name;

    // Constructor, getters, setters, equals và hashCode...

    public test(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        test myObject = (test) o;
        return id == myObject.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean equalsByName(test other) {
        return this.name.equals(other.name);
    }
}



