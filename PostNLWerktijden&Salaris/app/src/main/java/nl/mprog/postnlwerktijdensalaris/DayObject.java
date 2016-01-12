package nl.mprog.postnlwerktijdensalaris;

public class DayObject {
    public String day;
    public String districts;
    public String timeTotal;
    public String timeGoal;
    public String timeExtra;

    public DayObject(String day, String districts, String timeTotal, String timeGoal, String timeExtra) {
        this.day = day;
        this.districts = districts;
        this.timeTotal = timeTotal;
        this.timeGoal = timeGoal;
        this.timeExtra = timeExtra;
    }
}