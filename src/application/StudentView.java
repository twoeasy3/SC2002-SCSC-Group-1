package application;

import enquiry.*;
import helper.Console;
import helper.Fetcher;
import suggestions.Suggestion;

import java.util.ArrayList;
import java.util.List;
/**
 * Contains methods relating to the menus that a Student user interacts with.
 */
interface StudentView {
    /**
     * Prints the main menu for a Student in CAMs
     * @param student Student object using CAMs
     * @param campList List of all Camps
     */
    public static void printMenu(Student student, List<Camp> campList) {
        System.out.println(student.getName() + " (" + student.getFaculty() + ") Student Portal");
        if (student.getCommittee() == -1) {
            System.out.println("You are not currently a committee member of any camp.");
        } else {
            System.out.print("You are currently a committee member of " + Fetcher.getCampfromID(student.committee, campList).getName());
            System.out.println(", Current Points: " + ((StudentCommittee) student).getPoints() );
        }
        final String first5Options = "1. Change your password.\n" +
                "2. View eligible camps. (-o,-l,-s,-p,-f)\n" +
                "3. View your signups.\n" +
                "4. Sign up for camp. (-o,-l,-s,-p,-f)\n" +
                "5. Camp Enquiry Hub";
        System.out.println(first5Options);
        if (student.getCommittee() != -1) {
            System.out.println("6. Camp Committee Hub");
        }
        final String logOutOptions = "9. Log out\n0. Terminate CAMs";
        System.out.println(logOutOptions);
    }
    /**
     * View camp menu for CAMs. Allows user to select a camp to see the short summary.
     * @param student Student object viewing the Camps
     * @param campList List of all Camps.
     * @param enquiryList List of all Enquiries.
     */
    public static void viewCamps(Student student, List<Camp> campList, List<EnquiryAbstract> enquiryList) {
        
        List<Camp> eligibleCamps = new ArrayList<>();
        for (Camp camp : campList) {
            if (camp.checkEligibility(student.getFaculty()) && camp.isVisible()) {
                eligibleCamps.add(camp);
            }
        }
        String listMenu = CampListView.createNumberedCampList(eligibleCamps, student);
        boolean endLoop = false;
        while (!endLoop) {
            System.out.println("Showing all camps visible to you:");
            Camp selectedCamp = CampListView.campFromListSelector(eligibleCamps, listMenu);
            if (selectedCamp == null) {
                return;
            } else {
                CampView.showSummary(selectedCamp);
                boolean canEnquire = false;
                if (selectedCamp.checkCampStatus() == CampStatus.ONGOING || selectedCamp.checkCampStatus() == CampStatus.ENDED) {
                    System.out.println("Enquiries are closed because the camp has started. Press Enter to continue.");
                } else if (selectedCamp.checkCampStatus() == CampStatus.CLOSED && selectedCamp.isAttending(student)) {
                    System.out.println("Press Enter to continue or type 'enquiry' to start a new enquiry:");
                    canEnquire = true;
                } else if (selectedCamp.checkCampStatus() == CampStatus.CLOSED && !selectedCamp.isAttending(student)) {
                    System.out.println("Enquiries are closed because registration is over and you aren't attending. Press Enter to continue. ");
                } else if (selectedCamp.isBlacklisted(student)) {
                    System.out.println("Enquiries are closed to you because have cancelled your signup for this camp.");
                } else {
                    System.out.println("Press Enter to continue or type 'enquiry' to start a new enquiry:");
                    canEnquire = true;
                }
                String response = Console.nextString();
                if (response.equals("enquiry") && canEnquire) {
                    EnquiryEditor.writeEnquiry(student, selectedCamp, enquiryList);
                }
            }
        }
    }
    /**
     * Method to determine functionality for each choice, defined for Student exclusively.
     * @param student Student user working the menus
     * @param choice Input integer choice for menu
     * @param argument Any additional string arguments (used for Camp filters)
     * @param userList List of all User objects. Passed to other classes.
     * @param campList List of all Camp objects. Passed to other classes.
     * @param signupList List of all Signup objects. Passed to other classes.
     * @param enquiryList List of all Enquiry objects. Passed to other classes.
     * @param suggestionList List of all Suggestion objects. Passed to other classes.
     * @return SessionStatus to determine which state CAMs should continue with
     */
    static SessionStatus resolveCAMsMenu(Student student, int choice, String argument,
                                         List<User> userList,
                                         List<Camp> campList,
                                         List<Signup> signupList,
                                         List<EnquiryAbstract> enquiryList,
                                         List<Suggestion> suggestionList){
        SessionStatus status = SessionStatus.CONTINUE;
        switch(choice) {
            case 1:
                student.changePass(userList);
                return status;
            case 2:
                campList = CampListView.sortCampList(campList,argument);
                student.viewCamps(campList , enquiryList);
                return status;
            case 3:
                campList = CampListView.sortCampList(campList,argument);
                student.viewOwnedCamps(campList, signupList,userList);
                return status;
            case 4:
                campList = CampListView.sortCampList(campList,argument);
                student.signUpView(userList, campList,signupList);
                return status;
            case 5:
                EnquiryHub.hubLanding(student,enquiryList);
                return status;
            case 6:
                if (student instanceof StudentCommittee && ((StudentCommittee) student).getCamp() != null){
                    ((StudentCommittee) student).adminMenu(campList, student,suggestionList,enquiryList);
                }
                else{
                    System.out.println("Invalid Choice");
                }
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
