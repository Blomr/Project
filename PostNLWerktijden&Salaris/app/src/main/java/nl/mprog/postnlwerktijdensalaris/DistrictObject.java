package nl.mprog.postnlwerktijdensalaris;

public class DistrictObject {
    public String districtCode;
    public String timeGoalBusy;
    public String timeGoalCalm;

    public DistrictObject(String districtCode, String timeGoalBusy, String timeGoalCalm) {

        this.districtCode = districtCode;
        this.timeGoalBusy = timeGoalBusy;
        this.timeGoalCalm = timeGoalCalm;
    }
}