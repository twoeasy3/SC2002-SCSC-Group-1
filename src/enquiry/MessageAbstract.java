package enquiry;
import application.Camp;
import application.Student;
/**
 * Superclass that provides a general template for subclasses to use.
 */
public abstract class MessageAbstract {
    /**
     * Camp that the Message is pertinent to.
     */
    private Camp camp;
    /**
     * Author that the Message is from. Current specifications only allow Students to author Messages.
     */
    private Student author;
    /**
     * Text body of the Message.
     */
    private String description;

    /**
     * Simple constructor to fill out the variables.
     * @param camp Camp object the messages is relevant to
     * @param author Student object whom the message is authored by
     * @param description String of the text body for the Message.
     */

    public MessageAbstract(Camp camp, Student author, String description){
        this.camp = camp;
        this.author = author;
        this.description = description;
    }

    /**
     * Fetch the camp object associated with this Message
     * @return Camp object associated with this Message
     */
    public Camp getCamp() {
        return camp;
    }

    /**
     * Fetches the Student object who authored the Message
     * @return Student object who authored the Message
     */
    public Student getAuthor() {
        return author;
    }

    /**
     * Fetches the text body of the Message
     * @return Text body of the Message
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the text body of the Message
     * @param description New text body of the Message
     */
    public void setDescription(String description){
        this.description = description;
    }

}
