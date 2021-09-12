package automail;

import simulation.IMailDelivery;
import util.Configuration;

public class BulkRobot extends Robot{
    private static final double BULKRATE = 0.01;
    private static int numBulkRobots = 0;
    private static double totalBulkOpTime = 0;
    private static double avgBulkOpTime = 0;

    public BulkRobot(IMailDelivery delivery, MailPool mailPool, int number){
        super(delivery, mailPool, number, "B");
    }

    public static double getBULKRATE() {
        return BULKRATE;
    }

    public static int getNumBulkRobots() {
        return numBulkRobots;
    }

    public static void setNumBulkRobots(int numBulkRobots) {
        BulkRobot.numBulkRobots = numBulkRobots;
    }

    public static double getAvgBulkOpTime() {
        return avgBulkOpTime;
    }

    public static void setAvgBulkOpTime() {
        BulkRobot.avgBulkOpTime = BulkRobot.totalBulkOpTime / BulkRobot.numBulkRobots;
    }

    public static double getTotalBulkOpTime() {
        return totalBulkOpTime;
    }

    public static void incrementTotalBulkOpTime() {
        BulkRobot.totalBulkOpTime ++;
    }


}
