package com.realestate;

import java.util.Scanner;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.realestate.dao.UserDAO;
import com.realestate.entity.User;

public class UserMainApp {

	public static void main(String[] args) {

		try {

			UserCRUD usercrud = new UserCRUD();

			Scanner sc = new Scanner(System.in);

			int choice;
			int ch;

			// Using do while loop
			do {
				System.out.println();
				System.out.println("WELCOME TO REAL-ESTATE-SYSTEM");
				System.out.println();
				System.out.println("1) Create an Account \n2) LogIn \n3) Exit");
				choice = sc.nextInt();
				switch (choice) {
				case 1:
					System.out.println("Press 1) Create Buyer A/C 2) Create Seller A/C 3) Back");
					ch = sc.nextInt();
					if (ch == 1) {
						// buyer account
						usercrud.createBuyerAccount();
					} else if (ch == 2) {
						// seller account
						usercrud.createSellerAccount();
					} else if (ch == 3) {
						break;
					} else
						System.out.println("Wrong Input!!");
					break;

				case 2:
					usercrud.loginforUser();
					break;

				case 3:
					System.out.println("Thank you for visiting!!");
					System.exit(0);
					break;

				default:
					System.out.println("Wrong Input!!");

				}
			} while (true);

		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
