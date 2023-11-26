package application;
import enquiry.Enquiry;
import enquiry.EnquiryAbstract;
import helper.Console;
import helper.DataHandler;
import helper.Fetcher;
import helper.InputChecker;
import suggestions.Suggestion;

import java.util.List;

/**
 * Defines some statuses for the current session of CAMs.<br>
 *
 */
enum SessionStatus {
	/**
	 * CONTINUE - Normal state, CAMs continues with current ActiveUser<br>>
	 */
	CONTINUE,
	/**
	 * LOGOUT - Logout requested, CAMs will continue to login screen<br>
	 */
	LOGOUT,
	/**
	 * CLOSE - CAMs will terminate<br>
	 */
	CLOSE}

/**
 * CAMs is the main interface from which the application is built around
 */
public class CAMs {
	/**
	 * CAMs main interface. Initialises the program state and provides login for user
	 *
	 */
	public static void main() {

		List<User> userList = DataHandler.getUsers();
		List<Camp> campList = DataHandler.getCamps();
		Fetcher.populateCommittees(userList, campList);
		List<Signup> signupList = DataHandler.getSignups(userList, campList);
		List<EnquiryAbstract> enquiryList = DataHandler.getEnquiries(userList, campList);
		List<Suggestion> suggestionList = DataHandler.getSuggestions(userList, campList);

		SessionStatus sStatus = SessionStatus.LOGOUT;
		while (sStatus == SessionStatus.LOGOUT) {
			User activeUser = resolveLogin(userList); //login user
			sStatus = SessionStatus.CONTINUE;
			while (sStatus == SessionStatus.CONTINUE) {
				activeUser.printMenu(campList);
				//This entire portion allows for arguments in the options like 2-f
				String response = Console.nextString();
				if(response.length() != 0) {
					int choice;
					String argument;
					try {
						choice = Integer.parseInt(response.substring(0, 1));
						argument = InputChecker.resolveArgument(response);
					} catch (NumberFormatException e) {
						choice = 619;
						argument = "";
					}
					sStatus = activeUser.resolveCAMsMenu(choice, argument, userList, campList, signupList, enquiryList, suggestionList);
				}

			}
		}
	}

	/**
	 * The login screen for CAMs. This is called when CAMs is started or when the status is set to LOGOUT. <br>
	 * Will persist until a successful login. Upon successful login, User.checkForDefaultPass() is called to prompt if User is logging in using "password".
	 * @param userList List of all User objects
	 * @return User object of successful login
	 */
	public static User resolveLogin(List<User> userList){
		String response;
		boolean userFound = false;
		User activeUser = null;
		


		while (!userFound) {
			System.out.println("Please enter your username to login: ");
			response = Console.nextString();
			activeUser = Fetcher.getUserFromID(response,userList);
			if (activeUser == null) {
				System.out.println("User not found!");} else {
				userFound = true;}
		}
		boolean passwordMatch = false;
		while (!passwordMatch) {
			System.out.println("Please enter your password: ");
			response = Console.nextString();

			if (activeUser.matchPass(response)) {
				passwordMatch = true;
				System.out.println("Welcome, " + activeUser.getName() + " (" + activeUser.getFaculty() + ")!");
				activeUser.checkForDefaultPass(userList); //first time login prompt
			} else {
				System.out.println("Password Incorrect");
			}
		}
		
		return activeUser;
	}
}
	

