package application;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class CampListView {
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
    public static Camp campFromListSelector(List<Camp> selectableCamps, String listMenu) {
        Scanner sc = new Scanner(System.in);
        System.out.println(listMenu);
        System.out.println("0: Back to CAMs main menu ");
        System.out.println("Enter the number corresponding to the camp to select: ");
        int selection = -619;
        while (selection <0 || selection > selectableCamps.size()) {
            String response = sc.nextLine();
            if(InputChecker.intValidity(response)){
                selection = Integer.parseInt(response);
                if (selection < 0 || selection > selectableCamps.size()) {
                    System.out.println("Choice does not correspond to any camp on the list!");
                } else if (selection == 0) {
                    System.out.println("Quitting menu...");
                    return null;
                }
            }

        }
        return(selectableCamps.get(selection-1));
    }
}
