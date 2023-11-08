package application;
import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Student extends User{ 
	int committee; //-1 if not committee member, otherwise put camp.id
	
	public Student(String name, String id, String faculty,String password,int committee) {
		super();
		this.fillDetails(name, id, faculty, password);
		this.committee = committee;
	}

	public int getCommittee(){return this.committee;}
	public boolean checkStaff() {
		return false;
	}

	public void printMenu(List<Camp> campList){
		System.out.println("Student Portal");
		if(this.getCommittee() == -1){
			System.out.println("You are not currently a committee member of any camp.");}
		else{
			System.out.println("You are currently a committee member of " + Helper.getCampfromID(this.committee, campList).getName());//dangerous error possible
		}
		System.out.println("1. Change your password.");
		System.out.println("2. View eligible camps. (-o,-l,-s,-r,-p,-f)");
		System.out.println("3. View your signups.");
		System.out.println("4. Sign up for camp. (-o,-l,-s,-r,-p,-f)" );
		System.out.println("5. Camp Enquiry Hub");
		System.out.println("6. Camp Committee Hub");
		System.out.println("9. Log out");
		System.out.println("0. Terminate CAMs");
	}
	public void viewCamps(List<Camp> campList, List<Enquiry> enquiryList){
		List<Camp> eligibleCamps = new ArrayList<>();
		for (Camp camp : campList){
			if(camp.checkEligibility(this.getFaculty()) && camp.isVisible()){
				eligibleCamps.add(camp);
			}
		}
		String listMenu = Helper.createNumberedCampList(eligibleCamps,this);
		boolean endLoop = false;
		while (!endLoop){
			System.out.println(listMenu);
			Scanner sc = new Scanner(System.in);
			System.out.println("0: Back to CAMs main menu ");
			System.out.println("Enter the number corresponding to the camp to view more: ");
			String response = sc.nextLine();
			if (Helper.checkInputIntValidity(response)) {
				int selection = Integer.parseInt(response);
				if (selection < 0 || selection > eligibleCamps.size()) {
					System.out.println("Choice does not correspond to any camp on the list!");
				} else if (selection == 0) {
					System.out.println("Quitting View Camp menu...");
					endLoop = true;
				} else {
					Camp selectedCamp = eligibleCamps.get(selection - 1);
					selectedCamp.showSummary();
					boolean canEnquire = false;
					if (selectedCamp.checkCampStatus()==campStatus.ONGOING || selectedCamp.checkCampStatus()==campStatus.ENDED){
						System.out.println("Enquiries are closed because the camp has started. Press Enter to continue.");}
					else if (selectedCamp.checkCampStatus()==campStatus.CLOSED && selectedCamp.isAttending(this)){
						System.out.println("Press Enter to continue or type 'enquiry' to start a new enquiry:");
						canEnquire = true;}
					else if (selectedCamp.checkCampStatus()==campStatus.CLOSED && !selectedCamp.isAttending(this)){
						System.out.println("Enquiries are closed because registration is over and you aren't attending. Press Enter to continue. ");}
					else if (selectedCamp.isBlacklisted(this)){
						System.out.println("Enquiries are closed to you because have cancelled your signup for this camp.");}
					else{
						System.out.println("Press Enter to continue or type 'enquiry' to start a new enquiry:");
						canEnquire = true;}
					response = sc.nextLine();
					if(response.equals("enquiry") && canEnquire) {
						writeEnquiry(selectedCamp,enquiryList);
					}
				}
			}
		}
	}

	public void writeEnquiry(Camp camp, List<Enquiry> enquiryList){
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter your enquiry:");
		String response = sc.nextLine();
		Enquiry newEnquiry = new Enquiry(-1,camp,this,response,null,"",false);
		enquiryList.add(newEnquiry);
		DataHandler.saveEnquiries(enquiryList);
		}


	public void viewOwnedCamps(List<Camp> campList,List<Signup> signupList,List<User> schoolList){ //TODO
		Scanner sc = new Scanner(System.in);
		String response = "";
		List<Camp> attendingCamps = this.getAttendingCamps(campList,signupList);
		System.out.println("Showing events you are currently signed up for:" );
		if(attendingCamps.size() ==0){
			System.out.println("You are not registered for any camps.");
			System.out.println("Press Enter to continue.");
			response = sc.nextLine();
			return;
		}
		boolean endLoop = false;
		String listMenu = Helper.createNumberedCampList(attendingCamps,this);
		while(!endLoop){
			System.out.println(listMenu);
			System.out.println("0: Back to CAMs main menu ");
			System.out.println("Enter the number corresponding to the camp to view/edit: ");
			response = sc.nextLine();
			if (Helper.checkInputIntValidity(response)) {
				int selection = Integer.parseInt(response);
				if (selection < 0 || selection > attendingCamps.size()) {
					System.out.println("Choice does not correspond to any camp on the list!");
				} else if (selection == 0){
					System.out.println("Quitting View Attending Camp menu...");
					endLoop = true;
				}
				else {
					Camp selectedCamp = attendingCamps.get(selection - 1);
					selectedCamp.showSummary();
					System.out.println("Press Enter to go back.");
					if(this.committee != selectedCamp.getID()) {
						System.out.println("To cancel this signup type 'cancel'");
					}
					if(!selectedCamp.isFullCommittee() && this.committee == -1){
						System.out.println("To upgrade to Committee member type 'committee'"); }//TODO check for slots
					response = sc.nextLine();
					if(response.equals("cancel") && this.committee != selectedCamp.getID()){
						System.out.println("Once you cancel, you will not be able to sign up for this camp again! Are you sure? Y/N");
						int input = -1;
						while (input == -1) {
							input = Helper.parseUserBoolInput(sc.nextLine());
						}
						if (input == 1) {
							for(Signup signup : signupList){
								if(signup.getStudent() == this && signup.getCamp() == selectedCamp){
									signup.cancelSignup();
									DataHandler.saveSignups(signupList);
									endLoop = true;
									break;
								}
							}
							endLoop = true;
						} else {
							System.out.println("Backing out and showing you your signups...");
						}
					}
					else if(response.equals("committee") && !selectedCamp.isFullCommittee() && this.committee == -1){
						System.out.println("Once you change your role, you will not be able to cancel or join another committee! Are you sure? Y/N");
						int input = -1;
						while (input == -1) {
							input = Helper.parseUserBoolInput(sc.nextLine());
						}
						if (input == 1) {
							for(Signup signup : signupList){
								if(signup.getStudent() == this && signup.getCamp() == selectedCamp){
									signupList.remove(signup);
									DataHandler.saveSignups(signupList);
									this.committee = selectedCamp.getID();
									selectedCamp.promoteToComittee(this); //this only matters for memory
									DataHandler.saveUsers(schoolList);
									endLoop = true;
									break;
								}
							}
							endLoop = true;
						} else {
							System.out.println("Backing out and showing you your signups...");
						}
					}

				}
			}
		}
	}


	public List<Camp> getAttendingCamps(List<Camp> campList, List<Signup> signupList){
		List<Camp> attendingCamps = new ArrayList<>();
		Camp committeeCamp;
		for (Signup signup : signupList){
			if (signup.getStatus() && signup.getStudent().getID().equals(this.getID())){
				attendingCamps.add(signup.getCamp());
			}
		}
		committeeCamp = Helper.getCampfromID(this.getCommittee(),campList);
		if (committeeCamp != null){
			attendingCamps.add(committeeCamp);
		}
		return attendingCamps;
	}
	public List<Signup> signUpCamp(List<Camp> campList, List<Signup> signupList) { //TODO disallow when camp is full & clashes
		System.out.println("Showing events you are eligible for as a student of " + this.getFaculty() + " and have never signed up for...");
		List<Camp> campListCopy = new ArrayList<>(campList);
		List<Camp> attendingCamps = this.getAttendingCamps(campList,signupList);
		campListCopy.removeAll(attendingCamps); //remove all camps that student is already attending/committee-ing
		int i = 0;
		List<Camp> eligibleCamps = new ArrayList<>();
		boolean signUpExists;
		boolean clashExists;
		for (Camp camp : campListCopy) {
			signUpExists = false;
			if (camp.checkEligibility(this.getFaculty()) && camp.isVisible() && !camp.isFull()) {//camp eligibility check
				if(camp.isAttending(this) || camp.isBlacklisted(this)){
					signUpExists = true;
				}
				if(!signUpExists){//Now check for clashes
					clashExists = false;
					for(Camp attendingCamp : attendingCamps){//for each attending camp, see if eligible camp clashes
						if(attendingCamp.checkClash(camp.getStart(),camp.getEnd())){ //clash found
							clashExists = true;
							break;
						}
					}
					if (!clashExists){
						i++;
						eligibleCamps.add(camp);
					}
				}
			}

		}
		if (i == 0) {
			System.out.println("No camps are currently open for you :(");
			return signupList;
		}
		String menuList = Helper.createNumberedCampList(eligibleCamps,this);
		Scanner sc = new Scanner(System.in);
		boolean endLoop = false; //Flag to end looping menu; end when joined a camp or choosing to exit
		while(!endLoop) {//start of loop
			System.out.println(menuList); //eligible camps list
			System.out.println("0: Quit to menu. ");
			System.out.println("Enter the number corresponding to the camp you wish to learn more about: ");
			String response = sc.nextLine();
			if (Helper.checkInputIntValidity(response)) {
				int selection = Integer.parseInt(response);
				if (selection < 0 || selection > eligibleCamps.size()) {
					System.out.println("Choice does not correspond to any camp on the list!");
				} else if (selection == 0){
					System.out.println("Quitting Join Camp menu...");
					endLoop = true;
				}
				else {
					Camp selectedCamp = eligibleCamps.get(selection - 1);
					selectedCamp.showSummary();
					System.out.println("Join Camp as attendee? Y/N");
					int input = -1;
					while (input == -1) {
						input = Helper.parseUserBoolInput(sc.nextLine());
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
	return signupList;
	}
}
