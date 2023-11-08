package application;

import java.time.LocalDate;

public class Suggestion extends Enquiry {

    private int changeCategory;

    private String change;

    public Suggestion(int enquiryID, Camp camp, Student author, String description, User replyAuthor,
                      String reply, boolean resolved, int changeCategory, String change) {
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

    public void accept(){
        System.out.printf("DEBUG 1");
        switch (this.getChangeCategory()) {
            case 1 -> {
                this.getCamp().setName(this.getChange());
                break;
            }
            case 2 -> {
                System.out.printf("DEBUG 2");
                this.getCamp().setLocation(this.getChange());
                break;
            }
            case 3 -> {
                this.getCamp().setDescription(this.getChange());
                break;
            }
            case 4 -> {
                this.getCamp().setMaxSize(Integer.parseInt(this.getChange()));
                break;
            }
            case 5 -> {
                this.getCamp().setMaxComm(Integer.parseInt(this.getChange()));
                break;
            }
            default -> System.out.println("Error in acceptance");
        }
        this.setReply("Suggestion Accepted");
        this.getCamp().getSuggestionList().remove(this);


    }
    }