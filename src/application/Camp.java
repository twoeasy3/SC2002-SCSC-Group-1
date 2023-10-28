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
	//TODO implement committee and attendee slots
	
	
	public Camp(String name, String faculty, LocalDate start, LocalDate end, LocalDate regEnd,String desc, String loc, int maxSlots, int maxComm, String creator) {
		this.name = name;
		this.faculty = faculty; //Let's use this field =ALL to indicate no faculty restrictions
		//TODO code an index for campID
		this.id = 0; //placeholder
		this.startDate = start;
		this.endDate = end;
		this.regEnd = regEnd;
		this.location = loc;
		this.description = desc;
		this.maxSize = maxSlots;
		this.maxComm = maxComm;
		this.inCharge = creator;
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
	
	public boolean checkEligibility(String faculty) {
		if(this.faculty.equals(faculty) || this.faculty.equals("ALL")) {
			return true;
		}
		return false;
	}
	public boolean checkClash(LocalDate start1, LocalDate end1) {
		if(start1.isAfter(this.endDate) || end1.isBefore(this.startDate)) {
			return false;
		}
		return true;
	}
	
	

}
