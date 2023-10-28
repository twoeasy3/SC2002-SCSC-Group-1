package application;
import java.util.List;
import exceptions.UserNotFoundException;
import java.util.Scanner;
public class CAMs {

	public static void main(String[] args) {
		
		List<User> schoolList = DataHandler.Initialise();
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
			System.out.println("Password Correct");
		}
		else {
			System.out.println("Password Incorrect");
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
	

