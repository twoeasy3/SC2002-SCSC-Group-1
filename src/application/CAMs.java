package application;
import java.util.List;
import java.util.Scanner;
public class CAMs {

	public static void main(String[] args) {
		
		List<User> schoolList = DataHandler.Initialise();
		List<Camp> campList = DataHandler.getCamps();
		Scanner sc = new Scanner(System.in);
		String response;
		boolean userFound = false;
		User activeUser = new Student("ERRORUSER","ERROR@","ERR","ERROR");
		
		
		while (!userFound) {
			System.out.println("Please enter your username to login: ");
			response = sc.nextLine();
			activeUser = getUserObject(response,schoolList);
			if (!activeUser.getName().equals("ERRORUSER")) {
				userFound = true;}
			else {
				System.out.println("User not found!");}
		}
		System.out.println("Please enter your password: ");
		response = sc.nextLine();
		
		if (activeUser.matchPass(response)) {
			System.out.println("Welcome, " + activeUser.getName() + " (" + activeUser.getFaculty() + ")!");
		}
		else {
			System.out.println("Password Incorrect");
		}
		
		if (activeUser.checkStaff()) {
			System.out.println("Staff Portal");
			System.out.println("You have XX new queries."); //TODO 	A staff can view and reply to enquiries from students to the camp(s) his/her has created
			System.out.println("1. New Camp.");
			System.out.println("2. Edit your camp.");
			System.out.println("3. View all existing camps.");
			System.out.println("4. View camp queries.");
			System.out.println("5. View or approve committee suggestions");	
		}
		
		else{
			System.out.println("Student Portal");
			System.out.println("You are currently a committee member of XX camp."); //TODO
			System.out.println("1. View eligible camps.");
			System.out.println("2. View your signups.");
			System.out.println("3. Enquiry hub");
		}
		
		
	}
	
	public static User getUserObject(String userID, List<User> schoolList) {
		for (User user : schoolList) {;
			if(user.getID().equals(userID)) {
				return user;
			}
		}
		User errorUser = new Student("ERRORUSER","ERROR@","ERR","ERROR");
		return (errorUser);	
		}
	}
	

