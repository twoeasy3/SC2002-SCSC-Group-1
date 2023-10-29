package application;
public class Signup {
	private User student;
	private Camp camp;
	private boolean	status; //TRUE if signed up and attending; FALSE if signed up but subsequently cancelled
	public Signup(User student,Camp camp, boolean status ) {
		this.student = student;
		this.camp = camp;
		this.status = status;
	}

	public Camp getCamp() {
		return camp;
	}

	public User getStudent() {
		return student;
	}

	public void cancelSignup() {
		this.status = false;
		System.out.println("You have successfully cancelled your attendance for " + this.camp.getName());
		System.out.println("You may not sign up for this event again.");
	}
	public boolean matchStudent(String student) { //USE STUDENT ID
		if(this.student.getID().equals(student)) {
			return(true);
		}
		return(false);
	}
	public boolean matchCamp(Camp camp) { //USE CAMP  OBJECT
		if(this.camp.getID() == camp.getID()) {
			return(true);
		}
		return(false);
	}
}
