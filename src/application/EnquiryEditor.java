package application;

import java.util.List;
import java.util.Scanner;

public abstract class EnquiryEditor {

    public static void writeEnquiry(Student student,Camp camp, List<Enquiry> enquiryList){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your enquiry:");
        String response = sc.nextLine();
        Enquiry newEnquiry = new Enquiry(camp,student,response,null,"",false);
        enquiryList.add(newEnquiry);
        DataHandler.saveEnquiries(enquiryList);
    }
    // allows student to view the enquiry (description + reply)
    public static void viewEnquiry(Enquiry enquiry) {
        System.out.println(enquiry.getDescription());
        if (enquiry.getReply().isEmpty()) {
            System.out.println("This enquiry has not yet been replied");
        }
        else System.out.println(enquiry.getReply());
    }
    // assuming only description can be edited. but maybe camp can be updated as well?
    public static void editEnquiry(Enquiry e, String s, List<Enquiry> enquiryList) {
        e.setDescription(s + "\n This enquiry has been edited");
        DataHandler.saveEnquiries(enquiryList);
        System.out.println("Your enquiry has been edited");
    }
    // removes enquiry from both the camp and the user
    public static void deleteEnquiry(Student student,Enquiry e, List<Enquiry> enquiryList) {
        student.getEnquiryList().remove(e);
        e.getCamp().getEnquiryList().remove(e);
        enquiryList.remove(e);
        System.out.println("Your enquiry has been deleted");
    }
}
