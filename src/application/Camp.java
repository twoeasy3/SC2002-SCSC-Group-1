package application;
import enquiry.Enquiry;
import enquiry.EnquiryAbstract;
import suggestions.Suggestion;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The various states that a Camp can be, in real-time.<br>
 */
enum CampStatus {
	/**
	 *  OPEN - Registration date has not passed.
	 */
	OPEN,
	/**
	 * CLOSED - Registration date has passed, but Start Date has not passed.
	 */
	CLOSED,
	/**
	 * ONGOING - Start Date has passed, but End Date has not passed.
	 */
	ONGOING,
	/**
	 * ENDED - End Date has passed.
	 */
	ENDED}


/**
 * Representation of a camp event.
 * Many-to-Many relation with Student, One-to-Many relation with Staff.<br>
 * Camps/Events last a certain duration in discrete day(s).<br>
 * The Users involved can be a Staff-in-Charge, Attendee or Committee Member.<br>
 * Relations with Students are handled by the Signup class.<br>
 */
public class Camp {
	/**
	 * Keeps track of the highest value seen so far.
	 * When a new Camp is created, it will use this value +1.<br>
	 * This means that IDs may be skipped if Camps were deleted after the fact.
	 *
	 */
	private static int globalIDCounter = -1;
	/**
	 * Name of the camp for display.
	 * Due to current implementation, no commas in this field are allowed.
	 */
	private String name;
	/**
	 * String identifier for the faculty that the camp is open to.
	 * A full list of faculties in use by CAMs are:<br>
	 * ADM,ASE,CCEB,CEE,EEE,LKC,MAE,MSE,NBS,NIE,SBS,SCSE,SOH,SPMS,SSS,WKWSCI.<br>
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
	 * A list of enquiries associated with this camp. The Enquiry should still exist in the master EnquiriesList.
 	 */ 
	private ArrayList<EnquiryAbstract> enquiryList;
	/**
	 * A list of Suggestions associated with this camp. The Suggestion should still exist in the master SuggestionsList.
	 */
	private ArrayList<Suggestion> suggestionList;
	/**
	 * List of all Students attending the Camp
	 */
	private List<Student> attendeeList;
	/**
	 * List of all Students blacklisted from the Camp
	 */
	private List<Student> blackList;
	/**
	 * List of all Students who are Camp Committee members
	 */
	private List<Student> committeeList;

	/**
	 * Representation of a Camp in CAMs.
	 * Each Camp should be added to the master CampList to be parsed properly in CAMs.
	 * @param ID Integer ID to be assigned to the Camp. Either derived from the data .csv or by getGlobalIDCounter()
	 * @param name String name to be assigned to the Camp.
	 * @param faculty String of Faculty code that the Camp is designated.
	 * @param start LocalDate of the Start date
	 * @param end LocalDate of the End date
	 * @param regEnd LocalDate of the Registration End date
	 * @param desc String description of the camp
	 * @param loc String location/venue of the camp
	 * @param maxSlots Integer Max Slots of the camp. This count includes Attendee + Committee
	 * @param maxComm Integer of how many of the slots are exclusively for Committee members.
	 * @param creator UserID of the creator of the camp.
	 * @param visible Boolean on whether the camp is visible to students
	 */
	public Camp(int ID, String name, String faculty, LocalDate start, LocalDate end, LocalDate regEnd,
				String loc, String desc, int maxSlots, int maxComm, String creator, int visible) {
		this.id = ID;
		this.name = name;
		this.faculty = faculty;
		this.startDate = start;
		this.endDate = end;
		this.regEnd = regEnd;
		this.location = loc;
		this.description = desc;
		this.maxSize = maxSlots;
		this.maxComm = maxComm;
		this.inCharge = creator;
		this.visible = (visible==1);
		this.enquiryList = new ArrayList<EnquiryAbstract>(0);
		this.suggestionList = new ArrayList<Suggestion>(0);
		this.attendeeList = new ArrayList<>();
		this.blackList = new ArrayList<>();
		this.committeeList = new ArrayList<>();
		if(this.id > this.globalIDCounter){
			this.globalIDCounter = this.id;
		}
	}

	/**
	 * Returns camp name
	 * @return String of camp name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns list of all Suggestion items tied to this camp
	 * @return List of all Suggestion items tied to this camp
	 */
	public ArrayList<Suggestion> getSuggestionList() {
		return suggestionList;
	}

	/**
	 * Gets the camp ID number
	 * @return CampID of this camp
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Sets the location of the camp
	 * @param location New location string
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Sets the name of the camp
	 * @param name New Camp Name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the description of the camp
	 * @param description New camp description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the max slots of the camp
	 * @param maxSize New integer max size
	 */
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	/**
	 * Sets the max committee slots of the camp
	 * @param maxComm New integer max committee slots
	 */
	public void setMaxComm(int maxComm) {
		this.maxComm = maxComm;
	}

	/**
	 * Fetches the Start Date of the camp
	 * @return LocalDate of the Start date
	 */
	public LocalDate getStart() {
		return this.startDate;
	}

	/**
	 * Sets the Start Date of the camp
	 * @param startDate new LocalDate start date
	 */
	public void setStart(LocalDate startDate){this.endDate = startDate;}

	/**
	 * Fetches the End Date of the camp
	 * @return LocalDate of the End date
	 */
	public LocalDate getEnd() {
		return this.endDate;
	}

	/**
	 * Sets the End Date of the camp
	 * @param endDate new LocalDate end date
	 */
	public void setEnd(LocalDate endDate){this.endDate = endDate;}

	/**
	 * Fetches the Registration End Date of the camp.
	 * @return LocalDate of the Registration End Date
	 */
	public LocalDate getRegEnd() {return regEnd;}

	/**
	 * Sets the Registration End Date of the Camp
	 * @param regEnd LocalDate of the new Registration End Date
	 */
	public void setRegEnd(LocalDate regEnd){this.regEnd = regEnd;}

	/**
	 * Fetches the Location of the camp
	 * @return String name of the location
	 */
	public String getLocation()	{return location;}

	/**
	 * Fetches the Description of the camp
	 * @return String of the description
	 */
	public String getDescription(){return description;}

	/**
	 * Fetches the max slots of the camp
	 * @return Integer of the max slots of the camp
	 */
	public int getMaxSize(){return maxSize;}

	/**
	 * Fetches the max committee slots of the camp
	 * @return Integer of the max committee slots of the camp
	 */
	public int getMaxComm(){return maxComm;}

	/**
	 * Fetches the faculty the camp is open to
	 * @return String of the faculty
	 */
	public String getFaculty() {return this.faculty;}

	/**
	 * Fetches the visibility of the camp
	 * @return Boolean of the visibility, TRUE for visible
	 */
	public boolean isVisible(){return this.visible;}

	/**
	 * Sets the visibility of the camp
	 * @param visible Boolean visibility value. TRUE for visible.
	 */
	public void setVisibility(boolean visible){this.visible = visible;}

	/**
	 * Fetches the userID of the Staff in-charge of the camp.
	 * @return String userID of the Staff in-charge
	 */
	public String getInCharge(){return this.inCharge;}

	/**
	 * Gets the number of Attendees in the camp
	 * @return Integer value of the attendeeList size
	 */
	public int getAttendeeCount(){return attendeeList.size();}

	/**
	 * Gets the number of Committee in the camp
	 * @return Integer value of the committeeList size
	 */
	public int getCommitteeCount(){return committeeList.size();}

	/**
	 * Checks if camp is full (for attendees)
	 * @return Boolean value for camp full, TRUE denotes full
	 */
	public boolean isFull(){return (attendeeList.size()>= this.maxSize - this.maxComm);};

	/**
	 * Checks if camp is full (for committee)
	 * @return Boolean value for camp committee full, TRUE denotes full
	 */
	public boolean isFullCommittee(){return (committeeList.size() >= this.maxComm);}

	/**
	 * Fetches the current highest global ID count found by CAMs so far.
	 * @return Integer of the highest camp ID count
	 */
	public static int getGlobalIDCounter(){return globalIDCounter;}

	/**
	 * Checks if a camp is open to a certain faculty.
	 * @param faculty Faculty to check for
	 * @return Boolean value on whether that faculty is eligible
	 */
	public boolean checkEligibility(String faculty) {
		if(this.faculty.equals(faculty) || this.faculty.equals("ALL")) {
			return true;
		}
		return false;
	}

	/**
	 * Checks for clashes between this camp and another start and end date.<br>
	 * A Start Date falling on the same day and the other camp's End Date is considered a clash.
	 * @param start1 Start date of the other camp.
	 * @param end1 End date of the other camp.
	 * @return Boolean on whether the camps clash. Returns true for clash.
	 */
	public boolean checkClash(LocalDate start1, LocalDate end1) {
		if(start1.isBefore(this.endDate) && this.startDate.isBefore(end1)){
			return true;
		}
		return false;
	}

	/**
	 * Returns the state that a Camp is in real-time.<br>
	 * OPEN - Registration date has not passed. <br>
	 * CLOSED - Registration date has passed, but Start Date has not passed.<br>
	 * ONGOING - Start Date has passed, but End Date has not passed.<br>
	 * ENDED - End Date has passed.<br>
	 * @return The campStatus of the camp
	 */
	public CampStatus checkCampStatus(){
		if(LocalDate.now().isBefore(this.regEnd)){ //Open for Registration
			return CampStatus.OPEN;
		}
		else if(LocalDate.now().isAfter(this.regEnd) && LocalDate.now().isBefore(this.startDate)){ //Closed for registration but not started
			return CampStatus.CLOSED;
		}
		else if(!LocalDate.now().isBefore(this.startDate)&&!LocalDate.now().isAfter(this.endDate)){ //Camp ongoing
			return CampStatus.ONGOING;
		}
		else{
			return CampStatus.ENDED; //Camp ended
		}
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
	 * Moves a Student from the internal list of attendees to the list of committee.
	 * Other actions that are required (deleting of the Signup, changing Student variables) are not performed here.
	 * @param student Student to be moved.
	 */
	public void promoteToComittee(Student student){
		this.attendeeList.remove(student);
		this.committeeList.add(student);
	}
	/**
	 * Removes a student from the internal list of attendees for the camp.
	 * Adds student to blacklist.
	 * Conditions to do so should be checked elsewhere.
	 * @param student Student to be removed
	 */
	public void removeAttendee(Student student){
		if(attendeeList.contains(student)){
		attendeeList.remove(student);
		blackList.add(student);
		}
	}

	/**
	 * Only adds student to blacklist.
	 * Used when loading program state from files.
	 * @param student Student who already has a cancelled signup from file.
	 */
	public void addToBlackList(Student student){
		blackList.add(student);
	}

	/**
	 * Fetches the blacklist of students barred from signing up
	 * @return List of blacklisted students
	 */
	public List<Student> getBlackList(){return this.blackList;}

	/**
	 * Check if a specified Student is in the camp as an Attendee.
	 * @param student Student to check for
	 * @return Boolean value of whether they are attending or not.
	 */
	public boolean isAttending(Student student){
		return(attendeeList.contains(student));
	}
	/**
	 * Check if a specified Student is in the camp blacklist.
	 * @param student Student to check for
	 * @return Boolean value of whether they are blacklisted or not.
	 */
	public boolean isBlacklisted(Student student){
		return(blackList.contains(student));
	}

	/**
	 * Fetches the list of Enquiries associated with the camp
	 * @return enquiryList of all Enquiries associated with this camp
	 */
	public ArrayList<EnquiryAbstract> getEnquiryList() {
		return enquiryList;
	}

	/**
	 * Fetches the list of Students attending the camp
	 * @return attendeeList of all Students attending the camp
	 */
	public List<Student> getAttendeeList() {return this.attendeeList;}

	/**
	 * Fetches the list of Committee of the camp
	 * @return committeeList of all Students part of the camp committee
	 */
	public List<Student> getCommitteeList() {return this.committeeList;}

	/**
	 * Adds an Enquiry to the Camp's internal enquiry list. This is only for CAMs to work on in memory and does not contribute to the saved state.
	 * @param enquiry Enquiry to be added.
	 */
	public void addEnquiry(EnquiryAbstract enquiry){
		enquiryList.add(enquiry);
	}
	/**
	 * Adds a Suggestion to the Camp's internal suggestion list. This is only for CAMs to work on in memory and does not contribute to the saved state.
	 * @param suggestion Enquiry to be added.
	 */
	public void addSuggestion(Suggestion suggestion){suggestionList.add(suggestion);}
}
