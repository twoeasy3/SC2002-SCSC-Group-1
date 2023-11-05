package application;

import java.time.LocalDate;
import java.util.Locale;

public class Suggestion extends Enquiry {

    private int changeCategory;

    private String change;

    public Suggestion(String description, Student author, Camp camp, int changeCategory, String change) {
        super(description, author, camp);
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

    // accept function
    // 1. Camp Name
    // 2. / 3. Dates (End date cannot be before Start)
    // 3. Registration Close (Cannot be after Start)
    // 4. Location
    // 5. Description
    // 6. Faculty (Only from School to ALL)
    // 7. Total Slots (Cannot lower below current signups)
    public void acceptSuggestions(Suggestion s){
        switch (s.getChangeCategory()) {
            case 1 -> s.getCamp().setName(s.getChange());
            case 2 -> s.getCamp().setStartDate(LocalDate.parse(s.getChange()));
            case 3 -> s.getCamp().setEndDate(LocalDate.parse(s.getChange()));
            case 4 -> s.getCamp().setLocation(s.getChange());
            case 5 -> s.getCamp().setDescription(s.getDescription());
            case 6 -> s.getCamp().setFaculty("ALL");
            case 7 -> s.getCamp().setMaxSize(Integer.parseInt(s.getChange()));
            default -> System.out.println("Error in acceptance");
        }
        s.setReply("Suggestion Accepted");
        s.getCamp().getSuggestions_list().remove(s);


    }
    public void rejectSuggestions(Suggestion s) {
        s.setReply("Suggestion Rejected");
        s.getCamp().getSuggestions_list().remove(s);
    }
}
