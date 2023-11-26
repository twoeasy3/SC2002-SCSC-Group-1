package application;

import enquiry.*;
import helper.Console;
import helper.Fetcher;
import suggestions.Suggestion;

import java.util.ArrayList;
import java.util.List;
/**
 * Interface that contains abstract methods which allows a student to view different options.
 */
interface StudentViewInterface {
    /**
     * Method will be implemented in the StudentView class
     * @param student Student object using CAMs
     * @param campList List of all Camps
     */
    public static void printMenu(Student student, List<Camp> campList) {}
    /**
     * Method will be implemented in the StudentView class
     * @param student Student object viewing the Camps
     * @param campList List of all Camps.
     * @param enquiryList List of all Enquiries.
     */
    public static void viewCamps(Student student, List<Camp> campList, List<EnquiryAbstract> enquiryList) {}

    /**
     * Method will be implemented in the StudentView class
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
    static void resolveCAMsMenu(Student student, int choice, String argument,
                                         List<User> userList,
                                         List<Camp> campList,
                                         List<Signup> signupList,
                                         List<EnquiryAbstract> enquiryList,
                                         List<Suggestion> suggestionList){}
}
