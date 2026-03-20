package com.verizon.verizon.entity;

import com.verizon.verizon.userstatuses.*;
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

    // Optional params
    @Column(nullable = true)
    private LocalDateTime createdAt;
    @Column(nullable = true)
    private LocalDateTime lastLogin;
    @Column(nullable = true)
    private String accessToken;
    @Column(nullable = true)
    private String verificationToken;

    @Column(nullable = true)
    private LocalDateTime verificationTokenExpiry;

    @Column(nullable = true)
    private LocalDateTime verifiedAt;
    @Column(nullable = false)
    private String statusCode;
    @Transient
    private UserStatusContext userStatusContext;
    @Transient
    private UserStatus currentStatus;

    // non-primitive dependable params
    @ManyToOne()
    @JoinColumn(name = "security_question_id")
    private UserSecurityQuestion userSecurityQuestion;

    @ManyToMany()
    @JoinTable(name = "roles_id",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Roles> roles = new ArrayList<>();

    public void initializeStatusContext(){
        this.userStatusContext = new UserStatusContext();
        // Set the initial status based on statusCode from database
        this.currentStatus = createStatusFromCode(this.statusCode);
        userStatusContext.setStatus(currentStatus);
    }
    // Create appropriate status object from code
    private UserStatus createStatusFromCode(String code) {
        if ("ACTIVE".equals(code)) {
            return new ActiveStatus(this.userStatusContext);
        } else {
            return new NonActiveStatus(this.userStatusContext);
        }
    }

    // JPA callback - reinitialize transient fields after loading from database
    @PostLoad
    private void postLoad() {
        initializeStatusContext();
    }

    // Delegate methods that use the State pattern
    public boolean canLogin() {
        return userStatusContext.canLogin();
    }

    public String getNextStepMessage() {
        return userStatusContext.getNextStepMessage();
    }

    public void activate() {
        userStatusContext.activate();
        // After state changes, update the persisted statusCode
        this.statusCode = userStatusContext.getStatusCode();
    }

    public void deActivate() {
        userStatusContext.deActivate();
        // After state changes, update the persisted statusCode
        this.statusCode = userStatusContext.getStatusCode();
    }


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
        this.verificationToken = builder.verificationToken;
        this.verificationTokenExpiry = builder.verificationTokenExpiry;
        this.verifiedAt = builder.verifiedAt;

        // Initialize the state pattern after all fields are set
        initializeStatusContext();
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
        private String accessToken ;
        private String verificationToken;
        private LocalDateTime verificationTokenExpiry;
        private LocalDateTime verifiedAt;
        private String statusCode = "NONACTIVE";  // Default status
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime lastLogin = LocalDateTime.now();
        private UserSecurityQuestion userSecurityQuestion;
        private List<Roles> roles = new ArrayList<>();

        public Builder(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;

            // OPTIONAL PRIMITIVE PARAMS
            this.accessToken = "";
            this.createdAt = LocalDateTime.now();
            this.lastLogin = LocalDateTime.now();
            this.statusCode = "NONACTIVE";

            // ✅ Default values for verification fields
            this.verificationToken = null;
            this.verificationTokenExpiry = null;
            this.verifiedAt = null;

            this.userSecurityQuestion = null;
            this.roles = new ArrayList<>();


        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder verificationToken(String verificationToken) {
            this.verificationToken = verificationToken;
            return this;
        }

        public Builder verificationTokenExpiry(LocalDateTime verificationTokenExpiry) {
            this.verificationTokenExpiry = verificationTokenExpiry;
            return this;
        }

        public Builder verifiedAt(LocalDateTime verifiedAt) {
            this.verifiedAt = verifiedAt;
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

        public Builder statusCode(String statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public User build() {
            return new User(this);
        }
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

    public String getVerificationToken() { return verificationToken; }
    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public LocalDateTime getVerificationTokenExpiry() { return verificationTokenExpiry; }
    public void setVerificationTokenExpiry(LocalDateTime verificationTokenExpiry) {
        this.verificationTokenExpiry = verificationTokenExpiry;
    }

    public LocalDateTime getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public UserSecurityQuestion getUserSecurityQuestion() { return userSecurityQuestion; }
    public void setUserSecurityQuestion(UserSecurityQuestion userSecurityQuestion) {
        this.userSecurityQuestion = userSecurityQuestion;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
        // Also update the state pattern when status is manually changed
        if (this.userStatusContext != null) {
            this.currentStatus = createStatusFromCode(statusCode);
            this.userStatusContext.setStatus(this.currentStatus);
        }
    }
}
