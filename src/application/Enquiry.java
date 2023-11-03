package application;

public class Enquiry {
    // static variable to keep track of en_ID
    static int en_NUM = 1;

    // description of the enquiry. Usually a question
    private String description;
    // reply of the enquiry. Usually an answer
    private String reply;
    // unique ID whenever the Enquiry is generated
    private int en_ID;
    // status of the enquiry. Should change to ENUM?
    private boolean resolved;
    // author of the Enquiry
    private Student author;
    // Camp associated with the enquiry
    private Camp camp;

    // Constructor
    public Enquiry(String description, Student author, Camp camp) {
        this.description = description;
        this.author = author;
        this.camp = camp;

        this.resolved = false;
        en_NUM++;
        this.en_ID = en_NUM;
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

    public int getEn_ID() {
        return en_ID;
    }

    public Camp getCamp() {
        return camp;
    }

    // functions to create
    // Student
    // view Y
    // edit Y?
    // create Y
    // delete Y

    // Staff
    // reply

    // Camp committee
    // reply

    // functions to implement
    // Student
    // view Y
    // edit Y?
    // create Y
    // delete

    // Staff
    // reply

    // Camp committee
    // reply
}
