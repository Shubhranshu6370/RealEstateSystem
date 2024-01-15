package com.realestate.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

	@Id
	@Column(length = 10)
	private String userId;

	@Column(name = "first_name", length = 30, nullable = false)
	private String firstName;

	@Column(name = "last_name", length = 30, nullable = false)
	private String lastName;

	@Column(length = 30, nullable = false, unique = true)
	private String email;

	@Column(length = 30, nullable = false, unique = true)
	private String password;

	@Column(length = 10, nullable = false, unique = true)
	private String phoneNumber;

	@Column(length = 10)
	private String typeOfUser;

	@OneToMany
	@JoinColumn(name = "user_id")
	private List<Review> reviews;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Property> properties;

	public List<Property> getProperties() {
		return properties;
	}

	public User(String userId, String firstName, String lastName, String email, String password, String phoneNumber,
			String typeOfUser) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.typeOfUser = typeOfUser;
	}

}
