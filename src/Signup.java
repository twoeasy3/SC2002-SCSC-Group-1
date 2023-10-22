
public class Signup {
	private Student student;
	private Camp camp;
	private boolean	status; //TRUE if signed up and attending; FALSE if signed up but subsequently cancelled
	public Signup(Student student,Camp camp) {
		this.student = student;
		this.camp = camp;
		this.status = true;
	}
	public void cancelSignup() {
		this.status = false;
		System.out.println("You have successfully cancelled your attendance for " + this.camp.getName());
		System.out.println("You may not sign up for this event again.");
	}
	

}
