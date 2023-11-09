package application;

import enquiry.*;
import helper.Console;
import helper.Fetcher;
import suggestions.Suggestion;

import java.util.ArrayList;
import java.util.List;

interface StudentView {
    public static void printMenu(Student student, List<Camp> campList) {
        System.out.println("Student Portal");
        if (student.getCommittee() == -1) {
            System.out.println("You are not currently a committee member of any camp.");
        } else {
            System.out.println("You are currently a committee member of " + Fetcher.getCampfromID(student.committee, campList).getName());//dangerous error possible
        }
        final String first5Options = "1. Change your password.\n" +
                "2. View eligible camps. (-o,-l,-s,-r,-p,-f)\n" +
                "3. View your signups.\n" +
                "4. Sign up for camp. (-o,-l,-s,-r,-p,-f)\n" +
                "5. Camp Enquiry Hub";
        System.out.println(first5Options);
        if (student.getCommittee() != -1) {
            System.out.println("6. Camp Committee Hub");
        }
        final String logOutOptions = "9. Log out\n0. Terminate CAMs";
        System.out.println(logOutOptions);
    }

    public static void viewCamps(Student student, List<Camp> campList, List<Enquiry> enquiryList) {
        
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

    static SessionStatus resolveCAMsMenu(Student student, int choice, String argument,
                                         List<User> userList,
                                         List<Camp> campList,
                                         List<Signup> signupList,
                                         List<Enquiry> enquiryList,
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
