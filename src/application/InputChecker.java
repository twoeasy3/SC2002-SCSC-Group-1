package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InputChecker {


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
