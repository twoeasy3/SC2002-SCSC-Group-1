package application;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.*;

public class Camp {
	private String name;
	private String faculty;
	private int id; //identifier for camp 
	private int maxSize;
	private int maxComm;
	private LocalDate startDate;
	private LocalDate endDate;
	private LocalDate regEnd;
	private String inCharge; //creator of camp
	private List<Student> committeeList;
	private String location;
	private String description;
	private boolean visible;
	//TODO implement committee and attendee slots
	
	
	public Camp(int ID, String name, String faculty, LocalDate start, LocalDate end, LocalDate regEnd,
				String desc, String loc, int maxSlots, int maxComm, String creator, int visible) {
		this.id = ID;
		this.name = name;
		this.faculty = faculty; //Let's use this field =ALL to indicate no faculty restrictions
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
	public String getFaculty() {
		return this.faculty;
	}
	public boolean isVisible(){return this.visible;}
	public String getInCharge(){return this.inCharge;}
	
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
	
	

}
