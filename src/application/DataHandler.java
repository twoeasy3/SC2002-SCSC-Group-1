package application;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataHandler {
	public static List<Person> Initialise() {
		String studentFile = "data/student_list.csv";
		String staffFile = "data/staff_list.csv";// Replace with your CSV file path
        String line = "";
        String csvSeparator = ",";
        //Creating Students
		List<Person> schoolList = new ArrayList<>();
		
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
		for (Person person : schoolList) {
            System.out.println(person.getID() + " " + person.getName());
        }
		return schoolList;
	}
	
	
	
	private static String getIDFromEmail(String email) {
		int atPosition = email.indexOf("@");
		if (atPosition != -1) {
			return email.substring(0,atPosition);
			
		}
		return email;
	}

}
