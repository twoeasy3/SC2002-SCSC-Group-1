package application;
import java.awt.image.AreaAveragingScaleFilter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Student extends User{ 
	int committee; //-1 if not committee member, otherwise put camp.id

	ArrayList<Enquiry> enquiries_list;
	
	public Student(String name, String id, String faculty,String password,int committee) {
		super();
		this.fillDetails(name, id, faculty, password);
		this.committee = committee;
		this.enquiries_list = new ArrayList<Enquiry>(0);
	}

	public int getCommittee(){return this.committee;}
	public boolean checkStaff() {
		return false;
	}

	public ArrayList<Enquiry> getEnquiries_list() {
		return enquiries_list;
	}

	public void printMenu(){
		System.out.println("Student Portal");
		if(this.getCommittee() == -1){
			System.out.println("You are not currently a committee member of any camp.");}
		else{
			System.out.println("You are currently a committee member of XX camp.");//TODO
		}
		System.out.println("1. Change your password.");
		System.out.println("2. View eligible camps.");
		System.out.println("3. View your signups.");
		System.out.println("4. Sign up for camp.");
		System.out.println("5. Camp Enquiry Hub");
		System.out.println("6. Camp Committee Hub");
		System.out.println("0. Quit CAMs");
	}
	public void viewCamps(List<Camp> campList){
		System.out.println("Showing events you are eligible for as a student of " + this.getFaculty() );
		for(Camp camp : campList){
			if(camp.checkEligibility(this.getFaculty())&& camp.isVisible()){
				System.out.println(camp.getName() + " (" + camp.getFaculty() +")");
			}
		}
	}

	public void viewOwnedCamps(List<Camp> campList,List<Signup> signupList){ //TODO
		List<Camp> attendingCamps = this.getAttendingCamps(campList,signupList);
		StringBuilder sb = new StringBuilder();
		String listMenu = "";
		int i = 0;
		System.out.println("Showing events you are currently signed up for:" );
		for(Camp camp : attendingCamps){
			i++;
			listMenu = sb.append(i).append(": ").append(camp.getName()).append(" (").append(camp.getFaculty()).append(")").toString();
			if(camp.getID()==this.getCommittee()){
				listMenu = sb.append(" [COMMITTEE]").toString();}
			listMenu = sb.append("\n").toString();
		}
		System.out.println(listMenu.substring(0,listMenu.length()-1));
	}

	public List<Camp> getAttendingCamps(List<Camp> campList, List<Signup> signupList){
		List<Camp> attendingCamps = new ArrayList<>();
		Camp committeeCamp;
		for (Signup signup : signupList){
			if (signup.getStatus() && signup.getStudent().getID().equals(this.getID())){
				attendingCamps.add(signup.getCamp());
			}
		}
		committeeCamp = DataHandler.getCampfromID(this.getCommittee(),campList);
		if (committeeCamp != null){
			attendingCamps.add(committeeCamp);
		}
		return attendingCamps;
	}
	public List<Signup> signUpCamp(List<Camp> campList, List<Signup> signupList) { //TODO disallow when camp is full & clashes
		System.out.println("Showing events you are eligible for as a student of " + this.getFaculty() + " and have never signed up for...");
		StringBuilder sb = new StringBuilder(); //better performance
		String repeatList = "" ;//used to save and spit the list out again
		List<Camp> attendingCamps = this.getAttendingCamps(campList,signupList);
		campList.removeAll(attendingCamps); //remove all camps that student is already attending/committee-ing
		int i = 0;
		List<Camp> eligibleCamps = new ArrayList<>();
		boolean signUpExists;
		boolean clashExists;
		for (Camp camp : campList) {
			signUpExists = false;
			if (camp.checkEligibility(this.getFaculty()) && camp.isVisible()) {//camp eligibility check
				for (Signup signup : signupList) {//checking for existing signups
					if (signup.matchStudent(this.getID()) && signup.matchCamp(camp)) {//previous signup found
						signUpExists = true;
						break;
					}
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
						repeatList = sb.append(i).append(": ").append(camp.getName()).append(" (").append(camp.getFaculty()).append(")").append("\n").toString();
					}
				}
			}

		}
		if (i == 0) {
			System.out.println("No camps are currently open for you :(");
			return signupList;
		}
		Scanner sc = new Scanner(System.in);
		boolean endLoop = false; //Flag to end looping menu; end when joined a camp or choosing to exit
		while(!endLoop) {//start of loop
			System.out.println(repeatList); //eligible camps list
			System.out.println("0: Quit to menu. ");
			System.out.println("Enter the number corresponding to the camp you wish to learn more about: ");
			String response = sc.nextLine();
			if (checkInputIntValidity(response)) {
				int selection = Integer.parseInt(response);
				if (selection < 0 || selection > eligibleCamps.size()) {
					System.out.println("Choice does not correspond to any camp on the list!");
				} else if (selection == 0){
					System.out.println("Quitting Join Camp menu...");
					endLoop = true;
				}
				else {
					Camp selectedCamp = eligibleCamps.get(selection - 1);
					System.out.println(selectedCamp.getName() + " (" + selectedCamp.getFaculty() + ")");
					System.out.println("Registration End Date: " + selectedCamp.getRegEnd().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
					System.out.println("Start Date: " + selectedCamp.getStart().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
					System.out.println("End Date: " + selectedCamp.getEnd().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
					System.out.println("Venue: " + selectedCamp.getLocation());
					System.out.println(selectedCamp.getDescription());
					System.out.println("Join Camp as attendee? Y/N");
					int input = -1;
					while (input == -1) {
						input = parseUserBoolInput(sc.nextLine());
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
	// Enquiry Functions
	// creates an enquiry and sends it to both camp and the student
	public void createEnquiry(String description, Student author, Camp camp) {
		Enquiry enquiry = new Enquiry(description, author, camp);
		author.enquiries_list.add(enquiry);
		camp.getEnquiry_list().add(enquiry);
		System.out.println("Your enquiry has been created and sent");
	}

	// allows student to view the enquiry (description + reply)
	public void viewEnquiry(Enquiry enquiry) {
		System.out.println(enquiry.getDescription());
		if (enquiry.getReply().isEmpty()) {
			System.out.println("This enquiry has not yet been replied");
		}
		else System.out.println(enquiry.getReply());
	}
	// assuming only description can be edited. but maybe camp can be updated as well?
	public void editEnquiry(Enquiry e, String s) {
		e.setDescription(s + "\n This enquiry has been edited");
		System.out.println("Your enquiry has been edited");
	}
	// removes enquiry from both the camp and the user
	public void deleteEnquiry(Enquiry e) {
		this.enquiries_list.remove(e);
		Camp camp = e.getCamp();
		camp.getEnquiry_list().remove(e);
		System.out.println("Your enquiry has been deleted");
	}


}
