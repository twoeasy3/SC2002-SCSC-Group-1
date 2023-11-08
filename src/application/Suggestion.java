package application;

import java.time.LocalDate;
enum suggestionStatus{
    PENDING,REJECTED,APPROVED
}
public class Suggestion extends Message {

    private int changeCategory;
    private String change;
    private suggestionStatus status;

    public Suggestion(Camp camp, Student author, String description,
                      int changeCategory, String change,suggestionStatus status) {
        super(camp, author, description);
        this.changeCategory = changeCategory;
        this.change = change;
        this.status = status;

        this.getCamp().addSuggestion(this);
        if(this.status == suggestionStatus.APPROVED){
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

    public void reject() {
        this.status = suggestionStatus.REJECTED;
    }

    public void accept(){
        this.status = suggestionStatus.APPROVED;
        this.getCamp().doEditCamp(changeCategory,change);
        ((StudentCommittee) this.getAuthor()).addPoints(1);
    }
    }