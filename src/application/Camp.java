package application;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.*;

/**
 * Representation of a camp event.
 * Many-to-Many relation with Student, One-to-Many relation with Staff.
 * Camps/Events last a certain duration in discrete day(s).
 * The Users involved can be a Staff-in-Charge, Attendee or Committee Member.
 * Relations with Students are handled by the Signup class.
 */
public class Camp {
	/**
	 * Name of the camp for display.
	 * Due to current implementation, no commas in this field are allowed.
	 */
	private String name;
	/**
	 * String identifier for the faculty that the camp is open to.
	 * A full list of faculties in use by CAMs are:
	 * ADM,ASE,CCEB,CEE,EEE,LKC,MAE,MSE,NBS,NIE,SBS,SCSE,SOH,SPMS,SSS,WKWSCI.
	 * A value of ALL will indicate any User from any faculty is allowed to be involved.
	 */
	private String faculty;
	/**
	 * Incrementing integer identifier for the camp.
	 * Unseen by the User. Only for backend and for data storage.
	 */
	private int id; //identifier for camp
	/**
	 * Maximum slots available for Students for the camp.
	 * Does not take into account withdrawn signups.
	 */
	private int maxSize;
	/**
	 * Maximum slots available for Committee members for the camp.
	 * Set by creator, but should scale in size with camp size and duration.
	 */
	private int maxComm;
	/**
	 * The date the event starts on.
	 * A camp starting on the same date another ends is considered a clash.
	 */
	private LocalDate startDate;
	/**
	 * The date the event ends on.
	 * May be the same date as startDate, for a 1-day event.
	 * Can never be a date before startDate.
	 */
	private LocalDate endDate;
	/**
	 * The deadline for registration.
	 * Can never be a date after startDate.
	 */
	private LocalDate regEnd;
	/**
	 * The userID of the Staff who created the camp.
	 * This field is used to determine access and edit privileges.
	 */
	private String inCharge; //creator of camp
	/**
	 * A String of the location name the camp is held at.
	 * Due to current implementation, no commas in this field are allowed.
	 */
	private String location;
	/**
	 * A String of a short description of the camp.
	 * IMPORTANT: Due to current implementation, no commas in this field are allowed.
	 */
	private String description;
	/**
	 * Value to toggle visibility of the camp from Students.
	 * All Staff are still free to view.
	 */
	private boolean visible;
	/**
	 * A list of enquiries associated with this camp
 	 */ 
	private ArrayList<Enquiry> enquiryList;
	private List<Student> attendeeList;
	private List<Student> blackList;
	private List<Student> committeeList;

	/**
	 *
	 * @param ID
	 * @param name
	 * @param faculty
	 * @param start
	 * @param end
	 * @param regEnd
	 * @param desc
	 * @param loc
	 * @param maxSlots
	 * @param maxComm
	 * @param creator
	 * @param visible
	 */
	public Camp(int ID, String name, String faculty, LocalDate start, LocalDate end, LocalDate regEnd,
				String loc, String desc, int maxSlots, int maxComm, String creator, int visible) {
		this.id = ID;
		this.name = name;
		this.faculty = faculty;
		//TODO code an index for campID
		this.startDate = start;
		this.endDate = end;
		this.regEnd = regEnd;
		this.location = loc;
		this.description = desc;
		this.maxSize = maxSlots;
		this.maxComm = maxComm;
		this.inCharge = creator;
		this.visible = (visible==1);
		this.enquiryList = new ArrayList<Enquiry>(0);
		this.attendeeList = new ArrayList<>();
		this.blackList = new ArrayList<>();
		this.committeeList = new ArrayList<>();
	}

	public String getName() {
		return this.name;
	}

	public int getID() {
		return this.id;
	}

	public LocalDate getStart() {
		return this.startDate;
	}

	public LocalDate getEnd() {
		return this.endDate;
	}

	public LocalDate getRegEnd() {return regEnd;}
	public String getLocation()	{return location;}
	public String getDescription(){return description;}
	public int getMaxSize(){return maxSize;}
	public int getMaxComm(){return maxComm;}
	public String getFaculty() {
		return this.faculty;
	}
	public boolean isVisible(){return this.visible;}
	public String getInCharge(){return this.inCharge;}
	public int getAttendeeCount(){return attendeeList.size();}
	public int getCommitteeCount(){return committeeList.size();}
	public boolean isFull(){return (attendeeList.size()>= this.maxSize - this.maxComm);};
	public boolean isFullCommittee(){return (committeeList.size() >= this.maxComm);}
	
	public boolean checkEligibility(String faculty) {
		if(this.faculty.equals(faculty) || this.faculty.equals("ALL")) {
			return true;
		}
		return false;
	}
	public boolean checkClash(LocalDate start1, LocalDate end1) { //TODO check if this is correct
		if(start1.isBefore(this.endDate.plusDays(1)) && //if either start date falls between the duration of the other event
				start1.isAfter(this.startDate.plusDays(-1))||
				this.startDate.isBefore(end1.plusDays(1))&&
						this.startDate.isAfter(this.startDate.plusDays(-1))){
			return true; //this is clash
		}
		return false;
	}
	/**
	 * Appends a student to the internal list of attendee members for the camp.
	 * Conditions to do so should be checked elsewhere.
	 * @param student Student appended to list
	 */
	public void addAttendee(Student student){
		this.attendeeList.add(student);
	}

	/**
	 * Appends a student to the internal list of committee members for the camp.
	 * Conditions to do so should be checked elsewhere.
	 * @param student Student appended to list
	 */
	public void addCommittee(Student student){
		this.committeeList.add(student);
	}
	/**
	 * Removes a student from the internal list of attendees for the camp.
	 * Conditions to do so should be checked elsewhere.
	 * @param student Student to be removed
	 */
	public void removeAttendee(Student student){
		if(attendeeList.contains(student)){
		attendeeList.remove(student);
		blackList.add(student);
		}
	}
	public boolean isAttending(Student student){
		return(attendeeList.contains(student));
	}
	public boolean isBlacklisted(Student student){
		return(blackList.contains(student));
	}
	// access enquiry list


	public ArrayList<Enquiry> getEnquiryList() {
		return enquiryList;
	}
}
