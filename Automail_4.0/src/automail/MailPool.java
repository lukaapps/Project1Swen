package automail;

import java.util.LinkedList;
import java.util.Comparator;
import java.util.ListIterator;

import exceptions.ItemTooHeavyException;
import exceptions.DoesNotHaveSpaceException;

import javax.swing.*;

/**
 * addToPool is called when there are mail items newly arrived at the building to add to the MailPool or
 * if a robot returns with some undelivered items - these are added back to the MailPool.
 * The data structure and algorithms used in the MailPool is your choice.
 * 
 */
public class MailPool {

	private Charge chargeObject;

	private LinkedList<Item> pool;
	private LinkedList<Robot> robots;
	private int n = 0;

	public MailPool(){
		// Start empty
		pool = new LinkedList<Item>();
		robots = new LinkedList<Robot>();
	}

	/**
     * Adds an item to the mail pool
     * @param mailItem the mail item being added.
     */
	public void addToPool(MailItem mailItem) {
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
	
	//load items to the robot
	private void loadItem(ListIterator<Robot> i) throws ItemTooHeavyException, DoesNotHaveSpaceException {

		Robot robot = i.next();
		assert(robot.isEmpty());
		ListIterator<Item> j = pool.listIterator();
		if (pool.size() > 0) {
			try {
				if (robot.type.equals("R")){
					for(int k =0; k < Math.min(pool.size(), 2); k++){
						robot.addToRobot(j.next().mailItem); // hand first as we want higher priority delivered first
						j.remove();
					}
				} else if (robot.type.equals("B")){
					//System.out.println(pool.size());
					//System.out.println(pool.getFirst().mailItem);
					for(int k =0; k < Math.min(pool.size(), 5); k++){
						robot.addToRobot(j.next().mailItem); // hand first as we want higher priority delivered first
						j.remove();

					}

				}
				else{
					for(int k =0; k< Math.min(pool.size(), 1); k++){
						robot.addToRobot(j.next().mailItem); // hand first as we want higher priority delivered first
						j.remove();
					}
				}

			robot.dispatch(); // send the robot off if it has any items to deliver
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

		public Item(MailItem mailItem) {
			destination = mailItem.getDestFloor();
			this.charge = chargeObject.getCharge(destination);
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
