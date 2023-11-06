package application;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class CAMs {
	/**
	 * CAMs main interface. Initialises the program state and provides login for user
	 *
	 * @param args Unused.
	 */
	public static void main(String[] args) {

		List<User> schoolList = DataHandler.getUsers();
		List<Camp> campList = DataHandler.getCamps();
		Helper.populateCommittees(schoolList,campList);
		List<Signup> signupList = DataHandler.getSignups(schoolList, campList);
		Scanner sc = new Scanner(System.in);
		boolean quitCAMs = false;
		while (!quitCAMs) {
			User activeUser = resolveLogin(schoolList); //login user

			boolean activeSession = true;
			while (activeSession) {
				activeUser.printMenu();
				//This entire portion allows for arguments in the options like 2-f
				String response = sc.nextLine();
				int choice = -619;
				try{
					choice = Integer.parseInt(response.substring(0,1));
					response = response.replaceAll("[-\\s]+", "");
					response = response.substring(1);
				}
				catch (NumberFormatException e) {
					choice = 619;

				}
				//////
				switch (choice) {
					case 1:
						activeUser.changePass();
						DataHandler.saveUsers(schoolList);
						break;
					case 2:

						campList = Helper.sortCampList(campList,response);
						activeUser.viewCamps(campList);
						break;
					case 3:
						campList = Helper.sortCampList(campList,response);
						activeUser.viewOwnedCamps(campList, signupList);
						break;
					case 4:
						if (activeUser instanceof Student) {
							campList = Helper.sortCampList(campList,response);
							signupList = ((Student) activeUser).signUpCamp(campList, signupList);
						}
						break;
					case 5:
						if (activeUser instanceof Staff) {
							campList.add(((Staff) activeUser).createCamp());
							DataHandler.saveCamps(campList);
							System.out.println("Camp successfully created!");

						}
						break;
					case 9:
						System.out.println("Logging out from CAMs...");
						activeSession = false;
						break;
					case 0:
						System.out.println("Logging out and terminating CAMs...");
						activeSession = false;
						quitCAMs = true;
						break;

					default:
						System.out.println("Invalid Choice");
						break;
				}
			}

		}
	}
	public static User resolveLogin(List<User> schoolList){
		String response;
		boolean userFound = false;
		User activeUser = null;
		Scanner sc = new Scanner(System.in);


		while (!userFound) {
			System.out.println("Please enter your username to login: ");
			response = sc.nextLine();
			activeUser = getUserObject(response,schoolList);
			if (activeUser == null) {
				System.out.println("User not found!");} else {
				userFound = true;}
		}
		boolean passwordMatch = false;
		while (!passwordMatch) {
			System.out.println("Please enter your password: ");
			response = sc.nextLine();

			if (activeUser.matchPass(response)) {
				passwordMatch = true;
				System.out.println("Welcome, " + activeUser.getName() + " (" + activeUser.getFaculty() + ")!");
				activeUser.checkForDefaultPass(); //first time login prompt
				DataHandler.saveUsers(schoolList);
			} else {
				System.out.println("Password Incorrect");
			}
		}
		
		return activeUser;
	}
	/**
	 * Finds a User object and returns it.
	 * Used in login screen, object is selected as the active user.
	 * @param userID UserID as a String to look for.
	 * @param schoolList List of all Users loaded in by DataHandler.
	 * @return Appropriate User object if matching ID is found. If no user matched, dummy error object is returned.
	 *
	 */
	public static User getUserObject(String userID, List<User> schoolList) {
		for (User user : schoolList) {
			if(user.getID().equals(userID)) {
				return user;
			}
		}
        return null;
		}
	}
	

