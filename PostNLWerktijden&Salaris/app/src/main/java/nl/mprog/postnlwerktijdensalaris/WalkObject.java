package nl.mprog.postnlwerktijdensalaris;

public class WalkObject {
    public int id1;
    public int id2;
    public String districtCode;
    public String timeBegin1;
    public String timeEnd1;
    public String timeBegin2;
    public String timeEnd2;
    public String timeBegin3;
    public String timeEnd3;
    public String timeGoal;
    public String timeExtra;
    public String timeTotal;

    public WalkObject(int id1, int id2, String districtCode, String timeBegin1, String timeEnd1, String timeBegin2, String timeEnd2,
                      String timeBegin3, String timeEnd3, String timeGoal, String timeExtra, String timeTotal) {

        this.id1 = id1;
        this.id2 = id2;
        this.districtCode = districtCode;
        this.timeBegin1 = timeBegin1;
        this.timeEnd1 = timeEnd1;
        this.timeBegin2 = timeBegin2;
        this.timeEnd2 = timeEnd2;
        this.timeBegin3 = timeBegin3;
        this.timeEnd3 = timeEnd3;
        this.timeGoal = timeGoal;
        this.timeExtra = timeExtra;
        this.timeTotal = timeTotal;
    }
}