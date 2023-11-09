package application;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public abstract class CampView {
    static void printEnquiries(Camp camp){
        System.out.println("Enquiries: ");
        if(camp.getEnquiryList().size()==0){
            System.out.println("No enquiries for this camp yet.");
            return;
        }
        for(Enquiry enquiry: camp.getEnquiryList()){
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
