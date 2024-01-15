package com.realestate.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.realestate.entity.Property;
import com.realestate.entity.User;

public class PropertyDAO {

	private Session session;

	public PropertyDAO(Session session) {
		super();
		this.session = session;
	}

	public String fetchLastAddedPropertyId() {
		Object propertyId = session.createQuery("select max(s.propertyId) from Property s").getSingleResult();
		return String.valueOf(propertyId);
	}

	// Method to save property details
	public void saveProperty(Property property) {
		Transaction transaction = null;
		try {
			if (!session.getTransaction().isActive()) {
				transaction = session.beginTransaction();
			}

			// to generate the custom id
			String pId = fetchLastAddedPropertyId();

			if (pId.contains("null")) {
				pId = "P100";
			}

			String prefix = pId.substring(0, 1); // P
			int postfix = Integer.parseInt(pId.substring(1)); // 102
			String propId = prefix + (postfix + 1); // P + (102+1) = P + 103 = U103

			property.setPropertyId(propId);

			session.save(property);

			transaction.commit();

			System.out.println("Property details saved successfully!!");
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}

	}

	//Method to find the available property by type
	public List<Property> getAvailablePropertiesByType(String type) {
		session.beginTransaction();
		List<Property> properties = session
				.createQuery(
						"from Property p where p.type = :type and p.status = 'Available' and not exists "
								+ "(select b from Booking b where b.property = p and b.status = 'pending')",
						Property.class)
				.setParameter("type", type).getResultList();

		session.getTransaction().commit();

		return properties;
	}

	//Method to get property by id
	public Property getPropertyById(String id) {
		return session.get(Property.class, id);
	}

	//Method to show user feedback to the owner
	public void showFeedbackToOwner(String propId, String feedback) {
		// Fetch the property using the propId
		Property property = session.get(Property.class, propId);

		// Check if the property is not null
		if (property != null) {
			// Assuming there is a method to get the owner of the property
			String ownerName = property.getOwnerName();

			// Display feedback to the property owner (you can customize this part)
			System.out.println("Feedback for your property (Property ID: " + propId + "):");
			System.out.println("Feedback: " + feedback);
			System.out.println("Owner Name: " + ownerName);
		} else {
			System.out.println("Property not found with ID: " + propId);
		}
	}

	public List<Property> getPropertyOwnedByUser(User user) {
		return session.createQuery("from Property where user=:u", Property.class).setParameter("u", user)
				.getResultList();
	}

	// Update Property Details Method
	public Property updatePropertyById(String propId, Property prop) {

		Transaction transaction = null;
		try {
			if (!session.getTransaction().isActive()) {
				transaction = session.beginTransaction();
			}

			Property exist = session.get(Property.class, propId);

			exist.setPrice(prop.getPrice());
			exist.setContactNo(prop.getContactNo());
			session.saveOrUpdate(exist);

			transaction.commit();

			System.out.println("Property details saved successfully!!");

			return exist;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}

		return null;
	}

}
