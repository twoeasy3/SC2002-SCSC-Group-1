package application;

import enquiry.EnquiryAbstract;
import suggestions.Suggestion;

import java.util.*;

/**
 * This interface contains methods that will be implemented in the Admin Actions class
 */
interface AdminActionsInterface {
    /**
     * This method will be implemented in the AdminActions class
     * @param campList List of all Camps. Used in parsing camps that are available for the user to perform admin actions on.
     * @param activeUser User object of the active user.
     * @param suggestionList List of all Suggestions. Used to save the state of Suggestions.
     * @param enquiryList List of all Enquiries. Used to save the state of Enquiries.
     */
    default void adminMenu(List<Camp> campList, User activeUser, List<Suggestion> suggestionList,List<EnquiryAbstract> enquiryList) {}

    /**
     * This method will be implemented in the AdminActions class
     * @param camp Camp object to generate report for.
     * @param requestingUser User object generating the report. Used to determine if option 3 should be accessed.
     */
    static void reportMenu(Camp camp, User requestingUser) {}

    /**
     * This method will be implemented in the AdminActions class
     * @param camp Camp object to generate the report from.
     */
    static void printAttendeeReport(Camp camp) {}

    /**
     * This method will be implemented in the AdminActions class
     * @param camp Camp object to generate the report from.
     */
    static void printEnquiryReport(Camp camp) {}
    /**
     * This method will be implemented in the AdminActions class
     * @param camp Camp object to generate the report from.
     */
    static void printCommitteeReport(Camp camp) {}
}

