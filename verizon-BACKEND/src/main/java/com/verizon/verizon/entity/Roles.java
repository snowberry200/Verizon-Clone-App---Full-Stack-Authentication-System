package com.verizon.verizon.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="roles")
public class Roles{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;


    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();
    //required param
    private String name ;

    //no args constructor
    public Roles(){
        //for Jpa
    }
    //constructor for Role
    public Roles(Builder builder){
        this.name = builder.name;
        this.users = builder.users;
    }

    //to add single user
    // HELPER METHOD TO ADD A SINGLE USER
    public void addSingleUserInRole(User user) {
        // Initialize if null
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        // Add user if not already present
        if (!this.users.contains(user)) {
            this.users.add(user);
            user.addSingleRole(this);  // ← Maintain the OTHER side!
        }
    }
    //factory constructor

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    // a builder class
    public static class Builder {
        private final String name ;
        private List<User> users  = new ArrayList<>();

        //constructor for primitive independent param
        public Builder(String name) {
            this.name = name;
        }

        //constructor for non-primitive dependent param
        public Builder users(List<User> users){
            this.users = users;
            return this;
        }

        //build method
        public Roles build(){
            return new Roles (this);
        }


    }
}
