package com.realestate.entity;

import java.time.LocalDate;
import java.util.List;

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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Review {

	@Id
	@Column(length = 10)
	private String reviewId;

	@Column(length = 10, nullable = false)
	private int rating;

	@Column(length = 30, nullable = false)
	private String feedback;

	@ManyToOne
	@JoinColumn(name = "property_id")
	private Property property;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Review(int rating, String feedback, Property property, User user) {
		super();
		this.rating = rating;
		this.feedback = feedback;
		this.property = property;
		this.user = user;
	}

}
