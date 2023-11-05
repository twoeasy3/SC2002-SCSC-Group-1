package application;

public class Enquiry {
    // description of the enquiry. Usually a question
    private String description;
    // reply of the enquiry. Usually an answer
    private String reply;
    // author of the Enquiry
    private Student author;
    // Camp associated with the enquiry
    private Camp camp;

    // Constructor
    public Enquiry(String description, Student author, Camp camp) {
        this.description = description;
        this.author = author;
        this.camp = camp;
    }

    public String getDescription() {
        return description;
    }

    public String getReply() {
        return reply;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Camp getCamp() {
        return camp;
    }
    // reply to enquiry. Can be called by both staff and committee members. Sets the reply and
    // removes it from the camp ?Inefficient?
    public void replyEnquiry(Enquiry e, String s) {
        e.setReply(s);
        e.camp.getEnquiry_list().remove(e);
    }


    // functions to implement
    // Student
    // view
    // edit
    // create
    // delete

    // Staff
    // reply

    // Camp committee
    // reply
}
