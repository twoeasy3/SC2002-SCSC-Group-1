package application;

import java.time.LocalDate;

public class Suggestion extends Enquiry {

    private int changeCategory;

    private String change;

    public Suggestion(int enquiryID, Camp camp, Student author, String description, User replyAuthor,
                      String reply, boolean resolved) {
        super(enquiryID, camp, author, description, replyAuthor, reply, resolved);
        this.changeCategory = changeCategory;
        this.change = change;
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

    public void rejectSuggestions(Suggestion s) {
        s.setReply("Suggestion Rejected");
        s.getCamp().getSuggestionList().remove(s);
    }
}