package helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Helper class for different kinds of input checking from the user.
 */
public class InputChecker {

    /**
     * Resolves the argument when an option allows a user to optionally enter 2 arguments like "2 -c" <br>
     * The method strips the "-" and any spaces. <br>
     * @param response String input to parse the argument from
     * @return The selected argument
     */
    public static String resolveArgument(String response){
        response = response.replaceAll("[-\\s]+", "");
        response = response.substring(1);
        return response;
    }

    /**
     * Checks whether a String input is a valid date.<br>
     * Input to be in yyyyMMdd format. <br>
     * Dates in the past and dates after 2024 are not permitted <br>
     * @param input String input for date, supposed to be in yyyyMMdd format.
     * @return Boolean value on whether the date is valid or not.
     */
    public static boolean dateValidity(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        if (input.length() != 8) {
            System.out.println("Input date must be 8 digits only!");
            return false;
        }
        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c)) {
                System.out.println("Input date must be digits only!");
                return false;
            }
        }
        try {
            LocalDate date = LocalDate.parse(input, formatter);
            if (date.isBefore(LocalDate.now()) ){
                System.out.println("Input date is in the past!");
                return false;
            }
            if (date.isAfter(LocalDate.of(2024, 12, 31))) {
                System.out.println("Input date is too far in the future! (Dates in 2023 and 2024 allowed)");
                return false;
            }
            return true;
        }catch(Exception e){
            System.out.println("Input date is not a valid date!");
            return false;
        }


    }

    /**
     * Checks if integer input is valid.
     * @param input String input to check
     * @return Boolean value on whether input integer is valid.
     */
    public static boolean intValidity(String input){
        int test;
        try{
            test = Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("The input is not a valid integer.");
            return false;
        }

    }
    /**
     * Helper method to parse Y/N user input responses.
     * Supports lower and uppercase letters.
     *
     * @param input User text input when prompted Y/N
     * @return Returns 1 for true, 0 for false and -1 for invalid response.
     */
    public static int parseUserBoolInput(String input){
        if (input.equals("Y") || input.equals("y")){
            return 1; // true input
        }
        else if (input.equals("N")|| input.equals("n")){
            return 0; // false input
        }
        else{
            System.out.println("Non-Y/N response.");
            return -1;} //invalid input
    }

}
