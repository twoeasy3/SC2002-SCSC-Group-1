package application;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

interface ElevatedActions{
    default void adminMenu(List<Camp> campList, User activeUser){
        Scanner sc = new Scanner(System.in);
        String response;
        Camp selectedCamp = null;
        if(activeUser instanceof Staff){
            List<Camp> inchargeCamps = ((Staff) this).getOwnedCamps(campList);
            if(inchargeCamps.size()==0){
                System.out.println("You don't have any created camps to perform admin actions on! Press Enter to continue");
                response = sc.nextLine();
                return;
            }
            else if(inchargeCamps.size()==1){
                selectedCamp = inchargeCamps.get(0);
                System.out.println("Admin actions to perform on " + selectedCamp.getName());
            } else{
                String listMenu = Helper.createNumberedCampList(inchargeCamps,activeUser);
                System.out.println("Select camp to perform admin actions: ");
                selectedCamp = Helper.campFromListSelector(inchargeCamps,listMenu);
                if(selectedCamp == null){
                    return;
                }else{
                    System.out.println("Admin actions to perform on " + selectedCamp.getName());
                }
            }
        }
        else if (activeUser instanceof StudentCommittee){
            selectedCamp = ((StudentCommittee) activeUser).getCamp();
            System.out.println("Admin actions to perform on " + selectedCamp.getName());
        }
        System.out.println("1. Reply to enquiries");
        System.out.println("2. Generate Report");
        if(activeUser instanceof Staff) {
            System.out.println("3. Approve/Remove suggestions");
        }
        else{
            System.out.println("3. Make suggestion");
        }
        int choice = sc.nextInt();
        switch(choice){
            case 1:
                System.out.println("TODO enquiry selector");
                break;
            case 2:
                reportMenu(selectedCamp,activeUser);
                break;
            case 3:
                System.out.println("TODO suggestions hub");
                break;
        }
    }

    static void replyTo(Enquiry enquiry, User replyAuthor) {
        if(enquiry.getResolved()){
            System.out.println("Reply not possible, enquiry already replied to!");
        }
        else{
            System.out.println("SELECTED ENQUIRY:================");
            System.out.println(enquiry.getDescription());

            Scanner sc = new Scanner(System.in);
            System.out.println("Enter your reply to this: ");
            String response = sc.nextLine();
            enquiry.setDescription(response);
            enquiry.setReplyAuthor(replyAuthor);
            enquiry.setResolved(true);
        }

    }

    static void reportMenu(Camp camp,User requestingUser){
        Scanner sc = new Scanner(System.in);
        int response;
        System.out.println("Choose the report you want to generate:");
        System.out.println("1: Camp report with the list of students attending each camp.");
        System.out.println("2: Students’ enquiry report");
        if(requestingUser instanceof Staff){
            System.out.println("3: Camp committee performance report");
        }
        response = sc.nextInt();
        switch(response){
            case 1:
                System.out.println("1: Camp report with the list of students attending each camp.");
                ElevatedActions.printAttendeeReport(camp);
                break;
            case 2:
                System.out.println("2: Students’ enquiry report");
                ElevatedActions.printEnquiryReport(camp);
                break;
            case 3:
                if(requestingUser instanceof Staff){
                    System.out.println("3: Camp committee performance report");
                    ElevatedActions.printCommitteeReport(camp);
                }
                break;

        }
    }
    static void printAttendeeReport(Camp camp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<Student> campAttendees = camp.getAttendeeList();
        List<Student> campCommittee = camp.getCommitteeList();
        campAttendees.sort(Comparator.comparing(Student::getName,String.CASE_INSENSITIVE_ORDER));
        campCommittee.sort(Comparator.comparing(Student::getName,String.CASE_INSENSITIVE_ORDER));
        String fileName = LocalDate.now().format(formatter) + "AttendeeReport.txt";
        String line;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            line = String.format("%s (%s) Total Strength: [%d/%d] In-Charge: %s" +
                    "\nCamp Committee [%d/%d]:\n",camp.getName(),camp.getFaculty(),(camp.getAttendeeCount()+ camp.getCommitteeCount()),camp.getMaxSize(),
                    camp.getInCharge(),camp.getCommitteeCount(),camp.getMaxComm());
            writer.write(line);
            for(Student committee : campCommittee){
                line = String.format("%s (%s)\n",committee.getName(),committee.getFaculty());
                writer.write(line);
            }
            line = String.format("Camp Attendees [%d/%d]:\n",camp.getAttendeeCount(), camp.getMaxSize()-camp.getMaxComm());
            writer.write(line);
            for(Student student : campAttendees){
                line = String.format("%s (%s)\n",student.getName(),student.getFaculty());
                writer.write(line);
            }
            line = String.format("END OF REPORT");
            writer.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static void printEnquiryReport(Camp camp){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<Enquiry> enquiryList = camp.getEnquiryList();
        String fileName = LocalDate.now().format(formatter) + "EnquiryReport.txt";
        String line = "";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            line = String.format("%s (%s) Total Enquiries: [%d] In-Charge: \n",camp.getName(),camp.getFaculty(),enquiryList.size(),camp.getInCharge());
            writer.write(line);
            for(Enquiry enquiry : enquiryList) {
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
    static void printCommitteeReport(Camp camp){ //TODO THIS IS UNIMPLEMENTED!
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<Enquiry> enquiryList = camp.getEnquiryList();
        String fileName = LocalDate.now().format(formatter) + "EnquiryReport.txt";
        String line = "";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            line = String.format("%s (%s) Total Enquiries: [%d] In-Charge: \n",camp.getName(),camp.getFaculty(),enquiryList.size(),camp.getInCharge());
            writer.write(line);
            for(Enquiry enquiry : enquiryList) {
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
}

