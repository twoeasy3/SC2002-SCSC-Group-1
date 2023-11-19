package application;

import enquiry.*;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Contains a couple of methods that assist in outputting elements of a Camp to the command line.
 */
public class CampView {
    /**
     * Prints all enquiries for a single camp. Calls EnquiryView.singleEnquiryToString for all enquiries in the camp.
     * @param camp Camp to populate screen with enquiries of.
     */
    static void printEnquiries(Camp camp){
        System.out.println("Enquiries: ");
        if(camp.getEnquiryList().size()==0){
            System.out.println("No enquiries for this camp yet.");
            return;
        }
        for(Enquiry enquiry: camp.getEnquiryList()){
            System.out.println( EnquiryView.singleEnquiryToString(enquiry,true));
        }
    }

    /**
     * Prints out the summary of a camp when a camp is selected in various parts of CAMs<br>
     * Prints out the following:
     * 
     * Camp Name (Faculty)<br>
     * Registration End Date<br>
     * Start Date<br>
     * End Date<br>
     * Current attendee capacity<br>
     * Current committee capacity<br>
     * Venue<br>
     * Description<br>
     * 
     *
     * @param camp Camp to print summary for
     */
    static void showSummary(Camp camp){
        System.out.println("=============================================");
        System.out.println(camp.getName() + " (" + camp.getFaculty() + ")");
        System.out.println("Registration End Date: " + camp.getRegEnd().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
        System.out.println("Start Date: " + camp.getStart().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
        System.out.println("End Date: " + camp.getEnd().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
        System.out.println("Current attendee capacity: " + camp.getAttendeeCount() + "/" + (camp.getMaxSize() - camp.getMaxComm()));
        System.out.println("Current committee strength: " + camp.getCommitteeCount() + "/" + camp.getMaxComm());
        System.out.println("Venue: " + camp.getLocation());
        System.out.println(camp.getDescription());
        System.out.println("=============================================");
        printEnquiries(camp);
    }
}
