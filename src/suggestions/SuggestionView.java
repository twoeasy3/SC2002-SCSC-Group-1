package suggestions;
import application.Camp;
import helper.Console;
import application.Student;
import application.StudentCommittee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public interface SuggestionView{

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

    public static Suggestion selectSuggestion(Student student, SuggestionStatus sStatus) {
        List<Suggestion> relevantSuggestions = findEditableSuggestions(student, sStatus);
        if (relevantSuggestions.size() == 0) {
            return null;
        }

        System.out.println("Enter the number corresponding to the suggestion to select: ");
        int selection = Console.nextInt();
        while (true) {
            if (selection < 1 || selection > relevantSuggestions.size()) {
                System.out.println("Selection does not correspond to any suggestion on the list. ");
            } else {
                return relevantSuggestions.get(selection - 1);
            }

        }
    }
}
