package com.example.cash4books.cash4books.entity;
import javax.persistence.*;

@Entity
@Table(name = "users")
public class usersEntity {

    @Id
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
