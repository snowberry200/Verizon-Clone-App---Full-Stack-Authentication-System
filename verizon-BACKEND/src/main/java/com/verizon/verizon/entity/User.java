package com.verizon.verizon.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="user")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    //required params
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String  email;
    @Column(nullable = false)
    private String password;

    // Optional params
    @Column(nullable = true)
    private   boolean isActive;
    @Column(nullable = true)
    private  LocalDateTime createdAt;
    @Column(nullable = true)
    private LocalDateTime lastLogin;
    @Column(nullable = true)
    private String accessToken;

    // non-primitive dependable params
    @ManyToOne()
    @JoinColumn(name = "security_question_id")
    private UserSecurityQuestion userSecurityQuestion;
    @ManyToMany()
    @JoinTable(name="roles_id",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private List<Roles> roles = new ArrayList<>();


    public User(){
        //for JPA
    }

    public User(Builder builder){
        this.name = builder.name;
        this.email = builder.email;
        this.password = builder.password;
        this.roles = builder.roles;
        this.userSecurityQuestion = builder.userSecurityQuestion;
        this.createdAt = builder.createdAt;
        this.lastLogin = builder.lastLogin;
        this.isActive = builder.isActive;
        this.accessToken = builder.accessToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    // helper method to get single role
    public void addSingleRole(Roles role){
        // Initialize if null
        if(this.roles==null){
            this.roles = new ArrayList<>();
        }
        // Add role if not already present
        if(!this.roles.contains(role)){
            this.roles.add(role);
            role.addSingleUserInRole(this);  // ← Maintain the OTHER side!
        }
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserSecurityQuestion getUserSecurityQuestion() {
        return userSecurityQuestion;
    }

    public void setUserSecurityQuestion(UserSecurityQuestion userSecurityQuestion) {
        this.userSecurityQuestion = userSecurityQuestion;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public static class Builder{
        private  final String name;
        private final String  email;
        private final String password;
        private boolean isActive = false;
        private  String accessToken = "";
        private  LocalDateTime createdAt = LocalDateTime.now();
        private  LocalDateTime lastLogin = LocalDateTime.now();
        private  UserSecurityQuestion userSecurityQuestion ;
        private  List<Roles> roles = new ArrayList<>();

        // constructor for required  params
       public Builder(String name,String email,String password){
            this.name = name;
            this.email = email;
            this.password = password;
        }

        //constructors for optional params
        public Builder isActive(boolean isActive){
           this.isActive = isActive;
           return this;
        }
        public Builder accessToken(String accessToken){
           this.accessToken = accessToken;
           return  this;
        }
        public Builder createdAt (LocalDateTime createdAt){
           this.createdAt = createdAt;
           return this;
        }

        public Builder lastLogin (LocalDateTime lastLogin ){
           this.lastLogin = lastLogin;
           return this;
        }

        // constructor for non-primitive dependent types
        public Builder userSecurityQuestion (UserSecurityQuestion userSecurityQuestion){
           this.userSecurityQuestion = userSecurityQuestion;
           return this;
        }
        public Builder roles (List<Roles>roles){
           this.roles = roles;
           return this;
        }
        public User build (){
           return new User(this);
        }


    }

}