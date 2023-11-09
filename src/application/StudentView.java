package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class StudentView {
    public static void printMenu(Student student, List<Camp> campList){
        System.out.println("Student Portal");
        if(student.getCommittee() == -1){
            System.out.println("You are not currently a committee member of any camp.");}
        else{
            System.out.println("You are currently a committee member of " + Fetcher.getCampfromID(student.committee, campList).getName());//dangerous error possible
        }
        final String first5Options = "1. Change your password.\n" +
                                        "2. View eligible camps. (-o,-l,-s,-r,-p,-f)\n" +
                                        "3. View your signups.\n"+
                                        "4. Sign up for camp. (-o,-l,-s,-r,-p,-f)\n" +
                                        "5. Camp Enquiry Hub";
        System.out.println(first5Options);
        if(student.getCommittee()!= -1){
            System.out.println("6. Camp Committee Hub");
        }
        final String logOutOptions = "9. Log out\n0. Terminate CAMs";
        System.out.println(logOutOptions);
    }
    public static void viewCamps(Student student, List<Camp> campList, List<Enquiry> enquiryList){
        Scanner sc = new Scanner(System.in);
        List<Camp> eligibleCamps = new ArrayList<>();
        for (Camp camp : campList){
            if(camp.checkEligibility(student.getFaculty()) && camp.isVisible()){
                eligibleCamps.add(camp);
            }
        }
        String listMenu = CampListView.createNumberedCampList(eligibleCamps, student);
        boolean endLoop = false;
        while (!endLoop){
            System.out.println("Showing all camps visible to you:");
            Camp selectedCamp = CampListView.campFromListSelector(eligibleCamps,listMenu);
            if(selectedCamp == null) {
                return;
            }else {
                CampView.showSummary(selectedCamp);
                boolean canEnquire = false;
                if (selectedCamp.checkCampStatus()==campStatus.ONGOING || selectedCamp.checkCampStatus()==campStatus.ENDED){
                    System.out.println("Enquiries are closed because the camp has started. Press Enter to continue.");}
                else if (selectedCamp.checkCampStatus()==campStatus.CLOSED && selectedCamp.isAttending(student)){
                    System.out.println("Press Enter to continue or type 'enquiry' to start a new enquiry:");
                    canEnquire = true;}
                else if (selectedCamp.checkCampStatus()==campStatus.CLOSED && !selectedCamp.isAttending(student)){
                    System.out.println("Enquiries are closed because registration is over and you aren't attending. Press Enter to continue. ");}
                else if (selectedCamp.isBlacklisted(student)){
                    System.out.println("Enquiries are closed to you because have cancelled your signup for this camp.");}
                else{
                    System.out.println("Press Enter to continue or type 'enquiry' to start a new enquiry:");
                    canEnquire = true;}
                String response = sc.nextLine();
                if(response.equals("enquiry") && canEnquire) {
                    EnquiryEditor.writeEnquiry(student,selectedCamp,enquiryList);
                    }
            }
        }
    }


}
