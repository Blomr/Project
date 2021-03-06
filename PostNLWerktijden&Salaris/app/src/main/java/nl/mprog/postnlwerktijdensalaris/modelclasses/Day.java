/**
 * Day.java
 *
 * Class for making Day objects.
 *
 * Made by Remco Blom - mProg Project
 */

package nl.mprog.postnlwerktijdensalaris.modelclasses;

public class Day {
    public int id1;
    public int id2;
    public String day;
    public String districts;
    public String timeTotal;
    public String timeGoal;
    public String timeExtra;

    public Day(int id1, int id2, String day, String districts, String timeTotal, String timeGoal, String timeExtra) {
        this.id1 = id1;
        this.id2 = id2;
        this.day = day;
        this.districts = districts;
        this.timeTotal = timeTotal;
        this.timeGoal = timeGoal;
        this.timeExtra = timeExtra;
    }
}