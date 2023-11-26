package enquiry;
import helper.Console;
import application.Staff;
import application.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class implements methods relating to viewing of Enquiries
 */
public class EnquiryView {
    /**
     * Returns a preview of an enquiry to a String. Can be displayed or printed to report.<br>
     * Outputs the following:
     * Enquiry Author (faculty)<br>
     * Enquiry body text<br>
     * Reply Author (faculty,role), if applicable<br>
     * Reply body text, if applicable<br>
     * @param enquiry Enquiry to be output
     * @param printNoReplyLine Boolean to toggle whether to output reply line
     * @return Readable String output of the Enquiry
     */
    public static String singleEnquiryToString(EnquiryAbstract enquiry, boolean printNoReplyLine) {
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

    /**
     * Outputs a list of enquiries from a particular student, based on the value of resolvedOrNot. <br>
     * Returns all enquiries with the same resolved status.
     * @param student Student to pull enquiries from
     * @param resolvedOrNot Match of status of resolution to pull enquiries from
     * @return List of Enquiries that match the parameters
     */
    public static List<EnquiryAbstract> getRelevantEnquiries(Student student, boolean resolvedOrNot){
        List<EnquiryAbstract> relevantEnquiries = new ArrayList<>();
        for (EnquiryAbstract enquiry : student.getEnquiryList()){
            if (enquiry.getResolved()==resolvedOrNot){
                relevantEnquiries.add(enquiry);
            }

        }
        return relevantEnquiries;
    }

    /**
     * Helper method to output all the enquiries from a curated list. <br>
     * To be paired with selectEnquiry() for the selection functionality. <br>
     * @param eligibleEnquiries Curated list to output all enquiries, with an index number for each.
     */
    public static void viewRelevantEnquiries(List<EnquiryAbstract> eligibleEnquiries) {

        List<EnquiryAbstract> enquiryList = eligibleEnquiries;
        if(enquiryList.size()==0){
            System.out.println("No enquiries to show;");
            return;
        }
        Comparator<Enquiry> enquiryComparator = Comparator
                .comparing(enquiry -> enquiry.getCamp().getName());
        enquiryList.sort(enquiryComparator);
        String currentCampName = "";
        int i = 0;
        for (EnquiryAbstract enquiry : enquiryList){
            if(!currentCampName.equals(enquiry.getCamp().getName())){
                System.out.println(enquiry.getCamp().getName());
                currentCampName = enquiry.getCamp().getName();
            }
            i++;
            System.out.printf(i + ": ");
            System.out.println(singleEnquiryToString(enquiry,false));
        }

    }

    /**
     * Method to select an enquiry. The user should see the list from viewRelevantEnquiries() first. <br>
     * Reads an integer and selects the Enquiry that corresponds to it.
     * @param eligibleEnquiries Curated list of Enquiries to select from.
     * @return Enquiry object selected
     */
    public static EnquiryAbstract selectEnquiry(List<EnquiryAbstract> eligibleEnquiries) {
        List<EnquiryAbstract> relevantEnquiries = eligibleEnquiries;
        if (relevantEnquiries.size() == 0) {
            return null;
        }

        System.out.println("Enter the number corresponding to the enquiry to select: ");
        while (true) {
            int selection = Console.nextInt();
            if (selection < 1 || selection > relevantEnquiries.size()) {
                System.out.println("Selection does not correspond to any inquiry on the list. Please select again.");
            } else {
                return relevantEnquiries.get(selection - 1);
            }

        }

    }
}
