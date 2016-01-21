package nl.mprog.postnlwerktijdensalaris;

public class DistrictObject {
    public int id;
    public String districtCode;
    public String timeGoalBusy;
    public String timeGoalCalm;

    public DistrictObject(int id, String districtCode, String timeGoalBusy, String timeGoalCalm) {
        this.id = id;
        this.districtCode = districtCode;
        this.timeGoalBusy = timeGoalBusy;
        this.timeGoalCalm = timeGoalCalm;
    }
}