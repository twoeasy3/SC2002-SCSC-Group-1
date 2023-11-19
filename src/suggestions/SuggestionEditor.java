package suggestions;

import application.*;
import helper.Console;
import helper.DataHandler;

import java.util.Arrays;
import java.util.List;
/**
 * Methods that relate to editing Suggestions are implemented here.
 */
public interface SuggestionEditor {
    /**
     * This method creates Suggestions. To be used by StudentCommittee. <br>
     * Some restrictions are placed on the StudentCommittee on what they are allowed to suggest. StudentCommittee can only ever suggest in these 5 fields: <br>
     * Camp Name, Venue, Description, Max Slots and Committee Slots <br>
     * This makes use of CampEdit.tryEditCamp() to validate some of the fields that can be suggested.
     * @param selectedCamp Camp object to be suggested on
     * @param activeUser User object making the suggestion
     * @param suggestionList List of all Suggestions
     */
    static void suggestionMaker(Camp selectedCamp, User activeUser, List<Suggestion> suggestionList) {

        List<String> fieldNames = Arrays.asList("Camp Name", "Venue", "Description", "Max Slots", "Committee Slots");
        if (activeUser instanceof StudentCommittee) {
            System.out.println("Please enter a short description of your suggestion");
            String suggestionDesc = Console.nextString();
            System.out.println("Which category would your suggestion like to be in?");
            System.out.println("[1] Camp Name");
            System.out.println("[2] Venue");
            System.out.println("[3] Description");
            System.out.println("[4] Max Slots");
            System.out.println("[5] Committee Slots");
            int choice = Console.nextInt();
            if (choice >= 1 && choice <= 5) {
                System.out.println("Please enter the new " + fieldNames.get(choice - 1));
                String change = Console.nextString();
                if (CampEdit.tryEditCamp(selectedCamp, choice, change)) {
                    System.out.println("Your suggestion has been posted!");
                    suggestionList.add(new Suggestion(selectedCamp, (Student) activeUser,
                            suggestionDesc,  choice, change, SuggestionStatus.PENDING));
                } else {
                    System.out.println("Suggestion was not posted");
                }

            }

        }
    }

    /**
     * Interface for and resolves the selection of category value to change.
     * @param suggestionList List of all Suggestions to save program state
     * @param suggestion Suggestion to change category value
     */
    static void changeCategoryValue(List<Suggestion> suggestionList, Suggestion suggestion) {
        List<String> fieldNames = Arrays.asList("Camp Name", "Venue", "Description", "Max Slots", "Committee Slots");
        System.out.println("Select new category for Suggestion: ");
        System.out.println("[1] Camp Name");
        System.out.println("[2] Venue");
        System.out.println("[3] Description");
        System.out.println("[4] Max Slots");
        System.out.println("[5] Committee Slots");
        int choice = Console.nextInt();
        if (choice >= 1 && choice <= 5) {
            System.out.println("Please enter the new " + fieldNames.get(choice - 1));
            String change = Console.nextString();
            if (CampEdit.tryEditCamp(suggestion.getCamp(), choice, change)) {
                System.out.println("Your suggestion has been edited!");
                suggestion.setChangeCategory(choice);
                suggestion.setChange(change);
                DataHandler.saveSuggestions(suggestionList);
            } else {
                System.out.println("Suggestion was not changed");
            }
        }
    }

    /**
     * This menu provides the logic and the interface for editing a Suggestion
     * @param studComm The user editing the Suggestion
     * @param suggestionList List of all Suggestions to save program state
     */
    public static void editMenu(StudentCommittee studComm,List<Suggestion> suggestionList ) {
        Student student = studComm;
        while(true) {
            Suggestion selectedSuggestion = null;
            SuggestionView.viewEditableSuggestions(student, SuggestionStatus.PENDING);
            selectedSuggestion = SuggestionView.selectSuggestion(student, SuggestionStatus.PENDING);
            if (selectedSuggestion == null) {
                System.out.println("No pending suggestions found for editing.");
                Console.nextString();
                return;
            }
            System.out.println(SuggestionView.singleSuggestionToString(selectedSuggestion));
            System.out.println("1. Edit this Suggestion, 2. Delete this Suggestion, 3. Select a new Suggestion or 4. Exit");
            int selection = Console.nextInt();
            switch (selection) {
                case 1:
                    SuggestionEditor.editSuggestion(selectedSuggestion, suggestionList);
                    return;
                case 2:
                    SuggestionEditor.deleteSuggestion(studComm, selectedSuggestion, suggestionList);
                    return;
                case 3:
                    break;
                case 4:
                    System.out.println("Exiting");
                    return;
                default:
                    break;
            }
        }
    }

    /**
     * This provides the interface and logic for editing a Suggestion.<br>
     * The User can choose to change the Suggestion description or Suggestion change itself<br>
     * Any changes to the Suggestion value or category requires both to be re-entered<br>
     * @param suggestion Suggestion object to be edited
     * @param SuggestionList List of all Suggestion objects to save program state
     */
    public static void editSuggestion(Suggestion suggestion, List<Suggestion> SuggestionList) {
        System.out.println("Would you like to edit the 1. Description or 2. Suggestion Category/Value?");
        int selection = Console.nextInt();
        if(selection == 1){
            System.out.println("Enter new description for your suggestion: ");
            String newDescription = Console.nextString() + "\n**This suggestion has been edited**";
            suggestion.setDescription(newDescription);
            DataHandler.saveSuggestions(SuggestionList);
        }
        else if(selection == 2 ){
            SuggestionEditor.changeCategoryValue(SuggestionList,suggestion);
        }
        else{
            System.out.println("Unrecognised response, aborting...");
        }

    }

    /**
     * This method provides the logic to delete a Suggestion
     * @param studComm Student deleting the suggestion
     * @param suggestion Suggestion to be deleted
     * @param SuggestionList List of all Suggestion object, to save program state.
     */
    // removes Suggestion from both the camp and the user
    public static void deleteSuggestion(StudentCommittee studComm ,Suggestion suggestion, List<Suggestion> SuggestionList) {
        studComm.getCamp().getSuggestionList().remove(suggestion);
        SuggestionList.remove(suggestion);
        System.out.println("Your Suggestion has been deleted");
        studComm.addPoints(-1);
    }


}
