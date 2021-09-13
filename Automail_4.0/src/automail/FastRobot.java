package automail;

import exceptions.DoesNotHaveSpaceException;
import exceptions.ItemTooHeavyException;
import simulation.IMailDelivery;
import util.Configuration;

public class FastRobot extends Robot {
    private static final double FASTRATE = 0.05;
    private static final int INDIVIDUAL_MAX_WEIGHT = 2000;
    private static int numFastRobots = 0;
    private static double totalFastOpTime = 0;
    private static double avgFastOpTime = 0;

    public FastRobot(IMailDelivery delivery, MailPool mailPool, int number){
        super(delivery, mailPool, number, "F");
    }


    /**
     * Getters and setters for
     * FastRate - The rate at which the robot is being charged
     * NumFastRobots - the number of fast robots in the pool
     * AvgFastOpTime - the avg operating time for fast robots
     * TotalFastOpTime - the total operating time for all fast robots used to calculate AvgFastOpTime **/
    public static double getFASTRATE() {
        return FASTRATE;
    }

    public static int getNumFastRobots() {
        return numFastRobots;
    }

    public static void setNumFastRobots(int numFastRobots) {
        FastRobot.numFastRobots = numFastRobots;
    }

    public static double getAvgFastOpTime() {
        return avgFastOpTime;
    }

    public static void setAvgFastOpTime() {
        FastRobot.avgFastOpTime = FastRobot.totalFastOpTime / FastRobot.numFastRobots;
    }

    public static double getTotalFastOpTime() {
        return totalFastOpTime;
    }

    public static void incrementTotalFastOpTime() {
        FastRobot.totalFastOpTime ++;
    }

    /**
     * Overriding add to robot method to add mailitems to the hand or tube for given FastRobot
     * @param mailItem is the item intending to be added to robot
     * @param robot is the chosen robot for this case
     * @exception ItemTooHeavyException is an exception thrown in the case of the item being heavier than 2000
     * @exception  DoesNotHaveSpaceException is an exception thrown in the case that the robot is full of items
     * **/
    @Override
    public void addToRobot(MailItem mailItem, Robot robot) throws ItemTooHeavyException, DoesNotHaveSpaceException {
        if(robot.deliveryItem==null){
            deliveryItem = mailItem;
            if (deliveryItem.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
        }
        else{
            throw new DoesNotHaveSpaceException();
        }
    }
}
