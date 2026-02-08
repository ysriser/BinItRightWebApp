package tech3.binitright.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
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
	private String passwordUhash;
	private String locale;
	private String emailAddress;
	private String role;
	private LocalDateTime createdUat;
	
	public BinItRightUser() {}
	
	public BinItRightUser(String name, String username, String passwordUhash, String locale, String emailAddress, String role,LocalDateTime createdUat) {
		this.name = name;
		this.username = username;
		this.passwordUhash = passwordUhash;
		this.locale = locale;
		this.emailAddress = emailAddress;
		this.role = role;

    }
	
	@PrePersist
    protected void onCreate() {
        this.createdUat = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordUhash() {
		return passwordUhash;
	}

	public void setPasswordUhash(String passwordUhash) {
		this.passwordUhash = passwordUhash;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public LocalDateTime getCreatedUat() {
		return createdUat;
	}

	public void setCreatedUat(LocalDateTime createdUat) {
		this.createdUat = createdUat;
	}

}
	
