package com.realestate;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.realestate.dao.BookingDAO;
import com.realestate.dao.PropertyDAO;
import com.realestate.dao.ReviewDAO;
import com.realestate.dao.UserDAO;
import com.realestate.entity.Booking;
import com.realestate.entity.Property;
import com.realestate.entity.User;

public class UserCRUD {

	SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
	Session session = factory.openSession();

	Scanner sc = new Scanner(System.in);

	UserDAO userDao = new UserDAO(session);
	PropertyCRUD propCrud = new PropertyCRUD();
	BookingDAO bookDao = new BookingDAO(session);
	ReviewCRUD reviewCrud = new ReviewCRUD();
	ReviewDAO revDao = new ReviewDAO(session);
	PropertyDAO propertyDAO = new PropertyDAO(session);

	// Assuming you have a login method in your UserDAO class
	public void loginforUser() {
		System.out.println("************** LOGIN ************");
		System.out.println("Enter Email:");
		String emailLogin = sc.next();
		System.out.println("Enter Password:");
		String passwordLogin = sc.next();

		// Call the login method
		User loggedInUser = userDao.login(emailLogin, passwordLogin);

		if (loggedInUser != null) {
			System.out.println("Login successful!");

			if (loggedInUser.getTypeOfUser().equalsIgnoreCase("user")) {
				userMenu(loggedInUser);
			}
			if (loggedInUser.getTypeOfUser().equalsIgnoreCase("owner")) {
				ownerMenu(loggedInUser);
			}

		} else {
			System.out.println("Login failed. Invalid email or password.");
		}

	}

	// Create a new account for buyer
	public void createBuyerAccount() {
		System.out.println("************** Create New Account ************");

		System.out.println("Enter your First Name:");
		String firstName = sc.next();
		System.out.println("Enter your Last Name:");
		String lastName = sc.next();
		System.out.println("Enter your Email:");
		String emailSignUp = sc.next();
		System.out.println("Enter your Password:");
		String passwordSignUp = sc.next();
		System.out.println("Enter your Phone Number:");
		String phoneNumber = sc.next();

		// Call the signUp method with user input
		User newUser = userDao.signUpForUser(firstName, lastName, emailSignUp, passwordSignUp, phoneNumber);
		if (newUser != null) {
			System.out.println("Account created successfully!");
		} else {
			System.out.println("Account creation failed. Please try again.");
		}

	}

	// Create a new account for seller
	public void createSellerAccount() {
		System.out.println("************** Create New Account ************");

		System.out.println("Enter your First Name:");
		String firstName = sc.next();
		System.out.println("Enter your Last Name:");
		String lastName = sc.next();
		System.out.println("Enter your Email:");
		String emailSignUp = sc.next();
		System.out.println("Enter your Password:");
		String passwordSignUp = sc.next();
		System.out.println("Enter your Phone Number:");
		String phoneNumber = sc.next();

		// Call the signUp method with user input
		User newUser = userDao.signUpForOwner(firstName, lastName, emailSignUp, passwordSignUp, phoneNumber);
		if (newUser != null) {
			System.out.println("Account created successfully!");
		} else {
			System.out.println("Account creation failed. Please try again.");
		}

	}

	// ============================ USER-MENU =============================================
	public void userMenu(User user) {
		int ch;
		do {
			System.out.println();
			System.out.println("==================[ USER MENU ]======================");
			System.out.println();
			System.out.println(
					"1) View Property \n2) Buy a Property \n3) Book a Property for Rent \n4) Check Booking Status \n5) Give Feedback \n6) View Profile \n7) Log Out");
			ch = sc.nextInt();
			sc.nextLine();
			switch (ch) {
			case 1:
				propCrud.viewPropertyByType();
				break;

			case 2:
				propCrud.buyProperty(user);
				break;

			case 3:
				System.out.println();
				propCrud.bookProperty(user);
				System.out.println(
						"YOUR BOOKING IS PENDING! 'PLEASE WAIT FOR YOUR BOOKING CONFIRMATION FROM THE OWNER'S SIDE, THE OWNER WILL CONTACT YOU SOON!'");
				break;

			case 4:
				String bookingStatus = bookDao.checkBookingStatus(user);
				System.out.println("+--------- BOOKING STATUS ------------------------");
				System.out.println(bookingStatus);
				break;

			case 5:
				reviewCrud.reviewProperty(user);
				break;

			}
		} while (ch != 7);
	}

	public void viewProfile(User user) {
		// Display user details
		System.out.println("User Id: " + user.getUserId());
		System.out.println("First Name: " + user.getFirstName());
		System.out.println("Last Name: " + user.getLastName());
		System.out.println();

		// Check if the user has any properties
		List<Property> properties = propertyDAO.getPropertyOwnedByUser(user);

		if (properties.isEmpty()) {
			System.out.println("You have no properties uploaded.");
		}

		for (Property prop : properties) {
			System.out.println("Property ID : " + prop.getPropertyId());
			System.out.println("Property OwnerName : " + prop.getOwnerName());
		}
	}

	// Helper method to get a property by propertyId
	public Property getPropertyById(User user, String propertyId) {
		for (Property property : user.getProperties()) {
			if (property.getPropertyId().equals(propertyId)) {
				return property;
			}
		}
		return null; // Property not found
	}

	// ================================ OWNER-MENU
	// =============================================
	public void ownerMenu(User user) {
		int ch;
		do {
			System.out.println();
			System.out.println("==================[ OWNER MENU ]======================");
			System.out.println();
			System.out.println(
					"1) Save Property \n2) Show Bookings \n3) Confirm Bookings \n4) View Profile \n5) Property Reviews \n6) Update Property Details \n7) Log Out");

			// Input validation
			while (!sc.hasNextInt()) {
				System.out.println("Invalid input. Please enter a valid integer.");
				sc.next(); // Consume the invalid input
			}

			ch = sc.nextInt();
			sc.nextLine();
			switch (ch) {
			case 1:
				propCrud.saveProperty(user);
				break;

			case 2:
				bookDao.showBooking(user);
				break;

			case 3:
				bookDao.confirmBooking();
				break;

			case 4:
				viewProfile(user);
				break;

			case 5:
				System.out.println("+--------- PROPERTY REVIEWS ------------------------");
				revDao.showReview(user.getUserId());
				break;

			case 6:
				System.out.println("Enter property ID to update: ");
				String propId = sc.next();

				boolean found = false;

				// Validate if the entered property ID belongs to the owner
				List<Property> properties = propertyDAO.getPropertyOwnedByUser(user);
				for (Property prop : properties) {
					if (prop.getPropertyId().equalsIgnoreCase(propId)) {
						propCrud.updateProperty(propId);

						found = true;
						break;
					}

				}

				if (found == false) {
					System.out.println("This property Id does not belong to you!");
				}

				break;

			}

		} while (ch != 7);
	}

}