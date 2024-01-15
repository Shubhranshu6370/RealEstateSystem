package com.realestate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Property {

	@Id
	@Column(length = 10)
	private String propertyId;

	@Column(length = 30, nullable = false)
	private String ownerName;

	@Column(length = 30, nullable = false)
	private String description;

	@Column(length = 30, nullable = false)
	private String address;

	@Column(length = 20, nullable = false)
	private String city;

	@Column(length = 10, nullable = false)
	private String zipCode;

	@Column(length = 10, nullable = false)
	private int price;

	@Column(length = 20, nullable = false)
	private int squareFeet;

	@Column(length = 10, nullable = false)
	private int bedroom;

	@Column(length = 10, nullable = false)
	private int bathroom;

	@Column(length = 20, nullable = false)
	private String status;

	@Column(length = 20, nullable = false)
	private String type;

	@Column(length = 10, nullable = false)
	private String contactNo;

	@OneToOne
	private Booking bookings;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Property(String ownerName, String description, String address, String city, String zipCode, int price,
			int squareFeet, int bedroom, int bathroom, String status, String type, String contactNo) {
		super();
		this.ownerName = ownerName;
		this.description = description;
		this.address = address;
		this.city = city;
		this.zipCode = zipCode;
		this.price = price;
		this.squareFeet = squareFeet;
		this.bedroom = bedroom;
		this.bathroom = bathroom;
		this.status = status;
		this.type = type;
		this.contactNo = contactNo;
	}

}
