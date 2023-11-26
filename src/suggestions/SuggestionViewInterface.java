package suggestions;
import application.Camp;
import helper.Console;
import application.Student;
import application.StudentCommittee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * This interface contains the general methods that relate to viewing suggestions which will be implemented in the classes
 * that realize from this interface.
 */
public interface SuggestionViewInterface {
    /**
     * Method will be implemented in SuggestionView class
     * @param suggestion Suggestion to be output
     * @return Readable string output of the suggestion
     */
    public static void singleSuggestionToString(Suggestion suggestion) {}

    /**
     * Fetches all Suggestions that a Student can edit.<br>
     * Only PENDING Suggestions can be edited.
     * @param student Student whose Suggestions to fetch from
     * @param sStatus SuggestionStatus to match. Should be filled with PENDING, but left here for extendability.
     * @return List of Suggestions that suit the parameters
     */
    public static void findEditableSuggestions(Student student, SuggestionStatus sStatus){}

    /**
     * Prints the Suggestions that are editable. Calls findEditableSuggestions. <br>
     * Gives each found Suggestion an index to select from selectSuggestion()
     * @param student Student to show editable suggestions
     * @param sStatus SuggestionStatus to match. Should be filled with PENDING, but left here for extendability.
     */
    public static void viewEditableSuggestions(Student student, SuggestionStatus sStatus) {}

    /**
     * Method to select a Suggestion. The user should see the list from viewEditableSuggestions() first. <br>
     * Reads an integer and selects the Suggestion that corresponds to it.
     * @param student Student to search for Suggestions
     * @param sStatus SuggestionStatus to match.
     * @return Suggestion that the user has selected
     */
    public static void selectSuggestion(Student student, SuggestionStatus sStatus) {}
}
