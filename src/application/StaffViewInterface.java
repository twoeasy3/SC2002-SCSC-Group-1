package application;

import enquiry.EnquiryAbstract;
import helper.Console;
import suggestions.Suggestion;

import java.util.List;

/**
 * Interface that contains abstract methods relating to staff view to be implemented by StaffView class.
 */
public interface StaffViewInterface {
    /**
     * Method to be implemented in StaffView class
     * @param campList List of all Camps. Required for overriding from the User class.
     */
    static void printMenu(List<Camp> campList) {}

    /**
     * Method to be implemented in StaffView class
     * @param staff Staff object viewing the Camps
     * @param campList List of all Camps.
     * @param enquiryList List of all Enquiries.
     */
    static void viewCamps(Staff staff, List<Camp> campList, List<EnquiryAbstract> enquiryList) {}

    /**
     * Method to be implemented in StaffView class
     * @param staff Staff user working the menus
     * @param choice Input integer choice for menu
     * @param argument Any additional string arguments (used for Camp filters)
     * @param userList List of all User objects. Passed to other classes.
     * @param campList List of all Camp objects. Passed to other classes.
     * @param signupList List of all Signup objects. Passed to other classes.
     * @param enquiryList List of all Enquiry objects. Passed to other classes.
     * @param suggestionList List of all Suggestion objects. Passed to other classes.
     * @return SessionStatus to determine which state CAMs should continue with
     */
    static void resolveCAMsMenu(Staff staff, int choice, String argument,
                                         List<User> userList,
                                         List<Camp> campList,
                                         List<Signup> signupList,
                                         List<EnquiryAbstract> enquiryList,
                                         List<Suggestion> suggestionList){}
}
