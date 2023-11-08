package application;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Superclass intended to be used in CAMs interface.
 * Methods and variables whose behaviour is independent of the User type are defined here.
 */
public abstract class User {
	/**
	 * Given name of the user. Inherited by subclasses.
	 */
	private String name;
	/**
	 * Unique identifier of the user. Inherited by subclasses.
	 * As a rule of thumb, Staff don't have numerals in their UserIDs.
	 */
	private String id;
	/**
	 * String identifier of the faculty that the user belongs to. Inherited by subclasses.
	 * A full list of faculties in use by CAMs are:
	 * ADM,ASE,CCEB,CEE,EEE,LKC,MAE,MSE,NBS,NIE,SBS,SCSE,SOH,SPMS,SSS,WKWSCI.
	 * ALL may be seen as a value, but only in Camp objects and not here.
	 */
	private String faculty;
	/**
	 * Hashed password value using SHA-256.
	 * New users are set on default password value 'password'; starting with "5e884898..."
	 * Users on this hash value are assumed to be new users.
	 */
	private String password;

	/**
	 * Called by subclasses on construction. Relevant usage in DataHandler.getUsers().
	 * @param name Given name of user.
	 * @param id Unique identifier of user.
	 * @param faculty String identifier of user faculty.
	 * @param password SHA-256 hash value of password.
	 */
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

	/**
	 * Used to write the hashes back to save the program state.
	 * @return The hash of the user's password.
	 */
	public String getPassword() { return(this.password);}
	/**
	 * Boilerplate code to avoid storing passwords in plaintext in the .csv files at the very minimum.
	 *
	 * @param password Plaintext String of password intended to be hashed.
	 * @return Hashed String intended to be stored.
	 * If an Exception happens it will return the default password, but should be exceedingly rare.
	 */

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
			System.out.println("Error in password operations. Your password may be reset to the default.");
           return "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";} //have to return a string here, not too sure what to do
	}

	/**
	 * Used in login to authenticate the password.
	 * Calls User.hash() to generate the hash for the attempt to match with the stored hashed value.
	 * @param attempt Plaintext String of the attempted password.
	 * @return Boolean whether the attempt is the correct password.
	 */
	public boolean matchPass(String attempt) {
		if(this.password.equals(hash(attempt))) {
			return true;
		}
		else {
			return false;
		}
	}

	public void checkForDefaultPass(){
		if(hash("password").equals(this.password)){
			System.out.println("Welcome to CAMs. We require you to change your password.");
			this.changePass();
		}
	}

	/**
	 * Called when user requests or after checkForDefaultPass() finds a default password.
	 * Asks user for current password, then a new password.
	 * Current password must be correct.
	 * New password is hashed then stored in User.password
	 * After this is called, main() should call DataHandler.saveUsers() to update program state.
	 *
	 */
	public void changePass() {
		Scanner sc = new Scanner(System.in);
		String lastpass = "";
		while(!matchPass(lastpass)){
		System.out.println("Please enter your current password:");
		lastpass = sc.nextLine();
		if(!matchPass(lastpass)){
			System.out.println("Current password is incorrect.");}
		}
		if(matchPass(lastpass)) {
			System.out.println("Please enter your new password.");
			String newpass = sc.nextLine();
			this.password = hash(newpass);
			System.out.println("Password successfully updated.");
		}

	}



	/**
	 * Used to determine and generate the menu of options in CAMs.
	 */
	public abstract void printMenu(List<Camp> campList);

	/**
	 * Used to generate the list of camps that should be visible to the user.
	 * @param campList List of all Camp objects.
	 */
	public abstract void viewCamps(List<Camp> campList, List<Enquiry> enquiryList);

	/**
	 * Used to view the list of camps where the User can meaningfully interact with.
	 * Behaviour changes based on whether the User is a Student or Staff.
	 * @param campList List of all Camp objects.
	 */
	public abstract void viewOwnedCamps(List<Camp> campList,List<Signup> signupList, List<User> schoolList);


}
