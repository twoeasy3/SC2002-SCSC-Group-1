package suggestions;
import application.StudentCommittee;
import application.User;
import enquiry.Enquiry;
import enquiry.EnquiryEditor;
import enquiry.EnquiryView;
import helper.Console;
import application.Student;

import java.util.List;

/**
 * This class contains the menu for the making or editing enquiries.
 */
public class SuggestionHub {
    /**
     * This method prints the enquiries hub menu and resolves the selection
     * @param student Active User student object
     * @param suggestionList List of all Suggestions, to be passed in to update program state.
     */
    public static void hubLanding(Student student , List<Suggestion> suggestionList) {
        while (true) {
            final String menu = "Suggestions Hub.\n" +
                    "1: Make new suggestion\n" +
                    "2: View, edit or delete pending suggestion\n" +
                    "Enter the corresponding number to enter, or 0 to go back to admin hub.";
            System.out.println(menu);
            int choice = Console.nextInt();
            switch (choice) {
                case 1:
                    SuggestionEditor.suggestionMaker(((StudentCommittee) student).getCamp(), (User) student, suggestionList);
                    break;
                case 2:
                    SuggestionEditor.editMenu((StudentCommittee) student, suggestionList);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
