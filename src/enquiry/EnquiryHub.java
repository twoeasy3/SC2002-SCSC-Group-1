package enquiry;
import helper.Console;
import application.Student;

import java.util.List;

/**
 * This class contains the menu for the Enquiries Hub.
 */
public class EnquiryHub {
    /**
     * This method prints the enquiries hub menu and resolves the selection
     * @param student Active User student object
     * @param enquiryList List of all enquiries, to be passed in to update program state.
     */
    public static void hubLanding(Student student , List<EnquiryAbstract> enquiryList) {
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
