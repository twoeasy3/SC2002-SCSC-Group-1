package helper;

import java.util.Scanner;

public class Console {
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

    public static String nextString(){
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine();
        return response;
    }
}
