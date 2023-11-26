package suggestions;

import application.*;
import helper.Console;
import helper.DataHandler;

import java.util.Arrays;
import java.util.List;
/**
 * The suggestion editor interface has general methods to edit suggestions which will be implemented in the classes that
 * realize from this interface.
 */
public interface SuggestionEditorInterface {
    /**
     * This method will be implemented in the SuggestionEditor class
     * @param selectedCamp Camp object to be suggested on
     * @param activeUser User object making the suggestion
     * @param suggestionList List of all Suggestions
     */
    static void suggestionMaker(Camp selectedCamp, User activeUser, List<Suggestion> suggestionList) {}

    /**
     * This method will be implemented in the SuggestionEditor class
     * @param suggestionList List of all Suggestions to save program state
     * @param suggestion Suggestion to change category value
     */
    static void changeCategoryValue(List<Suggestion> suggestionList, Suggestion suggestion) {}

    /**
     * This method will be implemented in the SuggestionEditor class
     * @param studComm The user editing the Suggestion
     * @param suggestionList List of all Suggestions to save program state
     */
    public static void editMenu(StudentCommittee studComm,List<Suggestion> suggestionList ) {}

    /**
     * This method will be implemented in the SuggestionEditor class
     * The User can choose to change the Suggestion description or Suggestion change itself<br>
     * Any changes to the Suggestion value or category requires both to be re-entered<br>
     * @param suggestion Suggestion object to be edited
     * @param SuggestionList List of all Suggestion objects to save program state
     */
    public static void editSuggestion(Suggestion suggestion, List<Suggestion> SuggestionList) {}

    /**
     * This method will be implemented in the SuggestionEditor class
     * @param studComm Student deleting the suggestion
     * @param suggestion Suggestion to be deleted
     * @param SuggestionList List of all Suggestion object, to save program state.
     */
    // removes Suggestion from both the camp and the user
    public static void deleteSuggestion(StudentCommittee studComm ,Suggestion suggestion, List<Suggestion> SuggestionList) {}


}
