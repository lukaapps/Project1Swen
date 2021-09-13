package automail;

import com.unimelb.swen30006.wifimodem.WifiModem;

//import java.awt.Robot;
import java.lang.*;
import java.util.ArrayList;
import java.util.*;
import automail.Robot;
import util.Configuration;

public class Charge {

    /** Details about a Charge **/

    private double serviceFee = 0;
    private static ArrayList<Double> floorServiceFees = new ArrayList<Double>(Collections.nCopies(Building.getInstance().getnFloors() ,0.00));
    private double maintenanceCost = 0;
    private double avgOperatingTime = 0;
    private static boolean feeCharging = false;

    public WifiModem modem;

    /**
     * Initialisation of Charge
     * @param modem - Initialisation of WifiModem to lookup API cost**/
    public Charge(WifiModem modem) {
        this.modem = modem;
    }

    /**
     * bill method to calculate and print the final results for each delivery in terms of charge cost
     * if charge is turned on then it computes otherwise an empty string is returned
     * to hold the default output for the program
     * @param mailItem - is the reference for the delivery being calculated
     * @param robot is the reference for the robot making the delivery**/
    public String bill(MailItem mailItem, Robot robot) throws Exception {
            int floor = mailItem.getDestFloor();
            double charge = this.getCharge(floor, robot);
            double maintenanceCost = this.getMaintenanceCost();
            double serviceFee = this.serviceFee;
            double avgOperatingTime = this.avgOperatingTime;

            if (feeCharging == true){
                return String.format(" | Service Fee: %.2f | Maintenance: %.2f | Avg. Operating Time: %.2f | Total Charge: %.2f",
                        serviceFee, maintenanceCost, avgOperatingTime, charge);
            }
            else {
                return "";
            }
    }

    /**
     * setters and getters for Fee Charging and Service Fee
     * Fee charging is used to set whether or not the case allows for fee charging
     * @param status - is the result from the class Configuration for the output of fee charging in properties
     * Service Fee is used to look up the service fee based on the floor**/
    public static void setFeeCharging (boolean status){
        Charge.feeCharging = status;
    }

    /**
     * @param floor - used to calculate the service fee for the chosen floor
     * **/
    public double retriveServiceFee(int floor) throws Exception {
        WifiModem w = WifiModem.getInstance(floor);
        double serviceFee = w.forwardCallToAPI_LookupPrice(floor);
        if (serviceFee >= 0) {
            this.serviceFee = serviceFee;
            floorServiceFees.set(floor-Building.getInstance().getLowestFloor(), serviceFee);
        }
        else {
            this.serviceFee = floorServiceFees.get(floor-Building.getInstance().getLowestFloor());
        }
        return this.serviceFee;
    }


    /**
     * Maintenance cost - is the another factor that goes into the total charge for the given robot
     * and floor - maintenance cost is dependent on the type of robot and is calulated by rate multiplied by
     * average operating time
     * @param robot  - the robot in question - usually referencing type**/
    public void calculateMaintenanceCost( Robot robot) {
        if (robot instanceof BulkRobot) {
            BulkRobot.setAvgBulkOpTime();
            this.maintenanceCost = BulkRobot.getBULKRATE() * BulkRobot.getAvgBulkOpTime();
            this.avgOperatingTime = BulkRobot.getAvgBulkOpTime();
        }
        else if (robot instanceof FastRobot) {
            FastRobot.setAvgFastOpTime();
            this.maintenanceCost = FastRobot.getFASTRATE() * FastRobot.getAvgFastOpTime();
            this.avgOperatingTime = FastRobot.getAvgFastOpTime();
        }
        else  {
            NormalRobot.setAvgNormalOpTime();
            this.maintenanceCost = NormalRobot.getNORMALRATE() * NormalRobot.getAvgNormalOpTime();
            this.avgOperatingTime = NormalRobot.getAvgNormalOpTime();
        }
    }

    public double getMaintenanceCost() {
        return  this.maintenanceCost;
    }



    /**
     * Used to get the total charge for the given robot and floor
     * @param robot - the robot in question when assessing the maintenance cost
     * @param floor - the floor being used when looking up the API**/
    public double getCharge(int floor, Robot robot) throws Exception {
        if(feeCharging) {
            calculateMaintenanceCost(robot);
            return this.retriveServiceFee(floor) + this.getMaintenanceCost();
        }
        else{ return 0.0;}
    }



}
