package suggestions;

import application.*;
import helper.Console;
import helper.InputChecker;

import java.util.ArrayList;
import java.util.List;

public interface SuggestionResolver {
    default void resolveMenu(List<Camp> campList, User activeUser){

        String[] category = {"Name", "Description", "Venue", "Slots", "Committee Slots"};
        List<Camp> inchargeCamps = ((Staff) activeUser).getOwnedCamps(campList);
        ArrayList<Suggestion> suggestionList = new ArrayList<>();
        // create a list of suggestions
        for (Camp camp : inchargeCamps) {
            for (Suggestion suggestion : camp.getSuggestionList()) {
                suggestionList.add(suggestion);
            }
        }
        int i = 0;
        System.out.println("Select a Suggestion");
        for (Suggestion suggestion : suggestionList) {
            i++;
            System.out.println(i+ ": " + suggestion.getDescription());
        }
        int choice = Console.nextInt();
        Suggestion activeSuggestion = suggestionList.get(choice - 1);
        System.out.println("Changing: " + category[activeSuggestion.getChangeCategory()]);
        System.out.println("to: " + activeSuggestion.getChange());
        System.out.println("Accept? Y/N, any other input to exit without making a decision.");
        int decision = InputChecker.parseUserBoolInput(Console.nextString());
        if (decision == 1){
            System.out.println("Suggestion accepted");
            activeSuggestion.accept();
        }
        else if(decision == 0){
            System.out.println("Suggestion rejected");
            activeSuggestion.reject();
        }
        else{
            System.out.println("Decision adjourned. Suggestion still remains.");
            }

    }
}
