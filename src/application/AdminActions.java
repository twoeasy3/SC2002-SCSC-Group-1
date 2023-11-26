package application;

import enquiry.Enquiry;
import enquiry.EnquiryAbstract;
import enquiry.EnquiryReply;
import enquiry.EnquiryView;
import helper.Console;
import suggestions.Suggestion;
import suggestions.SuggestionHub;
import suggestions.SuggestionView;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDate;

/**
 * This contains methods that "Admins" would need to use. <br>
 * "Admin" classes that implement these methods are Staff and StudentCommittee. *
 */
interface AdminActions {
    /**
     * This method displays and resolves the options for the Admin menu. <br>
     * In the main CAMs menu, a check must be done to ensure Student accounts don't call for this method. <br>
     * This menu behaves differently based on whether the ActiveUser is a Staff or a StudentCommittee.
     * @param campList List of all Camps. Used in parsing camps that are available for the user to perform admin actions on.
     * @param activeUser User object of the active user.
     * @param suggestionList List of all Suggestions. Used to save the state of Suggestions.
     * @param enquiryList List of all Enquiries. Used to save the state of Enquiries.
     */
    default void adminMenu(List<Camp> campList, User activeUser, List<Suggestion> suggestionList,List<EnquiryAbstract> enquiryList) {

        String response;
        Camp selectedCamp = null;
        if (activeUser instanceof Staff) {
            List<Camp> inchargeCamps = ((Staff) this).getOwnedCamps(campList);
            if (inchargeCamps.isEmpty()) {
                System.out.println("You don't have any created camps to perform admin actions on! Press Enter to continue");
                Console.nextString();
                return;
            } else if (inchargeCamps.size() == 1) {
                selectedCamp = inchargeCamps.get(0);
                System.out.println("Admin actions to perform on " + selectedCamp.getName());
            } else {
                String listMenu = CampListView.createNumberedCampList(inchargeCamps, activeUser);
                System.out.println("Select camp to perform admin actions: ");
                selectedCamp = CampListView.campFromListSelector(inchargeCamps, listMenu);
                if (selectedCamp == null) {
                    return;
                } else {
                    System.out.println("Admin actions to perform on " + selectedCamp.getName());
                }
            }
        } else if (activeUser instanceof StudentCommittee) {
            selectedCamp = ((StudentCommittee) activeUser).getCamp();
            System.out.println("Admin actions to perform on " + selectedCamp.getName());
        }
        System.out.println("1. Reply to enquiries");
        System.out.println("2. Generate Report");
        if (activeUser instanceof Staff) {
            System.out.println("3. Approve/Remove suggestions");
        } else {
            System.out.println("3. Make/Edit suggestion");


        }
        int choice = Console.nextInt();
        switch (choice) {
            case 1:
                EnquiryReply.replyMenu(selectedCamp,activeUser,enquiryList);
                break;
            case 2:
                reportMenu(selectedCamp, activeUser);
                break;
            case 3:
                if(activeUser instanceof StudentCommittee) {
                    SuggestionHub.hubLanding((Student) activeUser, suggestionList);
                }
                else{
                    ((Staff)activeUser).resolveMenu(campList,activeUser);
                }
        }
    }

    /**
     * Menu to select report to generate.
     * Staff can generate 1 additional report, Camp Committee Performance Report.
     * The files are saved as yyyyMMdd{ReportName}.
     * @param camp Camp object to generate report for.
     * @param requestingUser User object generating the report. Used to determine if option 3 should be accessed.
     */
    static void reportMenu(Camp camp, User requestingUser) {
        
        int response;
        System.out.println("Choose the report you want to generate:");
        System.out.println("1: Camp report with the list of students attending the camp.");
        System.out.println("2: Student enquiry report");
        if (requestingUser instanceof Staff) {
            System.out.println("3: Camp committee performance report");
        }
        response = Console.nextInt();
        switch (response) {
            case 1:
                System.out.println("1: Camp report with the list of students attending each camp.");
                AdminActions.printAttendeeReport(camp);
                break;
            case 2:
                System.out.println("2: Student enquiry report");
                AdminActions.printEnquiryReport(camp);
                break;
            case 3:
                if (requestingUser instanceof Staff) {
                    System.out.println("3: Camp committee performance report");
                    AdminActions.printCommitteeReport(camp);
                }
                break;

        }
    }

    /**
     * Parses and saves a .txt report for Attendee Report.
     * This report prints the following:
     * CampName, Total Strength out of Max Slots<br>
     * In-Charge Staff Name<br>
     * Camp Committee count out of Max Committee.<br>
     * All Camp Committee member names.<br>
     * Attendee count out of Max Attendee slots.<br>
     * All Attendee names.<br>
     * Blacklisted Student count<br>
     * All blacklisted student names.<br>
     * @param camp Camp object to generate the report from.
     */
    static void printAttendeeReport(Camp camp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<Student> campAttendees = camp.getAttendeeList();
        List<Student> campCommittee = camp.getCommitteeList();
        campAttendees.sort(Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER));
        campCommittee.sort(Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER));
        String fileName = LocalDate.now().format(formatter) + "AttendeeReport.txt";
        String line;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            line = String.format("%s (%s) Total Strength: [%d/%d] In-Charge: %s \nCamp Committee [%d/%d]:\n", camp.getName(), camp.getFaculty(), (camp.getAttendeeCount() + camp.getCommitteeCount()), camp.getMaxSize(),
                    camp.getInCharge(), camp.getCommitteeCount(), camp.getMaxComm());
            writer.write(line);
            for (Student committee : campCommittee) {
                line = String.format("%s (%s)\n", committee.getName(), committee.getFaculty());
                writer.write(line);
            }
            line = String.format("Camp Attendees [%d/%d]:\n", camp.getAttendeeCount(), camp.getMaxSize() - camp.getMaxComm());
            writer.write(line);
            for (Student student : campAttendees) {
                line = String.format("%s (%s)\n", student.getName(), student.getFaculty());
                writer.write(line);
            }
            if (!camp.getBlackList().isEmpty()){
                line = String.format("Blacklisted: [%d]:\n", camp.getBlackList().size());
                writer.write(line);
                for (Student student : camp.getBlackList()) {
                    line = String.format("%s (%s)\n", student.getName(), student.getFaculty());
                    writer.write(line);
                }
            }
            line = "END OF REPORT";
            writer.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Parses and saves a .txt report for Enquiry Report.
     * This report prints the following:
     * CampName, Total Enquiries<br>
     * In-Charge Staff Name<br>
     * Every Enquiry still existing related to the Camp<br>
     * Author of the Enquiry<br>
     * Reply to the enquiry, or "There was no reply" if no reply<br>
     * Author of the Reply<br>
     * @param camp Camp object to generate the report from.
     */
    static void printEnquiryReport(Camp camp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<EnquiryAbstract> enquiryList = camp.getEnquiryList();
        String fileName = LocalDate.now().format(formatter) + "EnquiryReport.txt";
        String line;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            line = String.format("%s (%s) Total Enquiries: [%d] In-Charge: %s\n", camp.getName(), camp.getFaculty(), enquiryList.size(), camp.getInCharge());
            writer.write(line);
            for (EnquiryAbstract enquiry : enquiryList) {
                line = String.format("%s (%s) asked:\n" +
                        "%s\n", enquiry.getAuthor().getName(), enquiry.getAuthor().getFaculty(), enquiry.getDescription());
                writer.write(line);
                if (enquiry.getResolved()) {
                    line = String.format("%s (%s) answered:\n" +
                            "%s\n", enquiry.getReplyAuthor().getName(), enquiry.getReplyAuthor().getFaculty(), enquiry.getReply());
                    writer.write(line);
                } else {
                    writer.write("There was no reply.\n");
                }
                writer.write("===================================\n");
            }
            writer.write("END OF REPORT");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Parses and saves a .txt report for Attendee Report.
     * This report prints the following:
     * CampName, Camp Committee count out of Max Committee.<br>
     * In-Charge Staff Name<br>
     * One by one, each Camp Committee member<br>
     * Every Enquiry replied to by that member<br>
     * Every suggestion still existing by that member<br>
     * @param camp Camp object to generate the report from.
     */
    static void printCommitteeReport(Camp camp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<EnquiryAbstract> enquiryList = camp.getEnquiryList();
        List<Suggestion> suggestionList = camp.getSuggestionList();
        List<Student> committeeList = camp.getCommitteeList();
        List<StudentCommittee> committeeListByScore = new ArrayList<>();
        for (Student student : committeeList){
            if(student instanceof StudentCommittee){
                committeeListByScore.add((StudentCommittee) student);
            }
        }
        committeeListByScore.sort(Comparator.comparing(StudentCommittee::getPoints).thenComparing(StudentCommittee::getName));
        String fileName = LocalDate.now().format(formatter) + "CommitteeReport.txt";
        String line = "";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            line = String.format("%s (%s) Camp Committee [%d/%d] In-Charge: %s \n", camp.getName(), camp.getFaculty(), camp.getCommitteeCount(), camp.getMaxComm(),
                            camp.getInCharge() );
            writer.write(line);
            for (StudentCommittee studComm : committeeListByScore){
                line = "===================================================\n";
                line += String.format("%s (%s) Score: %d\n", studComm.getName(), studComm.getFaculty(), studComm.getPoints());
                line += "===================================================\n";
                writer.write(line);
                for(Suggestion suggestion : suggestionList){
                    if(suggestion.getAuthor()==(Student)studComm) {
                        line = (SuggestionView.singleSuggestionToString(suggestion));
                        writer.write(line +"\n");
                    }
                }
                for (EnquiryAbstract enquiry : enquiryList){
                    if(enquiry.getReplyAuthor() == (Student)studComm){
                        line = EnquiryView.singleEnquiryToString(enquiry,false);
                        writer.write(line +"\n");
                    }
                }
            }
            writer.write("END OF REPORT");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

