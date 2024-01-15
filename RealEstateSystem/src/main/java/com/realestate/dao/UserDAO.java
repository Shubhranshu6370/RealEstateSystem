package com.realestate.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.realestate.entity.Property;
import com.realestate.entity.User;

public class UserDAO {

	private Session session;

	public UserDAO(Session session) {
		super();
		this.session = session;
	}

	public String fetchLastAddedId() {
		Object userId = session.createQuery("select max(s.userId) from User s").getSingleResult();
		return String.valueOf(userId);
	}

	// method to create an account for normal users
	public User signUpForUser(String firstName, String lastName, String email, String password, String phoneNumber) {
		Transaction transaction = null;
		User newUser = null;
		try {
			if (!session.getTransaction().isActive()) {
				transaction = session.beginTransaction();
			}

			// to generate the custom id
			String userId = fetchLastAddedId();

			if (userId.contains("null")) {
				userId = "U100";
			}

			String prefix = userId.substring(0, 1); // U
			int postfix = Integer.parseInt(userId.substring(1)); // 102
			String uId = prefix + (postfix + 1); // U + (102+1) = U + 103 = U103

			newUser = new User(uId, firstName, lastName, email, password, phoneNumber, "User");

			// Saving the new user to the database
			session.save(newUser);

			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return newUser;
	}

	// method to create an account for normal users
	public User signUpForOwner(String firstName, String lastName, String email, String password, String phoneNumber) {
		Transaction transaction = null;
		User newUser = null;
		try {
			if (!session.getTransaction().isActive()) {
				transaction = session.beginTransaction();
			}

			// to generate the custom id
			String userId = fetchLastAddedId();

			if (userId.contains("null")) {
				userId = "U100";
			}

			String prefix = userId.substring(0, 1); // U
			int postfix = Integer.parseInt(userId.substring(1)); // 102
			String uId = prefix + (postfix + 1); // U + (102+1) = U + 103 = U103

			newUser = new User(uId, firstName, lastName, email, password, phoneNumber, "Owner");

			// Saving the new user to the database
			session.save(newUser);

			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return newUser;
	}

	
	// Method to login
	public User login(String email, String password) {
	    User user = null;
	    try {
	        user = session.createQuery("from User where email= :e and password= :p", User.class)
	                .setParameter("e", email).setParameter("p", password).getSingleResult();
	    } catch (NoResultException e) {
	        // Handle the case when no user is found
	        // For now, we set user to null, but you might want to log the error or take other actions
	    }
	    return user;
	}


	// update user information
	public void updateUserInfo(User user) {
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			// Merge the user object with the current session
			session.merge(user);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		}
	}
	
	
	// Method to update property details for property owners
    public void updateAccount(User user, String newAddress, double newPrice) {
        Transaction transaction = null;
        try {
            if (!session.getTransaction().isActive()) {
                transaction = session.beginTransaction();
            }

            // Check if the user is an owner
            if ("Owner".equals(user.getTypeOfUser())) {
                // Assuming there's a list of properties associated with the owner
                List<Property> properties = user.getProperties();

                // Check if the list is not null or empty
                if (properties != null && !properties.isEmpty()) {
                    // For simplicity, assuming the first property in the list is the one to be updated
                    Property property = properties.get(0);

                    // Update the property details
                    property.setAddress(newAddress);
                    property.setPrice(0);

                    // Merge the updated property object with the current session
                    session.merge(property);

                    transaction.commit();
                } else {
                    System.out.println("Owner does not have associated properties. Cannot update property details.");
                }
            } else {
                System.out.println("User is not a property owner. Cannot update property details.");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


	// Delete user account
	public void deleteUser(User user) {
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.delete(user);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	// method to fetch User details using email
	public User findByEmail(String userEmail) {
		try {
			User user = session.createQuery("from User where email = :em", User.class).setParameter("em", userEmail)
					.getSingleResult();

			return user;
			
		} catch (Exception e) {
			System.out.println("User details not found!!");
		}

		return null;
	}
	
	
	// Check if the user owns a specific property
	public boolean isPropertyOwner(String userId, String propertyId) {
	    Transaction transaction = null;
	    try {
	        // Assuming you have a method to fetch the property by ID
	        Property property = getPropertyById(Integer.parseInt(userId), Integer.parseInt(propertyId));

	        // Check if the property exists and belongs to the user
	        if (property != null && property.getPropertyId().equals(userId)) {
	            return true;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	// Helper method to get a property by propertyId
	public Property getPropertyById(int userId, int propertyId) {
	    // Assuming you have a method to fetch the user by ID
	    User user = getUserById(userId);

	    if (user != null) {
	        for (Property property : user.getProperties()) {
	            if (Integer.parseInt(property.getPropertyId()) == propertyId) {
	                return property;
	            }
	        }
	    }
	    return null; // Property not found
	}
	
	   // Assuming you have a method to fetch a user by ID
    public User getUserById(int userId) {
        try {
            User user = session.get(User.class, userId);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

















