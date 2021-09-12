package automail;

import exceptions.DoesNotHaveSpaceException;
import exceptions.ItemTooHeavyException;
import simulation.IMailDelivery;
import util.Configuration;

public class BulkRobot extends Robot{
    private static final double BULKRATE = 0.01;
    private static final int INDIVIDUAL_MAX_WEIGHT = 2000 ;
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

    public void addToRobot(MailItem mailItem, Robot robot) throws ItemTooHeavyException, DoesNotHaveSpaceException {
        if(robot.getTube().size() < 5){
            robot.getTube().addFirst(mailItem);
            if (mailItem.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
        }
        else {
            throw new DoesNotHaveSpaceException();
        }
    }

}
