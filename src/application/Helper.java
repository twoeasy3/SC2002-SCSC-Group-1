package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.List;

public class Helper {


    public static void populateCommittees(List<User> userList, List<Camp> campList){
        Camp campObject = null;
        for(User user : userList){
            if(user instanceof Student){
                if(((Student) user).getCommittee() != 1){
                    campObject = getCampfromID(((Student) user).getCommittee(),campList);
                    if(campObject!= null){
                        campObject.addCommittee(((Student) user));
                    }
                }
            }
        }
    }

    /**
     * Used in conjunction with extra arguments to select which sorted list to display
     * -o "Open" Camps first
     * -l Location
     * -s Start date
     * -p Popularity by signups
     * -f Faculty Alphabetically
     * @param campList
     * @param arg
     * @return
     */
    public static List<Camp> sortCampList(List<Camp> campList, String arg){
        if(arg.equals("")){
            campList.sort(Comparator.comparing(Camp::getName,String.CASE_INSENSITIVE_ORDER));
        }
        else if(arg.equals("o")){
            System.out.println("Sorting by status, then by earliest registration close");
            campList.sort(Comparator.comparing(Camp::checkCampStatus).thenComparing(Camp::getRegEnd));
        }
        else if(arg.equals("l")){
            System.out.println("Sorting by location");
            campList.sort(Comparator.comparing(Camp::getLocation,String.CASE_INSENSITIVE_ORDER));
        }
        else if(arg.equals("s")){
            System.out.println("Sorting by start date");
            campList.sort(Comparator.comparing(Camp::getStart));
        }
        else if(arg.equals("p")){
            System.out.println("Sorting by popularity");
            campList.sort(Comparator.comparing(Camp::getAttendeeCount).reversed());
        }
        else if(arg.equals("f")){
            System.out.println("Sorting by faculty");
            campList.sort(Comparator.comparing(Camp::getFaculty));
        }
        else{
            System.out.println("Unrecognised command, sorting by default");
            campList.sort(Comparator.comparing(Camp::getName,String.CASE_INSENSITIVE_ORDER));
        }
        return campList;
    }

    public static String createNumberedCampList(List<Camp> selectCamps,User user){
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int i=0;
        StringBuilder sb = new StringBuilder();
        String lineString = "";
        String listMenu = "";
        for(Camp camp : selectCamps){
            i++;
            lineString = String.format("%-2d: %-55s %-6s %-15s %8s to %-8s [%-3d/%-3d]",i,camp.getName(),camp.getFaculty(),camp.getLocation()
                    ,camp.getStart().format(customFormatter),
                    camp.getEnd().format(customFormatter),camp.getAttendeeCount(),
                    (camp.getMaxSize()- camp.getMaxComm()));
            //listMenu = sb.append(i).append(": ").append(camp.getName()).append(" (").append(camp.getFaculty()).append(") ").append(camp.getLocation()).append(" [").append(camp.getAttendeeCount()).append("/").append(camp.getMaxSize()-camp.getMaxComm()).append("] ").toString();
            if (!camp.isVisible()){
                lineString = lineString + " |HIDDEN|";
            }
            lineString = lineString +  " <" + camp.checkCampStatus() + "> ";
            if (user instanceof Student){
                if (((Student) user).getCommittee() == camp.getID()){
                    lineString = lineString + " {COMMITTEE}";
                }
                if (camp.isAttending(((Student) user))){
                    lineString = lineString + " {ATTENDING}";
                }
                if (camp.isBlacklisted(((Student) user))){
                    lineString = lineString + " {BANNED}";
                }
            }
            if (user instanceof Staff){
                if (user.getID().equals(camp.getInCharge())){
                    lineString = lineString + " {INCHARGE}";
                }
            }
            lineString = lineString + ("\n");
            listMenu = sb.append(lineString).toString();

        }
        return listMenu;
    }

    public static Camp getCampfromID(int id, List<Camp> campList){
        for(Camp camp : campList){
            if (camp.getID() == id){
                return camp;
            }
        }
        return null;
    }
    public static boolean checkInputDateValidity(String input) {
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
    public static boolean checkInputIntValidity(String input){
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
            System.out.println("Bad response. Y/N only!");
            return -1;} //invalid input
    }

}
