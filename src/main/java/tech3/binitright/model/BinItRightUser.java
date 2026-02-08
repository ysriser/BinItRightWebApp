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
	private String passwordUhash;
	private String locale;
	private String emailAddress;
	private String role;
	private LocalDateTime createdUat;

	public BinItRightUser() {}

	public BinItRightUser(final String name, final String username, final String passwordUhash,
			final String locale, final String emailAddress, final String role,final LocalDateTime createdUat) {
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

    public void setId(final Long id) {
        this.id = id;
    }

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getPasswordUhash() {
		return passwordUhash;
	}

	public void setPasswordUhash(final String passwordUhash) {
		this.passwordUhash = passwordUhash;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(final String locale) {
		this.locale = locale;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(final String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getRole() {
		return role;
	}

	public void setRole(final String role) {
		this.role = role;
	}

	public LocalDateTime getCreatedUat() {
		return createdUat;
	}

	public void setCreatedUat(final LocalDateTime createdUat) {
		this.createdUat = createdUat;
	}

}

