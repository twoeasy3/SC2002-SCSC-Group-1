import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
class Student { //TODO make this generic so Staff and Student can inherit
	String name;
	String id;
	String faculty;
	String password;
	int committee; //-1 if not committee member, otherwise put camp.id
	
	public Student(String name, String id, String faculty) {
		this.name = name;
		this.id = id;
		this.faculty = faculty;
		this.password = hash("password");
		this.committee = -1;
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
		if(hash(attempt) == this.password) {
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
		

}
