package application;

import java.util.List;

public class Fetcher {
    /**
     * Finds a User object and returns it.
     * Used in login screen, object is selected as the active user.
     * @param userID UserID as a String to look for.
     * @param schoolList List of all Users loaded in by DataHandler.
     * @return Appropriate User object if matching ID is found. If no user matched, dummy error object is returned.
     *
     */
    public static User getUserFromID (String userID, List<User> schoolList) {
        for (User user : schoolList) {
            if(user.getID().equals(userID)) {

                return user;
            }
        }
return null;
        }

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

    public static Camp getCampfromID(int id, List<Camp> campList){
        for(Camp camp : campList){
            if (camp.getID() == id){
                return camp;
            }
        }
        return null;
    }
}
