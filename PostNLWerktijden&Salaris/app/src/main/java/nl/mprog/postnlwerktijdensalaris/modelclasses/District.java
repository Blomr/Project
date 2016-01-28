/**
 * District.java
 *
 * Class for making District objects.
 *
 * Made by Remco Blom - mProg Project
 */

package nl.mprog.postnlwerktijdensalaris.modelclasses;

public class District {
    public int id;
    public String districtCode;
    public String timeGoalBusy;
    public String timeGoalCalm;

    public District(int id, String districtCode, String timeGoalBusy, String timeGoalCalm) {
        this.id = id;
        this.districtCode = districtCode;
        this.timeGoalBusy = timeGoalBusy;
        this.timeGoalCalm = timeGoalCalm;
    }
}