package application;

import enquiry.Enquiry;
import enquiry.EnquiryReply;
import enquiry.EnquiryView;
import helper.Console;
import suggestions.Suggestion;
import suggestions.SuggestionStatus;
import suggestions.SuggestionView;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDate;

interface AdminActions {
    default void adminMenu(List<Camp> campList, User activeUser, List<Suggestion> suggestionList,List<Enquiry> enquiryList) {

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
            System.out.println("3. Make suggestion");


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
                    suggestionMaker(selectedCamp, activeUser, campList, suggestionList);
                }
                else{
                    ((Staff)activeUser).resolveMenu(campList,activeUser);
                }
        }
    }

    static void suggestionMaker(Camp selectedCamp, User activeUser, List<Camp> campList, List<Suggestion> suggestionList) {
        
        List<String> fieldNames = Arrays.asList("Camp Name", "Venue", "Description", "Max Slots", "Committee Slots");
        if (activeUser instanceof StudentCommittee) {
            System.out.println("Please enter a short description of your suggestion");
            String suggestionDesc = Console.nextString();
            System.out.println("Which category would your suggestion like to be in?");
            System.out.println("[1] Camp Name");
            System.out.println("[2] Venue");
            System.out.println("[3] Description");
            System.out.println("[4] Max Slots");
            System.out.println("[5] Committee Slots");
            int choice = Console.nextInt();
            if (choice >= 1 && choice <= 5) {
                System.out.println("Please enter the new " + fieldNames.get(choice - 1));
                String change = Console.nextString();
                if (CampEdit.tryEditCamp(selectedCamp, choice, change)) {
                    System.out.println("Your suggestion has been posted!");
                    suggestionList.add(new Suggestion(selectedCamp, (Student) activeUser,
                            suggestionDesc,  choice, change, SuggestionStatus.PENDING));
                } else {
                    System.out.println("Suggestion was not posted");
                }

            }

        }
    }


    static void reportMenu(Camp camp, User requestingUser) {
        
        int response;
        System.out.println("Choose the report you want to generate:");
        System.out.println("1: Camp report with the list of students attending each camp.");
        System.out.println("2: Students’ enquiry report");
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
                System.out.println("2: Students’ enquiry report");
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

    static void printAttendeeReport(Camp camp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<Student> campAttendees = camp.getAttendeeList();
        List<Student> campCommittee = camp.getCommitteeList();
        campAttendees.sort(Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER));
        campCommittee.sort(Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER));
        String fileName = LocalDate.now().format(formatter) + "AttendeeReport.txt";
        String line;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            line = String.format("%s (%s) Total Strength: [%d/%d] In-Charge: %s Camp Committee [%d/%d]:", camp.getName(), camp.getFaculty(), (camp.getAttendeeCount() + camp.getCommitteeCount()), camp.getMaxSize(),
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

    static void printEnquiryReport(Camp camp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<Enquiry> enquiryList = camp.getEnquiryList();
        String fileName = LocalDate.now().format(formatter) + "EnquiryReport.txt";
        String line;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            line = String.format("%s (%s) Total Enquiries: [%d] In-Charge: %s\n", camp.getName(), camp.getFaculty(), enquiryList.size(), camp.getInCharge());
            writer.write(line);
            for (Enquiry enquiry : enquiryList) {
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
            }
            writer.write("END OF REPORT");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void printCommitteeReport(Camp camp) { //TODO THIS IS UNIMPLEMENTED!
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<Enquiry> enquiryList = camp.getEnquiryList();
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
            line = String.format("%s (%s) Camp Committee [%d/%d] In-Charge: %s", camp.getName(), camp.getFaculty(), camp.getCommitteeCount(), camp.getMaxComm(),
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
                for (Enquiry enquiry : enquiryList){
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

