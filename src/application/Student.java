package application;

import java.util.List;
import java.util.ArrayList;

public class Student extends User {
    int committee; //-1 if not committee member, otherwise put camp.id
    private ArrayList<Enquiry> enquiries_list;

    public Student(String name, String id, String faculty, String password) {
        super();
        this.fillDetails(name, id, faculty, password);
        this.committee = -1;
        enquiries_list = new ArrayList<Enquiry>(0);
    }

    public boolean checkStaff() {
        return false;
    }

    public void printMenu() {
        System.out.println("Student Portal");
        System.out.println("You are currently a committee member of XX camp."); //TODO
        System.out.println("1. View eligible camps.");
        System.out.println("2. View your signups.");
        System.out.println("3. Sign up for camp.");
        System.out.println("3. Camp Enquiry Hub");
        System.out.println("4. Camp Committee Hub");
    }

    public void viewCamps(List<Camp> campList) {
        System.out.println("Showing events you are eligible for as a student of " + this.getFaculty());
        for (Camp camp : campList) {
            if (camp.checkEligibility(this.getFaculty()) && camp.isVisible()) {
                System.out.println(camp.getName() + " (" + camp.getFaculty() + ")");
            }
        }
    }

    public void viewOwnedCamps(List<Camp> campList) { //TODO
        System.out.println("Unimplemented. ");
    }


    public boolean isCommittee() {
        return (!(this.committee == -1));
    }

    public List<Camp> signUpCamp(List<Camp> campList, List<Signup> signupList) { //TODO disallow when camp is full & clashes
        System.out.println("Showing events you are eligible for as a student of " + this.getFaculty() + " and have never signed up for...");
        int i = 0;
        List<Camp> eligibleCamps = null;
        for (Camp camp : campList) {
            if (camp.checkEligibility(this.getFaculty()) && camp.isVisible()) {
                for (Signup signup : signupList) {
                    if (!signup.matchStudent(this.getID()) && !signup.matchCamp(camp)) {
                        i++;
                        eligibleCamps.add(camp);
                        System.out.println(i + ": " + camp.getName() + " (" + camp.getFaculty() + ")");
                    }
                }
            }
        }
        if (i == 0) {
            System.out.println("No camps are currently open for you :(");

        }
        return eligibleCamps;
    }

    // Enquiry Functions
    // creates an enquiry and sends it to both camp and the student
    public void createEnquiry(String description, Student author, Camp camp) {
        Enquiry enquiry = new Enquiry(description, author, camp);
        author.enquiries_list.add(enquiry);
        camp.getEnquiry_list().add(enquiry);
        System.out.println("Your enquiry has been created and sent");
    }

    // allows student to view the enquiry (description + reply)
    public void viewEnquiry(Enquiry enquiry) {
        System.out.println(enquiry.getDescription());
        if (enquiry.getReply().isEmpty()) {
            System.out.println("This enquiry has not yet been replied");
        }
        else System.out.println(enquiry.getReply());
    }
    // assuming only description can be edited. but maybe camp can be updated as well?
    public void editEnquiry(Enquiry e, String s) {
        e.setDescription(s + "\n This enquiry has been edited");
        System.out.println("Your enquiry has been edited");
    }

    public void deleteEnquiry(Enquiry e) {
        int id = e.getEn_ID();
        this.enquiries_list.remove(e);
        Camp camp = e.getCamp();
        camp.getEnquiry_list().remove(e);
        System.out.println("Your enquiry has been deleted");
    }


}
