package application;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class User {
	private String name;
	private String id;
	private String faculty;
	private String password;
	
	
	public void fillDetails(String name, String id, String faculty, String password) {
		this.name = name;
		this.id = id;
		this.faculty = faculty;
		this.password = password;
	}
	
	public String getName() {
		return(this.name);
	}
	public String getID() {
		return(this.id);
	}
	public String getFaculty() {
		return(this.faculty);
	}
	
	public String hash(String password) { //not required by the document but seems extremely silly to keep plaintext passwords
		try {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		 byte[] hashedBytes = digest.digest(password.getBytes());
		 StringBuilder hexString = new StringBuilder();
        for (byte b : hashedBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();}
		catch (NoSuchAlgorithmException e) {
           return "default_value";} //have to return a string here, not too sure what to do
	}	
	public boolean matchPass(String attempt) {
		if(hash(attempt).equals(this.password)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void changePass(String lastpass, String newpass) {		 
		if(matchPass(lastpass)) {
			this.password = hash(newpass);
			System.out.println("Password successfully updated.");
		}
		else {
			System.out.println("Current password is incorrect.");
		}
	}
	public boolean checkStaff() { //overriden
		return false;
	}
	public void printMenu(){
		System.out.println("printMenu() for <User>");
	}
	public void viewCamps(List<Camp> campList){
		System.out.println("viewCamps() for <User>");
	}
	public void viewOwnedCamps(List<Camp> campList){System.out.println("viewOwnedCamps() for <User>");}
	public List<Camp> signUpCamp(List<Camp> campList, List<Signup> signupList){
		System.out.println("signUpCamp() for <User>");
		return campList;
	}
		

}
