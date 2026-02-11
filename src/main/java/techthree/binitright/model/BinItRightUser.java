package techthree.binitright.model;

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
    @Column(name = "user_id")
	private Long id;
	
	private String name;
	private String username;

    @Column(name = "password_hash")
    private String passwordHash;

	private String locale;
	private String emailAddress;
	private String role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
	
	public BinItRightUser() {}
	
	public BinItRightUser(String name, String username, String passwordHash, String locale, String emailAddress, String role) {
		this.name = name;
		this.username = username;
		this.passwordHash = passwordHash;
		this.locale = locale;
		this.emailAddress = emailAddress;
		this.role = role;

    }
	
	@PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
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

	public String getPassword_hash() {
		return passwordHash;
	}

	public void setPassword_hash(String passwordHash) {
		this.passwordHash = passwordHash;
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

	public LocalDateTime getCreated_at() {
		return createdAt;
	}

	public void setCreated_at(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
	
