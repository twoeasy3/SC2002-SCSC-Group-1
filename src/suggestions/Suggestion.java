package suggestions;

import application.Camp;
import application.CampEdit;
import enquiry.Message;
import application.Student;
import application.StudentCommittee;

/**
 * Suggestion class implementation extends the Message superclass.
 */
public class Suggestion extends Message {
    /**
     * The category that the suggestion proposes to change. The following categories are:
     * 1. Camp Name<br>
     * 2. Camp Venue<br>
     * 3. Camp Description<br>
     * 4. Max Slots<br>
     * 5. Committee Slots<br>
     *
     */
    private int changeCategory;
    /**
     * The value of the change proposed. This is always a String.
     */
    private String change;
    /**
     * SuggestionStatus of the Suggestion.
     * PENDING - Staff In-Charge has not made a decision on the suggestion<br>
     * REJECTED - Staff In-Charge has decided not to accept the suggestion<br>
     * ACCEPTED - Staff In-Charge has accepted the change and it has been applied.<br>
     */
    private SuggestionStatus status;

    /**
     * Suggestion constructor. Fills in all relevant fields with some extra behaviour. <br>
     * An APPROVED Suggestion being constructed directly credits 2 points to the author. (1 for suggestion, 1 for accepting) <br>
     * Any other Suggestion credits 1 point for its creation.<br>
     * @param camp Camp Object that the Suggestion is for
     * @param author Student who authored the Suggestion
     * @param description String explaining the rationale of the Suggestion
     * @param changeCategory Integer to select the category to change
     * @param change The String containing the value to change to
     * @param status The SuggestionStatus of the Suggestion.
     */
    public Suggestion(Camp camp, Student author, String description,
                      int changeCategory, String change, SuggestionStatus status) {
        super(camp, author, description);
        this.changeCategory = changeCategory;
        this.change = change;
        this.status = status;

        this.getCamp().addSuggestion(this);
        if(this.status == SuggestionStatus.APPROVED){
            ((StudentCommittee) this.getAuthor()).addPoints(2);
        }
        else{
            ((StudentCommittee) this.getAuthor()).addPoints(1);
        }
    }

    /**
     * Fetch the String change value
     * @return String of change value
     */
    public String getChange() {
        return change;
    }

    /**
     * Set the String change value
     * @param change New String for change value
     */
    public void setChange(String change) {
        this.change = change;
    }

    /**
     * Fetch the integer of the change category
     * @return Integer changeCategory
     */
    public int getChangeCategory() {
        return changeCategory;
    }

    /**
     * Set the change category
     * @param changeCategory Integer corresponding to the new category
     */
    public void setChangeCategory(int changeCategory) {
        this.changeCategory = changeCategory;
    }

    /**
     * Fetch the SuggestionStatus of the Suggestion
     * @return SuggestionStatus of the Suggestion
     */
    public SuggestionStatus getStatus() {return status;}

    /**
     * Rejects a Suggestion. Only sets SuggestionStatus to REJECTED. <br>
     * Record of the Suggestion persists for report generation and for points tallying.
     */
    public void reject() {
        this.status = SuggestionStatus.REJECTED;
    }
    /**
     * Accepts a Suggestion. Sets SuggestionStatus to ACCEPTED. <br>
     * Credits only 1 point in this instance because the program state already credited the author with 1 point for the creation of the Suggestion. <br>
     * Record of the Suggestion persists for report generation and for points tallying. <br>
     */
    public void accept(){
        this.status = SuggestionStatus.APPROVED;
        CampEdit.doEditCamp(this.getCamp(), changeCategory,change);
        ((StudentCommittee) this.getAuthor()).addPoints(1);
    }
    }