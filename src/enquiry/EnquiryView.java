package enquiry;
import helper.Console;
import application.Staff;
import application.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EnquiryView {

    public static String singleEnquiryToString(Enquiry enquiry, boolean printNoReplyLine) {
        String printLine = "";
        printLine += (enquiry.getAuthor().getName() + " (" + enquiry.getAuthor().getFaculty() + "): \n");
        printLine += (enquiry.getDescription() + "\n");
        if (enquiry.getResolved()) {
            if (enquiry.getReplyAuthor() instanceof Staff) {
                printLine += (enquiry.getReplyAuthor().getName() + " (" + enquiry.getReplyAuthor().getFaculty() + ") [INCHARGE]: \n");
            } else {
                printLine += (enquiry.getReplyAuthor().getName() + " (" + enquiry.getReplyAuthor().getFaculty() + ") [COMMITTEE]: \n");
            }
            printLine += (enquiry.getReply()+"\n");
        } else {
            if(printNoReplyLine){ printLine += ("No reply yet. \n");}
        }
        printLine += ("-----------------------");
        return(printLine);
    }

    public static List<Enquiry> getRelevantEnquiries(Student student, boolean resolvedOrNot){
        List<Enquiry> relevantEnquiries = new ArrayList<>();
        for (Enquiry enquiry : student.getEnquiryList()){
            if (enquiry.getResolved()==resolvedOrNot){
                relevantEnquiries.add(enquiry);
            }

        }
        return relevantEnquiries;
    }
    public static void viewRelevantEnquiries(List<Enquiry> eligibleEnquiries) {

        List<Enquiry> enquiryList = eligibleEnquiries;
        if(enquiryList.size()==0){
            System.out.println("No enquiries to show;");
            return;
        }
        Comparator<Enquiry> enquiryComparator = Comparator
                .comparing(enquiry -> enquiry.getCamp().getName());
        enquiryList.sort(enquiryComparator);
        String currentCampName = "";
        int i = 0;
        for (Enquiry enquiry : enquiryList){
            if(!currentCampName.equals(enquiry.getCamp().getName())){
                System.out.println(enquiry.getCamp().getName());
                currentCampName = enquiry.getCamp().getName();
            }
            i++;
            System.out.printf(i + ": ");
            System.out.println(singleEnquiryToString(enquiry,false));
        }

    }

    public static Enquiry selectEnquiry(List<Enquiry> eligibleEnquiries) {
        List<Enquiry> relevantEnquiries = eligibleEnquiries;
        if (relevantEnquiries.size() == 0) {
            return null;
        }

        System.out.println("Enter the number corresponding to the enquiry to select: ");
        int selection = Console.nextInt();
        while (true) {
            if (selection < 1 || selection > relevantEnquiries.size()) {
                System.out.println("Selection does not correspond to any inquiry on the list. ");
            } else {
                return relevantEnquiries.get(selection - 1);
            }

        }

    }
}
