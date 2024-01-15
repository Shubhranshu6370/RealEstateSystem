package com.realestate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.realestate.dao.BookingDAO;
import com.realestate.dao.PropertyDAO;
import com.realestate.entity.Booking;
import com.realestate.entity.Property;
import com.realestate.entity.User;

public class PropertyCRUD {

	SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
	Session session = factory.openSession();

	Scanner sc = new Scanner(System.in);

	PropertyDAO propDao = new PropertyDAO(session);
	BookingDAO bookDao = new BookingDAO(session);

	// To save/upload owner property method
	public void saveProperty(User user) {
		String type = null;
		System.out.println();

		System.out.println("Enter Property Owner Name:");
		String ownerName = sc.nextLine();

		System.out.println("Enter Description:");
		String description = sc.nextLine();

		System.out.println("Enter Address:");
		String address = sc.nextLine();

		System.out.println("Enter City:");
		String city = sc.nextLine();

		System.out.println("Enter Zipcode:");
		String zipcode = sc.nextLine();

		System.out.println("Enter Price:");
		int price = sc.nextInt();
		sc.nextLine();

		System.out.println("Enter SquareFeet:");
		int squareFeet = sc.nextInt();
		sc.nextLine();

		System.out.println("Enter Number of Bedroom:");
		int bedroom = sc.nextInt();
		sc.nextLine();

		System.out.println("Enter Number of Bathroom:");
		int bathroom = sc.nextInt();
		sc.nextLine();

		System.out.println("Enter Property Type:");
		System.out.println("1) Rent \n2) Sell");
		int typeChoice = sc.nextInt();
		if (typeChoice == 1) {
			type = "Rent";
		} else if (typeChoice == 2) {
			type = "Buy";
		}

		sc.nextLine();

		System.out.println("Enter your ContactNo:");
		String contactNo = sc.nextLine();

		Property property = new Property(ownerName, description, address, city, zipcode, price, squareFeet, bedroom,
				bathroom, "Available", type, contactNo);

		property.setUser(user);
		propDao.saveProperty(property);
	}

	// Method to view property by type
	public void viewPropertyByType() {
		List<Property> properties = new ArrayList<>();
		System.out.println("Enter the type of property you are looking for:");
		System.out.println("1) Rent \n2) Buy");
		String type;
		int choice = sc.nextInt();

		if (choice == 1) {
			type = "Rent";
			properties = propDao.getAvailablePropertiesByType(type);
		} else if (choice == 2) {
			type = "Buy";
			properties = propDao.getAvailablePropertiesByType(type);
		}

		if (properties.isEmpty()) {
			System.out.println(
					"Sorry! but there are currently no properties available. Please try again or check back later for updates.");
		} else {
			for (Property prop : properties) {
				System.out.println();
				System.out.println("+----------------------------------------");
				System.out.println("|");
				System.out.println("+--- Property Id: " + prop.getPropertyId());
				System.out.println("+--- Owner Name: " + prop.getOwnerName());
				System.out.println("+--- Description: " + prop.getDescription());
				System.out.println("+--- Address: " + prop.getAddress());
				System.out.println("+--- City: " + prop.getCity());
				System.out.println("+--- Price: " + prop.getPrice());
				System.out.println("+--- ContactNo: " + prop.getContactNo());
				System.out.println();
			}
		}
	}

	// Book/Rent property method
	public void bookProperty(User user) {
		System.out.println("Enter property id to rent: ");
		String propId = sc.next();

		System.out.println("Enter check in date:");
		String checkIn = sc.next();

		System.out.println("Enter check out date:");
		String checkOut = sc.next();

		Property prop = propDao.getPropertyById(propId);

		Booking booking = new Booking(LocalDate.parse(checkIn), LocalDate.parse(checkOut), "pending", prop, user);

		bookDao.saveBooking(booking);

	}

	// Method to buy  a property 
	public void buyProperty(User user) {
		System.out.println("Enter property id to Buy: ");
		String propId = sc.next();

		// Fetch user details
		String firstName = user.getFirstName();
		String lastName = user.getLastName();

		Property prop = propDao.getPropertyById(propId);

		if (prop != null) { // Check if prop is not null
			LocalDate bookingDate = LocalDate.now();

			Booking booking = new Booking("pending", bookingDate, prop, user);
			bookDao.saveBooking(booking);

			// Print details
			System.out.println();
			System.out.println("+------- PROPERTY DETAILS:");
			System.out.println("|");
			System.out.println("+--- Property ID: " + prop.getPropertyId());
			System.out.println("+--- Owner Name: " + prop.getOwnerName());
			System.out.println("+--- Address: " + prop.getAddress());
			System.out.println("+--- City: " + prop.getCity());
			System.out.println("+--- Price: " + prop.getPrice());
			System.out.println("+--- ContactNo: " + prop.getContactNo());
			System.out.println("|");
			System.out.println(
					"+--- YOUR BOOKING IS PENDING! 'PLEASE WAIT FOR YOUR BOOKING CONFIRMATION FROM THE OWNER'S SIDE, THE OWNER WILL CONTACT YOU SOON!' ");
		} else {
			System.out.println("Error: Property with ID " + propId + " not found.");
			// Handle the case when prop is null, maybe throw an exception or log an error.
		}
	}

	//Method to update property
	public void updateProperty(String propId) {
		String type = null;
		System.out.println();

		System.out.println("Enter Property Owner Name:");
		String ownerName = sc.nextLine();

		System.out.println("Enter Description:");
		String description = sc.nextLine();

		System.out.println("Enter Address:");
		String address = sc.nextLine();

		System.out.println("Enter City:");
		String city = sc.nextLine();

		System.out.println("Enter Zipcode:");
		String zipcode = sc.nextLine();

		System.out.println("Enter Price:");
		int price = sc.nextInt();
		sc.nextLine();

		System.out.println("Enter SquareFeet:");
		int squareFeet = sc.nextInt();
		sc.nextLine();

		System.out.println("Enter Number of Bedroom:");
		int bedroom = sc.nextInt();
		sc.nextLine();

		System.out.println("Enter Number of Bathroom:");
		int bathroom = sc.nextInt();
		sc.nextLine();

		System.out.println("Enter Property Type:");
		System.out.println("1) Rent \n2) Sell");
		int typeChoice = sc.nextInt();
		if (typeChoice == 1) {
			type = "Rent";
		} else if (typeChoice == 2) {
			type = "Buy";
		}

		sc.nextLine();

		System.out.println("Enter your ContactNo:");
		String contactNo = sc.nextLine();

		Property property = new Property(ownerName, description, address, city, zipcode, price, squareFeet, bedroom,
				bathroom, "Available", type, contactNo);

		propDao.updatePropertyById(propId, property);
	}

}
