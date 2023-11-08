package application;
public class StudentCommittee extends Student implements ElevatedActions{
    private Camp camp;
    private int points;
    public StudentCommittee(String name, String id, String faculty,String password,int committee,
                            Camp camp){
        super(name,id,faculty,password,committee);
        this.camp = camp;
    }

    public Camp getCamp(){return camp;}
    public void addPoints(int score){
        this.points += score;
    }

}
