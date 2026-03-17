package com.verizon.verizon.entity;

import com.verizon.verizon.userstatuses.UserStatusContext;
import com.verizon.verizon.userstatuses.UserStatus;
import com.verizon.verizon.userstatuses.StatusFactory;
import com.verizon.verizon.userstatuses.NonActiveStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    //required params
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    // Store statusCode in database (simple string)
    @Column(name = "status_code", nullable = false)
    private String statusCode = "NONACTIVE";

    // ✅ Use UserStatusContext instead of raw UserStatus
    @Transient
    private UserStatusContext statusContext;

    // Optional params
    @Column(nullable = true)
    private LocalDateTime createdAt;
    @Column(nullable = true)
    private LocalDateTime lastLogin;
    @Column(nullable = true)
    private String accessToken;

    // non-primitive dependable params
    @ManyToOne()
    @JoinColumn(name = "security_question_id")
    private UserSecurityQuestion userSecurityQuestion;

    @ManyToMany()
    @JoinTable(name = "roles_id",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Roles> roles = new ArrayList<>();

    public User() {
        //for JPA
    }

    private User(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.password = builder.password;
        this.roles = builder.roles;
        this.userSecurityQuestion = builder.userSecurityQuestion;
        this.createdAt = builder.createdAt;
        this.lastLogin = builder.lastLogin;
        this.accessToken = builder.accessToken;

        // ✅ Handle status context properly
        if (builder.statusContext != null) {
            this.statusContext = builder.statusContext;
            this.statusCode = this.statusContext.getStatusCode();
        } else {
            UserStatus defaultStatus = new NonActiveStatus();
            this.statusContext = new UserStatusContext(defaultStatus);
            this.statusCode = "NONACTIVE";
        }
    }

    // ✅ JPA lifecycle methods
    @PostLoad
    private void initStatusContext() {
        // Convert stored code to status object and wrap in context
        UserStatus status = StatusFactory.getStatusByCode(this.statusCode);
        this.statusContext = new UserStatusContext(status);
    }

    @PrePersist
    @PreUpdate
    private void updateStatusCode() {
        if (this.statusContext != null) {
            this.statusCode = this.statusContext.getStatusCode();
        }
    }

    // ✅ Getters and setters for status context
    public UserStatusContext getStatusContext() {
        if (this.statusContext == null && this.statusCode != null) {
            UserStatus status = StatusFactory.getStatusByCode(this.statusCode);
            this.statusContext = new UserStatusContext(status);
        }
        return this.statusContext;
    }

    public void setStatusContext(UserStatusContext statusContext) {
        this.statusContext = statusContext;
        if (statusContext != null) {
            this.statusCode = statusContext.getStatusCode();
        }
    }

    // ✅ Convenience method to set status directly
    public void setStatus(UserStatus newStatus) {
        if (this.statusContext == null) {
            this.statusContext = new UserStatusContext(newStatus);
        } else {
            this.statusContext.setStatus(newStatus);
        }
        this.statusCode = newStatus.getStatusCode();
    }

    // ✅ Convenience method to change status by code
    public void changeStatusByCode(String statusCode) {
        UserStatus newStatus = StatusFactory.getStatusByCode(statusCode);
        setStatus(newStatus);
    }

    // ✅ Delegate methods to context
    public boolean canLogin() {
        return getStatusContext().canLogin();
    }

    public String getNextStepMessage() {
        return getStatusContext().getNextStepMessage();
    }

    public String getStatusName() {
        return getStatusContext().getStatusName();
    }

    public String getStatusCode() {
        return statusCode;
    }

    // Regular getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public List<Roles> getRoles() { return roles; }
    public void setRoles(List<Roles> roles) { this.roles = roles; }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public UserSecurityQuestion getUserSecurityQuestion() { return userSecurityQuestion; }
    public void setUserSecurityQuestion(UserSecurityQuestion userSecurityQuestion) {
        this.userSecurityQuestion = userSecurityQuestion;
    }

    // helper method to add role
    public void addSingleRole(Roles role) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        if (!this.roles.contains(role)) {
            this.roles.add(role);
            role.addSingleUserInRole(this);
        }
    }

    public static class Builder {
        private final String name;
        private final String email;
        private final String password;

        // ✅ Use UserStatusContext in builder
        private UserStatusContext statusContext;
        private String accessToken = "";
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime lastLogin = LocalDateTime.now();
        private UserSecurityQuestion userSecurityQuestion;
        private List<Roles> roles = new ArrayList<>();

        public Builder(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }

        // ✅ Builder methods for status
        public Builder statusContext(UserStatusContext statusContext) {
            this.statusContext = statusContext;
            return this;
        }

        public Builder status(UserStatus status) {
            this.statusContext = new UserStatusContext(status);
            return this;
        }

        public Builder statusByCode(String statusCode) {
            UserStatus status = StatusFactory.getStatusByCode(statusCode);
            this.statusContext = new UserStatusContext(status);
            return this;
        }

        public Builder withActiveStatus() {
            return statusByCode("ACTIVE");
        }

        public Builder withNonActiveStatus() {
            return statusByCode("NONACTIVE");
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder lastLogin(LocalDateTime lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public Builder userSecurityQuestion(UserSecurityQuestion userSecurityQuestion) {
            this.userSecurityQuestion = userSecurityQuestion;
            return this;
        }

        public Builder roles(List<Roles> roles) {
            this.roles = roles;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}