package nl.mprog.postnlwerktijdensalaris;

public class MonthObject {
    public int id;
    public String month;
    public String days;
    public String salary;
    public String time;

    public MonthObject(int id, String month, int days, double salary, String time) {
        this.id = id;
        this.month = month;
        this.days = Integer.toString(days);
        this.salary = Double.toString(salary);
        this.time = time;
    }
}