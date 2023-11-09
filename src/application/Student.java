package application;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Student extends User implements StudentCampOptions{
	int committee; //-1 if not committee member, otherwise put camp.id

	private ArrayList<Enquiry> enquiryList;
	
	public Student(String name, String id, String faculty,String password,int committee) {
		super();
		this.fillDetails(name, id, faculty, password);
		this.committee = committee;
	}

	public int getCommittee(){return this.committee;}
	public void setCommittee(int campID){this.committee = campID;}

	public ArrayList<Enquiry> getEnquiryList() {
		return enquiryList;
	}

	public sessionStatus resolveCAMsMenu(int choice,String argument,
								List<User> userList,
								List<Camp> campList,
								List<Signup> signupList,
								List<Enquiry> enquiryList,
								List<Suggestion> suggestionList){
		sessionStatus status = sessionStatus.CONTINUE;
		switch(choice) {
			case 1:
				this.changePass(userList);
				return status;
			case 2:
				campList = CampListView.sortCampList(campList,argument);
				this.viewCamps(campList , enquiryList);
				return status;
			case 3:
				campList = CampListView.sortCampList(campList,argument);
				this.viewOwnedCamps(campList, signupList,userList);
				return status;
			case 4:
				campList = CampListView.sortCampList(campList,argument);
				this.signUpCamp(campList, signupList);
				return status;
			case 5:
				//ENQUIRIES HUB
				return status;
			case 6:
				if (this instanceof StudentCommittee && ((StudentCommittee)this).getCamp() != null){
					((StudentCommittee) this).adminMenu(campList,this,suggestionList);
				}
				else{
					System.out.println("Invalid Choice");
				}
				return status;
			case 9:
				System.out.println("Logging out from CAMs...");
				status = sessionStatus.LOGOUT;
				return status;
			case 0:
				System.out.println("Logging out and terminating CAMs...");
				status = sessionStatus.CLOSE;
				return status;

			default:
				System.out.println("Invalid Choice");
				return status;
		}
	}


	public void printMenu(List<Camp> campList){
		StudentView.printMenu(this,campList);
	}

	public void viewCamps(List<Camp> campList, List<Enquiry> enquiryList){
		StudentView.viewCamps(this,campList,enquiryList);
	}

	public void viewOwnedCamps(List<Camp> campList,List<Signup> signupList,List<User> userList) { //TODO
		Scanner sc = new Scanner(System.in);
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
				endLoop = StudentCampOptions.extraViewOptions(this, selectedCamp , userList, signupList);
			}
		}
	}

	public void signUpCamp(List<Camp> campList, List<Signup> signupList) { //TODO disallow when camp is full & clashes
		System.out.println("Showing events you are eligible for as a student of " + this.getFaculty() + " and have never signed up for...");
		List<Camp> eligibleCamps = StudentCampOptions.getEligibleCamps(this,campList,signupList);
		if (eligibleCamps == null || eligibleCamps.size() == 0) {
			System.out.println("No camps are currently open for you :(");
			return;
		}
		Scanner sc = new Scanner(System.in);
		boolean endLoop = false;
		String listMenu = CampListView.createNumberedCampList(eligibleCamps,this);
		while(!endLoop){
			System.out.println("Showing all camps created by you:");
			Camp selectedCamp = CampListView.campFromListSelector(eligibleCamps,listMenu);
			if(selectedCamp == null) {
				return;
			}else {
				CampView.showSummary(selectedCamp);
				System.out.println("Join Camp as attendee? Y/N");
				int input = -1;
				while (input == -1) {
					input = InputChecker.parseUserBoolInput(sc.nextLine());
				}
				if (input == 1) {
					signupList.add(new Signup(this,selectedCamp,true));
					DataHandler.saveSignups(signupList);
					System.out.println("Successfully signed up!");
					endLoop = true;
				} else {
					System.out.println("Backing out and showing you all eligible camps again...");
				}
			}
		}
	}
}

