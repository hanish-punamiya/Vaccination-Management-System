package edu.sjsu.cmpe275.Model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@NoArgsConstructor
@Entity
public class ConfirmationToken {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String token;
	@Column(nullable = false)
	private LocalDateTime createdAt;
	private LocalDateTime expiresAt;
	private LocalDateTime confirmedAt;
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name="app_user_id"
			)
	private User user;
	
	
	
	
	public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, User user) {
		super();
		this.token = token;
		this.createdAt = createdAt;
		this.expiresAt = expiresAt;
		this.user = user;
	}
	public ConfirmationToken() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}
	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}
	public LocalDateTime getConfirmedAt() {
		return confirmedAt;
	}
	public void setConfirmedAt(LocalDateTime confirmedAt) {
		this.confirmedAt = confirmedAt;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	

}
