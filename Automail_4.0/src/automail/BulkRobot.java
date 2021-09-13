package automail;

import exceptions.DoesNotHaveSpaceException;
import exceptions.ItemTooHeavyException;
import simulation.IMailDelivery;
import util.Configuration;

/**
 * Monday 3:15 Group 10
 * **/
public class BulkRobot extends Robot{
    /**Initalise BulkRobot using Polymorphism**/
    private static final double BULKRATE = 0.01;
    private static final int INDIVIDUAL_MAX_WEIGHT = 2000 ;
    private static int numBulkRobots = 0;
    private static double totalBulkOpTime = 0;
    private static double avgBulkOpTime = 0;

    public BulkRobot(IMailDelivery delivery, MailPool mailPool, int number){
        super(delivery, mailPool, number, "B");
    }

    /**Getters and setters for:
     * BULKRATE - rate at which bulk robot is charged
     * NumBulkRobots - number of bulk robots
     * TotalBulkTime - total operating time of all bulk robots  - used to calculate AvgBulkOpTime
     * AvgBulkOpTime - average bulk robot operating time**/
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

    /**
     * Overriding add to robot method to add mailitems to the hand or tube for given BulkRobot
     * @param mailItem is the item intending to be added to robot
     * @param robot is the chosen robot for this case
     * @exception ItemTooHeavyException is an exception thrown in the case of the item being heavier than 2000
     * @exception  DoesNotHaveSpaceException is an exception thrown in the case that the robot is full of items
     * **/
    @Override
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
