package enquiry;

import application.*;
import helper.Console;
import helper.DataHandler;

import java.util.List;

/**
 * Methods that relate to editing Enquiries are implemented here.
 */
public class EnquiryEditor {
    /**
     * UI for walking through a Student to edit their unanswered enquiry.<br>
     * Only the body text can be edited.<br>
     *
     * @param student Student editing the enquiry
     * @param enquiryList List of all enquiries to save program state.
     */
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

    /**
     * Method that interfaces with the user to write a new Enquiry.
     * Enquiry object is constructed and appended to enquiriesList and program state is saved.
     * @param student Student authoring the enquiry
     * @param camp Camp the enquiry pertains to
     * @param enquiryList List of all Enquiries, to store new enquiry and save program state.
     */
    public static void writeEnquiry(Student student, Camp camp, List<Enquiry> enquiryList){
        
        System.out.println("Enter your enquiry:");
        String response = Console.nextString();
        Enquiry newEnquiry = new Enquiry(camp,student,response,null,"",false);
        enquiryList.add(newEnquiry);
        DataHandler.saveEnquiries(enquiryList);
    }

    /**
     * Prints an enquiry on-screen and its reply. If unreplied, it will state as such
     * @param enquiry Enquiry to be printed
     */
    public static void viewEnquiry(Enquiry enquiry) {
        System.out.println(enquiry.getDescription());
        if (enquiry.getReply().isEmpty()) {
            System.out.println("This enquiry has not yet been replied");
        }
        else System.out.println(enquiry.getReply());
    }

    /**
     * Edits an enquiry. Edited enquiries have an extra line denoting so. <br>
     * To note that the .csv file doesn't contain the newline character,<br>
     * It has been substituted with 胡 with very low chance of collision in DataHandler.saveEnquiries. <br>
     * @param e Enquiry object to be edited
     * @param s New body text to be edited
     * @param enquiryList List of all enquiries, to save program state.
     */
    public static void editEnquiry(Enquiry e, String s, List<Enquiry> enquiryList) {
        e.setDescription(s + "\n**This enquiry has been edited**");
        DataHandler.saveEnquiries(enquiryList);
        System.out.println("Your enquiry has been edited");
    }

    /**
     * Deletes an enquiry owned by the student. Enquiry is removed from Student, Camp and enquiriesList.
     * @param student Student removing the enquiry
     * @param e Enquiry to be deleted
     * @param enquiryList List of all enquiries, to save program state.
     */
    public static void deleteEnquiry(Student student,Enquiry e, List<Enquiry> enquiryList) {
        student.getEnquiryList().remove(e);
        e.getCamp().getEnquiryList().remove(e);
        enquiryList.remove(e);
        DataHandler.saveEnquiries(enquiryList);
        System.out.println("Your enquiry has been deleted");
    }
}
