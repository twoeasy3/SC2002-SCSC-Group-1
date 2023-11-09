package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public interface StudentCampOptions {

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

    static List<Camp> getEligibleCamps(Student student, List<Camp> campList, List<Signup> signupList) { //TODO disallow when camp is full
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
                    && !camp.isFull() && camp.checkCampStatus() == campStatus.OPEN) {//camp eligibility check
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

    static boolean extraViewOptions(Student student, Camp selectedCamp, List<User> userList, List<Signup> signupList) {
        Scanner sc = new Scanner(System.in);
        CampView.showSummary(selectedCamp);
        System.out.println("Press Enter to go back.");
        if (student.getCommittee() != selectedCamp.getID()) {
            System.out.println("To cancel this signup type 'cancel'");
        }
        if (!selectedCamp.isFullCommittee() && student.getCommittee() == -1) {
            System.out.println("To upgrade to Committee member type 'committee'");
        }//TODO check for slots
        String response = sc.nextLine();
        if (response.equals("cancel") && student.getCommittee() != selectedCamp.getID()) {
            System.out.println("Once you cancel, you will not be able to sign up for this camp again! Are you sure? Y/N");
            int input = -1;
            while (input == -1) {
                input = InputChecker.parseUserBoolInput(sc.nextLine());
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
                input = InputChecker.parseUserBoolInput(sc.nextLine());
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

