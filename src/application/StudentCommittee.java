package application;

import suggestions.*;

/**
 * This class houses all the required variables and methods relevant to a Student Committee member. <br>
 * All Students should be constructed as a StudentCommittee, and then immediately upcast as a Student. <br>
 * This is to reflect the fact that all Students have the option to be a StudentCommittee member, while also retaining their Student status for all other camps.
 */
public class StudentCommittee extends Student implements AdminActionsInterface, SuggestionViewInterface, SuggestionEditorInterface {
    /**
     * Camp object in which the StudentCommittee is a committee member of
     */
    private Camp camp;

    /**
     * The total points that the StudentCommittee is sitting on. <br>
     * +1 for enquiry answered<br>
     * +1 for suggestion made<br>
     * +1 for suggestion accepted
     */
    private int points;

    /**
     * Constructor for StudentCommittee. Fills in a null object for camp for the time being.
     * @param name Name of Student
     * @param id StudentID (NTU email prefix)
     * @param faculty Student faculty
     * @param password Hashed password
     * @param committee CampID of camp student is committee of.
     */
    public StudentCommittee(String name, String id, String faculty,String password,int committee){
        super(name,id,faculty,password,committee);
        this.camp = null;
    }

    /**
     * Fetch the Camp object that the StudentCommittee is part of
     * @return Camp Object that the Student is committee member of
     */
    public Camp getCamp(){return camp;}

    /**
     * Set the Camp object that the StudentCommittee is part of<br>
     * Should only be used to set a StudentCommittee's camp from null to a camp
     * @param camp Camp object to assign to the StudentCommittee
     */
    public void setCamp(Camp camp){
        this.camp = camp;
    }

    /**
     * Adds an amount of points to the StudentCommittee's score. Can be negative.
     * Called for the following instances: <br>
     * +1 for enquiry answered<br>
     * +1 for suggestion made<br>
     * +1 for suggestion accepted<br>
     * -1 for suggestion deleted
     * @param score Points to be added
     */
    public void addPoints(int score){
        this.points += score;
    }

    /**
     * Fetch the score that the StudentCommittee is sitting on
     * @return Integer score of the StudentCommittee member
     */
    public int getPoints(){return points;}

}
