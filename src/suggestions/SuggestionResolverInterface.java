package suggestions;

import application.*;
import helper.Console;
import helper.InputChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * This interface is meant for resolving suggestions. The method will perform differently for different classes that
 * realize from this interface
 */
public interface SuggestionResolverInterface {
    /**
     * This method implements a menu for Staff-In-Charge to resolve Suggestions. <br>
     * Displays all Suggestions to resolve, then allows the Staff to select one and make a verdict<br>
     * @param campList List of all Camps
     * @param activeUser User object of the activeUser
     */
    default void resolveMenu(List<Camp> campList, User activeUser){}
}
