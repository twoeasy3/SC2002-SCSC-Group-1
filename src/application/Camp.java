package application;
import java.lang.reflect.Array;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.*;

enum campStatus{
	OPEN,CLOSED,ONGOING,ENDED}


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
	private ArrayList<Suggestion> suggestionList;
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
		this.suggestionList = new ArrayList<Suggestion>(0);
		this.attendeeList = new ArrayList<>();
		this.blackList = new ArrayList<>();
		this.committeeList = new ArrayList<>();
	}

	public String getName() {
		return this.name;
	}

	public ArrayList<Suggestion> getSuggestionList() {
		return suggestionList;
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
	 * Returns a status for the camp relative to the date today.
	 * @return 0 for Open registration, 1 for Closed, 2 for Ongoing and 3 for Ended
	 */
	public campStatus checkCampStatus(){
		if(LocalDate.now().isBefore(this.regEnd)){ //Open for Registration
			return campStatus.OPEN;
		}
		else if(LocalDate.now().isAfter(this.regEnd) && LocalDate.now().isBefore(this.startDate)){ //Closed for registration but not started
			return campStatus.CLOSED;
		}
		else if(!LocalDate.now().isBefore(this.startDate)&&!LocalDate.now().isAfter(this.endDate)){ //Camp ongoing
			return campStatus.ONGOING;
		}
		else{
			return campStatus.ENDED; //Camp ended
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
	public boolean isAttending(Student student){
		return(attendeeList.contains(student));
	}
	public boolean isBlacklisted(Student student){
		return(blackList.contains(student));
	}
	public void showSummary(){
		System.out.println("=============================================");
		System.out.println(this.name + " (" + this.faculty + ")");
		System.out.println("Registration End Date: " + this.regEnd.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
		System.out.println("Start Date: " + this.startDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
		System.out.println("End Date: " + this.endDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
		System.out.println("Current attendee capacity: " + this.getAttendeeCount() + "/" + (this.maxSize - this.maxComm));
		System.out.println("Current committee strength: " + this.getCommitteeCount() + "/" + this.maxComm);
		System.out.println("Venue: " + this.location);
		System.out.println(this.description);
		System.out.println("=============================================");
		this.printEnquiries();
	}
	public void printEnquiries(){
		System.out.println("Enquiries: ");
		if(enquiryList.size()==0){
			System.out.println("No enquiries for this camp yet.");
			return;
		}
		for(Enquiry enquiry:enquiryList){
			System.out.println(enquiry.getAuthor().getName()+ " (" + enquiry.getAuthor().getFaculty() +"): ");
			System.out.println(enquiry.getDescription());
			if(enquiry.getResolved()){
				if(enquiry.getReplyAuthor() instanceof Staff){
					System.out.println(enquiry.getReplyAuthor().getName()+ " (" + enquiry.getReplyAuthor().getFaculty() +") [INCHARGE]: ");
				}
				else{
					System.out.println(enquiry.getReplyAuthor().getName()+ " (" + enquiry.getReplyAuthor().getFaculty() +") [COMMITTEE]: ");
				}
				System.out.println(enquiry.getReply());
			}
			else{
				System.out.println("No reply yet.");
			}
			System.out.println("-----------------------");
		}
	}

	public boolean tryEditCamp(int category, String change) { //true for success, false for failure
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate proposedDate;
		int proposed;
		switch (category) {
			case 1: //NAME
				this.name = change;
				System.out.println("Camp updated. New name: " + this.name);
				return true;
			case 2: //VENUE
				this.location = change;
				System.out.println("Camp updated. New venue: " + this.location);
				return true;
			case 3: //DESCRIPTION
				this.description = change.replace(",", "");
				System.out.println("Camp updated. New description: ");
				System.out.println(this.description);
				return true;
			case 4: //MAX SLOTS; CONDITIONAL
				if(!Helper.checkInputIntValidity(change)){
					System.out.println("Input error. Not valid integer. Camp not updated.");
					return false;
				}
				proposed = Integer.parseInt(change);
				if(proposed < 10){
					System.out.println("Camp not updated. You can't create a camp with fewer than 10 slots!");
					return false;
				}
				else if(proposed < this.getAttendeeCount() + this.maxComm ){
					System.out.println("Camp not updated. Invalid input, current signups mean you need at least " + this.getAttendeeCount() + this.maxComm + " slots!");
					return false;
				}
				else if(proposed > 24757){
					System.out.println("Camp not updated. Your camp cannot have more open slots than NTU's enrolment this AY!");
					return false;
				}
				else{
					this.maxSize = proposed;
					System.out.println("Camp updated. New Max Slots: " + this.maxSize);
					return true;
				}
			case 5: //MAX COMM; CONDITIONAL
				if(!Helper.checkInputDateValidity(change)){
					System.out.println("Input error. Not valid integer. Camp not updated.");
					return false;
				}
				proposed = Integer.parseInt(change);
				if(proposed > 10){
					System.out.println("Camp not updated. You can't have more than 10 committee slots!");
					return false;
				}
				else if(proposed < this.committeeList.size()){
					System.out.println("Camp not updated. You already have " + this.committeeList.size() + " committee members!");
					return false;
				}
				else{
					this.maxComm = proposed;
					System.out.println("Camp updated. New Max Committee Slots: " + this.maxComm);
					return true;
				}
			case 6: //START DATE; ONLY IF EMPTY CAMP
				if (!Helper.checkInputDateValidity(change)) {
					System.out.println("General Date Error: Camp was not edited.");
					return false;
				}
				proposedDate = LocalDate.parse(change, formatter);
				if (proposedDate.isAfter(this.endDate)) {
					System.out.println("Date Error: Start date is after End Date. Camp was not edited.");
					return false;
				}
				if (proposedDate.isBefore(this.regEnd)) {
					System.out.println("Date Error: Start date before Registration End Date. Camp was not edited.");
					return false;
				} else {
					this.startDate = proposedDate;
					System.out.println("Camp updated. New start date: " + this.startDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
					return true;
				}
			case 7://END DATE; ONLY IF EMPTY CAMP
				if (!Helper.checkInputDateValidity(change)) {
					System.out.println("General Date Error: Camp was not edited.");
					return false;
				}
				proposedDate = LocalDate.parse(change, formatter);
				if (proposedDate.isBefore(this.startDate)) {
					System.out.println("Date Error: End date is before Start Date. Camp was not edited.");
					return false;
				}
				if (proposedDate.isBefore(this.regEnd)) {
					System.out.println("Date Error: Start date before Registration End Date. Camp was not edited.");
					return false;
				} else {
					this.endDate = proposedDate;
					System.out.println("Camp updated. New end date: " + this.endDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
					return true;
				}
			case 8: //REG END, ONLY IF EMPTY
				if (!Helper.checkInputDateValidity(change)) {
					System.out.println("General Date Error: Camp was not edited.");
					return false;
				}
				proposedDate = LocalDate.parse(change, formatter);
				if (proposedDate.isAfter(this.startDate)) {
					System.out.println("Date Error: Reg End date is after Start Date. Camp was not edited.");
					return false;
				} else {
					this.regEnd = proposedDate;
					System.out.println("Camp updated. New Reg End date: " + this.regEnd.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
					return true;
				}
			case 9: //VISIBILITY, ONLY IF EMPTY
				if(Helper.parseUserBoolInput(change) == -1){
					System.out.println("Camp not updated.");
					return false;
				}
				else if(Helper.parseUserBoolInput(change) == 1){
					this.visible = true;
					System.out.println("Camp updated and is now visible to eligible students.");
					return true;
				}
				else{
					this.visible = false;
					System.out.println("Camp updated and is no longer visible to students.");
					return true;
				}
			default:
				System.out.println("Error editing camp.");
				return false;
		}
	}
	// access enquiry list


	public ArrayList<Enquiry> getEnquiryList() {
		return enquiryList;
	}
	public List<Student> getAttendeeList() {return this.attendeeList;}
	public List<Student> getCommitteeList() {return this.committeeList;}
	public void addEnquiry(Enquiry enquiry){
		enquiryList.add(enquiry);
	}
}
