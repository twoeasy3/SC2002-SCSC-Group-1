package suggestions;

import application.*;
import helper.Console;
import helper.InputChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the necessary code for resolving suggestions
 */
public interface SuggestionResolver {
    /**
     * This method implements a menu for Staff-In-Charge to resolve Suggestions. <br>
     * Displays all Suggestions to resolve, then allows the Staff to select one and make a verdict<br>
     * @param campList List of all Camps
     * @param activeUser User object of the activeUser
     */
    default void resolveMenu(List<Camp> campList, User activeUser){

        String[] category = {"Name", "Description", "Venue", "Slots", "Committee Slots"};
        List<Camp> inchargeCamps = ((Staff) activeUser).getOwnedCamps(campList);
        ArrayList<Suggestion> suggestionList = new ArrayList<>();
        // create a list of suggestions
        for (Camp camp : inchargeCamps) {
            suggestionList.addAll(camp.getSuggestionList());
        }
        int i = 0;
        while(true) {
            if(suggestionList.size() == 0){
                System.out.println("No pending suggestions for you to rule on\n");
                return;
            }
            String currentCampName = "";
            System.out.println("Select a Suggestion");
            for (Suggestion suggestion : suggestionList) {
                if(suggestion.getCamp().getName() != currentCampName){
                    currentCampName = suggestion.getCamp().getName();
                    System.out.println("========" + currentCampName + "==========");
                }
                i++;
                System.out.println(i + ": ");
                System.out.println(suggestion.getAuthor().getName() + "'s suggestion: " + suggestion.getDescription());
            }
            System.out.println("Enter the number to select a suggestion or enter 0 to return");
            int choice = Console.nextInt();
            if (choice == 0) {
                return;
            }
            Suggestion activeSuggestion = suggestionList.get(choice - 1);
            System.out.println("========" + activeSuggestion.getCamp().getName() + "==========");
            System.out.println("Changing: " + category[activeSuggestion.getChangeCategory() - 1]);
            System.out.println("to: " + activeSuggestion.getChange());
            System.out.println("Accept? Y/N, any other input to exit without making a decision.");
            int decision = InputChecker.parseUserBoolInput(Console.nextString());
            if (decision == 1) {
                System.out.println("Suggestion accepted");
                activeSuggestion.accept();
                suggestionList.remove(activeSuggestion);
            } else if (decision == 0) {
                System.out.println("Suggestion rejected");
                activeSuggestion.reject();
                suggestionList.remove(activeSuggestion);
            } else {
                System.out.println("Decision adjourned. Suggestion still remains.");
            }
        }
    }
}
