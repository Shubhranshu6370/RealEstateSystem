package com.realestate.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Booking {

	@Id
	@Column(length = 20)
	private String bookingId;

	@Column(length = 10)
	private LocalDate checkInDate;

	@Column(length = 10)
	private LocalDate checkOutDate;

	@Column(length = 20, nullable = false)
	private String status;

	@Column
	private LocalDate bookingdate;

	@OneToOne
	private Property property;

	@OneToOne
	private User users;

	public Booking(LocalDate checkInDate, LocalDate checkOutDate, String status, Property property, User users) {
		super();
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.status = status;
		this.property = property;
		this.users = users;
	}

	public Booking(String status, LocalDate bookingdate, Property property, User users) {
		super();
		this.status = status;
		this.bookingdate = bookingdate;
		this.property = property;
		this.users = users;
	}

}
