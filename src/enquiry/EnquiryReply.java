package enquiry;

import application.Camp;
import application.StudentCommittee;
import application.User;
import helper.Console;
import helper.DataHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements all the methods related to replying to enquiries.
 */
public class EnquiryReply {
    /**
     * Returns a list of all unreplied enquiries from a camp
     * @param camp Camp to pull enquiries for
     * @return List of all unreplied enquiries from a camp
     */
    public static List<EnquiryAbstract> getCampEnquiries(Camp camp){
        List<EnquiryAbstract> noReplyEnquiries = new ArrayList<>();
        for (EnquiryAbstract enquiry : camp.getEnquiryList()){
            if(!enquiry.getResolved()){
                noReplyEnquiries.add(enquiry);
            }
        }
        return noReplyEnquiries;
    }

    /**
     * Menu interface for making a reply for Enquiries<br>
     * Outputs all unreplied Enquiries, then select one to reply to<br>
     * @param camp Camp object of the Enquiries to reply to
     * @param activeUser Current user doing the replies
     * @param enquiryList List of all Enquiries, to parse the enquiries from
     */
    public static void replyMenu(Camp camp, User activeUser, List<EnquiryAbstract> enquiryList ) {
        while(true) {
            EnquiryAbstract selectedEnquiry = null;
            List<EnquiryAbstract> eligibleEnquiries = EnquiryReply.getCampEnquiries(camp);
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

    /**
     * Provides the interface and logic from which to reply to the selected Enquiry
     * @param enquiry Enquiry to reply to
     * @param replyAuthor User who is doing the replying
     * @param enquiryList List of all Enquiries to save program state
     */
    public static void replyTo(EnquiryAbstract enquiry, User replyAuthor, List<EnquiryAbstract> enquiryList) {
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
