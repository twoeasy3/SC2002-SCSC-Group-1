package suggestions;
/**
 * SuggestionStatus of the Suggestion.
 */
public enum SuggestionStatus {
    /**
     * PENDING - Staff In-Charge has not made a decision on the suggestion
     *
     */
    PENDING,
    /**
     *  ACCEPTED - Staff In-Charge has accepted the change and it has been applied.
     */
    REJECTED,
    /**
     *  ACCEPTED - Staff In-Charge has accepted the change and it has been applied.<br>
     */
    APPROVED
}
