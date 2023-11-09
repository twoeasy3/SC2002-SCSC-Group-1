package suggestions;

import application.Camp;
import application.CampEdit;
import enquiry.Message;
import application.Student;
import application.StudentCommittee;

public class Suggestion extends Message {

    private int changeCategory;
    private String change;
    private SuggestionStatus status;

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
    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public int getChangeCategory() {
        return changeCategory;
    }

    public void setChangeCategory(int changeCategory) {
        this.changeCategory = changeCategory;
    }
    public SuggestionStatus getStatus() {return status;}

    public void reject() {
        this.status = SuggestionStatus.REJECTED;
    }

    public void accept(){
        this.status = SuggestionStatus.APPROVED;
        CampEdit.doEditCamp(this.getCamp(), changeCategory,change);
        ((StudentCommittee) this.getAuthor()).addPoints(1);
    }
    }