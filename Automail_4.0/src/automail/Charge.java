package automail;

import com.unimelb.swen30006.wifimodem.WifiModem;

//import java.awt.Robot;
import java.lang.*;
import automail.Robot;
import util.Configuration;

public class Charge {

    /** Details about a Charge **/

    private double serviceFee = 0;
    private double maintenanceCost = 0;
    private double avgOperatingTime = 0;
    private static boolean feeCharging = false;


    public WifiModem modem;


    public Charge(WifiModem modem) {
        this.modem = modem;
    }

    public String bill(MailItem mailItem, Robot robot) throws Exception {
            int floor = mailItem.getDestFloor();
            double charge = this.getCharge(floor, robot);
            double maintenanceCost = this.getMaintenanceCost();
            double serviceFee = this.serviceFee;
            double avgOperatingTime = this.avgOperatingTime;

            if (feeCharging == true){
                return String.format(" | Service Fee: %4f | Maintenance: %2f | Avg. Operating Time: %4f | Total Charge: %4f",
                        serviceFee, maintenanceCost, avgOperatingTime, charge);
            }
            else {
                return "";
            }
    }

    public static void setFeeCharging (boolean status){
        Charge.feeCharging = status;
    }

    public void setServiceFee(int floor) throws Exception {
        WifiModem w = WifiModem.getInstance(floor);
        double serviceFee = w.forwardCallToAPI_LookupPrice(floor);
        if (serviceFee >= 0) {
            this.serviceFee = serviceFee;
        }
    }

    public double getServiceFee() {
        return this.serviceFee;
    }

    public void calculateMaintenanceCost(int floor, Robot robot) {
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



    //TOTAL CHARGE
    public double getCharge(int floor, Robot robot) throws Exception {
        setServiceFee(floor);
        calculateMaintenanceCost(floor, robot);
        return this.getServiceFee() + this.getMaintenanceCost();
    }



}
