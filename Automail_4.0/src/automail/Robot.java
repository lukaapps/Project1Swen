package automail;
import exceptions.DoesNotHaveSpaceException;
import exceptions.ExcessiveDeliveryException;
import exceptions.ItemTooHeavyException;
import simulation.Clock;
import simulation.IMailDelivery;

import java.util.LinkedList;

/**
 * The robot delivers mail!
 */

/**
 * Monday 3:15 Group 10
 * **/
public abstract class Robot {

    private static final int INDIVIDUAL_MAX_WEIGHT = 2000;
    public final String type;

    private IMailDelivery delivery;
    private final String id;
    /** Possible states the robot can be in */
    public enum RobotState { DELIVERING, WAITING, RETURNING }
    private RobotState current_state;
    private int current_floor;
    private int destination_floor;
    private MailPool mailPool;
    private boolean receivedDispatch;


    protected MailItem deliveryItem = null;
    //private MailItem tube = null;
    private LinkedList<MailItem> tube = new LinkedList<MailItem>();
    private int deliveryCounter;

    private Charge chargeObject;

    /**
     * Initiates the robot's location at the start to be at the mailroom
     * also set it to be waiting for mail.
     * @param delivery governs the final delivery
     * @param mailPool is the source of mail items
     * @param number is used to create the id for a robot
     * @param type is the type of robot that is being used
     */
    public Robot(IMailDelivery delivery, MailPool mailPool, int number, String type){
    	this.id = type + number; //Change later "R" value - will be determinate on the type

    	current_state = RobotState.RETURNING;
        current_floor = Building.getInstance().getMailroomLocationFloor();
        this.type = type;
        this.delivery = delivery;
        this.mailPool = mailPool;
        this.receivedDispatch = false;
        this.deliveryCounter = 0;

    }

    /**
     * dispatch is called to create a charge object in robot class
     * @param charge - use to set the charge object in robot**/
    public void dispatch(Charge charge) {
        this.chargeObject = charge;
    	receivedDispatch = true;
    }

    /**
     * Convert is a simple method used to convert the type into a value for future use in operate
     * @param strength - value for type**/
    public int convert(String strength){
        if(strength.equals("F")){
            return 1;
        } else if(strength.equals("B")){
            return 5;
        } else{
            return 2;
        }
    }

    /**
     * This is called on every time step
     * @throws ExcessiveDeliveryException if robot delivers more than the capacity of the tube without refilling
     */
    public void operate() throws ExcessiveDeliveryException, Exception {
    	switch(current_state) {
    		/** This state is triggered when the robot is returning to the mailroom after a delivery */
    		case RETURNING:
                if(type.equals("B")) {
                    BulkRobot.incrementTotalBulkOpTime();
                }
                if(type.equals("R")) {
                    NormalRobot.incrementTotalNormalOpTime();
                }
                if(type.equals("F")){
                    FastRobot.incrementTotalFastOpTime();
                }
    			/** If its current position is at the mailroom, then the robot should change state */
                if(current_floor == Building.getInstance().getMailroomLocationFloor()){
        			/** Tell the sorter the robot is ready */
        			mailPool.registerWaiting(this);
                	changeState(RobotState.WAITING);
                    /** Returns chargeObject */
                    this.chargeObject = null;

                } else {
                	/** If the robot is not at the mailroom floor yet, then move towards it! */
                    moveTowards(Building.getInstance().getMailroomLocationFloor());
                	break;
                }
    		case WAITING:
                /** If the StorageTube is ready and the Robot is waiting in the mailroom then start the delivery */
                if(!isEmpty() && receivedDispatch){
                	receivedDispatch = false;
                	deliveryCounter = 0; // reset delivery counter
                	setDestination();
                	changeState(RobotState.DELIVERING);
                }
                break;
    		case DELIVERING:
                if(type.equals("B")) {
                    BulkRobot.incrementTotalBulkOpTime();
                }
                if(type.equals("R")) {
                    NormalRobot.incrementTotalNormalOpTime();
                }
                if(type.equals("F")){
                    FastRobot.incrementTotalFastOpTime();
                }
    			if(current_floor == destination_floor){ // If already here drop off either way
                    /** Delivery complete, report this to the simulator! */
                    if(type.equals("B") && tube.size() > 0){
                        deliveryItem = tube.pop();
                    }
                    //delivery.deliver(this, deliveryItem, "");
                    delivery.deliver(this, deliveryItem, chargeObject.bill(deliveryItem, this));

                    deliveryItem = null;
                    deliveryCounter++;
                    if(deliveryCounter > convert(type)){  // Implies a simulation bug - Modify based off type robot
                    	throw new ExcessiveDeliveryException();
                    }
                    /** Check if want to return, i.e. if there is no item in the tube*/
                    if(tube.size() == 0){
                    	changeState(RobotState.RETURNING);
                    }
                    else{
                        /** If there is another item, set the robot's route to the location to deliver the item */
                        if(!type.equals("B")){
                            deliveryItem = tube.pop();
                        }
                        else{deliveryItem = tube.getFirst();}
                        setDestination();
                        changeState(RobotState.DELIVERING);
                    }
    			} else {
	        		/** The robot is not at the destination yet, move towards it! */
	                moveTowards(destination_floor);



    			}
                break;
    	}
    }

    /**
     * Sets the route for the robot
     */
    private void setDestination() {
        /** Set the destination floor */
        if(type.equals("B") && tube.size() > 0){
            destination_floor = tube.getFirst().getDestFloor();
        }
        else {
            destination_floor = deliveryItem.getDestFloor();
        }
    }

    /**
     * Generic function that moves the robot towards the destination
     * @param destination the floor towards which the robot is moving
     */
    private void moveTowards(int destination) {
        if(type.equals("F")){
            if(current_floor <= destination-3){
                current_floor+= 3;
            }
            else if(current_floor < destination){
                current_floor += destination - current_floor;
            }
            else if(current_floor >= destination + 3 ) {
                current_floor -= 3;
            }
            else{
                current_floor -=  current_floor-destination;
            }

        } else {

            if (current_floor < destination) {
                current_floor++;

            } else {
                current_floor--;


            }
        }
    }
    
    public String getIdTube() {
    	return String.format("%s(%1d)", this.id, (tube.size() == 0 ? 0 : tube.size()));
    }
    
    /**
     * Prints out the change in state
     * @param nextState the state to which the robot is transitioning
     */
    private void changeState(RobotState nextState){
    	assert(!(deliveryItem == null && tube != null));
    	if (current_state != nextState) {
            System.out.printf("T: %3d > %7s changed from %s to %s%n", Clock.Time(), getIdTube(), current_state, nextState);
    	}
    	current_state = nextState;
    	if(nextState == RobotState.DELIVERING){
    	    if(type.equals("B") && tube.size() > 0){
                System.out.printf("T: %3d > %7s-> [%s]%n", Clock.Time(), getIdTube(), tube.getFirst().toString());
            }
    	    else {
                System.out.printf("T: %3d > %7s-> [%s]%n", Clock.Time(), getIdTube(), deliveryItem.toString());
            }
    	}
    }

    public void setTube(LinkedList<MailItem> tube) {
        this.tube = tube;
    }

    public LinkedList<MailItem> getTube() {
		return tube;
	}

	public boolean isEmpty() {
		return (deliveryItem == null && tube.size() == 0);
	}

    /**
     * abstract class addToRobot to be overridden in subclasses
     * @param mailItem - the item being added to robot
     * @param robot  - the robot the item is being added to **/
    public abstract void addToRobot(MailItem mailItem, Robot robot) throws ItemTooHeavyException, DoesNotHaveSpaceException;

}
