package nl.mprog.postnlwerktijdensalaris;

public class MonthObject {
    public int id;
    public String month;
    public int days;
    public double salary;
    public String time;

    public MonthObject(int id, String month, int days, double salary, String time) {
        this.id = id;
        this.month = month;
        this.days = days;
        this.salary = salary;
        this.time = time;
    }
}