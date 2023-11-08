package application;
public class StudentCommittee extends Student implements ElevatedActions{
    private Camp camp;
    private int points;
    public StudentCommittee(String name, String id, String faculty,String password,int committee){
        super(name,id,faculty,password,committee);
        this.camp = null;
    }

    public Camp getCamp(){return camp;}
    public void setCamp(Camp camp){
        this.camp = camp;
    }
    public void addPoints(int score){
        this.points += score;
    }
    public int getPoints(){return points;}

}
