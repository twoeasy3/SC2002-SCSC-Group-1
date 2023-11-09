package enquiry;

import application.Camp;
import application.Student;

public abstract class Message {
    private Camp camp;
    private Student author;
    private String description;

    public Message(Camp camp, Student author, String description){
        this.camp = camp;
        this.author = author;
        this.description = description;
    }

    public Camp getCamp() {
        return camp;
    }

    public Student getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }
}
