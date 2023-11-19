package helper;

import java.util.Scanner;

/**
 * This is a helper class to handle all user inputs. <br>
 * Eliminates the need to call for a scanner for each method.
 */
public class Console {
    /**
     * Returns the next integer that the user inputs, then flushes the rest of the input. <br>
     * This does not return until an integer is obtained, reducing the need to error-check for this instance for other methods.
     * @return First legal integer input from user.
     */
    public static int nextInt(){
        Scanner scanner = new Scanner(System.in);
        String response = "";
        boolean goodInput = false;
        while(goodInput == false){
            response = scanner.nextLine();
            goodInput = InputChecker.intValidity(response);
        }
        return Integer.parseInt(response);
    }

    /**
     * Returns the next input from the user. No error-checking.
     * @return Next input from the user.
     */
    public static String nextString(){
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine();
        return response;
    }
}
