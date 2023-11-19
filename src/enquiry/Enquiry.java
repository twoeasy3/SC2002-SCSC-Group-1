package enquiry;

import application.*;

/**
 * Enquiries consist of a question for a camp, and a reply if one has been made
 */
public class Enquiry extends Message {

    /**
     * Reply of the enquiry. Usually an answer
     */
    private String reply;
    /**
     * Status of the enquiry, can be resolved or unresolved.
     */
    private boolean resolved;
    /**
     * Author of the reply
     */
    private User replyAuthor;


    /**
     * Constructor for an enquiry.
     * @param camp Camp object the enquiry is associated with
     * @param author Student object who authored the enquiry
     * @param description Body text of the enquiry
     * @param replyAuthor User object who authored the reply
     * @param reply Body text of the reply
     * @param resolved Boolean on whether the enquiry has been replied to
     */
    public Enquiry(Camp camp, Student author, String description, User replyAuthor,
                   String reply, boolean resolved) {
        super(camp,author,description);
        this.reply = reply;
        this.resolved = resolved;
        this.replyAuthor = replyAuthor;

        this.getCamp().addEnquiry(this);
        this.getAuthor().addEnquiry(this);
        if(!(replyAuthor==null) && replyAuthor instanceof StudentCommittee){
            ((StudentCommittee) replyAuthor).addPoints(1);
        }
    }

    /**
     * Fetch the reply to the Enquiry
     * @return Reply string
     */
    public String getReply() {
        return reply;
    }

    /**
     * Set the reply to thr Enquiry
     * @param reply String to change reply to
     */
    public void setReply(String reply) {
        this.reply = reply;
    }

    /**
     * Set the reply Author
     * @param author User object as the reply Author
     */
    public void setReplyAuthor(User author) {
        this.replyAuthor = author;
        if (author instanceof StudentCommittee) {
            ((StudentCommittee) this.replyAuthor).addPoints(1);
        }
    }

    /**
     * Fetches reply author
     * @return User object who made the reply
     */
    public User getReplyAuthor(){
        return replyAuthor;
    }

    /**
     * Fetch resolved status of the enquiry
     * @return Boolen of enquiry status
     */
    public boolean getResolved(){
        return resolved;
    }

    /**
     * Set resolved status of the enquiry
     * @param status Boolean value to set the enquiry to.
     */
    public void setResolved(boolean status){resolved = status;}


}
