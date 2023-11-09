package enquiry;

import application.Camp;
import application.StudentCommittee;
import application.User;
import helper.Console;
import helper.DataHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class EnquiryReply {

    public static List<Enquiry> getCampEnquiries(Camp camp){
        List<Enquiry> noReplyEnquiries = new ArrayList<>();
        for (Enquiry enquiry : camp.getEnquiryList()){
            if(!enquiry.getResolved()){
                noReplyEnquiries.add(enquiry);
            }
        }
        return noReplyEnquiries;
    }
    public static void replyMenu(Camp camp, User activeUser, List<Enquiry> enquiryList ) {
        while(true) {
            Enquiry selectedEnquiry = null;
            List<Enquiry> eligibleEnquiries = EnquiryReply.getCampEnquiries(camp);
            EnquiryView.viewRelevantEnquiries(eligibleEnquiries);
            selectedEnquiry = EnquiryView.selectEnquiry(eligibleEnquiries);
            if (selectedEnquiry == null) {
                System.out.println("No enquiries found for editing.");
                String response = Console.nextString();
                return;
            }
            System.out.println(EnquiryView.singleEnquiryToString(selectedEnquiry, true));
            System.out.println("1. Reply to this enquiry, 2. Select a new enquiry or 3. Exit");
            int selection = Console.nextInt();
            switch (selection) {
                case 1:
                    EnquiryReply.replyTo(selectedEnquiry,activeUser, enquiryList);
                    return;
                case 2:
                    break;
                case 3:
                    System.out.println("Exiting");
                    return;
                default:
                    break;
            }
        }
    }


    public static void replyTo(Enquiry enquiry, User replyAuthor, List<Enquiry> enquiryList) {
        if (enquiry.getResolved()) {
            System.out.println("Reply not possible, enquiry already replied to!");
        } else {

            System.out.println("Enter your reply to this: ");
            String response = Console.nextString();
            enquiry.setReply(response);
            enquiry.setReplyAuthor(replyAuthor);
            enquiry.setResolved(true);
            if(replyAuthor instanceof StudentCommittee){
                ((StudentCommittee)replyAuthor).addPoints(1);
            }
            DataHandler.saveEnquiries(enquiryList);
        }

    }
}
