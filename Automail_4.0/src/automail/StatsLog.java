package automail;

public class StatsLog {
    private int totalDelivered = 0;
    private int totalLookups = 0;
    private int totalSuccessfulLookups = 0;
    private double totalActivityUnits = 0;
    private double totalActivityCost = 0;
    private double totalServiceCost = 0;

    public String toString() {
        return String.format("Total Delivered: %4d | Total Lookups: %2d | Total Successful Lookups: %4d | Total Unsuccessful Lookups: %4d \n"
                        + "Total Activity Units: %4f | Total Activity Cost: %4f | Total Service Cost: %4f ",
                this.totalDelivered, this.totalLookups, this.totalSuccessfulLookups, this.totalLookups - this.totalSuccessfulLookups,
                this.totalActivityUnits, this.totalActivityCost, this.totalServiceCost);
    }
    public void logLookUp() {
        this.totalLookups += 1;
    }
    public void logSuccessfulLookUp() {
        this.totalSuccessfulLookups += 1;
    }
    public void logDelivery() {
        this.totalDelivered += 1;
    }
    public void logActivityUnits(double activityUnits) {
        this.totalActivityUnits += activityUnits;
    }
    public void logActivityCost(double activityUnits) {
        this.totalActivityCost += activityUnits;
    }
    public void logServiceCost(double serviceCost) {
        this.totalServiceCost += serviceCost;
    }
}
