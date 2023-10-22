import java.time.*;
import java.time.temporal.*;

public class Camp {
	String name;
	String faculty;
	int id; //identifier for camp 
	int maxSize;
	LocalDate startDate;
	LocalDate endDate;
	String inCharge; //creator of camp
	
	String location;
	String description;
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

}
