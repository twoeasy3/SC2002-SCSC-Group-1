package application;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Responsible for reading files from data/ into the program state.
 * Also writes program state back into the file for persistence.
 *
 */
public class DataHandler {
	/**
	 * Reads both student_list and staff_list CSVs
	 * Builds the appropriate objects (Student/Staff)
	 *
	 * @return List of all User objects described in the CSVs.
	 */
	public static List<User> getUsers() {
		String studentFile = "data/student_list.csv";
		String staffFile = "data/staff_list.csv";
        String line;
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
					int committee = Integer.parseInt(lineData[4]);
					Student newStudent = new StudentCommittee(name,id,faculty,passwordHash,committee);
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
					//committee field ignored for Staff
					Staff newStaff = new Staff(name,id,faculty,passwordHash);
					schoolList.add(newStaff);
					line = br.readLine();
					
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		/*DEBUG
		for (User User : schoolList) {
            System.out.println(User.getID() + " " + User.getName());
        } */
		return schoolList;
	}

	/**
	 * Reads data/camps.csv
	 * Builds Camp objects described in camps.csv
	 *
	 * @return List of all Camp objects described in the CSV.
	 */
	public static List<Camp> getCamps(){
		 //Creating Camps
		List<Camp> campList = new ArrayList<>();
		String campFile = "data/camps.csv";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String line;
        String csvSeparator = ",";
		try (BufferedReader br = new BufferedReader(new FileReader(campFile))){
			line = br.readLine();
			while((line!= null && line.length()>1)) {
				String[] lineData = line.split(csvSeparator);
				if (lineData.length>=10) {
					int ID = Integer.parseInt(lineData[0].replace("\uFEFF", "")); //Invisible characters in the CSV for some reason
					String name = lineData[1];
					String faculty = lineData[2];
					LocalDate startDate = LocalDate.parse(lineData[3], formatter);
					LocalDate endDate = LocalDate.parse(lineData[4], formatter);
					LocalDate regEnd =  LocalDate.parse(lineData[5], formatter);
					String loc = lineData[6];
					String desc =  lineData[7];
					int maxSlots = Integer.parseInt(lineData[8]);
					int maxComm = Integer.parseInt(lineData[9]);
					String inCharge =  lineData[10];
					int visible = Integer.parseInt(lineData[11]);
					Camp newCamp = new Camp(ID,name,faculty,startDate,endDate,regEnd,loc,desc,maxSlots,maxComm,inCharge,visible);
					campList.add(newCamp);
					line = br.readLine();
					
					
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		/*DEBUG
		for (Camp camp : campList) {
            System.out.println(camp.getName() + " " + camp.getFaculty());
        }*/
		return campList;
	}
	/**
	 * Reads data/signups.csv
	 * Builds Signup objects described in signups.csv
	 *
	 * @return List of all Signup objects described in the CSV.
	 */
	public static List<Signup> getSignups(List<User> schoolList, List<Camp> campList){
		//Creating Signup
		List<Signup> signupList = new ArrayList<>();
		String signupFile = "data/signups.csv";
		String line;
		String csvSeparator = ",";
		String userID;
		boolean studentExists = false;
		boolean campExists = false;
		int campID;
		User foundStudent = null;
		Camp foundCamp = null;
		try (BufferedReader br = new BufferedReader(new FileReader(signupFile))){
			line = br.readLine();
			while((line!= null && line.length()>1)) {
				String[] lineData = line.split(csvSeparator);
				if (lineData.length>2) {
					userID = lineData[0];
					studentExists = false;
					for (User user : schoolList) {
						if (user.getID().equals(userID)) {
							foundStudent = user;
							studentExists = true;
							break;
						}
					}
					if (studentExists) {
						campID = Integer.parseInt(lineData[1]);
						campExists = false;
						for (Camp camp : campList) {
							if (camp.getID() == campID) {
								foundCamp = camp;
								campExists = true;
							}
						}
					}
				}
				if(studentExists && campExists){
					boolean status = (1==Integer.parseInt(lineData[2]));
					Signup newSignup = new Signup(foundStudent,foundCamp,status);
					signupList.add(newSignup);

				}
				line = br.readLine();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		/*DEBUG
		for (Signup signup : signupList) {
            System.out.println(signup.getCamp().getName() + " " + signup.getStudent().getName());
        }*/
		return signupList;
	}
	public static List<Enquiry> getEnquiries(List<User> schoolList, List<Camp> campList) {
		List<Enquiry> enquiryList = new ArrayList<>();
		String enquiryFile = "data/enquiries.csv";
		String line;
		String csvSeparator = "\\|";
		int enquiryID = 0;
		int campID;
		String userID;
		String replyUserID;
		boolean studentExists = false;
		boolean campExists = false;
		Enquiry enquiry = null;
		User foundStudent = null;
		Camp foundCamp = null;
		User foundReplyAuthor = null;
		try (BufferedReader br = new BufferedReader(new FileReader(enquiryFile))) {
			line = br.readLine();
			while ((line != null && line.length() > 1)) {
				String[] lineData = line.split(csvSeparator);
				if (lineData.length > 2) {
					enquiryID = Integer.parseInt(lineData[0]);
					userID = lineData[2];
					studentExists = false;
					for (User user : schoolList) {
						if (user.getID().equals(userID)) {
							foundStudent = user;
							studentExists = true;
							break;
						}
					}
					if (studentExists) {
						campID = Integer.parseInt(lineData[1]);
						campExists = false;
						for (Camp camp : campList) {
							if (camp.getID() == campID) {
								foundCamp = camp;
								campExists = true;
							}
						}
					}
				}
				if (studentExists && campExists) {
					boolean status = (1 == Integer.parseInt(lineData[6]));
					if (status) {
						replyUserID = lineData[4];
						for (User user : schoolList) {
							if (user.getID().equals(replyUserID)) {
								foundReplyAuthor = user;
								enquiry = new Enquiry(enquiryID,foundCamp,(Student) foundStudent, lineData[3],
										foundReplyAuthor, lineData[5],true);
								enquiryList.add(enquiry);
								break;
							}
						}


					} else {
						enquiry = new Enquiry(enquiryID, foundCamp,(Student) foundStudent, lineData[3],
								null, "", false);
						enquiryList.add(enquiry);
					}
				}
				line = br.readLine();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return enquiryList;
	}
	/**
	 *Writes the current state of all User objects into their respective CSVs.
	 *Currently only required for password updates.
	 */
	public static void saveUsers(List<User> userList){
		String studentFile = "data/student_list.csv";
		String staffFile = "data/staff_list.csv";
		String line;
		String csvSeparator = ",";
		try (BufferedWriter writerStaff = new BufferedWriter(new FileWriter(staffFile));
			BufferedWriter writerStudent = new BufferedWriter(new FileWriter(studentFile))) {
			for (User user : userList) {
				line = user.getName() + csvSeparator
						+ user.getID() + "@e.ntu.edu.sg" + csvSeparator
						+ user.getFaculty() + csvSeparator
						+ user.getPassword();
				if(user instanceof Student) {
					line += csvSeparator + ((Student) user).getCommittee();
					writerStudent.write(line);
					writerStudent.newLine();
				}
				else{
					writerStaff.write(line);
					writerStaff.newLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void saveCamps(List<Camp> campList){
		String campFile = "data/camps.csv";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String line;
		String csvSeparator = ",";
		String visible;

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(campFile))) {
			for (Camp camp : campList) {
				if (camp.isVisible()){visible = "1";}else{visible="0";}
				line = camp.getID()+ csvSeparator
						+ camp.getName() + csvSeparator
						+ camp.getFaculty() + csvSeparator
						+ camp.getStart().format(formatter) + csvSeparator
						+ camp.getEnd().format(formatter) + csvSeparator
						+ camp.getRegEnd().format(formatter) + csvSeparator
						+ camp.getLocation() + csvSeparator
						+ camp.getDescription() + csvSeparator
						+ camp.getMaxSize() + csvSeparator
						+ camp.getMaxComm() + csvSeparator
						+ camp.getInCharge() + csvSeparator
						+ visible;

					writer.write(line);
					writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Saves all student-camp attendee relationships to csv.
	 * Called in signUpCamp(), so non-signups don't cause a write to file.
	 * @param signupList List of all Signup objects.
	 */
	public static void saveSignups(List<Signup> signupList){
		String signupFile = "data/signups.csv";
		String line;
		String csvSeparator = ",";
		String status;
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(signupFile))) {
			for (Signup signup : signupList) {
				if (signup.getStatus()){status = "1";}else{status="0";}
				line = signup.getStudent().getID() + csvSeparator
						+ signup.getCamp().getID() + csvSeparator
						+ status;
				writer.write(line);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void saveEnquiries(List<Enquiry> enquiryList){
		String enquiryFile = "data/enquiries.csv";
		String line;
		String csvSeparator = "|";
		String status;
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(enquiryFile))) {
			for (Enquiry enquiry : enquiryList) {
				if (enquiry.getResolved()){status = "1";}else{status="0";}
				line = enquiry.getEnquiryID() + csvSeparator
						+ enquiry.getCamp().getID() + csvSeparator
						+ enquiry.getAuthor().getID() + csvSeparator
						+ enquiry.getDescription() + csvSeparator;

				if(status.equals("1")){
					line += enquiry.getReplyAuthor().getID() + csvSeparator
							+ enquiry.getReply() + csvSeparator
							+ status;
				}
				else{
					line += csvSeparator+ csvSeparator + status;
				}
				writer.write(line);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Returns the correct format for userID from the .csv
	 * CSVs have emails instead of userID as per format.
	 *
	 * @return Preceding string before the "@" symbol.
	 */
	private static String getIDFromEmail(String email) {
		int atPosition = email.indexOf("@");
		if (atPosition != -1) {
			return email.substring(0,atPosition);
			
		}
		return email;
	}

}
