package automail;

import simulation.IMailDelivery;

public class NormalRobot extends Robot {
    public NormalRobot(IMailDelivery delivery, MailPool mailPool, int number){
        super(delivery, mailPool, number, "R");
    }

}
