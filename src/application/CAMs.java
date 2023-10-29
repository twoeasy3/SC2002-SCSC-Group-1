package application;
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
		User activeUser = new Student("ERRORUSER","ERROR@","ERR","ERROR");
		
		
		while (!userFound) {
			System.out.println("Please enter your username to login: ");
			response = sc.nextLine();
			activeUser = getUserObject(response,schoolList);
            if (activeUser.getName().equals("ERRORUSER")) {
                System.out.println("User not found!");} else {
                    userFound = true;}
        }
		System.out.println("Please enter your password: ");
		response = sc.nextLine();
		
		if (activeUser.matchPass(response)) {
			System.out.println("Welcome, " + activeUser.getName() + " (" + activeUser.getFaculty() + ")!");
		}
		else {
			System.out.println("Password Incorrect");
		}

		activeUser.printMenu();
		int choice = sc.nextInt();
		switch(choice){
			case 1:
				activeUser.viewCamps(campList);
				break;
			case 2:
				activeUser.viewOwnedCamps(campList);
				break;
			case 3:
				if (!activeUser.checkStaff()){
					List<Camp> eligibleCamps;
					eligibleCamps = activeUser.signUpCamp(campList,signupList);}

			default:
				System.out.println("Invalid Choice");
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
        return new Student("ERRORUSER","ERROR@","ERR","ERROR");
		}
	}
	

