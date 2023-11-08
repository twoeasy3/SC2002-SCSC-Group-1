package application;

public class Enquiry extends Message{

    /**
     * reply of the enquiry. Usually an answer
     */
    private String reply;
    /**
     * status of the enquiry. Should change to ENUM?
     */
    private boolean resolved;

    /**
     * Author of the reply
     */
    private User replyAuthor;
    /**
     * Camp associated with the enquiry
     */

    // Constructor
    public Enquiry(Camp camp, Student author, String description, User replyAuthor,
                    String reply, boolean resolved) {
        super(camp,author,description);
        this.reply = reply;
        this.resolved = resolved;
        this.replyAuthor = replyAuthor;

        this.getCamp().addEnquiry(this);
        if(!(replyAuthor==null) && replyAuthor instanceof StudentCommittee){
            ((StudentCommittee) replyAuthor).addPoints(1);
        }

    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
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
