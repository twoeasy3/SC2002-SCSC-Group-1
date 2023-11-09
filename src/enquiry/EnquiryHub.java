package enquiry;
import helper.Console;
import application.Student;

import java.util.List;

public class EnquiryHub {

    public static void hubLanding(Student student , List<Enquiry> enquiryList) {
        while (true) {
            final String menu = "Welcome to student enquiry hub.\n" +
                    "1: View, Edit or Delete unanswered enquiries\n" +
                    "2: View answered enquiries.\n" +
                    "Enter the corresponding number to enter, or 0 to go back to CAMs.";
            System.out.println(menu);
            int choice = Console.nextInt();
            switch (choice) {
                case 1:
                    EnquiryEditor.editMenu(student, enquiryList);
                    break;
                case 2:
                    EnquiryView.viewRelevantEnquiries(EnquiryView.getRelevantEnquiries(student,true));
                    System.out.println("Press Enter to continue.");
                    Console.nextString();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
