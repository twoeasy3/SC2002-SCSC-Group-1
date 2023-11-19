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
 * This class implements all methods that pertain to viewing Suggestions.
 */
public interface SuggestionView{
    /**
     * Returns a preview of a Suggestion to a String. Can be displayed or printed to report.<br>
     * Outputs the following:
     * 
     * Suggestion Author (faculty)<br>
     * Suggestion body text<br>
     * Suggestion Category<br>
     * SuggestionStatus of the Suggestion<br>
     * 
     * @param suggestion Suggestion to be output
     * @return Readable string output of the suggestion
     */
    static String singleSuggestionToString(Suggestion suggestion) {
        String printLine = "";
        List<String> fieldNames = Arrays.asList("Camp Name", "Venue", "Description", "Max Slots", "Committee Slots");
        printLine = String.format("%s (%s) suggests changing %s to %s.", suggestion.getAuthor().getName(),
                suggestion.getAuthor().getFaculty(),
                fieldNames.get(suggestion.getChangeCategory()-1),
                suggestion.getChange()) + "\n";
        printLine += suggestion.getDescription() + "\n";
        printLine += String.format("[%s]",suggestion.getStatus().name()) + "\n";
        printLine += "-----------------------";
        return(printLine);
    }

    /**
     * Fetches all Suggestions that a Student can edit.<br>
     * Only PENDING Suggestions can be edited.
     * @param student Student whose Suggestions to fetch from
     * @param sStatus SuggestionStatus to match. Should be filled with PENDING, but left here for extendability.
     * @return List of Suggestions that suit the parameters
     */
    public static List<Suggestion> findEditableSuggestions(Student student, SuggestionStatus sStatus){
        Camp targetCamp = ((StudentCommittee) student).getCamp();
        List<Suggestion> relevantSuggestions = new ArrayList<>();
        for (Suggestion suggestion : targetCamp.getSuggestionList()){
            if (suggestion.getStatus()==sStatus && suggestion.getAuthor() == student){
                relevantSuggestions.add(suggestion);
            }
        }
        return  relevantSuggestions;
    }

    /**
     * Prints the Suggestions that are editable. Calls findEditableSuggestions. <br>
     * Gives each found Suggestion an index to select from selectSuggestion()
     * @param student Student to show editable suggestions
     * @param sStatus SuggestionStatus to match. Should be filled with PENDING, but left here for extendability.
     */
    public static void viewEditableSuggestions(Student student, SuggestionStatus sStatus) {
        Camp targetCamp = ((StudentCommittee) student).getCamp();
        List<Suggestion> suggestionList = findEditableSuggestions(student,sStatus);
        if(suggestionList.size()==0){
            System.out.println("No suggestions to show;");
            return;
        }
        Comparator<Suggestion> suggestionComparator = Comparator
                .comparing(suggestion -> suggestion.getCamp().getName());
        suggestionList.sort(suggestionComparator);
        int i = 0;
        for (Suggestion suggestion : suggestionList){
            i++;
            System.out.printf(i + ": ");
            System.out.println(singleSuggestionToString(suggestion));
        }
    }

    /**
     * Method to select a Suggestion. The user should see the list from viewEditableSuggestions() first. <br>
     * Reads an integer and selects the Suggestion that corresponds to it.
     * @param student Student to search for Suggestions
     * @param sStatus SuggestionStatus to match.
     * @return Suggestion that the user has selected
     */
    public static Suggestion selectSuggestion(Student student, SuggestionStatus sStatus) {
        List<Suggestion> relevantSuggestions = findEditableSuggestions(student, sStatus);
        if (relevantSuggestions.size() == 0) {
            return null;
        }

        System.out.println("Enter the number corresponding to the suggestion to select: ");
        while (true) {
            int selection = Console.nextInt();
            if (selection < 1 || selection > relevantSuggestions.size()) {
                System.out.println("Selection does not correspond to any suggestion on the list. Please select again.");
            } else {
                return relevantSuggestions.get(selection - 1);
            }

        }
    }
}
