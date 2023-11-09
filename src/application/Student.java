package application;
import enquiry.Enquiry;
import helper.Console;
import helper.InputChecker;
import suggestions.Suggestion;

import java.util.ArrayList;
import java.util.List;


public class Student extends User implements StudentCampOptions{
	int committee; //-1 if not committee member, otherwise put camp.id

	private List<Enquiry>  enquiryList= new ArrayList<>();
	
	public Student(String name, String id, String faculty,String password,int committee) {
		super();
		this.fillDetails(name, id, faculty, password);
		this.committee = committee;
	}

	public int getCommittee(){return this.committee;}
	public void setCommittee(int campID){this.committee = campID;}

	public List<Enquiry> getEnquiryList() {
		return enquiryList;
	}
	public void addEnquiry(Enquiry enquiry){
		enquiryList.add(enquiry);
	}

	public void printMenu(List<Camp> campList){
		StudentView.printMenu(this,campList);
	}

	public SessionStatus resolveCAMsMenu(int choice, String argument,
										 List<User> userList,
										 List<Camp> campList,
										 List<Signup> signupList,
										 List<Enquiry> enquiryList,
										 List<Suggestion> suggestionList){
		return StudentView.resolveCAMsMenu(this, choice, argument,
				userList,campList,signupList,enquiryList,suggestionList);
	}

	public void viewCamps(List<Camp> campList, List<Enquiry> enquiryList){
		StudentView.viewCamps(this,campList,enquiryList);
	}

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

	public void signUpView(List<User> userList, List<Camp> campList, List<Signup> signupList) { //TODO disallow when camp is full & clashes
		System.out.println("Showing events you are eligible for as a student of " + this.getFaculty() + " and have never signed up for...");
		List<Camp> eligibleCamps = StudentCampOptions.getEligibleCamps(this,campList,signupList);
		if (eligibleCamps == null || eligibleCamps.size() == 0) {
			System.out.println("No camps are currently open for you :(");
			return;
		}
		
		boolean endLoop = false;
		String listMenu = CampListView.createNumberedCampList(eligibleCamps,this);
		while(!endLoop){
			System.out.println("Showing all camps created by you:");
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
					input = InputChecker.parseUserBoolInput(response.substring(0));
					response = InputChecker.resolveArgument(response);
				}
				if (input == 1 && response.equals("c") && this.getCommittee() == -1){
					StudentCampOptions.joinCommittee(this,selectedCamp, userList,campList);
					System.out.println("Successfully signed up as committee!");
				}
				if (input == 1) {
					StudentCampOptions.joinCamp(this,selectedCamp,signupList);
					System.out.println("Successfully signed up as attendee!");
					endLoop = true;
				} else {
					System.out.println("Backing out and showing you all eligible camps again...");
				}
			}
		}
	}
}

