package application;
import java.time.*;
import java.time.temporal.*;

public class Camp {
	private String name;
	private String faculty;
	private int id; //identifier for camp 
	private int maxSize;
	private LocalDate startDate;
	private LocalDate endDate;
	private String inCharge; //creator of camp
	
	private String location;
	private String description;
	//TODO implement committee and attendee slots
	
	
	public Camp(String name, String faculty, int size, LocalDate start, LocalDate end, String creator) {
		this.name = name;
		this.faculty = faculty; //Let's use this field =ALL to indicate no faculty restrictions
		//TODO code an index for campID
		this.id = 0; //placeholder
		this.maxSize = size;
		this.startDate = start;
		this.endDate = end;
		this.inCharge = creator;
	}
	
	public String getName() {
		return this.name;
	}
	public int getID() {
		return this.id;
	}
	/*public boolean checkClash(LocalDate start1, LocalDate end1) {
		if(start1.after(this.startDate)&&)
	}*/
	
	

}
