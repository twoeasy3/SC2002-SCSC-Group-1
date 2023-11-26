package application;

import enquiry.Enquiry;
import enquiry.EnquiryAbstract;
import helper.Console;
import suggestions.Suggestion;

import java.util.List;

/**
 * Contains methods relating to the menus that a Staff user interacts with.
 */
public interface StaffView {
    /**
     * Prints the main menu for a Staff in CAMs.
     * @param campList List of all Camps. Required for overriding from the User class.
     */
    static void printMenu(List<Camp> campList) {
        final String staffMenu = "Staff Portal\n" +
                                    "1. Change your password.\n"+
                                    "2. View all active camps. (-o,-l,-s,-p,-f)\n"+
                                    "3. View and Edit your camps. (-o,-l,-s,-p,-f)\n"+
                                    "4. Create a camp\n"+
                                    "5. Camp Admin Hub\n"+
                                    "9. Log out\n"+
                                    "0. Terminate CAMs";
        System.out.println(staffMenu);
    }

    /**
     * View camp menu for CAMs. Allows user to select a camp to see the short summary.
     * @param staff Staff object viewing the Camps
     * @param campList List of all Camps.
     * @param enquiryList List of all Enquiries.
     */
    static void viewCamps(Staff staff, List<Camp> campList, List<EnquiryAbstract> enquiryList) {
        
        boolean endLoop = false;
        String listMenu = CampListView.createNumberedCampList(campList, staff);
        while(!endLoop){
            System.out.println("Staff Privilege, showing all camps:");
            Camp selectedCamp = CampListView.campFromListSelector(campList,listMenu);
            if(selectedCamp == null) {
                return;
            }else {
                CampView.showSummary(selectedCamp);
                System.out.println("Viewing Mode - Press Enter to return");
                String response = Console.nextString();
            }
        }

    }
    /**
     * Method to determine functionality for each choice, defined for Staff exclusively.
     * @param staff Staff user working the menus
     * @param choice Input integer choice for menu
     * @param argument Any additional string arguments (used for Camp filters)
     * @param userList List of all User objects. Passed to other classes.
     * @param campList List of all Camp objects. Passed to other classes.
     * @param signupList List of all Signup objects. Passed to other classes.
     * @param enquiryList List of all Enquiry objects. Passed to other classes.
     * @param suggestionList List of all Suggestion objects. Passed to other classes.
     * @return SessionStatus to determine which state CAMs should continue with
     */
    static SessionStatus resolveCAMsMenu(Staff staff, int choice, String argument,
                                         List<User> userList,
                                         List<Camp> campList,
                                         List<Signup> signupList,
                                         List<EnquiryAbstract> enquiryList,
                                         List<Suggestion> suggestionList){
        SessionStatus status = SessionStatus.CONTINUE;
        switch(choice) {
            case 1:
                staff.changePass(userList);
                return status;
            case 2:
                campList = CampListView.sortCampList(campList,argument);
                staff.viewCamps(campList , enquiryList);
                return status;
            case 3:
                campList = CampListView.sortCampList(campList,argument);
                staff.viewOwnedCamps(campList, signupList,userList);
                return status;
            case 4:
                staff.createCamp(campList);
                return status;
            case 5:
                staff.adminMenu(campList, staff,suggestionList,enquiryList);
                return status;
            case 9:
                System.out.println("Logging out from CAMs...");
                status = SessionStatus.LOGOUT;
                return status;
            case 0:
                System.out.println("Logging out and terminating CAMs...");
                status = SessionStatus.CLOSE;
                return status;
            default:
                System.out.println("Invalid Choice");
                return status;
        }
    }
}
