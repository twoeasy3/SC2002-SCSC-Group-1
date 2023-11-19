package application;

import helper.Console;
import helper.DataHandler;
import helper.Fetcher;
import helper.InputChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Methods that deal with Student-Camp interactions are implemented in this class.
 */
public interface StudentCampOptions {
    /**
     * Adds a Student to the camp committee, then sets the camp to the Student's active committee. <br>
     * Checks not performed here.
     * @param student Student to become committee
     * @param selectedCamp Camp to be committee for.
     * @param userList List of all Users to save program state.
     * @param campList List of all Camps to save program state.
     */
    static void joinCommittee(Student student, Camp selectedCamp, List<User> userList, List<Camp> campList){
        selectedCamp.addCommittee(student);
        ((StudentCommittee)student).setCamp(selectedCamp);
        student.setCommittee(selectedCamp.getID());
        DataHandler.saveUsers(userList);
        DataHandler.saveCamps(campList);
    }

    /**
     * Adds a Student to the camp attendee by creating a Signup object between the Student and Camp. <br>
     * @param student Student to sign up
     * @param selectedCamp Camp to sign up for
     * @param signupList List of all Signups to save program state.
     */
    static void joinCamp(Student student, Camp selectedCamp,  List<Signup> signupList){
        signupList.add(new Signup(student,selectedCamp,true));
        DataHandler.saveSignups(signupList);

    }

    /**
     * Gets a list of Camp objects that the Student is involved in, including as Attendee and Committee.
     * Used to keep track of clashes.
     * @param student Student to fetch involved camps for
     * @param campList List of all Camps to fetch from
     * @param signupList List of all signups to parse
     * @return List of all Camps student is involved in.
     */
    static List<Camp> getOwnedCamps(Student student, List<Camp> campList, List<Signup> signupList) {
        List<Camp> ownedCamps = new ArrayList<>();
        for (Signup signup : signupList) {
            if (signup.matchStudent(student) && signup.getStatus()) {
                ownedCamps.add(signup.getCamp());
            }
        }
        if (student.committee != -1) {
            ownedCamps.add(Fetcher.getCampfromID(student.committee, campList));
        }
        return ownedCamps;
    }

    /**
     * Finds Camps that the Student can sign up for. The camps must be: <br>
     * Camp status must be OPEN<br>
     * Camp must be visible<br>
     * Student's faculty must be eligible for the camp<br>
     * Student must not already be involved<br>
     * Camp must not clash with other camps student is involved in<br>
     * Camp must have open slots remaining<br>
     * Student must not already be blacklisted<br>
     *
     * @param student Student to check eligibility
     * @param campList List of all Camps to check from
     * @param signupList List of all signups
     * @return List of all camps that Student can join
     */
    static List<Camp> getEligibleCamps(Student student, List<Camp> campList, List<Signup> signupList) {
        List<Camp> campListCopy = new ArrayList<>(campList);
        List<Camp> attendingCamps = StudentCampOptions.getOwnedCamps(student, campList, signupList);
        campListCopy.removeAll(attendingCamps); //remove all camps that student is already attending/committee-ing
        int i = 0;
        List<Camp> eligibleCamps = new ArrayList<>();
        boolean signUpExists;
        boolean clashExists;
        for (Camp camp : campListCopy) {
            signUpExists = false;
            if (camp.checkEligibility(student.getFaculty()) && camp.isVisible()
                    && !camp.isFull() && camp.checkCampStatus() == CampStatus.OPEN) {//camp eligibility check
                if (camp.isAttending(student) || camp.isBlacklisted(student)) {
                    signUpExists = true;
                }
                if (!signUpExists) {//Now check for clashes
                    clashExists = false;
                    for (Camp attendingCamp : attendingCamps) {//for each attending camp, see if eligible camp clashes
                        if (attendingCamp.checkClash(camp.getStart(), camp.getEnd())) { //clash found
                            clashExists = true;
                            break;
                        }
                    }
                    if (!clashExists) {
                        i++;
                        eligibleCamps.add(camp);
                    }
                }
            }

        }
        return(eligibleCamps);
    }

    /**
     * Provides the menus and interface for expanded options on the view signups list. <br>
     * Lets user cancel their signup, shows a warning if they choose to do so.<br>
     * Allows user to upgrade their status from Attendee to Committee.
     * @param student Student who is viewing the options
     * @param selectedCamp Camp for which the options are for
     * @param userList List of all Users to save program state
     * @param signupList List of all Signups to save program state
     * @return Boolean on whether a signup has happened
     */
    static boolean extraSignupOptions(Student student, Camp selectedCamp, List<User> userList, List<Signup> signupList) {
        
        CampView.showSummary(selectedCamp);
        System.out.println("Press Enter to go back.");
        if (student.getCommittee() != selectedCamp.getID()) {
            System.out.println("To cancel this signup type 'cancel'");
        }
        if (!selectedCamp.isFullCommittee() && student.getCommittee() == -1) {
            System.out.println("To upgrade to Committee member type 'committee'");
        }
        String response = Console.nextString();
        if (response.equals("cancel") && student.getCommittee() != selectedCamp.getID()) {
            System.out.println("Once you cancel, you will not be able to sign up for this camp again! Are you sure? Y/N");
            int input = -1;
            while (input == -1) {
                input = InputChecker.parseUserBoolInput(Console.nextString());
            }
            if (input == 1) {
                for (Signup signup : signupList) {
                    if (signup.getStudent() == student && signup.getCamp() == selectedCamp) {
                        signup.cancelSignup();
                        DataHandler.saveSignups(signupList);
                        return true;
                    }
                }
            } else {
                System.out.println("Backing out and showing you your signups...");
                return false;
            }
        } else if (response.equals("committee") && !selectedCamp.isFullCommittee() && student.getCommittee() == -1) {
            System.out.println("Once you change your role, you will not be able to cancel or join another committee! Are you sure? Y/N");
            int input = -1;
            while (input == -1) {
                input = InputChecker.parseUserBoolInput(Console.nextString());
            }
            if (input == 1) {
                for (Signup signup : signupList) {
                    if (signup.getStudent() == student && signup.getCamp() == selectedCamp) {
                        signupList.remove(signup);
                        DataHandler.saveSignups(signupList);
                        student.setCommittee(selectedCamp.getID());
                        selectedCamp.promoteToComittee(student); //this only matters for memory
                        DataHandler.saveUsers(userList);
                        return true;
                    }
                }
                return true;
            } else {
                System.out.println("Backing out and showing you your signups...");
                return false;
            }
        }
        return false;

    }


}

