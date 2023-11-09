package application;

import helper.InputChecker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class CampEdit {
    /**
     * Checks whether the change suggested is legal. Does not edit the camp.
     * The change is to be supplied as a String, tryEditCamp() will attempt the appropriate conversions.
     * If illegal, it will print a message to the User explaining why.
     * Separated from DoEditCamp for use in the Suggestions feature.
     * When used by Staff, a true return means it is safe to call DoEditCamp with the same parameters
     * @param camp
     * @param category Category assigned int to change.
     * @param change The change to be made. Regardless of the data type, this is always a String.
     * @return Boolean on whether change is legal. Returns true for legal.
     */
    public static boolean tryEditCamp(Camp camp, int category, String change) { //true for success, false for failure
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate proposedDate;
        int proposed;
        switch (category) {
            case 1: //NAME
                return true;
            case 2: //VENUE
                return true;
            case 3: //DESCRIPTION
                return true;
            case 4: //MAX SLOTS; CONDITIONAL
                if(!InputChecker.intValidity(change)){
                    System.out.println("Input error. Not valid integer. Camp not updated.");
                    return false;
                }
                proposed = Integer.parseInt(change);
                if(proposed < 10){
                    System.out.println("Camp not updated. You can't create a camp with fewer than 10 slots!");
                    return false;
                }
                else if(proposed < camp.getAttendeeCount() + camp.getMaxComm() ){
                    System.out.println("Camp not updated. Invalid input, current signups mean you need at least " + camp.getAttendeeCount() + camp.getMaxComm() + " slots!");
                    return false;
                }
                else if(proposed > 24757){
                    System.out.println("Camp not updated. Your camp cannot have more open slots than NTU's enrolment this AY!");
                    return false;
                }
                else{
                    System.out.println("# of Camp Slots acceptable");
                    return true;
                }
            case 5: //MAX COMM; CONDITIONAL
                if(!InputChecker.dateValidity(change)){
                    System.out.println("Input error. Not valid integer. Camp not updated.");
                    return false;
                }
                proposed = Integer.parseInt(change);
                if(proposed > 10){
                    System.out.println("Camp not updated. You can't have more than 10 committee slots!");
                    return false;
                }
                else if(proposed < camp.getCommitteeCount()){
                    System.out.println("Camp not updated. You already have " + camp.getCommitteeCount() + " committee members!");
                    return false;
                }
                else{
                    System.out.println("# of Committee Slots acceptable");
                    return true;
                }
            case 6: //START DATE; ONLY IF EMPTY CAMP
                if (!InputChecker.dateValidity(change)) {
                    System.out.println("General Date Error: Camp was not edited.");
                    return false;
                }
                proposedDate = LocalDate.parse(change, formatter);
                if (proposedDate.isAfter(camp.getEnd())) {
                    System.out.println("Date Error: Start date is after End Date. Camp was not edited.");
                    return false;
                }
                if (proposedDate.isBefore(camp.getRegEnd())) {
                    System.out.println("Date Error: Start date before Registration End Date. Camp was not edited.");
                    return false;
                } else {
                    System.out.println("Date Accepted");
                    return true;
                }
            case 7://END DATE; ONLY IF EMPTY CAMP
                if (!InputChecker.dateValidity(change)) {
                    System.out.println("General Date Error: Camp was not edited.");
                    return false;
                }
                proposedDate = LocalDate.parse(change, formatter);
                if (proposedDate.isBefore(camp.getStart())) {
                    System.out.println("Date Error: End date is before Start Date. Camp was not edited.");
                    return false;
                }
                if (proposedDate.isBefore(camp.getRegEnd())) {
                    System.out.println("Date Error: Start date before Registration End Date. Camp was not edited.");
                    return false;
                } else {
                    System.out.println("Date Accepted");
                    return true;
                }
            case 8: //REG END, ONLY IF EMPTY
                if (!InputChecker.dateValidity(change)) {
                    System.out.println("General Date Error: Camp was not edited.");
                    return false;
                }
                proposedDate = LocalDate.parse(change, formatter);
                if (proposedDate.isAfter(camp.getStart())) {
                    System.out.println("Date Error: Reg End date is after Start Date. Camp was not edited.");
                    return false;
                } else {
                    System.out.println("Date Accepted");
                    return true;
                }
            case 9: //VISIBILITY, ONLY IF EMPTY
                if(InputChecker.parseUserBoolInput(change) == -1){
                    System.out.println("Camp not updated.");
                    return false;
                }
                else{
                    return true;
                }

            default:
                System.out.println("Error editing camp.");
                return false;
        }
    }

    /**
     * Sibling of tryEditCamp().
     * IMPORTANT: doEditCamp() assumes that the change is legal and possible from tryEditCamp.
     * Fatal error possible if doEditCamp() is called without checking.
     * @param camp
     * @param category Category assigned int to change.
     * @param change LEGAL change to be made.
     */
    public static void doEditCamp(Camp camp, int category, String change) { //true for success, false for failure
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate proposedDate;

        switch (category) {
            case 1: //NAME
                camp.setName(change.replace(",", ""));
                System.out.println("Camp updated. New name: " + camp.getName());
                return;
            case 2: //VENUE
                camp.setLocation(change.replace(",", ""));
                System.out.println("Camp updated. New venue: " + camp.getLocation());
                return;
            case 3: //DESCRIPTION
                camp.setDescription(change.replace(",", ""));
                System.out.println("Camp updated. New description: ");
                System.out.println(camp.getDescription());
                return;
            case 4: //MAX SLOTS; CONDITIONAL
                camp.setMaxSize(Integer.parseInt(change));
                System.out.println("Camp updated. New Max Slots: " + camp.getMaxSize());
                return;
            case 5: //MAX COMM; CONDITIONAL
                camp.setMaxComm(Integer.parseInt(change));
                System.out.println("Camp updated. New Max Committee Slots: " + camp.getMaxComm());
                return;
            case 6: //START DATE; ONLY IF EMPTY CAMP
                proposedDate = LocalDate.parse(change, formatter);
                camp.setStart(proposedDate);
                System.out.println("Camp updated. New start date: " + camp.getStart().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
                return;
            case 7://END DATE; ONLY IF EMPTY CAMP
                proposedDate = LocalDate.parse(change, formatter);
                camp.setEnd(proposedDate);
                System.out.println("Camp updated. New end date: " + camp.getEnd().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
                return;
            case 8: //REG END, ONLY IF EMPTY
                proposedDate = LocalDate.parse(change, formatter);
                camp.setRegEnd(proposedDate);
                System.out.println("Camp updated. New Reg End date: " + camp.getRegEnd().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
                return;
            case 9: //VISIBILITY, ONLY IF EMPTY
                if(InputChecker.parseUserBoolInput(change) == 1){
                    camp.setVisibility(true);
                    System.out.println("Camp updated and is now visible to eligible students.");
                    return;
                }
                else{
                    camp.setVisibility(false);
                    System.out.println("Camp updated and is no longer visible to students.");
                    return;
                }
            default:
                System.out.println("Error editing camp.");
                return;
        }
    }
}
