package application;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataHandler {
	public static List<User> Initialise() {
		String studentFile = "data/student_list.csv";
		String staffFile = "data/staff_list.csv";// Replace with your CSV file path
        String line = "";
        String csvSeparator = ",";
        //Creating Students
		List<User> schoolList = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(studentFile))){
			line = br.readLine();
			while((line!= null && line.length()>1)) {
				String[] lineData = line.split(csvSeparator);
				if (lineData.length>=2) {
					String name = lineData[0];
					String id = getIDFromEmail(lineData[1]);
					String faculty = lineData[2];
					String passwordHash = lineData[3];
					Student newStudent = new Student(name,id,faculty,passwordHash);
					schoolList.add(newStudent);
					line = br.readLine();
					
					
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		//Creating Staff
		try (BufferedReader br = new BufferedReader(new FileReader(staffFile))){
			line = br.readLine();
			while((line != null && line.length()>1)) {
				String[] lineData = line.split(csvSeparator);
				if (lineData.length>=2) {
					String name = lineData[0];
					String id = getIDFromEmail(lineData[1]);
					String faculty = lineData[2];
					String passwordHash = lineData[3];
					Staff newStaff = new Staff(name,id,faculty,passwordHash);
					schoolList.add(newStaff);
					line = br.readLine();
					
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		//DEBUG
		for (User User : schoolList) {
            System.out.println(User.getID() + " " + User.getName());
        }
		return schoolList;
	}	
	
	public static List<Camp> getCamps(){
		 //Creating Camps
		List<Camp> campList = new ArrayList<>();
		String campFile = "data/camps.csv";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String line = "";
        String csvSeparator = ",";
		try (BufferedReader br = new BufferedReader(new FileReader(campFile))){
			line = br.readLine();
			while((line!= null && line.length()>1)) {
				String[] lineData = line.split(csvSeparator);
				if (lineData.length>=2) {
					String name = lineData[0];
					String faculty = lineData[1];
					LocalDate startDate = LocalDate.parse(lineData[2], formatter);
					LocalDate endDate = LocalDate.parse(lineData[3], formatter);
					LocalDate regEnd =  LocalDate.parse(lineData[4], formatter);
					String loc = lineData[5];
					String desc =  lineData[6];
					int maxSlots = Integer.parseInt(lineData[7]);
					int maxComm = Integer.parseInt(lineData[8]);
					String inCharge =  lineData[9];
					Camp newCamp = new Camp(name,faculty,startDate,endDate,regEnd,loc,desc,maxSlots,maxComm,inCharge);
					campList.add(newCamp);
					line = br.readLine();
					
					
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		//DEBUG
		for (Camp camp : campList) {
            System.out.println(camp.getName() + " " + camp.getFaculty());
        }
		return campList;
	}
	
	
	private static String getIDFromEmail(String email) {
		int atPosition = email.indexOf("@");
		if (atPosition != -1) {
			return email.substring(0,atPosition);
			
		}
		return email;
	}

}
