package com.realestate.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.realestate.entity.Booking;
import com.realestate.entity.Property;
import com.realestate.entity.User;

public class BookingDAO {

	private Session session;

	public BookingDAO(Session session) {
		super();
		this.session = session;
	}

	public String fetchLastAddedId() {
		Object bookId = session.createQuery("select max(s.bookingId) from Booking s").getSingleResult();
		return String.valueOf(bookId);
	}

	// Save Booking Method
	public void saveBooking(Booking booking) {
		Transaction transaction = null;
		try {
			if (!session.getTransaction().isActive()) {
				transaction = session.beginTransaction();
			}

			// To Generate The Custom Id
			String pId = fetchLastAddedId();

			if (pId.contains("null")) {
				pId = "B100";
			}

			String prefix = pId.substring(0, 1); // P
			int postfix = Integer.parseInt(pId.substring(1)); // 102
			String propId = prefix + (postfix + 1); // P + (102+1) = P + 103 = U103

			booking.setBookingId(propId);
			booking.setBookingdate(LocalDate.now());

			session.save(booking);

			transaction.commit();

			System.out.println();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	// Modify Show Booking Method
	public void showBooking(User owner) {
		try {
			List<Property> properties = owner.getProperties();

			if (properties.isEmpty()) {
				System.out.println("You have no properties.");
				return;
			}

			for (Property property : properties) {
				List<Booking> bookings = session
						.createQuery("from Booking where property= :p and status = 'pending'", Booking.class)
						.setParameter("p", property).getResultList();

				if (!bookings.isEmpty()) {
					System.out.println(
							"+------ PENDING BOOKINGS FOR PROPERTY " + property.getPropertyId() + " ---------");
					for (Booking book : bookings) {
						System.out.println("|");
						System.out.println("|");
						System.out.println("+-- Booking Id: " + book.getBookingId());
						System.out.println("+-- Booking Date: " + book.getBookingdate());
						System.out.println("+-- Booked By: " + book.getUsers().getFirstName() + " "
								+ book.getUsers().getLastName());
						System.out.println("+-- Contact No.: " + book.getUsers().getPhoneNumber());
						System.out.println("|");
						System.out.println("+-----------------------------------------------");
					}
				} else {
					System.out.println("No pending bookings for Property " + property.getPropertyId());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Modify Check Booking Status Method
	public String checkBookingStatus(User user) {
		try {
			List<Booking> bookings = session.createQuery("from Booking where users.userId = :userId", Booking.class)
					.setParameter("userId", user.getUserId()).getResultList();

			if (bookings.isEmpty()) {
				return "SORRY! You have no Confirmed Bookings";
			} else {
				StringBuilder statusInfo = new StringBuilder();
				for (Booking booking : bookings) {
					statusInfo.append("|\n");

					if ("Booked".equals(booking.getStatus())) {
						statusInfo.append("+-- YOUR BOOKING HAS BEEN CONFIRMED!\n");
						statusInfo.append("+-- Booking ID: ").append(booking.getBookingId()).append("\n");

						// Check if getProperty() is not null before accessing getPropertyId()
						Property property = booking.getProperty();
						if (property != null) {
							statusInfo.append("+-- Property: ").append(property.getPropertyId()).append("\n");
						} else {
							statusInfo.append("+-- Property information not available\n");
						}

						statusInfo.append("+-- Check-In Date: ").append(booking.getCheckInDate()).append("\n");
						statusInfo.append("+-- Check-Out Date: ").append(booking.getCheckOutDate()).append("\n");
						statusInfo.append("+-- Status: ").append(booking.getStatus()).append("\n");
					} else {
						// Booking is pending
						Property property = booking.getProperty();
						statusInfo.append("+-- Property ID: ");

						// Check if getProperty() is not null before accessing getPropertyId()
						if (property != null) {
							statusInfo.append(property.getPropertyId());
						} else {
							statusInfo.append("N/A");
						}

						statusInfo.append("\n");

						statusInfo.append("+-- Status: ").append(booking.getStatus()).append("\n");
						statusInfo.append(
								"+-- PLEASE WAIT FOR YOUR BOOKING CONFIRMATION FROM THE OWNER'S SIDE, THE OWNER WILL CONTACT YOU SOON!\n");
					}
				}

				return statusInfo.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Error while fetching booking status.";
		}
	}

	// Confirm Booking Method
	public void confirmBooking() {
		Transaction transaction = null;
		Scanner sc = new Scanner(System.in);

		try {
			if (!session.getTransaction().isActive()) {
				transaction = session.beginTransaction();
			}

			// Get owner input for booking ID
			System.out.print("Enter Booking ID: ");
			String bookingId = sc.nextLine();

			// Retrieve the Booking based on bookingId
			Booking booking = session.get(Booking.class, bookingId);

			if (booking == null) {
				System.out.println("Booking not found with ID: " + bookingId);
				return;
			}

			// Update the booking status to booked (you may have a 'status' field in your
			// Booking entity)
			booking.setStatus("Booked");
			Property prop = booking.getProperty();
			prop.setStatus("Booked");

			prop.setBookings(booking);

			// Update the booking in the database
			session.saveOrUpdate(booking);
			session.saveOrUpdate(prop);

			transaction.commit();

			System.out.println("BOOKING CONFIRMED SUCCESSFULLY!!");
			System.out.println();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}
}
