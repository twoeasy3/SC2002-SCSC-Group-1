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
		Fetcher.populateCommittees(schoolList,campList);
		List<Signup> signupList = DataHandler.getSignups(schoolList, campList);
		List<Enquiry> enquiryList = DataHandler.getEnquiries(schoolList,campList);
		List<Suggestion> suggestionList = DataHandler.getSuggestions(schoolList,campList);
		Scanner sc = new Scanner(System.in);
		boolean quitCAMs = false;
		while (!quitCAMs) {
			User activeUser = resolveLogin(schoolList); //login user

			boolean activeSession = true;
			while (activeSession) {
				activeUser.printMenu(campList);
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
						campList = CampListView.sortCampList(campList,response);
						activeUser.viewCamps(campList , enquiryList);
						break;
					case 3:
						campList = CampListView.sortCampList(campList,response);
						activeUser.viewOwnedCamps(campList, signupList,schoolList);
						break;
					case 4:
						if (activeUser instanceof Student) {
							campList = CampListView.sortCampList(campList,response);
							((Student) activeUser).signUpCamp(campList, signupList);
						}
						break;
					case 5:
						if (activeUser instanceof Staff) {
							campList.add(((Staff) activeUser).createCamp());
							DataHandler.saveCamps(campList);
							System.out.println("Camp successfully created!");

						}
						if (activeUser instanceof Student) {
							System.out.println("Welcome to Enquiries Hub");
							System.out.println("[1]: Manage my Enquiries");
							System.out.println("[2]: Back");
							choice = sc.nextInt();
							switch (choice) {
								case 1:

								}
							}
						break;
					case 6:
						if (activeUser instanceof Staff){
							((Staff) activeUser).adminMenu(campList,activeUser,suggestionList);
						}
						else if (activeUser instanceof StudentCommittee && ((StudentCommittee) activeUser).getCamp()!= null){
							((StudentCommittee) activeUser).adminMenu(campList,activeUser,suggestionList);
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

	/**
	 *
	 * @param schoolList
	 * @return
	 */
	public static User resolveLogin(List<User> schoolList){
		String response;
		boolean userFound = false;
		User activeUser = null;
		Scanner sc = new Scanner(System.in);


		while (!userFound) {
			System.out.println("Please enter your username to login: ");
			response = sc.nextLine();
			activeUser = Fetcher.getUserFromID(response,schoolList);
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
}
	

