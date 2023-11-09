package application;

import java.util.List;
import java.util.Scanner;

public abstract class StaffView {
    public static void printMenu(List<Camp> campList) {
        final String staffMenu = "Staff Portal\n" +
                                    "1. Change your password.\n"+
                                    "2. View all active camps. (-o,-l,-s,-r,-p,-f)\n"+
                                    "3. View and Edit your camps. (-o,-l,-s,-r,-p,-f)\n"+
                                    "4. Camp enquiry hub.\n"+
                                    "5. Create a camp\n"+
                                    "6. Camp Admin Hub\n"+
                                    "9. Log out\n"+
                                    "0. Terminate CAMs";
        System.out.println(staffMenu);
    }

    public static void viewCamps(Staff staff, List<Camp> campList, List<Enquiry> enquiryList) {
        Scanner sc = new Scanner(System.in);
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
                String response = sc.nextLine();
            }
        }

    }

    public static sessionStatus resolveCAMsMenu(Staff staff, int choice, String argument,
                                                 List<User> userList,
                                                 List<Camp> campList,
                                                 List<Signup> signupList,
                                                 List<Enquiry> enquiryList,
                                                 List<Suggestion> suggestionList){
        sessionStatus status = sessionStatus.CONTINUE;
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
                //ENQURIES HUB
                return status;
            case 5:
                staff.createCamp(campList);
                return status;
            case 6:
                staff.adminMenu(campList, staff,suggestionList);
                return status;
            case 9:
                System.out.println("Logging out from CAMs...");
                status = sessionStatus.LOGOUT;
                return status;
            case 0:
                System.out.println("Logging out and terminating CAMs...");
                status = sessionStatus.CLOSE;
                return status;
            default:
                System.out.println("Invalid Choice");
                return status;
        }
    }
}
