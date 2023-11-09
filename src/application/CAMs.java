package application;
import enquiry.Enquiry;
import helper.Console;
import helper.DataHandler;
import helper.Fetcher;
import helper.InputChecker;
import suggestions.Suggestion;

import java.util.List;

enum SessionStatus { CONTINUE,LOGOUT,CLOSE}
public class CAMs {
	/**
	 * CAMs main interface. Initialises the program state and provides login for user
	 *
	 * @param args Unused.
	 */
	public static void main(String[] args) {

		List<User> userList = DataHandler.getUsers();
		List<Camp> campList = DataHandler.getCamps();
		Fetcher.populateCommittees(userList, campList);
		List<Signup> signupList = DataHandler.getSignups(userList, campList);
		List<Enquiry> enquiryList = DataHandler.getEnquiries(userList, campList);
		List<Suggestion> suggestionList = DataHandler.getSuggestions(userList, campList);
		
		SessionStatus sStatus = SessionStatus.LOGOUT;
		while (sStatus == SessionStatus.LOGOUT) {
			User activeUser = resolveLogin(userList); //login user
			sStatus = SessionStatus.CONTINUE;
			while (sStatus == SessionStatus.CONTINUE) {
				activeUser.printMenu(campList);
				//This entire portion allows for arguments in the options like 2-f
				String response = Console.nextString();
				int choice;
				String argument;
				try {
					choice = Integer.parseInt(response.substring(0, 1));
					argument = InputChecker.resolveArgument(response);
				} catch (NumberFormatException e) {
					choice = 619;
					argument = "";
				}
				sStatus = activeUser.resolveCAMsMenu(choice,argument,userList,campList,signupList,enquiryList,suggestionList);

			}
		}
	}

	/**
	 *
	 * @param userList
	 * @return
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
	

