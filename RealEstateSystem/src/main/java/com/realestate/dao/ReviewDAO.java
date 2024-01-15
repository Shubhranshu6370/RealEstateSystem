package com.realestate.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Scanner;

import com.realestate.entity.Property;
import com.realestate.entity.Review;
import com.realestate.entity.User;

public class ReviewDAO {

	Scanner sc = new Scanner(System.in);
	private Session session;

	public ReviewDAO(Session session) {
		super();
		this.session = session;
	}

	public String fetchLastAddedReviewId() {
		Object reviewId = session.createQuery("select max(f.reviewId) from Review f").getSingleResult();
		return String.valueOf(reviewId);
	}

	// Method to save review details
	public void reviewProperty(Review review) {
		Transaction transaction = null;
		try {
			if (!session.getTransaction().isActive()) {
				transaction = session.beginTransaction();
			}

			// to generate the custom review id
			String rId = fetchLastAddedReviewId();

			if (rId.contains("null")) {
				rId = "R100";
			}

			String prefix = rId.substring(0, 1); // R
			int postfix = Integer.parseInt(rId.substring(1)); // 102
			String revId = prefix + (postfix + 1); // R + (102+1) = R + 103 = R103

			review.setReviewId(revId);

			session.save(review);

			transaction.commit();

			System.out.println("Review  saved successfully!!");
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}

	}

	// Show Review Method
	public void showReview(String userId) {
		try {
			String hql = "from Review r where r.property.user.userId = :userId";
			Query<Review> query = session.createQuery(hql, Review.class);
			query.setParameter("userId", userId);
			List<Review> reviews = query.getResultList();

			if (reviews.isEmpty()) {
				System.out.println("No reviews available for your properties.");
				return;
			}

			// Display reviews
			for (Review review : reviews) {
				Property property = review.getProperty();

//	            System.out.println("+---------------- REVIEWS ----------------------");
				System.out.println("|");
				System.out.println("+--- Property Id: " + property.getPropertyId());
				System.out.println("+--- Review Id: " + review.getReviewId());
				System.out.println("+--- Feedback: " + review.getFeedback());
				System.out.println("+--- Rating: " + review.getRating());
				System.out.println("+--- User Id: " + review.getUser().getUserId());
				System.out.println("+-----------------------------------------------");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
