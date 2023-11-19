package helper;

import application.Camp;
import application.Student;
import application.StudentCommittee;
import application.User;

import java.util.List;

/**
 * Helper class for fetching various data from different forms
 */
public class Fetcher {
    /**
     * Finds a User object and returns it.
     * Used in login screen, object is selected as the active user.
     * @param userID UserID as a String to look for.
     * @param userList List of all Users loaded in by DataHandler.
     * @return Appropriate User object if matching ID is found. If no user matched, dummy error object is returned.
     *
     */
    public static User getUserFromID (String userID, List<User> userList) {
        for (User user : userList) {
            if(user.getID().equals(userID)) {

                return user;
            }
        }
        return null;
    }

    /**
     * Puts all committee Student objects in the Committee Lists in the corresponding Camps. <br>
     * Loaded in memory for CAMs<br>
     * @param userList List of all User objects to iterate through
     * @param campList List of all Camp objects to iterate through
     */
    public static void populateCommittees(List<User> userList, List<Camp> campList){
        Camp campObject = null;
        for(User user : userList){
            if(user instanceof Student){
                if(((Student) user).getCommittee() != 1){
                    campObject = getCampfromID(((Student) user).getCommittee(),campList);
                    if(campObject!= null){
                        campObject.addCommittee(((Student) user));
                        ((StudentCommittee) user).setCamp(campObject);
                    }
                }
            }
        }
    }

    /**
     * Fetches a Camp object from a camp ID.
     * @param id Camp ID to fetch
     * @param campList List of all Camp objects, to pull the Camp object from.
     * @return Camp object requested. Returns NULL if not found.
     */
    public static Camp getCampfromID(int id, List<Camp> campList){
        for(Camp camp : campList){
            if (camp.getID() == id){
                return camp;
            }
        }
        return null;
    }
}
