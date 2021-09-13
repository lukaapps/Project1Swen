package automail;

import exceptions.DoesNotHaveSpaceException;
import exceptions.ItemTooHeavyException;
import simulation.IMailDelivery;
import util.Configuration;

public class NormalRobot extends Robot {
    private static final double NORMALRATE = 0.025;
    private static final int INDIVIDUAL_MAX_WEIGHT = 2000 ;
    private static int numNormalRobots = 0;
    private static double totalNormalOpTime = 0;
    private static double avgNormalOpTime = 0;

    public NormalRobot(IMailDelivery delivery, MailPool mailPool, int number){
        super(delivery, mailPool, number, "R");
    }

    /**
     * Getters and setters for
     * NormalRate - The rate at which the robot is being charged
     * NumNormalRobots - the number of normal robots in the pool
     * AvgNormalOpTime - the avg operating time for normal robots
     * TotalNomralOpTime - the total operating time for all normal robots used to calculate AvgNormalOpTime **/

    public static double getNORMALRATE() {
        return NORMALRATE;
    }

    public static int getNumNormalRobots() {
        return numNormalRobots;
    }

    public static void setNumNormalRobots(int numNormalRobots) {
        NormalRobot.numNormalRobots = numNormalRobots;
    }

    public static double getAvgNormalOpTime() {
        return avgNormalOpTime;
    }

    public static void setAvgNormalOpTime() {
        NormalRobot.avgNormalOpTime = NormalRobot.totalNormalOpTime / NormalRobot.numNormalRobots;
    }

    public static double getTotalNormalOpTime() {
        return totalNormalOpTime;
    }

    public static void incrementTotalNormalOpTime() {
        NormalRobot.totalNormalOpTime ++;
    }

    /**
     * Overriding add to robot method to add mailitems to the hand or tube for given NormalRobot
     * @param mailItem is the item intending to be added to robot
     * @param robot is the chosen robot for this case
     * @exception ItemTooHeavyException is an exception thrown in the case of the item being heavier than 2000
     * @exception  DoesNotHaveSpaceException is an exception thrown in the case that the robot is full of items
     * **/
    @Override
    public void addToRobot(MailItem mailItem, Robot robot) throws ItemTooHeavyException, DoesNotHaveSpaceException {
        if (deliveryItem == null) {
            deliveryItem = mailItem;
            if (deliveryItem.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
        }
        else if(robot.getTube().size() == 0){
            robot.getTube().addLast(mailItem);
            if (mailItem.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
        }
        else {
            throw new DoesNotHaveSpaceException();
        }
    }

}
