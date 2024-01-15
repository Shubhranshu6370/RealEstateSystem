package com.realestate;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.realestate.dao.PropertyDAO;
import com.realestate.dao.ReviewDAO;
import com.realestate.entity.Property;
import com.realestate.entity.Review;
import com.realestate.entity.User;

public class ReviewCRUD {

	SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
	Session session = factory.openSession();

	Scanner sc = new Scanner(System.in);

	ReviewDAO reviewDao = new ReviewDAO(session);
	PropertyDAO propDao = new PropertyDAO(session);

	//Method to give review to the property from user
	public void reviewProperty(User user) {
		try {
			System.out.println("Enter Property id: ");
			String propId = sc.next();

			// Consume the newline character left in the buffer
			sc.nextLine();

			System.out.println("Feedback: ");
			// Use nextLine() to read the entire line of feedback
			String feedback = sc.nextLine();

			System.out.println("Rating(1/5): ");

			// Check if the next input is an integer
			if (sc.hasNextInt()) {
				// Process the rating here
				int rating = sc.nextInt();

				// Save feedback to the database
				saveFeedbackToDatabase(user, propId, feedback, rating);

				// Remove the unnecessary sc.nextLine() here
			} else {
				// Handle the case where the input is not an integer
				System.out.println("Invalid input. Please enter an integer for the rating.");
			}
		} catch (InputMismatchException e) {
			// Handle the exception if it occurs
			System.out.println("Invalid input. Please enter a valid integer for the rating.");
		}
	}

	//Method to save feedback from user
	public void saveFeedbackToDatabase(User user, String propId, String feedback, int rating) {

		try {
			Property prop = propDao.getPropertyById(propId);

			// Assuming you have a Review entity class with a constructor that takes
			// necessary parameters
			Review review = new Review(rating, feedback, prop, user);

			// Save the review to the database
			reviewDao.reviewProperty(review);

			// Show the feedback to the owner (you may need to implement this method in
			// PropertyDAO)
			propDao.showFeedbackToOwner(propId, feedback);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}