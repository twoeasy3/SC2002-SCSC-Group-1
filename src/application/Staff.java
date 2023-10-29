package application;
import java.util.List;
public class Staff extends User { //TODO make this generic so Staff and Student can inherit
	int inCharge; //-1 if not In Char, otherwise put camp.id

	public Staff(String name, String id, String faculty, String password) {
		super();
		this.fillDetails(name, id, faculty, password);
		this.inCharge = -1;
	}

	public boolean checkStaff() {
		return true;
	}

	public void printMenu() {
		System.out.println("Staff Portal");
		System.out.println("You have XX new queries."); //TODO 	A staff can view and reply to enquiries from students to the camp(s) his/her has created
		System.out.println("1. View all active camps.");
		System.out.println("2. Edit your camps.");
		System.out.println("3. Camp enquiry hub.");
		System.out.println("5. View or approve committee suggestions");
	}

	public void viewCamps(List<Camp> campList) {
		System.out.println("Staff privilege; showing all open events ");
		for (Camp camp : campList) {
			System.out.println(camp.getName() + " (" + camp.getFaculty() + ")");
		}
	}

	public void viewOwnedCamps(List<Camp> campList) {
		boolean campFound = false;
		System.out.println("Showing all camps created by you:");
		for (Camp camp : campList) {
			if (camp.getInCharge().equals(this.getID())) {
				System.out.println(camp.getName() + " (" + camp.getFaculty() + ")");
				campFound = true;
			}
		}
		if (!campFound) {
			System.out.println("No active camps created by you found.");
		}
	}
	public List<Camp> signUpCamp(List<Camp> campList, List<Signup> signupList){
		System.out.println("Unimplemented");
		return campList;
	}
}


