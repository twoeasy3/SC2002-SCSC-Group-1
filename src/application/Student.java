package application;
import enquiry.Enquiry;
import enquiry.EnquiryAbstract;
import helper.Console;
import helper.InputChecker;
import suggestions.Suggestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Account type for User. Its exclusive complement is Staff.
 * Students exclusively have the rights to join Camps and write enquiries.
 * Students can also sign up to be StudentCommittee, so no Student object should be inititalised as Student. It should be initialied as StudentCommittee and immediately cast up to Student.
 */
public class Student extends User implements StudentCampOptions{
	/**
	 * CampID that the student is a committee member of. -1 if the Student is not a Committee Member
	 */
	int committee; //-1 if not committee member, otherwise put camp.id
	/**
	 * List of all Enquiries that this Student has authored
	 */
	private List<EnquiryAbstract>  enquiryList= new ArrayList<>();

	/**
	 * Standard constructor for Student object.
	 * Calls upon User constructor.
	 * @param name Name of Student
	 * @param id UserID of Student
	 * @param faculty Faculty of Student
	 * @param password Hashed password
	 * @param committee CampID that Student is Committee of
	 */
	public Student(String name, String id, String faculty,String password,int committee) {
		super(name, id, faculty, password);
		this.committee = committee;
	}

	/**
	 * Fetch the CampID that the Student is a committee member of
	 * @return CampID that the Student is committee member of
	 */
	public int getCommittee(){return this.committee;}

	/**
	 * Set the CampID that the Student signed up for as committee
	 * @param campID CampID that the Student signed up for as committee.
	 */
	public void setCommittee(int campID){this.committee = campID;}

	/**
	 * Fetches the list of all Enquiries the Student has authored
	 * @return List of all enquiries that Student has authored
	 */
	public List<EnquiryAbstract> getEnquiryList() {
		return enquiryList;
	}

	/**
	 * Adds an Enquiry to the list of Enquiries that the Student has authored
	 * @param enquiry New Enquiry object to add to the Student's enquiryList
	 */
	public void addEnquiry(EnquiryAbstract enquiry){
		enquiryList.add(enquiry);
	}

	/**
	 * Prints the main menu options for a Student user.<br>
	 * Calls StudentView.printMenu()
	 * @param campList List of all Camp objects
	 */
	public void printMenu(List<Camp> campList){
		StudentView.printMenu(this,campList);
	}

	/**
	 * Used to determine which methods to call when option is given on the CAMs main menu.
	 * @param choice Input integer choice for menu
	 * @param argument Any additional string arguments (used for Camp filters)
	 * @param userList List of all User objects. Passed to other classes.
	 * @param campList List of all Camp objects. Passed to other classes.
	 * @param signupList List of all Signup objects. Passed to other classes.
	 * @param enquiryList List of all Enquiry objects. Passed to other classes.
	 * @param suggestionList List of all Suggestion objects. Passed to other classes.
	 * @return SessionStatus to determine the state CAMs will continue with
	 */
	public SessionStatus resolveCAMsMenu(int choice, String argument,
										 List<User> userList,
										 List<Camp> campList,
										 List<Signup> signupList,
										 List<EnquiryAbstract> enquiryList,
										 List<Suggestion> suggestionList){
		return StudentView.resolveCAMsMenu(this, choice, argument,
				userList,campList,signupList,enquiryList,suggestionList);
	}

	/**
	 * General view menu for Students.
	 * This view shows all Camps of the Student's faculty and those open to ALL.
	 * This view shows Blacklisted and non-Open camps.
	 * @param campList List of all Camp objects.
	 * @param enquiryList List of all Enquiry objects
	 */
	public void viewCamps(List<Camp> campList, List<EnquiryAbstract> enquiryList){
		StudentView.viewCamps(this,campList,enquiryList);
	}

	/**
	 * Used to view the list of camps where the Student has signed up for.
	 * @param campList List of all Camp objects.
	 * @param signupList List of all Signups
	 * @param userList List of all User objects
	 */
	public void viewOwnedCamps(List<Camp> campList,List<Signup> signupList,List<User> userList) { //TODO
		List<Camp> ownedCamps = StudentCampOptions.getOwnedCamps(this, campList, signupList);
		if (ownedCamps.size() == 0) {
			System.out.println("You aren't registered for any camp!");
			return;
		}
		boolean endLoop = false;
		String listMenu = CampListView.createNumberedCampList(ownedCamps, this);
		while (!endLoop) {
			System.out.println("Showing all camps you have signed up for:");
			Camp selectedCamp = CampListView.campFromListSelector(ownedCamps, listMenu);
			if (selectedCamp == null) {
				return;
			} else {
				endLoop = StudentCampOptions.extraSignupOptions(this, selectedCamp , userList, signupList);
			}
		}
	}

	/**
	 * General menu for Camps the Student can sign up for. <br>
	 * The conditions checked for an eligible Camp are as follows:<br>
	 * Same faculty or "ALL"<br>
	 * Not already signed up or Committee<br>
	 * Not blacklisted<br>
	 * Camp registration date not past<br>
	 * Camp still have empty spots.<br>
	 *
	 * @param userList List of all User objects. Used to save user state.
	 * @param campList List of all Camp objects. Used for camp data.
	 * @param signupList List of all Signup objects. Used to save signup state.
	 */
	public void signUpView(List<User> userList, List<Camp> campList, List<Signup> signupList) { //TODO disallow when camp is full & clashes
		System.out.println("Showing events you are eligible for as a student of " + this.getFaculty() + " and have never signed up for...");
		List<Camp> eligibleCamps = StudentCampOptions.getEligibleCamps(this,campList,signupList);
		if (eligibleCamps == null || eligibleCamps.size() == 0) {
			System.out.println("No camps are currently open for you :(");
			return;
		}

		String listMenu = CampListView.createNumberedCampList(eligibleCamps,this);
			System.out.println("Showing all camps you can sign up for:");
			Camp selectedCamp = CampListView.campFromListSelector(eligibleCamps,listMenu);
			if(selectedCamp == null) {
				return;
			}else {
				CampView.showSummary(selectedCamp);
				System.out.printf("Join Camp as attendee? Y/N ");
				if(this.committee == -1){
					System.out.printf("You may join as a committee member instead by adding -c to a Y response.\n");}
				int input = -1;
				String response = "";
				while (input == -1) {
					response = Console.nextString();
					input = InputChecker.parseUserBoolInput(response.substring(0,1));
					response = InputChecker.resolveArgument(response);
				}
				if (input == 1 && response.equals("c") && this.getCommittee() == -1){
					StudentCampOptions.joinCommittee(this,selectedCamp, userList,campList);
					System.out.println("Successfully signed up as committee!");
				}
				else if (input == 1) {
					StudentCampOptions.joinCamp(this,selectedCamp,signupList);
					System.out.println("Successfully signed up as attendee!");
				} else {
					System.out.println("Backing out and showing you all eligible camps again...");
				}
			}
	}
}

