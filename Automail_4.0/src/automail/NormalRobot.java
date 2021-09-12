package automail;

import simulation.IMailDelivery;
import util.Configuration;

public class NormalRobot extends Robot {
    private static final double NORMALRATE = 0.025;
    private static int numNormalRobots = 0;
    private static double totalNormalOpTime = 0;
    private static double avgNormalOpTime = 0;

    public NormalRobot(IMailDelivery delivery, MailPool mailPool, int number){
        super(delivery, mailPool, number, "R");
    }

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


}
