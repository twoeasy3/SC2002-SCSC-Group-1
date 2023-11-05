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
		List<Signup> signupList = DataHandler.getSignups(schoolList,campList);
		Scanner sc = new Scanner(System.in);
		String response;
		boolean userFound = false;
		User activeUser = null;
		
		
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
		boolean quitCAMs = false;
		while(!quitCAMs) {
			activeUser.printMenu();
			int choice = sc.nextInt();
			switch (choice) {
				case 1:
					activeUser.changePass();
					DataHandler.saveUsers(schoolList);
					break;
				case 2:
					activeUser.viewCamps(campList);
					break;
				case 3:
					activeUser.viewOwnedCamps(campList,signupList);
					break;
				case 4:
					if (activeUser instanceof Student) {
						signupList = ((Student) activeUser).signUpCamp(campList, signupList);
					}
					break;
				case 5:
					if (activeUser instanceof Staff){
						campList.add(((Staff) activeUser).createCamp());
						DataHandler.saveCamps(campList);
						System.out.println("Camp successfully created!");

					}
					break;
				case 0:
					System.out.println("Quitting CAMs...");
					quitCAMs = true;
					break;

				default:
					System.out.println("Invalid Choice");
					break;
			}
		}
		
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
	

