package application;

public class Enquiry {
    /**
     * static variable to keep track of enquiryID
     */
    static int en_NUM = 1;

    /**
     * description of the enquiry. Usually a question
     */
    private String description;
    /**
     * reply of the enquiry. Usually an answer
     */
    private String reply;
    /**
     * unique ID whenever the Enquiry is generated
     */
    private int enquiryID;
    /**
     * status of the enquiry. Should change to ENUM?
     */
    private boolean resolved;
    /**
     * author of the Enquiry
     */
    private Student author;
    /**
     * Author of the reply
     */
    private User replyAuthor;
    /**
     * Camp associated with the enquiry
     */
    private Camp camp;

    // Constructor
    public Enquiry(int enquiryID, Camp camp, Student author, String description, User replyAuthor,
                    String reply, boolean resolved) {
        this.enquiryID = enquiryID;
        this.camp = camp;
        this.author = author;
        this.description = description;
        this.replyAuthor = replyAuthor;
        this.reply = reply;
        this.resolved = resolved;

        this.camp.addEnquiry(this);
        if(!(replyAuthor==null) && replyAuthor instanceof StudentCommittee){
            ((StudentCommittee) replyAuthor).addPoints(1);
        }

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

    public int getEnquiryID() {
        return enquiryID;
    }

    public Camp getCamp() {
        return camp;
    }
    public Student getAuthor(){
        return author;
    }
    public void setReplyAuthor(User author){
        this.replyAuthor = author;
        ((StudentCommittee)this.replyAuthor).addPoints(1);}

    public User getReplyAuthor(){
        return replyAuthor;
    }
    public boolean getResolved(){
        return resolved;
    }
    public void setResolved(boolean status){resolved = status;}


}
