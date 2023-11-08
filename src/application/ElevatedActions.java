package application;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public interface ElevatedActions{

    public static void replyTo(Enquiry enquiry, User replyAuthor) {
        if(enquiry.getResolved()){
            System.out.println("Reply not possible, enquiry already replied to!");
            return;
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

    public static void reportMenu(Camp camp,User requestingUser){
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
    public static void printAttendeeReport(Camp camp);
        List<Student> campAttendees = camp.get
    public static void printEnquiryReport(Camp camp);
    public static void printCommitteeReport(Camp camp);
}
