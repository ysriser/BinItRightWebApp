package tech3.binitright.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

@MappedSuperclass
public abstract class BinItRightUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userUid")
    private Long id;

    private String name;
    private String username;
    private String passwordHash;
    private String locale;
    private String emailAddress;
    private String role;
    private LocalDateTime createdAt;

    public BinItRightUser() {
    }

    public BinItRightUser(final String name, final String username, final String passwordHash,
                          final String locale, final String emailAddress, final String role, 
                          final LocalDateTime createdAt) {
        this.name = name;
        this.username = username;
        this.passwordHash = passwordHash;
        this.locale = locale;
        this.emailAddress = emailAddress;
        this.role = role;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public final Long getId() {
        return id;
    }

    public final void setId(final Long id) {
        this.id = id;
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final String getUsername() {
        return username;
    }

    public final void setUsername(final String username) {
        this.username = username;
    }

    public final String getPasswordHash() {
        return passwordHash;
    }

    public final void setPasswordHash(final String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public final String getLocale() {
        return locale;
    }

    public final void setLocale(final String locale) {
        this.locale = locale;
    }

    public final String getEmailAddress() {
        return emailAddress;
    }

    public final void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public final String getRole() {
        return role;
    }

    public final void setRole(final String role) {
        this.role = role;
    }

    public final LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public final void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}