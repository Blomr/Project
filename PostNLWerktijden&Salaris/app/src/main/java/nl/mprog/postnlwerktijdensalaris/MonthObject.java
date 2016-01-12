package nl.mprog.postnlwerktijdensalaris;

public class MonthObject {
    public String month;
    public String days;
    public String salary;
    public String time;

    public MonthObject(String month, int days, double salary, String time) {
        this.month = month;
        this.days = Integer.toString(days);
        this.salary = Double.toString(salary);
        this.time = time;
    }
}