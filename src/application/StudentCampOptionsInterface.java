package application;

import helper.Console;
import helper.DataHandler;
import helper.Fetcher;
import helper.InputChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface that contains the general methods for student camp options that students can perform. The methods are
 * implemented in the classes that realize from this interface.
 */
public interface StudentCampOptionsInterface {
    /**
     * Method will be implemented in StudentCampOptions class.
     * @param student Student to become committee
     * @param selectedCamp Camp to be committee for.
     * @param userList List of all Users to save program state.
     * @param campList List of all Camps to save program state.
     */
    static void joinCommittee(Student student, Camp selectedCamp, List<User> userList, List<Camp> campList){}

    /**
     * Method will be implemented in StudentCampOptions class.
     * @param student Student to sign up
     * @param selectedCamp Camp to sign up for
     * @param signupList List of all Signups to save program state.
     */
    static void joinCamp(Student student, Camp selectedCamp,  List<Signup> signupList){}

    /**
     * Method will be implemented in StudentCampOptions class.
     * @param student Student to fetch involved camps for
     * @param campList List of all Camps to fetch from
     * @param signupList List of all signups to parse
     * @return List of all Camps student is involved in.
     */
    static void getOwnedCamps(Student student, List<Camp> campList, List<Signup> signupList) {}

    /**
     * Method will be implemented in StudentCampOptions class.
     *
     * @param student Student to check eligibility
     * @param campList List of all Camps to check from
     * @param signupList List of all signups
     * @return List of all camps that Student can join
     */
    static void getEligibleCamps(Student student, List<Camp> campList, List<Signup> signupList) {}

    /**
     * Method will be implemented in StudentCampOptions class.
     * @param student Student who is viewing the options
     * @param selectedCamp Camp for which the options are for
     * @param userList List of all Users to save program state
     * @param signupList List of all Signups to save program state
     * @return Boolean on whether a signup has happened
     */
    static void extraSignupOptions(Student student, Camp selectedCamp, List<User> userList, List<Signup> signupList) {
    }


}

