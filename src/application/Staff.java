package application;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

	public void printMenu() {
		System.out.println("Staff Portal");
		System.out.println("You have XX new queries."); //TODO 	A staff can view and reply to enquiries from students to the camp(s) his/her has created
		System.out.println("1. Change your password.");
		System.out.println("2. View all active camps.");
		System.out.println("3. Edit your camps.");
		System.out.println("4. Camp enquiry hub.");
		System.out.println("5. Create a camp");
		System.out.println("0. Quit CAMs");
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
	public Camp createCamp() {
		String response;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter name for camp: ");
		String name = sc.nextLine();
		String faculty = "None";
		System.out.println("Camp open to all? Y/N (If N, camp will be " + this.getFaculty() + " only.)");
		while (faculty.equals("None")) {
			switch (this.parseUserBoolInput(sc.nextLine())) {
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
			dateCheck = this.checkInputDateValidity(dateString);
		}
		LocalDate startDate = LocalDate.parse(dateString, formatter);
		LocalDate endDate = LocalDate.of(2024, 12, 31); //default value
		dateCheck = false;
		while (!dateCheck) {
			System.out.println("Enter end date for camp (yyyyMMdd): ");
			dateString = sc.nextLine();
			if (!checkInputDateValidity(dateString)){
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
			if (!checkInputDateValidity(dateString)){
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
			if (this.checkInputIntValidity(response)) {
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
			if (this.checkInputIntValidity(response)) {
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
			visibility = this.parseUserBoolInput(sc.nextLine());
		}
		return (new Camp(-1, name, faculty, startDate, endDate, regEnd,
				description, location, maxSize, maxComm, this.getID(), visibility));
	}



}


