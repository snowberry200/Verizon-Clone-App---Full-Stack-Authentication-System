package com.verizon.verizon.entity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_security_question")
public class UserSecurityQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String answer;

    @ManyToOne
    @JoinColumn(name = "security_question_id", nullable = false)
    private SecurityQuestion question;

    @OneToMany(mappedBy = "userSecurityQuestion")
    private List<User> users = new ArrayList<>();

    public UserSecurityQuestion() {}

    public UserSecurityQuestion(Builder builder) {
        this.answer = builder.answer;
        this.question = builder.question;
        this.users = builder.users;
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.setUserSecurityQuestion(null);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getAnswer() { return answer; }
    public SecurityQuestion getQuestion() { return question; }
    public List<User> getUsers() { return users; }

    public void setQuestion(SecurityQuestion question) { this.question = question; }
    public void setAnswer(String answer) { this.answer = answer; }
    public void setUsers(List<User> users) { this.users = users; }

    public static class Builder {
        private final String answer;
        private SecurityQuestion question;
        private List<User> users = new ArrayList<>();

        //require primitive param
        public Builder(String answer) {
            this.answer = answer;
        }
        // required non-primitive dependent param
        public Builder question(SecurityQuestion question) {
            this.question = question;
            return this;
        }
        // optional non-primitive dependent param
        public Builder users(List<User> users) {
            this.users = users;
            return this;
        }

        public UserSecurityQuestion build() {
            return new UserSecurityQuestion(this);
        }
    }
        // create method to add single user
    public void addSingleUser(User user){
        if(this.users == null){
            this.users= new ArrayList<>();
        }
        if(!this.users.contains(user)){
            this.users.add(user);
            user.setUserSecurityQuestion(this); // <-- maintain the other side
        }
    }

}