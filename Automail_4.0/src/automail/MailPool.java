package automail;

import java.util.LinkedList;
import java.util.Comparator;
import java.util.ListIterator;

import com.unimelb.swen30006.wifimodem.WifiModem;
import exceptions.ItemTooHeavyException;
import exceptions.DoesNotHaveSpaceException;

import javax.swing.*;

/**
 * addToPool is called when there are mail items newly arrived at the building to add to the MailPool or
 * if a robot returns with some undelivered items - these are added back to the MailPool.
 * The data structure and algorithms used in the MailPool is your choice.
 * 
 */
/**
 * Monday 3:15 Group 10
 * **/
public class MailPool {

	private Charge chargeObject;

	private LinkedList<Item> pool;
	private LinkedList<Robot> robots;

	public MailPool(WifiModem wModem){
		// Start empty
		this.chargeObject = new Charge(wModem);
		pool = new LinkedList<Item>();
		robots = new LinkedList<Robot>();
	}

	/**
     * Adds an item to the mail pool
     * @param mailItem the mail item being added.
     */
	public void addToPool(MailItem mailItem) throws Exception {
		Item item = new Item(mailItem);
		pool.add(item);
		pool.sort(new ItemComparator());
	}

	/**
	 * pops item from mail pool
	 *
	 * **/


	/**
     * load up any waiting robots with mailItems, if any.
     */
	public void loadItemsToRobot() throws ItemTooHeavyException, DoesNotHaveSpaceException {
		//List available robots
		ListIterator<Robot> i = robots.listIterator();
		while (i.hasNext()) loadItem(i);
	}
	
	/**
	 * Used to call on the addToRobot function in subclasses
	 * @param i  - is the list of robots and used to add to each individual robot
	 * A loop is run on each robot dependent on type and tube size
	 * in this loop the items are added to each robot - the loops size are specified by the type of robot**/
	private void loadItem(ListIterator<Robot> i) throws ItemTooHeavyException, DoesNotHaveSpaceException {

		Robot robot = i.next();
		assert(robot.isEmpty());
		ListIterator<Item> j = pool.listIterator();
		if (pool.size() > 0) {
			try {
				if (robot.type.equals("R") && robot.getTube().size() < 1){
					int y = pool.size();
					for(int k =0; k < Math.min(y, 2); k++){
						robot.addToRobot(j.next().mailItem, robot); // hand first as we want higher priority delivered first
						j.remove();
					}
				} else if (robot.type.equals("B") && robot.getTube().size() < 5){
					int y = pool.size();
					//System.out.println(pool.getFirst().mailItem);
					for(int n =0; n < Math.min(y, 5); n++){
						robot.addToRobot(j.next().mailItem, robot); // hand first as we want higher priority delivered first
						j.remove();
					}

				}
				else if (robot.type.equals("F")){
					robot.addToRobot(j.next().mailItem, robot); // hand first as we want higher priority delivered first
					j.remove();
				}

			robot.dispatch(chargeObject); // send the robot off if it has any items to deliver
			i.remove();       // remove from mailPool queue
			} catch (Exception e) {
	            throw e;
	        }
		}
	}

	public LinkedList<Item> getPool(){
		return pool;
	}

	/**
     * @param robot refers to a robot which has arrived back ready for more mailItems to deliver
     */	
	public void registerWaiting(Robot robot) { // assumes won't be there already
		robots.add(robot);
	}

	private class Item {
		int destination;
		double charge;

		MailItem mailItem;
		// Use stable sort to keep arrival time relative positions

		public Item(MailItem mailItem) throws Exception {
			this.destination = mailItem.getDestFloor();
			this.mailItem = mailItem;
		}
	}

	public class ItemComparator implements Comparator<Item> {
		@Override
		public int compare(Item i1, Item i2) {
			int order = 0;
				if (i1.destination < i2.destination) {
					order = 1;
				} else if (i1.destination > i2.destination) {
					order = -1;
				}
				return order;

		}
	}

}
