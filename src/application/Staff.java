package application;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Staff extends User implements ElevatedActions{
	int inCharge; //-1 if not In Char, otherwise put camp.id

	public Staff(String name, String id, String faculty, String password) {
		super();
		this.fillDetails(name, id, faculty, password);
		this.inCharge = -1;
	}

	public boolean checkStaff() {
		return true;
	}

	public void printMenu(List<Camp> campList){
		StaffView.printMenu(campList);
	}
	public void viewCamps(List<Camp> campList, List<Enquiry> enquiryList){
		StaffView.viewCamps(this,campList,enquiryList);
	}
	public sessionStatus resolveCAMsMenu(int choice, String argument,
												List<User> userList,
												List<Camp> campList,
												List<Signup> signupList,
												List<Enquiry> enquiryList,
												List<Suggestion> suggestionList){
		return StaffView.resolveCAMsMenu(this, choice, argument,
									userList,campList,signupList,enquiryList,suggestionList);
	}

	public List<Camp> getOwnedCamps(List<Camp> campList){
		int i= 0;
		List<Camp> ownedCamps = new ArrayList<>();
		Scanner sc = new Scanner(System.in);
		for (Camp camp : campList) {
			if (camp.getInCharge().equals(this.getID())) {
				i++;
				ownedCamps.add(camp);
			}
		}
		if (i == 0) {
			System.out.println("No active camps created by you found. Press enter to continue.");
			String continueInput = sc.nextLine();
			return null;
		}
		return ownedCamps;
	}

	public void viewOwnedCamps(List<Camp> campList, List<Signup> signupList, List<User> userList) {
		Scanner sc = new Scanner(System.in);
		List<Camp> ownedCamps = this.getOwnedCamps(campList);
		if (ownedCamps == null){
			return;
		}
		boolean endLoop = false;
		String listMenu = CampListView.createNumberedCampList(ownedCamps,this);
		while(!endLoop){
			System.out.println("Showing all camps created by you:");
			Camp selectedCamp = CampListView.campFromListSelector(ownedCamps,listMenu);
			if(selectedCamp == null) {
				return;
			}else {
				CampView.showSummary(selectedCamp);
				System.out.println("Edit Camp? Y/N");
				int input = -1;
				while (input == -1) {
					input = InputChecker.parseUserBoolInput(sc.nextLine());
				}
				if (input == 1) {
					this.staffEditCamp(selectedCamp, campList);
					endLoop = true;
				} else {
					System.out.println("Backing out and showing you all created camps again...");
				}
			}
		}
	}

	public void staffEditCamp(Camp camp, List<Camp> campList) {
		Scanner sc = new Scanner(System.in);
		int maxOptions = 5;
		System.out.println("Edit Menu:");
		System.out.println("1:Name");
		System.out.println("2:Venue");
		System.out.println("3:Description");
		System.out.println("4:Maximum Slots");
		System.out.println("5:Maximum Committee");
		if (camp.getCommitteeCount() + camp.getAttendeeCount() == 0) {
			System.out.println("6:Start Date");
			System.out.println("7:End Date");
			System.out.println("8:Registration End Date");
			System.out.println("9:Set Visibility");
			System.out.println("0:Delete Camp");
			maxOptions = 9;
		}
		System.out.println("Select component to edit:");
		String response = sc.nextLine();
		if (InputChecker.intValidity(response)) {
			int selection = Integer.parseInt(response);
			if (selection < 0 || selection > maxOptions) {
				System.out.println("Choice does not correspond to any option!");
			} else if (selection >= 1 && selection <= 5) {
				System.out.println("Enter the new value to change to:");
			} else if (selection >= 6 && selection <= 8) {
				System.out.println("Enter the new date, 8 digits in yyyyMMdd format.");
			} else if (selection == 9) {
				System.out.println("Do you want this camp to be visible to students? Y/N");
			} else if (selection == 0) {
				System.out.println("Are you sure you want to delete this camp? Y/N");
				switch (InputChecker.parseUserBoolInput(sc.nextLine())) {
					case 0:
						System.out.println("Camp not deleted.");
						return;
					case 1:
						System.out.println(camp.getName() + " has been deleted.");
						campList.remove(camp);
						DataHandler.saveCamps(campList);
						return;
					default:
						System.out.println("Camp not deleted.");
						return;
				}
			}
				response = sc.nextLine();
				if (camp.tryEditCamp(selection, response)) {
					camp.doEditCamp(selection, response);
					DataHandler.saveCamps(campList);
				}
			}

		}
	public void createCamp(List<Camp> campList) {
		String response;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter name for camp: ");
		String name = sc.nextLine();
		String faculty = "None";
		System.out.println("Camp open to all? Y/N (If N, camp will be " + this.getFaculty() + " only.)");
		while (faculty.equals("None")) {
			switch (InputChecker.parseUserBoolInput(sc.nextLine())) {
				case 0:
					faculty = this.getFaculty();
					break;
				case 1:
					faculty = "ALL";
					break;
				default:
					break;
			}
		}
		boolean dateCheck = false;
		String dateString = " ";
		while (!dateCheck) {
			System.out.println("Enter start date for camp (yyyyMMdd): ");
			dateString = sc.nextLine();
			dateCheck = InputChecker.dateValidity(dateString);
		}
		LocalDate startDate = LocalDate.parse(dateString, formatter);
		LocalDate endDate = LocalDate.of(2024, 12, 31); //default value
		dateCheck = false;
		while (!dateCheck) {
			System.out.println("Enter end date for camp (yyyyMMdd): ");
			dateString = sc.nextLine();
			if (!InputChecker.dateValidity(dateString)){
				continue;
			}
			endDate = LocalDate.parse(dateString, formatter);
			if (endDate.isBefore(startDate)) {
				System.out.println("End date cannot be before start date!");
			}
			else{
				dateCheck = true;
			}
		}
		LocalDate regEnd = startDate; //default value
		dateCheck = false;
		while (!dateCheck) {
			System.out.println("Enter registration end date for camp (yyyyMMdd): ");
			dateString = sc.nextLine();
			if (!InputChecker.dateValidity(dateString)){
				continue;
			}
			regEnd = LocalDate.parse(dateString, formatter);
			if (regEnd.isAfter(startDate)) {
				System.out.println("Registration cannot end after start date!");
			}
			else{
				dateCheck = true;
			}
		}
		int maxSize = -619;
		while (maxSize < 10 || maxSize > 24757) {
			System.out.println("Enter maximum number of camp attendees: ");
			response = sc.nextLine();
			if (InputChecker.intValidity(response)) {
				maxSize = Integer.parseInt(response);
				if (maxSize < 10) {
					System.out.println("Camps in CAMs must have at least 10 open slots!");
				} else if (maxSize > 24757) {
					System.out.println("Your camp cannot have more open slots than NTU's enrolment this AY!");
				}
			}
		}
		int maxComm = -619;
		while (maxComm < 0 || maxComm > 10) {
			System.out.println("Enter maximum number of camp committee members: ");
			response = sc.nextLine();
			if (InputChecker.intValidity(response)) {
				maxComm = Integer.parseInt(response);
				if (maxComm < 0) {
					System.out.println("You can't have a negative number of committee members!");
				} else if (maxComm > 10) {
					System.out.println("Your camp cannot have more than 10 committee members!");
				}
			}
		}
		System.out.println("Enter venue name for camp: ");
		String location = sc.nextLine();
		System.out.println("Enter short description for camp: ");
		String description = sc.nextLine().replace(",", "");

		int visibility = -1;
		while (visibility == -1) {
			System.out.println("Set camp to be visible to students now? Y/N");
			visibility = InputChecker.parseUserBoolInput(sc.nextLine());
		}
		campList.add(new Camp(-2, name, faculty, startDate, endDate, regEnd,
				description, location, maxSize, maxComm, this.getID(), visibility));
		DataHandler.saveCamps(campList);
	}



}


