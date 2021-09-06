package automail;

import simulation.IMailDelivery;

public class Automail {

    private Robot[] robots;
    private MailPool mailPool;

    
    public Automail(MailPool mailPool, IMailDelivery delivery, int numRegRobots, int numFastRobots, int numBulkRobots) {  	
    	/** Initialize the MailPool */
    	
    	this.mailPool = mailPool;
        int totalRobots = numBulkRobots + numRegRobots + numFastRobots;
        int i, j, k;
    	/** Initialize robots, currently only regular robots */
    	robots = new Robot[totalRobots];
    	for (i = 0; i < numRegRobots; i++) robots[i] = new NormalRobot(delivery, mailPool, i);
    	for(j = i; j < numRegRobots+numFastRobots; j++) robots[j] = new FastRobot(delivery, mailPool, j);
    	for(k = j; k < totalRobots; k++) robots[k] = new BulkRobot(delivery, mailPool, k);
    }

    public Robot[] getRobots() {
        return robots;
    }

    public MailPool getMailPool() {
        return mailPool;
    }
}
