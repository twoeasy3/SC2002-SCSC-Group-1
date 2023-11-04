package application;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
public class Student extends User{ 
	int committee; //-1 if not committee member, otherwise put camp.id
	
	public Student(String name, String id, String faculty,String password) {
		super();
		this.fillDetails(name, id, faculty, password);
		this.committee = -1;		
	}
	
	public boolean checkStaff() {
		return false;
	}

	public void printMenu(){
		System.out.println("Student Portal");
		System.out.println("You are currently a committee member of XX camp."); //TODO
		System.out.println("1. Change your password.");
		System.out.println("2. View eligible camps.");
		System.out.println("3. View your signups.");
		System.out.println("4. Sign up for camp.");
		System.out.println("5. Camp Enquiry Hub");
		System.out.println("6. Camp Committee Hub");
	}
	public void viewCamps(List<Camp> campList){
		System.out.println("Showing events you are eligible for as a student of " + this.getFaculty() );
		for(Camp camp : campList){
			if(camp.checkEligibility(this.getFaculty())&& camp.isVisible()){
				System.out.println(camp.getName() + " (" + camp.getFaculty() +")");
			}
		}
	}

	public void viewOwnedCamps(List<Camp> campList){ //TODO
		System.out.println("Unimplemented. ");
	}
	public Camp createCamp(){
		System.out.println("Unimplemented. ");
		return(new Camp(-1,"default","default",LocalDate.now(),LocalDate.now(),LocalDate.now(),"default",
				"default",100 ,10,"DEFAULT",0));
	}

	public boolean isCommittee(){
		return (this.committee != -1);
	}

	public List<Camp> signUpCamp(List<Camp> campList, List<Signup> signupList) { //TODO disallow when camp is full & clashes
		System.out.println("Showing events you are eligible for as a student of " + this.getFaculty() + " and have never signed up for...");
		int i = 0;
		List<Camp> eligibleCamps = null;
		for (Camp camp : campList) {
			if (camp.checkEligibility(this.getFaculty()) && camp.isVisible()) {
				for (Signup signup : signupList) {
					if (!signup.matchStudent(this.getID()) && !signup.matchCamp(camp)) {
						i++;
						eligibleCamps.add(camp);
						System.out.println(i + ": " + camp.getName() + " (" + camp.getFaculty() + ")");
					}
				}
			}
		}
		if (i == 0) {
			System.out.println("No camps are currently open for you :(");

		}
		return eligibleCamps;
	}
}
