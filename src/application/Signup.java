package application;

/**
 * Represents the many-to-many relations between Student and Camp.
 * A signup is a persistent record and can never be deleted.
 * Only 1 signup between any unique Student and Camp is allowed.
 */
public class Signup {
	/**
	 * The User object associated in this signup
	 */
	private User student;
	/**
	 * The Camp object associated in this signup
	 */
	private Camp camp;
	/**
	 * Status of the signup.
	 * TRUE value indicates the signup is still active.
	 * FALSE value indicates the signup has been rescinded. Disallows Student from signing up a second time.
	 */
	private boolean	status; //TRUE if signed up and attending; FALSE if signed up but subsequently cancelled

	/**
	 * Creates a new Signup between a Student and a Camp.
	 * @param student User object associated with the signup.
	 * @param camp Camp object associated with the signup.
	 * @param status Whether the signup is active or not. Relevant when called by DataHandler.
	 */
	public Signup(User student,Camp camp, boolean status ) {
		this.student = student;
		this.camp = camp;
		this.status = status;
	}

	/**
	 *
	 * @return Camp object associated with this signup.
	 */
	public Camp getCamp() {
		return camp;
	}
	/**
	 *
	 * @return User object associated with this signup.
	 */
	public User getStudent() {
		return student;
	}
	/**
	 * Currently unused.
	 * Changes the status of a signup from active to inactive.
	 * Then, prints a confirmation message and information to User about rescinding the signup.
	 */
	public void cancelSignup() {
		this.status = false;
		System.out.println("You have successfully cancelled your attendance for " + this.camp.getName());
		System.out.println("You may not sign up for this event again.");
	}

	/**
	 * Code elsewhere iterates through the signups to find signups of a particular User.
	 * This method helps with that.
	 * @param student String userID of the student to match with the signup.
	 * @return Boolean on whether this signup is made by the student.
	 */
	public boolean matchStudent(String student) { //USE STUDENT ID
		if(this.student.getID().equals(student)) {
			return(true);
		}
		return(false);
	}
	/**
	 * Code elsewhere iterates through the signups to find signups of a particular Camp.
	 * This method helps with that.
	 * @param camp Camp object to match with the signup.
	 * @return Boolean on whether this signup is for that particular camp.
	 */
	public boolean matchCamp(Camp camp) { //USE CAMP  OBJECT
		if(this.camp.getID() == camp.getID()) {
			return(true);
		}
		return(false);
	}
}
