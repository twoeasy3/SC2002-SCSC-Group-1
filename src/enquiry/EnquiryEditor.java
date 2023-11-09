package enquiry;

import application.*;
import helper.Console;
import helper.DataHandler;

import java.util.List;

public abstract class EnquiryEditor {
    public static void editMenu(Student student, List<Enquiry> enquiryList ) {
        while(true) {
            Enquiry selectedEnquiry = null;
            List<Enquiry> eligibleEnquiries = EnquiryView.getRelevantEnquiries(student,false);
            EnquiryView.viewRelevantEnquiries(eligibleEnquiries);
            selectedEnquiry = EnquiryView.selectEnquiry(eligibleEnquiries);
            if (selectedEnquiry == null) {
                System.out.println("No enquiries found for editing.");
                String response = Console.nextString();
                return;
            }
            System.out.println(EnquiryView.singleEnquiryToString(selectedEnquiry, true));
            System.out.println("1. Edit this enquiry, 2. Delete this enquiry, 3. Select a new enquiry or 4. Exit");
            int selection = Console.nextInt();
            switch (selection) {
                case 1:
                    System.out.println("Enter new enquiry text:");
                    String newText = Console.nextString();
                    EnquiryEditor.editEnquiry(selectedEnquiry, newText, enquiryList);
                    return;
                case 2:
                    EnquiryEditor.deleteEnquiry(student, selectedEnquiry, enquiryList);
                    return;
                case 3:
                    break;
                case 4:
                    System.out.println("Exiting");
                    return;
                default:
                    break;
            }
        }
    }

    public static void writeEnquiry(Student student, Camp camp, List<Enquiry> enquiryList){
        
        System.out.println("Enter your enquiry:");
        String response = Console.nextString();
        Enquiry newEnquiry = new Enquiry(camp,student,response,null,"",false);
        enquiryList.add(newEnquiry);
        DataHandler.saveEnquiries(enquiryList);
    }
    // allows student to view the enquiry (description + reply)
    public static void viewEnquiry(Enquiry enquiry) {
        System.out.println(enquiry.getDescription());
        if (enquiry.getReply().isEmpty()) {
            System.out.println("This enquiry has not yet been replied");
        }
        else System.out.println(enquiry.getReply());
    }
    // assuming only description can be edited. but maybe camp can be updated as well?
    public static void editEnquiry(Enquiry e, String s, List<Enquiry> enquiryList) {
        e.setDescription(s + "\n**This enquiry has been edited**");
        DataHandler.saveEnquiries(enquiryList);
        System.out.println("Your enquiry has been edited");
    }
    // removes enquiry from both the camp and the user
    public static void deleteEnquiry(Student student,Enquiry e, List<Enquiry> enquiryList) {
        student.getEnquiryList().remove(e);
        e.getCamp().getEnquiryList().remove(e);
        enquiryList.remove(e);
        System.out.println("Your enquiry has been deleted");
    }
}
