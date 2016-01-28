/**
 * Month.java
 *
 * Class for making Month objects.
 *
 * Made by Remco Blom - mProg Project
 */

package nl.mprog.postnlwerktijdensalaris.modelclasses;

public class Month {
    public int id;
    public String month;
    public int days;
    public double salary;
    public String time;

    public Month(int id, String month, int days, double salary, String time) {
        this.id = id;
        this.month = month;
        this.days = days;
        this.salary = salary;
        this.time = time;
    }
}