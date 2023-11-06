package application;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
public class Staff extends User {
	int inCharge; //-1 if not In Char, otherwise put camp.id

	public Staff(String name, String id, String faculty, String password) {
		super();
		this.fillDetails(name, id, faculty, password);
		this.inCharge = -1;
	}

	public boolean checkStaff() {
		return true;
	}

	public void printMenu(List<Camp> campList) {
		System.out.println("Staff Portal");
		System.out.println("You have XX new queries."); //TODO 	A staff can view and reply to enquiries from students to the camp(s) his/her has created
		System.out.println("1. Change your password.");
		System.out.println("2. View all active camps. (-o,-l,-s,-r,-p,-f)");
		System.out.println("3. View and Edit your camps. (-o,-l,-s,-r,-p,-f)");
		System.out.println("4. Camp enquiry hub.");
		System.out.println("5. Create a camp");
		System.out.println("9. Log out");
		System.out.println("0. Terminate CAMs");
	}

	public void viewCamps(List<Camp> campList) {
		System.out.println("Staff privilege; showing all open events ");
		String listMenu = Helper.createNumberedCampList(campList,this);

		boolean endLoop = false;
		while (!endLoop){
			System.out.println(listMenu);
			Scanner sc = new Scanner(System.in);
			System.out.println("0: Back to CAMs main menu ");
			System.out.println("Enter the number corresponding to the camp to view more: ");
			String response = sc.nextLine();
			if (Helper.checkInputIntValidity(response)) {
				int selection = Integer.parseInt(response);
				if (selection < 0 || selection > campList.size()) {
					System.out.println("Choice does not correspond to any camp on the list!");
				} else if (selection == 0) {
					System.out.println("Quitting View Camp menu...");
					endLoop = true;
				} else {
					Camp selectedCamp = campList.get(selection - 1);
					selectedCamp.showSummary();
					System.out.println("Press enter to continue.");
					response = sc.nextLine();

				}
			}
		}
	}



	public void viewOwnedCamps(List<Camp> campList, List<Signup> signupList, List<User> schoolList) {
		int i= 0;
		List<Camp> ownedCamps = new ArrayList<>();
		Scanner sc = new Scanner(System.in);
		System.out.println("Showing all camps created by you:");
		for (Camp camp : campList) {
			if (camp.getInCharge().equals(this.getID())) {
				i++;
				ownedCamps.add(camp);
			}
		}
		if (i == 0) {
			System.out.println("No active camps created by you found. Press enter to continue.");
			String continueInput = sc.nextLine();
			return;
		}

		boolean endLoop = false;
		String listMenu = Helper.createNumberedCampList(ownedCamps,this);
		while(!endLoop){
		System.out.println(listMenu);
		System.out.println("0: Back to CAMs main menu ");
		System.out.println("Enter the number corresponding to the camp to view/edit: ");
		String response = sc.nextLine();
		if (Helper.checkInputIntValidity(response)) {
			int selection = Integer.parseInt(response);
			if (selection < 0 || selection > ownedCamps.size()) {
				System.out.println("Choice does not correspond to any camp on the list!");
			} else if (selection == 0){
				System.out.println("Quitting View Camp menu...");
				endLoop = true;
			}
			else {
				Camp selectedCamp = ownedCamps.get(selection - 1);
				selectedCamp.showSummary();
				System.out.println("Edit Camp? Y/N");
				int input = -1;
				while (input == -1) {
					input = Helper.parseUserBoolInput(sc.nextLine());
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
		if (Helper.checkInputIntValidity(response)) {
			int selection = Integer.parseInt(response);
			if (selection < 0 || selection > maxOptions) {
				System.out.println("Choice does not correspond to any camp on the list!");
			} else if (selection >= 1 && selection <= 5) {
				System.out.println("Enter the new value to change to:");
			} else if (selection >= 6 && selection <= 8) {
				System.out.println("Enter the new date, 8 digits in yyyyMMdd format.");
			} else if (selection == 9) {
				System.out.println("Do you want this camp to be visible to students? Y/N");
			} else if (selection == 0) {
				System.out.println("Are you sure you want to delete this camp? Y/N");
				switch (Helper.parseUserBoolInput(sc.nextLine())) {
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
					DataHandler.saveCamps(campList);
				}
			}

		}
	public Camp createCamp() {
		String response;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter name for camp: ");
		String name = sc.nextLine();
		String faculty = "None";
		System.out.println("Camp open to all? Y/N (If N, camp will be " + this.getFaculty() + " only.)");
		while (faculty.equals("None")) {
			switch (Helper.parseUserBoolInput(sc.nextLine())) {
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
			dateCheck = Helper.checkInputDateValidity(dateString);
		}
		LocalDate startDate = LocalDate.parse(dateString, formatter);
		LocalDate endDate = LocalDate.of(2024, 12, 31); //default value
		dateCheck = false;
		while (!dateCheck) {
			System.out.println("Enter end date for camp (yyyyMMdd): ");
			dateString = sc.nextLine();
			if (!Helper.checkInputDateValidity(dateString)){
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
			if (!Helper.checkInputDateValidity(dateString)){
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
			if (Helper.checkInputIntValidity(response)) {
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
			if (Helper.checkInputIntValidity(response)) {
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
			visibility = Helper.parseUserBoolInput(sc.nextLine());
		}
		return (new Camp(-2, name, faculty, startDate, endDate, regEnd,
				description, location, maxSize, maxComm, this.getID(), visibility));
	}



}


