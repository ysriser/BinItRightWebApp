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
	private String password_hash;
	private String locale;
	private String emailAddress;
	private String role;
	private LocalDateTime created_at;
	
	public BinItRightUser() {}
	
	public BinItRightUser(String name, String username, String password_hash, String locale, String emailAddress, String role,LocalDateTime created_at) {
		this.name = name;
		this.username = username;
		this.password_hash = password_hash;
		this.locale = locale;
		this.emailAddress = emailAddress;
		this.role = role;

    }
	
	@PrePersist
    protected void onCreate() {
        this.created_at = LocalDateTime.now();
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
		return password_hash;
	}

	public void setPassword_hash(String password_hash) {
		this.password_hash = password_hash;
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
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

}
	
